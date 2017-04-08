/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2017 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.swe.fast;

import java.io.IOException;
import java.io.Writer;


/**
 * <p>
 * Null writer used to skip data, thus it doesn't actually write anything!
 * </p>
 *
 * @author Alex Robin
 * @since Feb 20, 2017
 */
public class NullWriter extends Writer
{

    @Override
    public Writer append(char c) throws IOException
    {
        return this;
    }


    @Override
    public Writer append(CharSequence csq, int start, int end) throws IOException
    {
        return this;
    }


    @Override
    public Writer append(CharSequence csq) throws IOException
    {
        return this;
    }


    @Override
    public void write(char[] cbuf) throws IOException
    {
    }


    @Override
    public void write(int c) throws IOException
    {
    }


    @Override
    public void write(String str, int off, int len) throws IOException
    {
    }


    @Override
    public void write(String str) throws IOException
    {
        // TODO Auto-generated method stub
        super.write(str);
    }


    @Override
    public void write(char[] arg0, int arg1, int arg2) throws IOException
    {        
    }
    
    
    @Override
    public void close() throws IOException
    {        
    }


    @Override
    public void flush() throws IOException
    {
    }

}
