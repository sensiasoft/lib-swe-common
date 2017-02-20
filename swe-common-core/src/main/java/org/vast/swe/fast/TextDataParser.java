/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2015 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.swe.fast;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import net.opengis.swe.v20.Boolean;
import net.opengis.swe.v20.Category;
import net.opengis.swe.v20.Count;
import net.opengis.swe.v20.DataArray;
import net.opengis.swe.v20.DataBlock;
import net.opengis.swe.v20.DataChoice;
import net.opengis.swe.v20.DataComponent;
import net.opengis.swe.v20.DataRecord;
import net.opengis.swe.v20.Quantity;
import net.opengis.swe.v20.Text;
import net.opengis.swe.v20.TextEncoding;
import net.opengis.swe.v20.Time;
import org.vast.cdm.common.DataStreamParser;
import org.vast.cdm.common.DataStreamWriter;
import org.vast.swe.SWEHelper;
import org.vast.util.DateTimeFormat;
import org.vast.util.ReaderException;


/**
 * <p>
 * New implementation of text data parser with better efficiency since the 
 * parsing tree is pre-computed during init instead of being re-evaluated
 * while iterating through the component tree.
 * </p>
 *
 * @author Alex Robin <alex.robin@sensiasoftware.com>
 * @since Dec 7, 2016
 */
public class TextDataParser extends AbstractDataParser
{
    static final String INVALID_BOOLEAN_MSG = "Invalid boolean value: ";
    static final String INVALID_INTEGER_MSG = "Invalid integer value: ";
    static final String INVALID_DECIMAL_MSG = "Invalid decimal value: ";
    static final String INVALID_CHOICE_MSG = "Invalid choice selector value: ";
    
    Reader reader;
    String tokenSep = ",";
    char[] blockSep = "\n".toCharArray();
    boolean collapseWhiteSpaces = true;
    StringBuilder tokenBuf = new StringBuilder(32);
    String[] lastSplit;
    int tokenIndex = -1;    
    int blockSepIndex = 0;
    
    
    protected class BooleanParser extends BaseProcessor
    {
        @Override
        public int process(DataBlock data, int index) throws IOException
        {
            String token = readToken();
            boolean val = false;

            if (token.length() == 1)
            {
                char c = token.charAt(0);
                if (c == '0')
                    val = false;
                else if (c == '1')
                    val = true;
                else
                    throw new ReaderException(INVALID_BOOLEAN_MSG + token);
            }
            else
            {
                if (token.equalsIgnoreCase("false"))
                    val = false;
                else if (token.equalsIgnoreCase("true"))
                    val = true;
                else
                    throw new ReaderException(INVALID_BOOLEAN_MSG + token);
            }
            
            data.setBooleanValue(index, val);
            return ++index;
        }
    }
    
    
    private class IntegerParser extends BaseProcessor
    {
        @Override
        public int process(DataBlock data, int index) throws IOException
        {
            String token = readToken();
            
            try
            {
                int val = Integer.parseInt(token);
                data.setIntValue(index, val);
                return ++index;
            }
            catch (NumberFormatException e)
            {
                throw new ReaderException(INVALID_INTEGER_MSG + token);
            }            
        }
    }   
    
    
    protected class DecimalParser extends BaseProcessor
    {
        @Override
        public int process(DataBlock data, int index) throws IOException
        {
            String token = readToken();
            
            try
            {
                double val;
                
                if ("INF".equals(token) || "+INF".equals(token))
                    val = Double.POSITIVE_INFINITY;
                else if ("-INF".equals(token))
                    val = Double.NEGATIVE_INFINITY;
                else
                    val = Double.parseDouble(token);
                
                data.setDoubleValue(index, val);
                return ++index;
            }
            catch (NumberFormatException e)
            {
                throw new ReaderException(INVALID_DECIMAL_MSG + token);
            }
        }
    }    
    
    
    protected class IsoDateTimeParser extends BaseProcessor
    {
        DateTimeFormat timeFormat = new DateTimeFormat();
        
        @Override
        public int process(DataBlock data, int index) throws IOException
        {
            String token = readToken();
            
            try
            {
                double val = timeFormat.parseIso(token);
                data.setDoubleValue(index, val);
                return ++index;
            }
            catch (ParseException e)
            {
                throw new ReaderException(e.getMessage());
            }
        }
    }    
    
    
    protected class StringParser extends BaseProcessor
    {
        @Override
        public int process(DataBlock data, int index) throws IOException
        {
            String token = readToken();
            data.setStringValue(index, token);
            return ++index;
        }
    }  
    
    
    protected class ChoiceTokenParser extends ChoiceProcessor
    {
        ArrayList<String> choiceTokens;
        
