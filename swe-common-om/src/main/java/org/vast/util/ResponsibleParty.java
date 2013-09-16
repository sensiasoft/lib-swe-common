/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "SensorML DataProcessing Engine".
 
 The Initial Developer of the Original Code is the VAST team at the University of Alabama in Huntsville (UAH). <http://vast.uah.edu> Portions created by the Initial Developer are Copyright (C) 2007 the Initial Developer. All Rights Reserved. Please Contact Mike Botts <mike.botts@uah.edu> for more information.
 
 Contributor(s): 
 Alexandre Robin <robin@nsstc.uah.edu>
 
 ******************************* END LICENSE BLOCK ***************************/

package org.vast.util;

/**
 * <p>
 * SensorML ResponsibleParty
 * </p>
 *
 * <p>Copyright (c) 2005</p>
 * @author Alexandre Robin
 * @since Feb 16, 2006
 * @version 1.0
 */
public class ResponsibleParty extends Contact
{
    protected String individualName;
    protected String organizationName;
    protected String positionName;
    protected String voiceNumber;
    protected String faxNumber;
    protected String deliveryPoint;
    protected String city;
    protected String administrativeArea;
    protected String postalCode;
    protected String country;
    protected String email;
    protected String website;
    protected String hoursOfService;
    protected String contactInstructions;


    public String getAdministrativeArea()
    {
        return administrativeArea;
    }


    public void setAdministrativeArea(String administrativeArea)
    {
        this.administrativeArea = administrativeArea;
    }


    public String getCity()
    {
        return city;
    }


    public void setCity(String city)
    {
        this.city = city;
    }


    public String getContactInstructions()
    {
        return contactInstructions;
    }


    public void setContactInstructions(String contactInstructions)
    {
        this.contactInstructions = contactInstructions;
    }


    public String getCountry()
    {
        return country;
    }


    public void setCountry(String country)
    {
        this.country = country;
    }


    public String getDeliveryPoint()
    {
        return deliveryPoint;
    }


    public void setDeliveryPoint(String deliveryPoint)
    {
        this.deliveryPoint = deliveryPoint;
    }


    public String getEmail()
    {
        return email;
    }


    public void setEmail(String email)
    {
        this.email = email;
    }


    public String getWebsite()
	{
		return website;
	}


	public void setWebsite(String website)
	{
		this.website = website;
	}


	public String getFaxNumber()
    {
        return faxNumber;
    }


    public void setFaxNumber(String faxNumber)
    {
        this.faxNumber = faxNumber;
    }


    public String getHoursOfService()
    {
        return hoursOfService;
    }


    public void setHoursOfService(String hoursOfService)
    {
        this.hoursOfService = hoursOfService;
    }


    public String getIndividualName()
    {
        return individualName;
    }


    public void setIndividualName(String individualName)
    {
        this.individualName = individualName;
    }


    public String getOrganizationName()
    {
        return organizationName;
    }


    public void setOrganizationName(String organizationName)
    {
        this.organizationName = organizationName;
    }


    public String getPositionName()
    {
        return positionName;
    }


    public void setPositionName(String positionName)
    {
        this.positionName = positionName;
    }


    public String getPostalCode()
    {
        return postalCode;
    }


    public void setPostalCode(String postalCode)
    {
        this.postalCode = postalCode;
    }


    public String getVoiceNumber()
    {
        return voiceNumber;
    }


    public void setVoiceNumber(String voiceNumber)
    {
        this.voiceNumber = voiceNumber;
    }
}
