/***************************** BEGIN LICENSE BLOCK ***************************

The contents of this file are subject to the Mozilla Public License, v. 2.0.
If a copy of the MPL was not distributed with this file, You can obtain one
at http://mozilla.org/MPL/2.0/.

Software distributed under the License is distributed on an "AS IS" basis,
WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
for the specific language governing rights and limitations under the License.
 
Copyright (C) 2012-2017 Sensia Software LLC. All Rights Reserved.
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.util;


/**
 * <p>
 * Exception thrown when linked data cannot be resolved
 * </p>
 *
 * @author Alex Robin
 * @since Apr 6, 2017
 */
public class ResolveException extends RuntimeException
{
    private static final long serialVersionUID = 6322001229575359720L;

    
    public ResolveException(String message)
    {
        super(message);
    }
    
    
    public ResolveException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
