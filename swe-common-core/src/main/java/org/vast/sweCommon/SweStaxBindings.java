/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are Copyright (C) 2014 Sensia Software LLC.
 All Rights Reserved.
 
 Contributor(s): 
    Alexandre Robin <alex.robin@sensiasoftware.com>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.sweCommon;

import net.opengis.swe.v20.bind.XMLStreamBindings;
import org.vast.data.DataComponentFactory;


/**
 * <p>
 * Helper wrapping the auto-generated XML stream bindings
 * </p>
 *
 * <p>Copyright (c) 2014 Sensia Software LLC</p>
 * @author Alexandre Robin <alex.robin@sensiasoftware.com>
 * @since Sep 25, 2014
 */
public class SweStaxBindings extends XMLStreamBindings
{

    public SweStaxBindings()
    {
        super(new DataComponentFactory());
    }
    
}
