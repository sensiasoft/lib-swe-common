/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are copyright (C) 2018, Sensia Software LLC
 All Rights Reserved. This software is the property of Sensia Software LLC.
 It cannot be duplicated, used, or distributed without the express written
 consent of Sensia Software LLC.
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.ogc.gml;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import javax.xml.namespace.QName;
import org.vast.util.TimeExtent;
import net.opengis.gml.v32.AbstractTimeGeometricPrimitive;
import net.opengis.gml.v32.TimeInstant;
import net.opengis.gml.v32.TimePeriod;
import net.opengis.gml.v32.TimePosition;
import net.opengis.gml.v32.impl.GMLFactory;


public class GenericTemporalFeatureImpl extends GenericFeatureImpl implements ITemporalFeature
{
    private static final long serialVersionUID = 2901698262626551458L;
    public static final QName PROP_VALID_TIME = new QName(GMLStaxBindings.NS_URI, "validTime", GMLStaxBindings.NS_PREFIX_GML);
    

    public GenericTemporalFeatureImpl(QName qname)
    {
        super(qname);
    }
    

    @Override
    public TimeExtent getValidTime()
    {
        AbstractTimeGeometricPrimitive validTime = (AbstractTimeGeometricPrimitive)properties.get(PROP_VALID_TIME);
        if (validTime == null)
            return null;
        
        if (validTime instanceof TimeInstant)
        {
            OffsetDateTime dateTime = ((TimeInstant)validTime).getTimePosition().getDateTimeValue();
            if (dateTime == null)
                return null;
            
            Instant instant = dateTime.withOffsetSameInstant(ZoneOffset.UTC).toInstant();
            return TimeExtent.instant(instant);
        }
        else if (validTime instanceof TimePeriod)
        {
            OffsetDateTime beginTime = ((TimePeriod)validTime).getBeginPosition().getDateTimeValue();
            OffsetDateTime endTime = ((TimePeriod)validTime).getEndPosition().getDateTimeValue();
            if (beginTime == null || endTime == null)
                return null;
            
            Instant begin = beginTime.withOffsetSameInstant(ZoneOffset.UTC).toInstant();            
            Instant end = endTime.withOffsetSameInstant(ZoneOffset.UTC).toInstant();
            return TimeExtent.period(begin, end);
        }
        
        return null;
    }
    
    
    public void setValidTimeInstant(OffsetDateTime dateTime)
    {
        GMLFactory gmlFac = new GMLFactory();
        TimePosition time = gmlFac.newTimePosition(dateTime);
        TimeInstant instant = gmlFac.newTimeInstant(time);
        setProperty(PROP_VALID_TIME, instant);
    }
    
    
    public void setValidTimePeriod(OffsetDateTime beginTime, OffsetDateTime endTime)
    {
        if (beginTime.equals(endTime))
        {
            setValidTimeInstant(beginTime);
            return;
        }
        
        GMLFactory gmlFac = new GMLFactory();
        TimePosition begin = gmlFac.newTimePosition(beginTime);
        TimePosition end = gmlFac.newTimePosition(endTime);
        TimePeriod period = gmlFac.newTimePeriod(begin, end);
        setProperty(PROP_VALID_TIME, period);
    }
}
