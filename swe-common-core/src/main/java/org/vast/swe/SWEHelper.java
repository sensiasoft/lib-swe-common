/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "SensorML DataProcessing Engine".
 
 The Initial Developer of the Original Code is the VAST team at the University of Alabama in Huntsville (UAH). <http://vast.uah.edu> Portions created by the Initial Developer are Copyright (C) 2007 the Initial Developer. All Rights Reserved. Please Contact Mike Botts <mike.botts@uah.edu> for more information.
 
 Contributor(s): 
    Alexandre Robin <robin@nsstc.uah.edu>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.swe;

import java.nio.ByteOrder;
import net.opengis.swe.v20.BinaryComponent;
import net.opengis.swe.v20.BinaryMember;
import net.opengis.swe.v20.ByteEncoding;
import net.opengis.swe.v20.DataArray;
import net.opengis.swe.v20.DataComponent;
import net.opengis.swe.v20.DataEncoding;
import net.opengis.swe.v20.BinaryEncoding;
import net.opengis.swe.v20.DataRecord;
import net.opengis.swe.v20.DataType;
import net.opengis.swe.v20.JSONEncoding;
import net.opengis.swe.v20.Quantity;
import net.opengis.swe.v20.TextEncoding;
import net.opengis.swe.v20.Time;
import net.opengis.swe.v20.Vector;
import net.opengis.swe.v20.XMLEncoding;
import org.vast.cdm.common.CDMException;
import org.vast.cdm.common.DataStreamParser;
import org.vast.cdm.common.DataStreamWriter;
import org.vast.data.AbstractDataComponentImpl;
import org.vast.data.AbstractSimpleComponentImpl;
import org.vast.data.BinaryComponentImpl;
import org.vast.data.BinaryEncodingImpl;
import org.vast.data.CountImpl;
import org.vast.data.DataArrayImpl;
import org.vast.data.DataComponentHelper;
import org.vast.data.DataIterator;
import org.vast.data.DataValue;
import org.vast.data.SWEFactory;
import org.vast.data.ScalarIterator;
import org.vast.data.TextEncodingImpl;


/**
 * <p>
 * Helper class for creating common data structures and encodings
 * </p>
 *
 * @author Alex Robin>
 * @since Feb 26, 2015
 */
public class SWEHelper extends SWEFactory
{  
    public static final String DEFAULT_TIME_REF = "http://www.opengis.net/def/trs/BIPM/0/UTC";
    public static final String DEF_IMAGE = "http://www.opengis.net/def/property/OGC/0/Image";
    
    
    /**
     * Wraps the given component(s) into a record with an ISO time stamp
     * @param subComponents 
     * @return new DataRecord instance containing time stamp and all other components
     */
    public DataRecord wrapWithIsoTimeStamp(DataComponent... subComponents)
    {
        return wrapWithTimeStamp(Time.ISO_TIME_UNIT, false, DEFAULT_TIME_REF, subComponents);
    }
    
    
    /**
     * Wraps the given component(s) into a record with a local relative (on-board) time stamp
     * @param uomCode unit of local time stamp (must be a valid time unit)
     * @param subComponents 
     * @return new DataRecord instance containing time stamp and all other components
     */
    public DataRecord wrapWithOnBoardTimeStamp(String uomCode, DataComponent... subComponents)
    {
        return wrapWithTimeStamp(uomCode, true, DEFAULT_TIME_REF, subComponents);
    }
    
    
    protected DataRecord wrapWithTimeStamp(String uom, boolean isUomCode, String timeRef, DataComponent... subComponents)
    {
        DataRecord rec = newDataRecord(subComponents.length + 1);
        
        Time c1 = newTime();
        
        if (isUomCode)
            c1.getUom().setCode(uom);
        else
            c1.getUom().setHref(Time.ISO_TIME_UNIT);
        
        c1.setDefinition(SWEConstants.DEF_SAMPLING_TIME);
        c1.setReferenceFrame(timeRef);
        rec.addComponent("time", c1);
        
        for (DataComponent childComp: subComponents)
            rec.addComponent(childComp.getName(), childComp);
        
        return rec;
    }
    
    
    /**
     * Builds a 2D-array component representing an RGB image
     * @param width
     * @param height
     * @param dataType
     * @return new DataArray instance
     */
    public DataArray newRgbImage(int width, int height, DataType dataType)
    {
        DataArray imgArray = newDataArray(height);
        imgArray.setDefinition(DEF_IMAGE);
        DataArray imgRow = newDataArray(width);
                
        DataRecord imgPixel = newDataRecord(3);
        if (dataType.isIntegralType())
        {
            imgPixel.addComponent("red", newCount(dataType));
            imgPixel.addComponent("green", newCount(dataType));
            imgPixel.addComponent("blue", newCount(dataType));
        }
        else
        {
            imgPixel.addComponent("red", newQuantity(dataType));
            imgPixel.addComponent("green", newQuantity(dataType));
            imgPixel.addComponent("blue", newQuantity(dataType));            
        }
        
        imgRow.addComponent("pixel", imgPixel);
        imgArray.setElementType("row", imgRow);
        return imgArray;
    }
    
    
    /**
     * Builds a vector component with the specified axes
     * @param def Definition of the whole vector
     * @param crs reference frame of the vector
     * @param names array containing name of each individual vector element
     * @param defs array containing semantic definition of each individual vector element
     * @param uoms array containing unit of measure of each individual vector element
     * @param axes array containing axis name of each individual vector element
     * @return new Vector component instance
     */
    public Vector newLocationVector(String def, String crs, String[] names, String[] defs, String[] uoms, String[] axes)
    {
        Vector loc = newVector();
        loc.setDefinition(def);
        loc.setReferenceFrame(crs);

        Quantity c;        
        for (int i = 0; i < names.length; i++)
        {
            c = newQuantity(DataType.DOUBLE);
            c.getUom().setCode(uoms[i]);
            c.setDefinition(defs[i]);
            c.setAxisID(axes[i]);
            loc.addComponent(names[i], c);
        }
        
        return loc;
    }
    
    
    /* ******************************** */
    /* Encoding and parser/writer stuff */
    /* ******************************** */
    
