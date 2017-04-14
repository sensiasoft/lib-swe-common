/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2015 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package net.opengis.gml.v32.impl;

import net.opengis.gml.v32.Envelope;


/**
 * <p>
 * Implementation of GML Envelope derived from JTS Envelope class.
 * </p>
 *
 * @author Alex Robin
 * @since Dec 23, 2014
 */
public class EnvelopeJTS extends com.vividsolutions.jts.geom.Envelope implements Envelope
{
    private static final long serialVersionUID = 9013205842041680887L;
    protected String srsName;
    protected Integer srsDimension;
    protected String[] axisLabels;
    protected String[] uomLabels;
    
    
    public EnvelopeJTS()
    {
    }
    
    
    /**
     * Gets the lowerCorner property
     */
    @Override
    public double[] getLowerCorner()
    {
        return new double[] {getMinX(), getMinY()};
    }
    
    
    /**
     * Checks if lowerCorner is set
     */
    @Override
    public boolean isSetLowerCorner()
    {
        return !isNull();
    }
    
    
    /**
     * Sets the lowerCorner property
     */
    @Override
    public void setLowerCorner(double[] lowerCorner)
    {
        if (!isNull())
        {
            double maxX = getMaxX();
            double maxY = getMaxY();
            super.init(lowerCorner[0], maxX, lowerCorner[1], maxY);
        }
        else
            super.expandToInclude(lowerCorner[0], lowerCorner[1]);
    }
    
    
    /**
     * Gets the upperCorner property
     */
    @Override
    public double[] getUpperCorner()
    {
        return new double[] {getMaxX(), getMaxY()};
    }
    
    
    /**
     * Checks if upperCorner is set
     */
    @Override
    public boolean isSetUpperCorner()
    {
        return !isNull();
    }
    
    
    /**
     * Sets the upperCorner property
     */
    @Override
    public void setUpperCorner(double[] upperCorner)
    {
        if (!isNull())
        {
            double minX = getMinX();
            double minY = getMinY();
            super.init(minX, upperCorner[0], minY, upperCorner[1]);
        }
        else
            super.expandToInclude(upperCorner[0], upperCorner[1]);
    }
    
    
    /**
     * Gets the srsName property
     */
    @Override
    public String getSrsName()
    {
        return srsName;
    }
    
    
    /**
     * Checks if srsName is set
     */
    @Override
    public boolean isSetSrsName()
    {
        return (srsName != null);
    }
    
    
    /**
     * Sets the srsName property
     */
    @Override
    public void setSrsName(String srsName)
    {
        this.srsName = srsName;
    }
    
    
    /**
     * Gets the srsDimension property
     */
    @Override
    public int getSrsDimension()
    {
        return srsDimension;
    }
    
    
    /**
     * Checks if srsDimension is set
     */
    @Override
    public boolean isSetSrsDimension()
    {
        return (srsDimension != null);
    }
    
    
    /**
     * Sets the srsDimension property
     */
    @Override
    public void setSrsDimension(int srsDimension)
    {
        this.srsDimension = srsDimension;
    }
    
    
    /**
     * Unsets the srsDimension property
     */
    @Override
    public void unSetSrsDimension()
    {
        this.srsDimension = null;
    }
    
    
    /**
     * Gets the axisLabels property
     */
    @Override
    public String[] getAxisLabels()
    {
        return axisLabels;
    }
    
    
    /**
     * Checks if axisLabels is set
     */
    @Override
    public boolean isSetAxisLabels()
    {
        return (axisLabels != null);
    }
    
    
    /**
     * Sets the axisLabels property
     */
    @Override
    public void setAxisLabels(String[] axisLabels)
    {
        this.axisLabels = axisLabels;
    }
    
    
    /**
     * Gets the uomLabels property
     */
    @Override
    public String[] getUomLabels()
    {
        return uomLabels;
    }
    
    
    /**
     * Checks if uomLabels is set
     */
    @Override
    public boolean isSetUomLabels()
    {
        return (uomLabels != null);
    }
    
    
    /**
     * Sets the uomLabels property
     */
    @Override
    public void setUomLabels(String[] uomLabels)
    {
        this.uomLabels = uomLabels;
    }
}
