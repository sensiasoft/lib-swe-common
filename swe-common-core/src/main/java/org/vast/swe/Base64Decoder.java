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
 * Filter Stream used to decode a base64 encoded input stream
 * to a raw binary stream. This is inspired from Apache Jakarta
 * Commons Base64 encoder/decoder class.  
 * </p>
 *
 * @author Alex Robin
 * @since Feb 14, 2006
 * */
public class Base64Decoder extends FilterInputStream
{
    private byte[] charBuf = new byte[4];
    private byte[] byteBuf = new byte[2];
    private byte[] byteBufSave = new byte[2];
    private int unusedBytes = 0;
    private int unusedBytesSave = 0;
    private static byte[] base64ToVal = new byte[256];

    
    // build lookup arrays
    static
    {
        for (int i = 0; i < 256; i++)
            base64ToVal[i] = (byte) -1;

        for (int i = 'A', j = 0; i <= 'Z'; i++, j++)
            base64ToVal[i] = (byte) (j);

        for (int i = 'a', j = 0; i <= 'z'; i++, j++)
            base64ToVal[i] = (byte) (j + 26);

        for (int i = '0', j = 0; i <= '9'; i++, j++)
            base64ToVal[i] = (byte) (j + 52);
        
        base64ToVal['+'] = 62;
        base64ToVal['/'] = 63;
        base64ToVal['='] = 0;
    }


    public Base64Decoder(InputStream in)
    {
        super(in);
    }


    @Override
    public int read() throws IOException
    {
        byte[] b = new byte[1];
        int val = read(b, 0, 1);

        if (val <= 0)
            return -1;
        else
            return (b[0]&0xFF);
    }


    @Override
    public int read(byte[] b, int off, int length) throws IOException
    {
        int numDecodedByte = 0;
        int destIndex = off;
        
        // add unused bytes at begining of buffer (max 2!)
        for (int i=0; i<unusedBytes; i++)
        {
            b[destIndex] = byteBuf[i];
            numDecodedByte++;
            destIndex++;
                        
            // if desired length was less than number of unused bytes
            if (numDecodedByte >= length)
            {
                // shift bytes in buffer
                byteBuf[0] = byteBuf[1];
                unusedBytes -= numDecodedByte;
                return numDecodedByte;
            }
        }
                
        // read new data
        unusedBytes = 0;
        while (numDecodedByte < length)
        {
            int val = 0;
                        
            // read next quadruplet
            for (int j=0; j<4; j++)
            {
                val = in.read();
                
                // stop if EOF
                if (val == -1)
                    break;
                
                // discard non base64 character
                if (base64ToVal[val] == -1)
                {
                    j--;
                    continue;
                }

                charBuf[j] = (byte)val;
            }
            
            if (val == -1)
                break;
            
            // lookup bits values
            byte b1 = base64ToVal[charBuf[0]];
            byte b2 = base64ToVal[charBuf[1]];
            byte b3 = base64ToVal[charBuf[2]];
            byte b4 = base64ToVal[charBuf[3]];
            
            // compute byte 1
            b[destIndex] = (byte) (b1 << 2 | b2 >>> 4);
            numDecodedByte++;
            destIndex++;
            
            // compute byte 2
            if (charBuf[2] != '=')
            {
                byte byte2 = (byte) ((b2 << 4) | (b3 >>> 2));
                
                if (length > numDecodedByte)
                {
                    b[destIndex] = byte2;
                    numDecodedByte++;
                    destIndex++;
                }
                else
                {
                    byteBuf[unusedBytes] = byte2;
                    unusedBytes++;
                }
            }
            
            // compute byte 3
            if (charBuf[3] != '=')
            {
                byte byte3 = (byte) (b3 << 6 | b4);
                
                if (length > numDecodedByte)
                {
                    b[destIndex] = byte3;
                    numDecodedByte++;
                    destIndex++;
                }
                else
                {
                    byteBuf[unusedBytes] = byte3;
                    unusedBytes++;
                }
            }
            
            //System.out.println(length + " - " + numDecodedByte);
            //if (charBuf[2] == '=' || charBuf[3] == '=')
            //    break;
        }       

        return numDecodedByte;
    }


    @Override
    public int read(byte[] b) throws IOException
    {
        return read(b, 0, b.length);
    }


    @Override
    public synchronized void mark(int readlimit)
    {
        byteBufSave[0] = byteBuf[0];
        byteBufSave[1] = byteBuf[1];
        unusedBytesSave = unusedBytes;
        super.mark(readlimit+3);
    }


    @Override
    public synchronized void reset() throws IOException
    {
        byteBuf[0] = byteBufSave[0];
        byteBuf[1] = byteBufSave[1];
        unusedBytes = unusedBytesSave;
        super.reset();
    }   
}
