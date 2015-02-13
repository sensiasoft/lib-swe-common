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
 * POJO class for XML type EnvelopeType(@http://www.opengis.net/gml/3.2).
 *
 * This is a complex type.
 */
public class EnvelopeImpl implements Envelope
{
    static final long serialVersionUID = 1L;
    protected double[] lowerCorner;
    protected double[] upperCorner;
    protected String srsName;
    protected Integer srsDimension;
    protected String[] axisLabels;
    protected String[] uomLabels;
    
    
    public EnvelopeImpl()
    {
    }
    
    
    /**
     * Gets the lowerCorner property
     */
    @Override
    public double[] getLowerCorner()
    {
        return lowerCorner;
    }
    
    
    /**
     * Checks if lowerCorner is set
     */
    @Override
    public boolean isSetLowerCorner()
    {
        return (lowerCorner != null);
    }
    
    
    /**
     * Sets the lowerCorner property
     */
    @Override
    public void setLowerCorner(double[] lowerCorner)
    {
        this.lowerCorner = lowerCorner;
    }
    
    
    /**
     * Gets the upperCorner property
     */
    @Override
    public double[] getUpperCorner()
    {
        return upperCorner;
    }
    
    
    /**
     * Checks if upperCorner is set
     */
    @Override
    public boolean isSetUpperCorner()
    {
        return (upperCorner != null);
    }
    
    
    /**
     * Sets the upperCorner property
     */
    @Override
    public void setUpperCorner(double[] upperCorner)
    {
        this.upperCorner = upperCorner;
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
