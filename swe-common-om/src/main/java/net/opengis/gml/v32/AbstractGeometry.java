/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2015 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package net.opengis.gml.v32;



/**
 * POJO class for XML type AbstractGeometryType(@http://www.opengis.net/gml/3.2).
 *
 * This is a complex type.
 */
@SuppressWarnings("javadoc")
public interface AbstractGeometry extends AbstractGML
{
    
    /**
     * @return the geometry bounding (hyper)rectangle
     */
    public Envelope getGeomEnvelope();
    
    
    /**
     * Gets the srsName property
     */
    public String getSrsName();
    
    
    /**
     * Checks if srsName is set
     */
    public boolean isSetSrsName();
    
    
    /**
     * Sets the srsName property
     */
    public void setSrsName(String srsName);
    
    
    /**
     * Gets the srsDimension property
     */
    public int getSrsDimension();
    
    
    /**
     * Checks if srsDimension is set
     */
    public boolean isSetSrsDimension();
    
    
    /**
     * Sets the srsDimension property
     */
    public void setSrsDimension(int srsDimension);
    
    
    /**
     * Unsets the srsDimension property
     */
    public void unSetSrsDimension();
    
    
    /**
     * Gets the axisLabels property
     */
    public String[] getAxisLabels();
    
    
    /**
     * Checks if axisLabels is set
     */
    public boolean isSetAxisLabels();
    
    
    /**
     * Sets the axisLabels property
     */
    public void setAxisLabels(String[] axisLabels);
    
    
    /**
     * Gets the uomLabels property
     */
    public String[] getUomLabels();
    
    
    /**
     * Checks if uomLabels is set
     */
    public boolean isSetUomLabels();
    
    
    /**
     * Sets the uomLabels property
     */
    public void setUomLabels(String[] uomLabels);
}
