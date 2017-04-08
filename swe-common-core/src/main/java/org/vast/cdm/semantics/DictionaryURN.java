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

package org.vast.cdm.semantics;


/**
 * <p>
 * TODO DictionaryURN type description
 * </p>
 *
 * @author Alex Robin
 * */
public class DictionaryURN
{
    // basic
    static public final String ogcData = "urn:x-ogc:def:phenomenon";
    static public final String bool = ogcData + ":boolean";
    static public final String count = ogcData + ":count";
    static public final String quantity = ogcData + ":quantity";
    static public final String time = quantity + ":time";
    static public final String category = ogcData + ":category";
    static public final String digital = ogcData + ":digital";
    
    // complex
    static public final String group = ogcData + ":group";
    static public final String array = ogcData + ":array";
    
    // geometrical
    static public final String distance = quantity + ":distance";
    static public final String angle = quantity + ":angle";
    
    // measurementIndex
    static public final String measurementIndex = count + ":measurementIndex";
}
