/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "SensorML DataProcessing Engine".
 
 The Initial Developer of the Original Code is the VAST team at the University of Alabama in Huntsville (UAH). <http://vast.uah.edu> Portions created by the Initial Developer are Copyright (C) 2007 the Initial Developer. All Rights Reserved. Please Contact Mike Botts <mike.botts@uah.edu> for more information.
 
 Contributor(s): 
    Alexandre Robin <robin@nsstc.uah.edu>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.cdm.common;


/**
 * <p>
 * Binary Block holds the attributes of Swe Common Binary Block encodings.
 * It extends Binary Options.
 * </p>
 *
 * <p>Copyright (c) 2007</p>
 * @author Alexandre Robin & Gregoire Berthiau
 * @since Sep 15, 2008
 * @version 1.0
 */
public class BinaryBlock extends BinaryOptions
{    
    private static final long serialVersionUID = -5489375012750387976L;
    public String encryption;
    public String compression;
    
    
    public BinaryBlock() 
    {
    }
    
    
    public BinaryBlock copy()
    {
    	BinaryBlock newVal = new BinaryBlock();
    	newVal.compression = compression;
    	newVal.encryption = encryption;
    	newVal.byteLength = byteLength;
    	newVal.paddingAfter = paddingAfter;
    	newVal.paddingBefore = paddingBefore;
    	newVal.componentName = new String(componentName);
    	return newVal;
    }


    public String toString(String indent)
    {
        StringBuffer text = new StringBuffer();
        text.append("BinaryValue: ");
                
        text.append(this.byteLength + " bits, " + this.componentName + "\n");

        return text.toString();
    }
}
