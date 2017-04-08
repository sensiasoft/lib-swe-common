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

import net.opengis.OgcProperty;

/**
 * <p>
 * Tagging interface for data components with constraints
 * </p>
 *
 * @author Alex Robin
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