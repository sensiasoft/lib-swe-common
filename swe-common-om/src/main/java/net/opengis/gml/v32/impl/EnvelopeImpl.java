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

import java.util.Arrays;
import java.util.Objects;
import net.opengis.gml.v32.Envelope;


/**
 * POJO class for XML type EnvelopeType(@http://www.opengis.net/gml/3.2).
 *
 * This is a complex type.
 */
public class EnvelopeImpl implements Envelope
{
    private static final long serialVersionUID = -7116070138059440115L;
    protected double[] lowerCorner;
    protected double[] upperCorner;
    protected String srsName;
    protected Integer srsDimension;
    protected String[] axisLabels;
    protected String[] uomLabels;
    
    
    public EnvelopeImpl()
    {
    }
    
    
    public EnvelopeImpl(int numDims)
    {
        lowerCorner = new double[numDims];
        upperCorner = new double[numDims];
        srsDimension = numDims;        
    }
    
    
    public EnvelopeImpl(String crs, double minX, double maxX, double minY, double maxY)
    {
        lowerCorner = new double[] {minX, minY};
        upperCorner = new double[] {maxX, maxY};
        srsDimension = 2;
        srsName = crs;
    }
    
    
    public EnvelopeImpl(String crs, double minX, double maxX, double minY, double maxY, double minZ, double maxZ)
    {
        lowerCorner = new double[] {minX, minY, minZ};
        upperCorner = new double[] {maxX, maxY, maxZ};
        srsDimension = 3;
        srsName = crs;
    }
    
    
    public EnvelopeImpl(String crs, double[] lowerCorner, double[] upperCorner)
    {
        this.lowerCorner = lowerCorner;
        this.upperCorner = upperCorner;
        this.srsDimension = lowerCorner.length;
        this.srsName = crs;
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


    @Override
    public boolean equals(Object obj)
    {
        return obj instanceof Envelope &&
               Objects.equals(getSrsName(), ((Envelope)obj).getSrsName()) &&
               Arrays.equals(getLowerCorner(), ((Envelope)obj).getLowerCorner()) &&
               Arrays.equals(getUpperCorner(), ((Envelope)obj).getUpperCorner());
    }
    
    
    @Override
    public int hashCode()
    {
        return Objects.hash(srsName,
                            Arrays.hashCode(lowerCorner),
                            Arrays.hashCode(upperCorner));
    }
}
