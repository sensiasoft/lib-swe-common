/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are Copyright (C) 2014 Sensia Software LLC.
 All Rights Reserved.
 
 Contributor(s): 
    Alexandre Robin <alex.robin@sensiasoftware.com>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.cdm.common;

import net.opengis.swe.v20.AbstractDataComponent;


public interface BlockComponent extends DataComponent
{
    
    public AbstractDataComponent getElementType();

}
