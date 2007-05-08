/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "SensorML DataProcessing Engine".
 
 The Initial Developer of the Original Code is the
 University of Alabama in Huntsville (UAH).
 Portions created by the Initial Developer are Copyright (C) 2006
 the Initial Developer. All Rights Reserved.
 
 Contributor(s): 
    Alexandre Robin <robin@nsstc.uah.edu>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.xml.transform;

import java.util.ArrayList;
import java.util.List;

import org.vast.xml.DOMHelper;
import org.w3c.dom.Node;

public class DOMTransform
{
    protected DOMHelper sourceDomHelper;
    protected DOMHelper resultDomHelper;
    protected List<TransformTemplate> templates;
    
    
    public DOMTransform()
    {
        templates = new ArrayList<TransformTemplate>();
    }
    
    
    public DOMHelper transform(DOMHelper sourceDoc)
    {
        this.sourceDomHelper = sourceDoc;
        this.resultDomHelper = new DOMHelper();
        this.applyTemplates(sourceDoc.getBaseElement(), resultDomHelper.getBaseElement());
        return resultDomHelper;
    }
    
    
    public DOMHelper transform(DOMHelper sourceDoc, DOMHelper resultDoc)
    {
        this.sourceDomHelper = sourceDoc;
        this.resultDomHelper = resultDoc;
        this.applyTemplates(sourceDoc.getBaseElement(), resultDoc.getBaseElement());
        return resultDomHelper;
    }
    
    
    public DOMHelper transform(DOMHelper sourceDoc, DOMHelper resultDoc, Node resultNode)
    {
        this.sourceDomHelper = sourceDoc;
        this.resultDomHelper = resultDoc;
        this.applyTemplates(sourceDoc.getBaseElement(), resultNode);
        return resultDomHelper;
    }
    
    
    public void applyTemplates(Node srcNode, Node resultNode)
    {
        for (int i=0; i<templates.size(); i++)
        {
            TransformTemplate nextTemplate = templates.get(i);
            if (nextTemplate.isMatch(this, srcNode, resultNode))
                nextTemplate.apply(this, srcNode, resultNode);
        }
    }


    public DOMHelper getResultDomHelper()
    {
        return resultDomHelper;
    }


    public void setResultDomHelper(DOMHelper resultDomHelper)
    {
        this.resultDomHelper = resultDomHelper;
    }


    public DOMHelper getSourceDomHelper()
    {
        return sourceDomHelper;
    }


    public void setSourceDomHelper(DOMHelper sourceDomHelper)
    {
        this.sourceDomHelper = sourceDomHelper;
    }


    public List<TransformTemplate> getTemplates()
    {
        return templates;
    }


    public void setTemplates(List<TransformTemplate> templates)
    {
        this.templates = templates;
    }
}
