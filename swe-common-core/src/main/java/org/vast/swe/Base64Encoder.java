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

import java.io.*;


/**
 * <p>
 * Filter Stream used to encode binary data from output stream
 * to a base64 stream. This is inspired from Apache Jakarta
 * Commons Base64 encoder/decoder class.  
 * </p>
 *
 * @author Alex Robin
 * @since Feb 14, 2006
 * */
public class Base64Encoder extends FilterOutputStream
{
    private byte[] charBuf = new byte[4];
    private byte[] byteBuf = new byte[2];
    private int unusedByte = 0;
    private byte[] byteToWrite = new byte[1];
    private static byte[] ValToBase64 = new byte[64];
    public boolean addCR = true;
    private int charCount = 0;

    // build lookup arrays
    static
    {
        for (int i = 0; i <= 25; i++)
            ValToBase64[i] = (byte) ('A' + i);

        for (int i = 26, j = 0; i <= 51; i++, j++)
            ValToBase64[i] = (byte) ('a' + j);

        for (int i = 52, j = 0; i <= 61; i++, j++)
            ValToBase64[i] = (byte) ('0' + j);

        ValToBase64[62] = (byte) '+';
        ValToBase64[63] = (byte) '/';
    }


    public Base64Encoder(OutputStream out)
    {
        super(out);
    }


    @Override
    public void write(byte[] b, int off, int len) throws IOException
    {
        int byteIndex = off;
        int byteLeft = len;

        while ((unusedByte + byteLeft) >= 3)
        {
            byte b1, b2, b3;
            
            if (unusedByte == 0)
            {
                b1 = b[byteIndex];
                byteIndex++;
                byteLeft--;
            }
            else // use previously unused byte if available
            {
                b1 = byteBuf[0];
                unusedByte--;
            }
            
            if (unusedByte == 0)
            {
                b2 = b[byteIndex];
                byteIndex++;
                byteLeft--;
            }
            else // use previously unused byte if available
            {
                b2 = byteBuf[1];
                unusedByte--;
            }
            
            b3 = b[byteIndex];
            byteIndex++;
            byteLeft--;
            
            byte c1 = (byte) ((b1 >> 2) & 0x3F);
            byte c2 = (byte) (((b1 & 0x03) << 4) | ((b2 >> 4) & 0x0F));
            byte c3 = (byte) (((b2 & 0x0F) << 2) | ((b3 >> 6) & 0x03));
            byte c4 = (byte) (b3 & 0x3F);

            charBuf[0] = ValToBase64[c1&0xFF];
            charBuf[1] = ValToBase64[c2&0xFF];
            charBuf[2] = ValToBase64[c3&0xFF];
            charBuf[3] = ValToBase64[c4&0xFF];

            out.write(charBuf, 0, 4);
            addCR();
        }
        
        // keep unused bytes in local buffer
        for (int i=0; i<byteLeft; i++)
            byteBuf[unusedByte+i] = b[byteIndex+i];
        unusedByte += byteLeft;
    }


    @Override
    public void write(byte[] b) throws IOException
    {
        write(b, 0, b.length);
    }


    @Override
    public void write(int b) throws IOException
    {
        byteToWrite[0] = (byte) b;
        write(byteToWrite, 0, 1);
    }


    @Override
    public void flush() throws IOException
    {
        if (unusedByte != 0)
        {
            // make sure we write unused bytes with eventual '=' padding chars
            if (unusedByte == 2)
            {
                byte b1 = byteBuf[0];
                byte b2 = byteBuf[1];
                
                byte c1 = (byte) ((b1 >> 2) & 0x3F);
                byte c2 = (byte) (((b1 & 0x03) << 4) | ((b2 >> 4) & 0x0F));
                byte c3 = (byte) ((b2 & 0x0F) << 2);
    
                charBuf[0] = ValToBase64[c1&0xFF];
                charBuf[1] = ValToBase64[c2&0xFF];
                charBuf[2] = ValToBase64[c3&0xFF];
                charBuf[3] = '=';
            }
            else if (unusedByte == 1)
            {
                byte b1 = byteBuf[0];
                
                byte c1 = (byte) ((b1 >> 2) & 0x3F);
                byte c2 = (byte) ((b1 & 0x03) << 4);
    
                charBuf[0] = ValToBase64[c1&0xFF];
                charBuf[1] = ValToBase64[c2&0xFF];
                charBuf[2] = '=';
                charBuf[3] = '=';
            }
            
            out.write(charBuf, 0, 4);
            addCR();
            unusedByte = 0;
        }
        
        super.flush();
    }
    
    
    protected void addCR() throws IOException
    {
        if (addCR)
        {
            charCount += 4;
            
            if (charCount >= 76)
            {
                out.write('\n');
                charCount = 0;
            }
        }
    }
    
    
    public static byte[] encode(byte[] data) throws IOException
    {
        ByteArrayOutputStream output = new ByteArrayOutputStream(data.length*4/3+3);
        Base64Encoder encoder = new Base64Encoder(output);
        encoder.write(data);
        encoder.close();
        return output.toByteArray();
    }
}
