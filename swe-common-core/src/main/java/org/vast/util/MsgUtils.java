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


public class MsgUtils
{
    
    private MsgUtils() {}
    
    
    public static String getFullExceptionMessage(Throwable e)
    {
        StringBuilder buf = new StringBuilder();
        
        while (e != null)
        {
            buf.append(e.getMessage()).append('\n');
            e = e.getCause();
        }
        
        return buf.toString();
    }
}
