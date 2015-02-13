/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are Copyright (C) 2014 Sensia Software LLC.
 All Rights Reserved.
 
 Contributor(s): 
    Alexandre Robin <alex.robin@sensiasoftware.com>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ogc.gml;

import net.opengis.gml.v32.impl.GMLFactory;
import net.opengis.gml.v32.bind.XMLStreamBindings;


/**
 * <p>
 * Helper wrapping the auto-generated GML StAX bindings
 * </p>
 *
 * @author Alex Robin <alex.robin@sensiasoftware.com>
 * @since Sep 25, 2014
 */
public class GMLStaxBindings extends XMLStreamBindings
{

    public GMLStaxBindings()
    {
        this(false);
    }
    
    
    public GMLStaxBindings(boolean useJTS)
    {
        super(new GMLFactory(useJTS));
        
        nsContext.registerNamespace("gml", net.opengis.gml.v32.bind.XMLStreamBindings.NS_URI);
        nsContext.registerNamespace("xlink", net.opengis.swe.v20.bind.XMLStreamBindings.XLINK_NS_URI);
    }
    
}
