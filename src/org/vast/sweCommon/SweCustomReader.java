package org.vast.sweCommon;

import org.vast.cdm.common.CDMException;
import org.vast.data.AbstractDataComponent;
import org.vast.xml.DOMHelper;
import org.w3c.dom.Element;


public interface SweCustomReader
{
	public AbstractDataComponent readComponent(DOMHelper dom, Element componentElt) throws CDMException;
}
