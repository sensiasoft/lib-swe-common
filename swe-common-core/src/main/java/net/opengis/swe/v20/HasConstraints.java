/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "SensorML DataProcessing Engine".
 
 The Initial Developer of the Original Code is Sensia Software LLC.
 Portions created by the Initial Developer are Copyright (C) 2014
 the Initial Developer. All Rights Reserved.
 
 Please Contact Alexandre Robin <alex.robin@sensiasoftware.com> or
 Mike Botts <mike.botts@botts-inc.net> for more information.
 
 Contributor(s): 
    Alexandre Robin <alex.robin@sensiasoftware.com>
 
******************************* END LICENSE BLOCK ***************************/

package net.opengis.swe.v20;

import net.opengis.OgcProperty;

/**
 * <p>
 * Tagging interface for data components with constraints
 * </p>
 *
 * <p>Copyright (c) 2014 Sensia Software LLC</p>
 * @author Alexandre Robin <alex.robin@sensiasoftware.com>
 * @since Nov 8, 2014
 */
@SuppressWarnings("javadoc")
public interface HasConstraints<ConstraintType extends DataConstraint>
{

    /**
     * Gets the constraint property
     */
    public abstract ConstraintType getConstraint();


    /**
     * Gets extra info (name, xlink, etc.) carried by the constraint property
     */
    public abstract OgcProperty<ConstraintType> getConstraintProperty();


    /**
     * Checks if constraint is set
     */
    public abstract boolean isSetConstraint();


    /**
     * Sets the constraint property
     */
    public abstract void setConstraint(ConstraintType constraint);

}