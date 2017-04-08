/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are Copyright (C) 2014 Sensia Software LLC.
 All Rights Reserved.
 
 Contributor(s): 
    Alexandre Robin <alex.robin@sensiasoftware.com>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.swe;

import net.opengis.swe.v20.bind.XMLStreamBindings;
import org.vast.data.SWEFactory;


/**
 * <p>
 * Helper wrapping the auto-generated SWE Common StAX bindings
 * </p>
 *
 * @author Alex Robin
 * @since Sep 25, 2014
 */
public class SWEStaxBindings extends XMLStreamBindings
{
    public final static String NS_PREFIX_SWE = "swe";
    public final static String NS_PREFIX_XLINK = "xlink";
    
    
    public SWEStaxBindings()
    {
        super(new SWEFactory());
        
        nsContext.registerNamespace(NS_PREFIX_SWE, net.opengis.swe.v20.bind.XMLStreamBindings.NS_URI);
        nsContext.registerNamespace(NS_PREFIX_XLINK, net.opengis.swe.v20.bind.XMLStreamBindings.XLINK_NS_URI);
    }
    
}