        public ChoiceTokenParser(DataChoice choice)
        {
            choiceTokens = new ArrayList<String>(choice.getNumItems());
            for (DataComponent item: choice.getItemList())
                choiceTokens.add(item.getName());
        }
        
        @Override
        public int process(DataBlock data, int index) throws IOException
        {
            String token = readToken();
            
            int selectedIndex = choiceTokens.indexOf(token);
            if (selectedIndex < 0)
                throw new ReaderException(INVALID_CHOICE_MSG + token);
            
            data.setIntValue(index, selectedIndex);
            // TODO set proper datablock for selected choice item
            return super.process(data, ++index, selectedIndex);
        }
    }
    
    
    protected class ArraySizeParser extends BaseProcessor
    {
        ArrayProcessor arrayProcessor;
        
        @Override
        public int process(DataBlock data, int index) throws IOException
        {
            String token = readToken();
            
            try
            {
                int val = Integer.parseInt(token);
                arrayProcessor.arraySize = val;
                data.setIntValue(index, val);
                return ++index;
            }
            catch (NumberFormatException e)
            {
                throw new ReaderException(INVALID_INTEGER_MSG + token);
            } 
        }
    }
    
    
    private String readToken() throws IOException
    {
        try
        {
            if (tokenIndex < 0 || tokenIndex >= lastSplit.length)
            {
                // read text until next block separator
                tokenBuf.setLength(0);
                blockSepIndex = 0;
                int b;
                do
                {
                    b = reader.read();
                    if (b < 0)
                        break;
                    
                    tokenBuf.append((char)b);
                    
                    // check if we have a block separator
                    if (b == blockSep[blockSepIndex])
                        blockSepIndex++;
                    else
                        blockSepIndex = 0;
                }
                while (blockSepIndex < blockSep.length);
                
                // remove trailing separator
                if (blockSepIndex == blockSep.length)
                    tokenBuf.setLength(tokenBuf.length()-blockSep.length);
                
                // convert to string and trim white spaces if requested
                String recString = tokenBuf.toString();
                if (this.collapseWhiteSpaces)
                    recString.trim();
                
                if (recString.isEmpty())
                    return null;
                
                lastSplit = recString.split(tokenSep);
                tokenIndex = 0;
            }
            
            // return next token, trimming white spaces if requested
            String nextToken = lastSplit[tokenIndex++];
            if (this.collapseWhiteSpaces)
                nextToken = nextToken.trim();            
            return nextToken;
        }
        catch (IOException e)
        {
            throw new ReaderException("Cannot parse next token", e);
        }
    }
    
    
    protected void init()
    {
        this.tokenSep = ((TextEncoding)dataEncoding).getTokenSeparator();
        this.blockSep = ((TextEncoding)dataEncoding).getBlockSeparator().toCharArray();
        //this.decimalSep = ((TextEncoding)dataEncoding).getDecimalSeparator().charAt(0);
        this.collapseWhiteSpaces = ((TextEncoding)dataEncoding).getCollapseWhiteSpaces();   
    }
    
    
    @Override
    protected boolean moreData() throws IOException
    {
        String token = readToken();
        tokenIndex--;
        return !(token == null);
    }
    

    @Override
    public void setInput(InputStream is) throws IOException
    {
        this.reader = new InputStreamReader(is, StandardCharsets.UTF_8);     
    }
    

