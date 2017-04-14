/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2015 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.data;

import net.opengis.swe.v20.TextEncoding;


/**
 * POJO class for XML type TextEncodingType(@http://www.opengis.net/swe/2.0).
 *
 * This is a complex type.
 */
public class TextEncodingImpl extends AbstractEncodingImpl implements TextEncoding
{
    private static final long serialVersionUID = -2462346983639710029L;
    protected Boolean collapseWhiteSpaces = true;
    protected String decimalSeparator = ".";
    protected String tokenSeparator = ",";
    protected String blockSeparator = "\n";
    
    
    public TextEncodingImpl()
    {
    }
    
    
    public TextEncodingImpl(String tokenSeparator, String blockSeparator)
    {
        this.tokenSeparator = tokenSeparator;
        this.blockSeparator = blockSeparator;
    }
    
    
    @Override
    public TextEncodingImpl copy()
    {
        TextEncodingImpl newObj = new TextEncodingImpl();
        newObj.collapseWhiteSpaces = this.collapseWhiteSpaces;
        newObj.decimalSeparator = this.decimalSeparator;
        newObj.tokenSeparator = this.tokenSeparator;
        newObj.blockSeparator = this.blockSeparator;
        return newObj;
    }
    
    
    /**
     * Gets the collapseWhiteSpaces property
     */
    @Override
    public boolean getCollapseWhiteSpaces()
    {
        return collapseWhiteSpaces;
    }
    
    
    /**
     * Checks if collapseWhiteSpaces is set
     */
    @Override
    public boolean isSetCollapseWhiteSpaces()
    {
        return (collapseWhiteSpaces != null);
    }
    
    
    /**
     * Sets the collapseWhiteSpaces property
     */
    @Override
    public void setCollapseWhiteSpaces(boolean collapseWhiteSpaces)
    {
        this.collapseWhiteSpaces = collapseWhiteSpaces;
    }
    
    
    /**
     * Unsets the collapseWhiteSpaces property
     */
    @Override
    public void unSetCollapseWhiteSpaces()
    {
        this.collapseWhiteSpaces = null;
    }
    
    
    /**
     * Gets the decimalSeparator property
     */
    @Override
    public String getDecimalSeparator()
    {
        return decimalSeparator;
    }
    
    
    /**
     * Checks if decimalSeparator is set
     */
    @Override
    public boolean isSetDecimalSeparator()
    {
        return (decimalSeparator != null);
    }
    
    
    /**
     * Sets the decimalSeparator property
     */
    @Override
    public void setDecimalSeparator(String decimalSeparator)
    {
        this.decimalSeparator = decimalSeparator;
    }
    
    
    /**
     * Gets the tokenSeparator property
     */
    @Override
    public String getTokenSeparator()
    {
        return tokenSeparator;
    }
    
    
    /**
     * Sets the tokenSeparator property
     */
    @Override
    public void setTokenSeparator(String tokenSeparator)
    {
        this.tokenSeparator = tokenSeparator;
    }
    
    
    /**
     * Gets the blockSeparator property
     */
    @Override
    public String getBlockSeparator()
    {
        return blockSeparator;
    }
    
    
    /**
     * Sets the blockSeparator property
     */
    @Override
    public void setBlockSeparator(String blockSeparator)
    {
        this.blockSeparator = blockSeparator;
    }


    @Override
    public String toString()
    {
        StringBuilder buf = new StringBuilder();
        buf.append("TextEncoding:");
        buf.append(" blockSep=").append(blockSeparator).append(',');
        buf.append(" tokenSep=").append(tokenSeparator).append(',');
        buf.append(" decimalSep=").append(decimalSeparator).append(',');
        buf.append(" collapseWS=").append(collapseWhiteSpaces);
        return buf.toString();
    }
}
