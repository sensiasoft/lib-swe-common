/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2015 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.swe;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.HashSet;
import org.vast.cdm.common.CDMException;
import net.opengis.swe.v20.DataComponent;
import net.opengis.swe.v20.DataEncoding;
import net.opengis.swe.v20.ScalarComponent;


/**
 * <p>
 * Delegating writer for writing only the selected components.<br/>
 * This is used to implement filtering by observed property in SOS. 
 * </p>
 *
 * @author Alex Robin
 * @since Oct 9, 2015
 */
public class FilteredWriter extends AbstractDataWriter
{
    AbstractDataWriter writer;
    HashSet<String> enabledDefUris;
    Record enabledRecord;
    
    
    public FilteredWriter(AbstractDataWriter writer, Collection<String> enabledDefUris)
    {
        this.writer = writer;
        this.enabledDefUris = new HashSet<>();
        this.enabledDefUris.addAll(enabledDefUris);
    }

    
    @Override
    protected void processAtom(ScalarComponent component) throws IOException
    {
        if (isComponentEnabled(component) || isParentEnabled(component))
            writer.processAtom(component);
    }


    @Override
    protected boolean processBlock(DataComponent component) throws IOException
    {
        if (!isParentEnabled(component) && isComponentEnabled(component))
            enabledRecord = currentRecord;
        
        return writer.processBlock(component);
    }
    
    
    private boolean isParentEnabled(DataComponent component)
    {
        if (enabledRecord != null && componentStack.contains(enabledRecord))
            return true;
        
        return false;
    }
    
    
    private boolean isComponentEnabled(DataComponent component)
    {
        String defUri = component.getDefinition();
        
        if (defUri == null)
            return false;
        
        if (enabledDefUris != null)
        {
            if (!enabledDefUris.contains(defUri))
                return false;
        }
        
        return true;
    }
    
    
    @Override
    protected void endDataBlock() throws CDMException, IOException
    {
        writer.endDataBlock();
    }
    
    
    @Override
    public void setDataComponents(DataComponent dataInfo)
    {
        writer.setDataComponents(dataInfo);
        super.dataComponents = writer.getDataComponents();
    }


    @Override
    public void setDataEncoding(DataEncoding dataEncoding)
    {
        writer.setDataEncoding(dataEncoding);
        super.dataEncoding = writer.getDataEncoding();
    }


    @Override
    public void setOutput(OutputStream os) throws IOException
    {
        writer.setOutput(os);
    }


    @Override
    public void close() throws IOException
    {
        writer.close();        
    }


    @Override
    public void flush() throws IOException
    {
        writer.flush();
    }


    @Override
    public void reset()
    {
        super.reset();
        writer.reset();
    }
}
