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
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DOMTransform
{
    protected DOMHelper sourceDomHelper;
    protected DOMHelper resultDomHelper;
    protected List<TransformTemplate> templates;
    
    
    public DOMTransform()
    {
        templates = new ArrayList<TransformTemplate>();
    }
    
    
    public void init()
    {        
    }
    
    
    public DOMHelper transform(DOMHelper sourceDoc)
    {
        this.sourceDomHelper = sourceDoc;
        this.resultDomHelper = new DOMHelper();
        this.init();
        this.applyTemplates(sourceDoc.getBaseElement(), resultDomHelper.getDocument());
        return resultDomHelper;
    }
    
    
    public DOMHelper transform(DOMHelper sourceDoc, DOMHelper resultDoc)
    {
        this.sourceDomHelper = sourceDoc;
        this.resultDomHelper = resultDoc;
        this.init();
        this.applyTemplates(sourceDoc.getBaseElement(), resultDoc.getBaseElement());
        return resultDomHelper;
    }
    
    
    public DOMHelper transform(DOMHelper sourceDoc, DOMHelper resultDoc, Node resultNode)
    {
        this.sourceDomHelper = sourceDoc;
        this.resultDomHelper = resultDoc;
        this.init();
        this.applyTemplates(sourceDoc.getBaseElement(), resultNode);
        return resultDomHelper;
    }
    
    
    public void applyTemplates(Node srcNode, Node resultNode)
    {
        for (int i=0; i<templates.size(); i++)
        {
            TransformTemplate nextTemplate = templates.get(i);
            if (nextTemplate.isMatch(this, srcNode, resultNode))
            {
                nextTemplate.apply(this, srcNode, resultNode);
                break;
            }
        }
    }
    
    
    public void applyTemplatesToChildNodes(Node srcNode, Node resultNode)
    {
        // process child nodes
        NodeList nodes = srcNode.getChildNodes();
        if (nodes != null)
            for (int i=0; i<nodes.getLength(); i++)
                applyTemplates(nodes.item(i), resultNode);
    }
    
    
    public void applyTemplatesToAttributes(Node srcNode, Node resultNode)
    {
        // process attributes
        NamedNodeMap attribs = srcNode.getAttributes();
        if (attribs != null)
            for (int i=0; i<attribs.getLength(); i++)
                applyTemplates(attribs.item(i), resultNode);
    }
    
    
    public void applyTemplatesToChildElements(Node srcNode, Node resultNode)
    {
        // process child elements
        NodeList elts = sourceDomHelper.getAllChildElements((Element)srcNode);
        if (elts != null)
            for (int i=0; i<elts.getLength(); i++)
                applyTemplates(elts.item(i), resultNode);
    }
    
    
    protected boolean isElement(Node node)
    {
        return (node.getNodeType() == Node.ELEMENT_NODE); 
    }
    
    
    protected boolean isAttribute(Node node)
    {
        return (node.getNodeType() == Node.ATTRIBUTE_NODE); 
    }
    
    
    protected boolean isDocument(Node node)
    {
        return (node.getNodeType() == Node.DOCUMENT_NODE); 
    }
    
    
    protected boolean isComment(Node node)
    {
        return (node.getNodeType() == Node.COMMENT_NODE); 
    }
    
    
    protected boolean isProcessingInstruction(Node node)
    {
        return (node.getNodeType() == Node.PROCESSING_INSTRUCTION_NODE); 
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
