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
 * <p>
 * Tagging interface for data components with local and reference frames
 * </p>
 *
 * @author Alex Robin
 * @since Nov 8, 2014
 */
@SuppressWarnings("javadoc")
public interface HasRefFrames
{

    /**
     * Gets the referenceFrame property
     */
    public abstract String getReferenceFrame();
    
    
    /**
     * Checks if referenceFrame is set
     */
    public boolean isSetReferenceFrame();


    /**
     * Sets the referenceFrame property
     */
    public abstract void setReferenceFrame(String referenceFrame);


    /**
     * Gets the localFrame property
     */
    public abstract String getLocalFrame();


    /**
     * Checks if localFrame is set
     */
    public abstract boolean isSetLocalFrame();


    /**
     * Sets the localFrame property
     */
    public abstract void setLocalFrame(String localFrame);

}