    /**
     * Helper method to instantiate the proper parser for the given encoding
     * @param encoding
     * @return instance of parser capable of handling the given encoding
     */
    public static DataStreamParser createDataParser(DataEncoding encoding)
    {
        DataStreamParser parser = null;
        
        if (encoding instanceof TextEncoding)
            parser = new AsciiDataParser();
        else if (encoding instanceof BinaryEncoding)
            parser = new BinaryDataParser();
        else if (encoding instanceof XMLEncoding)
            parser = new XmlDataParser();
        else if (encoding instanceof JSONEncoding)
            parser = new JSONDataParser();
        
        parser.setDataEncoding(encoding);
        return parser;
    }
    
    
    /**
     * Helper method to instantiate the proper writer for the given encoding
     * @param encoding
     * @return instance of writer capable of handling the given encoding
     */
    public static DataStreamWriter createDataWriter(DataEncoding encoding)
    {
        DataStreamWriter writer = null;
        
        if (encoding instanceof TextEncoding)
            writer = new AsciiDataWriter();
        else if (encoding instanceof BinaryEncoding)
            writer = new BinaryDataWriter();
        else if (encoding instanceof XMLEncoding)
            writer = new XmlDataWriter();
        else if (encoding instanceof JSONEncoding)
            writer = new JSONDataWriter();
        
        writer.setDataEncoding(encoding);
        return writer;
    }
    
    
    public static DataEncoding getDefaultEncoding(DataComponent dataComponents)
    {
        // TODO scan to see if there is a large array and use binary encoding in that case
        
        return new TextEncodingImpl();
    }
    
    
    /**
     * Get default binary encoding for the given component tree.<br/>
     * Data types used will be ones specified in each scalar component.
     * @param dataComponents component whose children will be mapped to encoding options
     * @return binary encoding instance pre-configured for the component
     */
    public static BinaryEncoding getDefaultBinaryEncoding(DataComponent dataComponents)
    {
        BinaryEncodingImpl encoding = new BinaryEncodingImpl();
        encoding.setByteEncoding(ByteEncoding.RAW);
        encoding.setByteOrder(ByteOrder.BIG_ENDIAN);
        
        // use default encoding info for each data value
        ScalarIterator it = new ScalarIterator(dataComponents);
        while (it.hasNext())
        {
            DataComponent[] nextPath = it.nextPath();
            DataValue nextScalar = (DataValue)nextPath[nextPath.length-1];
            
            // build path (just use / for root)
            StringBuffer pathString = new StringBuffer();
            pathString.append(DataComponentHelper.PATH_SEPARATOR);
            for (int i = 0; i < nextPath.length; i++)
            {
                pathString.append(nextPath[i].getName());
                pathString.append(DataComponentHelper.PATH_SEPARATOR);
            }
            
            BinaryComponentImpl binaryOpts = new BinaryComponentImpl();
            binaryOpts.setCdmDataType(nextScalar.getDataType());
            binaryOpts.setRef(pathString.substring(0, pathString.length()-1));
            
            encoding.addMemberAsComponent(binaryOpts);
            nextScalar.setEncodingInfo(binaryOpts);
        }
        
        return encoding;
    }
    
    
    /**
     * Ajusts encoding settings to ensures that data produced with the encoding
     * can be embedded in XML (e.g. for binary encoding this enforces base64)
     * @param encoding
     * @return copy of encoding, reconfigured appropriately
     */
    public static DataEncoding ensureXmlCompatible(DataEncoding encoding)
    {
        // if binary data, ensure encoding is set to base64
        if (encoding instanceof BinaryEncoding)
        {
            encoding = encoding.copy();
            ((BinaryEncoding) encoding).setByteEncoding(ByteEncoding.BASE_64);
        }
        
        return encoding;
    }
    
    
    /**
     * Assigns binary components and blocks definitions to the actual data component.
     * This sets the encodingInfo attribute of the component so it can be used to generate specialized datablocks.
     * For scalars, it also sets the default data type so it is the same as in the encoded stream.
     * @param dataComponents 
     * @param encoding 
     * @throws CDMException 
     */
    public static void assignBinaryEncoding(DataComponent dataComponents, BinaryEncoding encoding) throws CDMException
    {
        for (BinaryMember binaryOpts: encoding.getMemberList())
        {
            DataComponent comp = DataComponentHelper.findComponentByPath(binaryOpts.getRef(), dataComponents);
            ((AbstractDataComponentImpl)comp).setEncodingInfo(binaryOpts);
            
            // for scalars, also set default data type
            if (binaryOpts instanceof BinaryComponent)
                ((AbstractSimpleComponentImpl)comp).setDataType(((BinaryComponentImpl)binaryOpts).getCdmDataType());
        }
        
        // set default data type for implicit array size components if not already set
        DataIterator it = new DataIterator(dataComponents);
        while (it.hasNext())
        {
            DataComponent comp = it.next();
            if (comp instanceof DataArrayImpl)
            {
                CountImpl count = (CountImpl)((DataArrayImpl)comp).getArraySizeComponent();
                if (count != null && count.getEncodingInfo() == null)
                {
                    BinaryComponent binaryOpts = new BinaryComponentImpl();
                    binaryOpts.setCdmDataType(count.getDataType());
                    count.setEncodingInfo(binaryOpts);
                }
            }
        }
    }
}
