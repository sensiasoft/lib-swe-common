/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "OGC Service Framework".
 
 The Initial Developer of the Original Code is the VAST team at the University of Alabama in Huntsville (UAH). <http://vast.uah.edu> Portions created by the Initial Developer are Copyright (C) 2007 the Initial Developer. All Rights Reserved. Please Contact Mike Botts <mike.botts@uah.edu> for more information.
 
 Contributor(s): 
    Alexandre Robin <robin@nsstc.uah.edu>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.decompression;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.vast.cdm.common.BinaryBlock;
import org.vast.cdm.common.CDMException;
import org.vast.cdm.common.DataComponent;
import org.vast.cdm.common.DataHandler;
import org.vast.math.Vector3d;
import org.vast.sweCommon.DataInputExt;
import org.vast.sweCommon.SWEReader;


/**
 * <p><b>Title:</b>
 * Compressed Stream Reader
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Base interface for Compressed Stream Readers of all versions
 * </p>
 *
 * <p>Copyright (c) 2007</p>
 * @author Alexandre Robin & Gregoire Berthiau
 * @date Sep 22, 2008
 * @version 1.0
 */
public abstract class CompressedStreamReader
{
    
    /**
     * Reads an Observation object from the given InputStream
     * @param is
     * @param dataHandler
     * @return
     * @throws OMException
     */
    public void readCompressedStream(DataInputExt inputStream, DataComponent blockInfo) throws CDMException
    {
        parse(inputStream, blockInfo);

    }
    
    protected abstract void parse(DataInputExt inputStream, DataComponent blockInfo) throws CDMException;

    public abstract void init(BinaryBlock binaryBlock, DataComponent blockComponent) throws CDMException;
    
}
