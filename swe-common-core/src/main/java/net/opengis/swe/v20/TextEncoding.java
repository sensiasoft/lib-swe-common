/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2015 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package net.opengis.swe.v20;



/**
 * POJO class for XML type TextEncodingType(@http://www.opengis.net/swe/2.0).
 *
 * This is a complex type.
 */
@SuppressWarnings("javadoc")
public interface TextEncoding extends DataEncoding
{
    
    
    /**
     * Gets the collapseWhiteSpaces property
     */
    public boolean getCollapseWhiteSpaces();
    
    
    /**
     * Checks if collapseWhiteSpaces is set
     */
    public boolean isSetCollapseWhiteSpaces();
    
    
    /**
     * Sets the collapseWhiteSpaces property
     */
    public void setCollapseWhiteSpaces(boolean collapseWhiteSpaces);
    
    
    /**
     * Unsets the collapseWhiteSpaces property
     */
    public void unSetCollapseWhiteSpaces();
    
    
    /**
     * Gets the decimalSeparator property
     */
    public String getDecimalSeparator();
    
    
    /**
     * Checks if decimalSeparator is set
     */
    public boolean isSetDecimalSeparator();
    
    
    /**
     * Sets the decimalSeparator property
     */
    public void setDecimalSeparator(String decimalSeparator);
    
    
    /**
     * Gets the tokenSeparator property
     */
    public String getTokenSeparator();
    
    
    /**
     * Sets the tokenSeparator property
     */
    public void setTokenSeparator(String tokenSeparator);
    
    
    /**
     * Gets the blockSeparator property
     */
    public String getBlockSeparator();
    
    
    /**
     * Sets the blockSeparator property
     */
    public void setBlockSeparator(String blockSeparator);
}
