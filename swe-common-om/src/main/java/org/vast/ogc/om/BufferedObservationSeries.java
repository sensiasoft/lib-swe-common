/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Initial Developer of the Original Code is Sensia Software LLC.
 Portions created by the Initial Developer are Copyright (C) 2014
 the Initial Developer. All Rights Reserved.

 Please Contact Alexandre Robin <alex.robin@sensiasoftware.com> or 
 Mike Botts <mike.botts@botts-inc.net for more information.
 
 Contributor(s): 
    Alexandre Robin <alex.robin@sensiasoftware.com>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ogc.om;

import java.util.LinkedHashMap;
import java.util.Map;
import javax.xml.namespace.QName;
import org.vast.cdm.common.DataBlock;
import org.vast.cdm.common.DataComponent;
import org.vast.cdm.common.DataEncoding;
import org.vast.ogc.gml.FeatureImpl;
import org.vast.ogc.gml.IFeature;
import org.vast.sweCommon.SWEData;
import org.vast.util.TimeExtent;


/**
 * <p>
 * Implementation of IObservationSeries whose data is stored entirely in memory
 * and is fully mutable. Data is stored internally using a DataList object.
 * </p>
 *
 * <p>Copyright (c) 2012</p>
 * @author Alexandre Robin
 * @since Aug 20, 2012
 * @version 1.0
 */
public class BufferedObservationSeries extends FeatureImpl implements IObservationSeries
{
    protected TimeExtent phenomenonTimeExtent;
    protected TimeExtent resultTimeExtent;
    protected TimeExtent validTimeExtent;
    protected IProcedure procedure;
    protected Map<String, IFeature> featuresOfInterest;
    protected SWEData resultData;
    
    
    public BufferedObservationSeries()
    {
        super(new QName("ObservationSeries"));
        featuresOfInterest = new LinkedHashMap<String, IFeature>();
    }
    
    
    @Override
    public TimeExtent getPhenomenonTimeExtent()
    {
        return this.phenomenonTimeExtent;
    }
    

    @Override
    public void setPhenomenonTimeExtent(TimeExtent timeExtent)
    {
        this.phenomenonTimeExtent = timeExtent;
    }
    

    @Override
    public TimeExtent getResultTimeExtent()
    {
        return this.resultTimeExtent;
    }
    

    @Override
    public void setResultTimeExtent(TimeExtent timeExtent)
    {
        this.resultTimeExtent = timeExtent;
    }
    

    @Override
    public Map<String, IFeature> getFeaturesOfInterest()
    {
        return featuresOfInterest;
    }
    

    @Override
    public IProcedure getProcedure()
    {
        return procedure;
    }


    @Override
    public void setProcedure(IProcedure procedure)
    {
        this.procedure = procedure;
    }


    @Override
    public int getElementCount()
    {
        return resultData.getElementCount();
    }


    @Override
    public DataComponent getElementType()
    {
        return resultData.getElementType();
    }


    @Override
    public void setElementType(DataComponent elementType)
    {
        resultData.setElementType(elementType);
    }


    @Override
    public DataEncoding getEncoding()
    {
        return resultData.getEncoding();
    }


    @Override
    public void setEncoding(DataEncoding dataEncoding)
    {
        resultData.setEncoding(dataEncoding);
    }


    @Override
    public DataComponent getNextElement()
    {
        return resultData.getNextElement();
    }


    @Override
    public DataBlock getNextDataBlock()
    {
        return resultData.getNextDataBlock();
    }


    @Override
    public void pushNextDataBlock(DataBlock dataBlock)
    {
        resultData.pushNextDataBlock(dataBlock);
    }
}
