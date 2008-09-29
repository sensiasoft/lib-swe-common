/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "OGC Service Framework".
 
 The Initial Developer of the Original Code is the VAST team at the University of Alabama in Huntsville (UAH). <http://vast.uah.edu> Portions created by the Initial Developer are Copyright (C) 2007 the Initial Developer. All Rights Reserved. Please Contact Mike Botts <mike.botts@uah.edu> for more information.
 
 Contributor(s): 
    Alexandre Robin <robin@nsstc.uah.edu>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ogc.om;

import java.io.OutputStream;

import org.vast.xml.DOMHelper;
import org.w3c.dom.Element;


/**
 * <p><b>Title:</b>
 * Observation Writer
 * </p>
 *
 * <p><b>Description:</b><br/>
 * Base interface for Observation Writers of all versions
 * </p>
 *
 * <p>Copyright (c) 2007</p>
 * @author Alexandre Robin
 * @date Feb 23, 2007
 * @version 1.0
 */
public interface ObservationWriter
{
    
    /**
     * Writes XML for an Observation in the given OutputStream
     * @param os
     * @param obs
     * @throws OMException
     */
    public void writeObservation(OutputStream os, AbstractObservation obs) throws OMException;
    
    
    /**
     * Builds a DOM Element for the given Observation 
     * @param dom
     * @param obs
     * @return
     * @throws OMException
     */
    public Element writeObservation(DOMHelper dom, AbstractObservation obs) throws OMException;
    
}