    @Override
    public void close() throws IOException
    {
        if (reader != null)
            reader.close();
    }
    
    
    @Override
    public void visit(Boolean comp)
    {
        addToProcessorTree(new BooleanParser());
    }
    
    
    @Override
    public void visit(Count comp)
    {
        addToProcessorTree(new IntegerParser());
    }
    
    
    @Override
    public void visit(Quantity comp)
    {
        addToProcessorTree(new DecimalParser());
    }
    
    
    @Override
    public void visit(Time comp)
    {
        if (comp.isIsoTime())
            addToProcessorTree(new IsoDateTimeParser());
        else
            addToProcessorTree(new DecimalParser());
    }
    
    
    @Override
    public void visit(Category comp)
    {
        addToProcessorTree(new StringParser());
    }
    
    
    @Override
    public void visit(Text comp)
    {
        addToProcessorTree(new StringParser());
    }
    
    
    @Override
    public void visit(DataArray array)
    {
        if (array.isImplicitSize())
        {
            ArrayProcessor arrayProcessor = new ArrayProcessor();
            ArraySizeParser sizeParser = new ArraySizeParser();
            sizeParser.arrayProcessor = arrayProcessor;
            addToProcessorTree(sizeParser);
            addToProcessorTree(arrayProcessor);
            array.getElementType().accept(this);        
            processorStack.pop();
        }
        else
            super.visit(array);
    }
    
    
    @Override
    public void visit(DataChoice choice)
    {
        addToProcessorTree(new ChoiceTokenParser(choice));
        for (DataComponent item: choice.getItemList())
            item.accept(this);        
        processorStack.pop();
    }
    
    
    public static final void main(String[] args) throws Exception
    {
        SWEHelper fac = new SWEHelper();
        int arraySize = 5;
        
        DataArray array = fac.newDataArray(arraySize);
        array.setName("array");
        DataRecord rec = fac.newDataRecord();
        rec.addComponent("f0", fac.newTimeStampIsoUTC());
        rec.addComponent("f1", fac.newQuantity());
        rec.addComponent("f2", fac.newQuantity());
        DataRecord rec2 = fac.newDataRecord();
        rec2.addComponent("f3", fac.newCount());
        rec2.addComponent("f4", fac.newQuantity());
        rec.addComponent("rec2", rec2);  
        DataChoice choice = fac.newDataChoice();
        choice.addComponent("f5", fac.newCategory());
        choice.addComponent("f6", fac.newQuantity());
        rec.addComponent("choice", choice);
        array.setElementType("elt", rec);
        
        // generate data
        String tokenSep = "!";
        String blockSep = "@@\n"; 
        DateTimeFormat timeFormat = new DateTimeFormat();
        double now = new Date().getTime()/1000.;
        StringBuilder buf = new StringBuilder();
        for (int r=0; r<100000; r++)
        {
            for (int i=0; i<arraySize; i++)
            {
                double val = i + r*10;
                buf.append(timeFormat.formatIso(now+val, 0)).append(tokenSep)
                   .append(val/1000.).append(tokenSep)
                   .append(val+100).append(tokenSep)
                   .append(((int)val)+200).append(tokenSep)
                   .append(val+300).append(tokenSep)
                   .append("f5").append(tokenSep)
                   .append("test" + i);
                if (i == arraySize-1)
                    buf.append(blockSep);
                else
                    buf.append(tokenSep);
            }
        }
                
        /*for (int it=0; it<10; it++)
        {
            ByteArrayInputStream is = new ByteArrayInputStream(buf.toString().getBytes()); 
            
            // init & launch parser
            //DataStreamParser parser = new AsciiDataParser();
            DataStreamParser parser = new TextDataParser();
            parser.setDataComponents(array);
            parser.setDataEncoding(fac.newTextEncoding(tokenSep, blockSep));
            parser.setInput(is);
            parser.setRenewDataBlock(false);
            
            long t0 = System.currentTimeMillis();
            DataBlock dataBlk;
            do
            {
                //System.out.println();
                dataBlk = parser.parseNextBlock();
                //if (dataBlk != null)
                {
                //    for (int i=0; i<dataBlk.getAtomCount();i++)
                //    System.out.print(dataBlk.getStringValue(i) + ",");
                }
            }
            while (dataBlk != null);        
            System.out.println("Exec Time = " + (System.currentTimeMillis()-t0));
        }*/
        
        for (int it=0; it<10; it++)
        {
            ByteArrayInputStream is = new ByteArrayInputStream(buf.toString().getBytes()); 
            
            // init parser
            DataStreamParser parser = new TextDataParser();
            parser.setDataComponents(array);
            parser.setDataEncoding(fac.newTextEncoding(tokenSep, blockSep));
            parser.setInput(is);
            parser.setRenewDataBlock(false);
            
            // init writer
            /*DataStreamWriter writer = new TextDataWriter();
            writer.setDataComponents(array);
            writer.setDataEncoding(fac.newTextEncoding(tokenSep, blockSep));*/
            DataStreamWriter writer = new XmlDataWriter();
            writer.setDataComponents(array);
            writer.setDataEncoding(fac.newXMLEncoding());
            /*DataStreamWriter writer = new JsonDataWriter();
            writer.setDataComponents(array);
            writer.setDataEncoding(fac.newXMLEncoding());*/
            writer.setOutput(System.out);//new ByteArrayOutputStream(1024*1024));            
            
            long t0 = System.currentTimeMillis();
            DataBlock dataBlk;
            do
            {
                dataBlk = parser.parseNextBlock();
                if (dataBlk != null)
                {
                    writer.write(dataBlk);
                    writer.flush();
                }
            }
            while (dataBlk != null); 
            
            System.out.println("Exec Time = " + (System.currentTimeMillis()-t0));
        }
    }
}
