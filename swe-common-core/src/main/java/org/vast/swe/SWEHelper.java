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

package org.vast.swe;

import java.nio.ByteOrder;
import net.opengis.swe.v20.Boolean;
import net.opengis.swe.v20.BinaryComponent;
import net.opengis.swe.v20.BinaryMember;
import net.opengis.swe.v20.ByteEncoding;
import net.opengis.swe.v20.Category;
import net.opengis.swe.v20.Count;
import net.opengis.swe.v20.DataArray;
import net.opengis.swe.v20.DataComponent;
import net.opengis.swe.v20.DataEncoding;
import net.opengis.swe.v20.BinaryEncoding;
import net.opengis.swe.v20.DataRecord;
import net.opengis.swe.v20.DataType;
import net.opengis.swe.v20.JSONEncoding;
import net.opengis.swe.v20.Quantity;
import net.opengis.swe.v20.Text;
import net.opengis.swe.v20.TextEncoding;
import net.opengis.swe.v20.Time;
import net.opengis.swe.v20.Vector;
import net.opengis.swe.v20.XMLEncoding;
import org.vast.cdm.common.CDMException;
import org.vast.cdm.common.DataStreamParser;
import org.vast.cdm.common.DataStreamWriter;
import org.vast.data.AbstractDataComponentImpl;
import org.vast.data.AbstractSimpleComponentImpl;
import org.vast.data.BinaryComponentImpl;
import org.vast.data.BinaryEncodingImpl;
import org.vast.data.CountImpl;
import org.vast.data.DataArrayImpl;
import org.vast.data.DataIterator;
import org.vast.data.DataValue;
import org.vast.data.SWEFactory;
import org.vast.data.ScalarIterator;
import org.vast.data.TextEncodingImpl;


/**
 * <p>
 * Helper class for creating common data structures and encodings and browsing 
 * a data component tree.
 * </p>
 *
 * @author Alex Robin>
 * @since Feb 26, 2015
 */
public class SWEHelper extends SWEFactory
{  
    public final static String PATH_SEPARATOR = "/";
    
    // temporal reference systems
    public static final String TIME_REF_UTC = "http://www.opengis.net/def/trs/BIPM/0/UTC";
    public static final String TIME_REF_GPS = "http://www.opengis.net/def/trs/USNO/0/GPS";
    
    // EPSG crs URI prefix
    public static final String EPSG_URI_PREFIX = "http://www.opengis.net/def/crs/EPSG/0/";
    public static final String SWE_PROP_URI_PREFIX = "http://sensorml.com/ont/swe/property/";
    
    // other spatial reference frames
    public static final String REF_FRAME_LLA = getEpsgUri(4979);
    public static final String REF_FRAME_ECEF = getEpsgUri(4978);
    public static final String REF_FRAME_ENU = "http://www.opengis.net/def/crs/OGC/0/ENU";
    public static final String REF_FRAME_NED = "http://www.opengis.net/def/crs/OGC/0/NED";
    
    // component definitions
    public static final String DEF_SYSTEM_ID = SWE_PROP_URI_PREFIX + "SystemID";
    public static final String DEF_COORD = SWE_PROP_URI_PREFIX + "Coordinate";
    public static final String DEF_LOCATION = SWE_PROP_URI_PREFIX + "Location";
    public static final String DEF_VELOCITY = SWE_PROP_URI_PREFIX + "Velocity";
    public static final String DEF_ORIENTATION = SWE_PROP_URI_PREFIX + "Orientation";
    public static final String DEF_IMAGE = SWE_PROP_URI_PREFIX + "Image";
    
    
    /**
     * @param epsgCode
     * @return the CRS URI for the given EPSG integer code
     */
    public static String getEpsgUri(int epsgCode)
    {
        return EPSG_URI_PREFIX + epsgCode;
    }
    
    
    public static String getPropertyUri(String propName)
    {
        return SWE_PROP_URI_PREFIX + propName;
    }
    
    
    /**
     * Creates a new boolean component
     * @param definition URI pointing to semantic definition of component in a dictionary
     * @param label short human readable label identifying the component (shown in UI)
     * @param description textual description of this component (can be long)
     * @return the new Boolean component object
     */
    public Boolean newBoolean(String definition, String label, String description)
    {
        Boolean b = newBoolean();
        b.setDefinition(definition);
        b.setLabel(label);
        b.setDescription(description);
        return b;
    }
    
    
    /**
     * Creates a new Text component
     * @param definition URI pointing to semantic definition of component in a dictionary
     * @param label short human readable label identifying the component (shown in UI)
     * @param description textual description of this component (can be long)
     * @return the new Text component object
     */
    public Text newText(String definition, String label, String description)
    {
        Text tx = newText();
        tx.setDefinition(definition);
        tx.setLabel(label);
        tx.setDescription(description);
        return tx;
    }
    
    
    /**
     * Creates a new count (integer) component
     * @param definition URI pointing to semantic definition of component in a dictionary
     * @param label short human readable label identifying the component (shown in UI)
     * @param description textual description of this component (can be long)
     * @param dataType data type to use for this component (if null, {@link DataType#INT} will be used)
     * @return the new Count component object
     */
    public Count newCount(String definition, String label, String description, DataType dataType)
    {
        if (dataType == null)
            dataType = DataType.INT;
        
        Count c = newCount(dataType);
        c.setDefinition(definition);
        c.setLabel(label);
        c.setDescription(description);
        return c;
    }
    
    
    /**
     * Creates a new count (integer) component
     * @param definition URI pointing to semantic definition of component in a dictionary
     * @param label short human readable label identifying the component (shown in UI)
     * @param description textual description of this component (can be long)
     * @return the new Count component object
     */
    public Count newCount(String definition, String label, String description)
    {
        return newCount(definition, label, null, DataType.INT);
    }
    
    
    /**
     * Creates a new category component
     * @param definition URI pointing to semantic definition of component in a dictionary
     * @param label short human readable label identifying the component (shown in UI)
     * @param description textual description of this component (can be long) or null
     * @param codeSpace URI of this category code space
     * @return the new Category component object
     */
    public Category newCategory(String definition, String label, String description, String codeSpace)
    {
        Category c = newCategory();
        c.setDefinition(definition);
        c.setLabel(label);
        c.setDescription(description);
        c.setCodeSpace(codeSpace);        
        return c;
    }
    
    
    /**
     * Creates a new quantity (decimal) component
     * @param definition URI pointing to semantic definition of component in a dictionary
     * @param label short human readable label identifying the component (shown in UI)
     * @param description textual description of this component (can be long) or null
     * @param uomCode UCUM code for this decimal quantity's unit of measure
     * @param dataType data type to use for this component (if null, {@link DataType#DOUBLE} will be used)
     * @return the new Quantity component object
     */
    public Quantity newQuantity(String definition, String label, String description, String uomCode, DataType dataType)
    {
        if (dataType == null)
            dataType = DataType.DOUBLE;
        
        Quantity q = newQuantity(dataType);
        q.setDefinition(definition);
        q.setLabel(label);
        q.setDescription(description);
        q.getUom().setCode(uomCode);
        
        return q;
    }
    
    
    /**
     * Creates a new quantity (decimal) component with default data type
     * @param definition URI pointing to semantic definition of component in a dictionary
     * @param label short human readable label identifying the component (shown in UI)
     * @param description textual description of this component (can be long) or null
     * @param uomCode UCUM code for this decimal quantity's unit of measure
     * @return the new Quantity component object
     */
    public Quantity newQuantity(String definition, String label, String description, String uomCode)
    {
        return newQuantity(definition, label, description, uomCode, DataType.DOUBLE);
    }
    
    
    /**
     * Creates a new sampling time component
     * @param uom unit of time stamp
     * @param isUomCode true if uom is a UCUM code, false if it is a URI
     * @param timeRef URI of temporal reference frame for this time stamp
     * @return the new Time component object
     */
    public Time newTimeStamp(String uom, boolean isUomCode, String timeRef)
    {
        Time ts = newTime();
        ts.setLabel("Sampling Time");
        
        if (isUomCode)
            ts.getUom().setCode(uom);
        else
            ts.getUom().setHref(uom);
        
        ts.setDefinition(SWEConstants.DEF_SAMPLING_TIME);
        ts.setReferenceFrame(timeRef);
        
        return ts;
    }
    
    
    /**
     * Creates a new sampling time component with ISO calendar and UTC time frame
     * @return the new Time component object
     */
    public Time newTimeStampIsoUTC()
    {
        return newTimeStamp(Time.ISO_TIME_UNIT, false, TIME_REF_UTC);
    }
    
    
    /**
     * Creates a new sampling time component with ISO calendar and UTC time frame
     * @return the new Time component object
     */
    public Time newTimeStampIsoGPS()
    {
        return newTimeStamp(Time.ISO_TIME_UNIT, false, TIME_REF_GPS);
    }
    
    
    /**
     * Creates a new sampling time component 
     * @param uomCode time unit used for this onboard time stamp (e.g. 's', 'ms', 'ns', etc.)
     * @param timeRef URI of temporal reference frame for this time stamp (e.g. missionStart, etc.)
     * @return the new Time component object
     */
    public Time newTimeStampOnBoardClock(String uomCode, String timeRef)
    {
        return newTimeStamp(uomCode, true, timeRef);
    }
    
    
    /**
     * Wraps the given component(s) into a record with a time stamp
     * @param timeStamp Time component representing the time stamp
     * @param subComponents list of components to wrap with the time stamp
     * @return new DataRecord instance containing the time stamp and all other components
     */    
    public DataRecord wrapWithTimeStamp(Time timeStamp, DataComponent... subComponents)
    {
        DataRecord rec = newDataRecord(subComponents.length + 1);
        rec.addComponent("time", timeStamp);
        
        for (DataComponent childComp: subComponents)
            rec.addComponent(childComp.getName(), childComp);
        
        return rec;
    }
    
    
    /**
     * Creates a component for carrying system ID (e.g. station ID, sensor ID, device ID, etc...)
     * @return new Text instance
     */
    public Text newSystemIdComponent()
    {
        Text t = newText();
        t.setDefinition(DEF_SYSTEM_ID);
        return t;
    }
  
    
    /**
     * Creates a 3D vector component with the specified CRS and axes
     * @param def definition of the whole vector
     * @param crs reference frame of the vector
     * @param names array containing name of each individual vector element
     * @param uoms array containing unit of measure of each individual vector element
     * @param axes array containing axis name of each individual vector element
     * @return the new Vector component object
     */
    public Vector newVector(String def, String crs, String[] names, String[] uoms, String[] axes)
    {
        Vector loc = newVector();
        loc.setDefinition(def);
        loc.setReferenceFrame(crs);

        Quantity c;        
        for (int i = 0; i < names.length; i++)
        {
            c = newQuantity(DataType.DOUBLE);
            //c.setDefinition(DEF_COORD);
            c.getUom().setCode(uoms[i]);
            c.setAxisID(axes[i]);
            loc.addComponent(names[i], c);
        }
        
        return loc;
    }
       
    
    /**
     * Creates a 3D location vector with latitude/longitude/altitude axes (EPSG 4979) 
     * @param def semantic definition of location vector (if null, {@value #DEF_LOCATION} is used)
     * @return the new Vector component object
     */
    public Vector newLocationVectorLLA(String def)
    {
        if (def == null)
            def = DEF_LOCATION;
        
        return newVector(
                def,
                REF_FRAME_LLA,
                new String[] {"lat", "lon", "alt"},
                new String[] {"deg", "deg", "m"},
                new String[] {"Lat", "Long", "h"});
    }
    
    
    /**
     * Creates a 3D location vector with ECEF X/Y/Z axes (EPSG 4978) 
     * @param def semantic definition of location vector (if null, {@value #DEF_LOCATION} is used)
     * @return the new Vector component object
     */
    public Vector newLocationVectorECEF(String def)
    {
        if (def == null)
            def = DEF_LOCATION;
        
        return newVector(
                def,
                REF_FRAME_ECEF,
                new String[] {"x", "y", "z"},
                new String[] {"m", "m", "m"},
                new String[] {"X", "Y", "Z"});
    }
    
    
    /**
     * Creates a 3D velocity vector in an ortho-normal frame with X/Y/Z axes
     * @param def semantic definition of velocity vector (if null, {@value #DEF_VELOCITY} is used)
     * @param refFrame reference frame within which the vector is expressed
     * @param uomCode unit of velocity to use on all 3 axes
     * @return the new Vector component object
     */
    public Vector newVelocityVector(String def, String refFrame, String uomCode)
    {
        if (def == null)
            def = DEF_VELOCITY;
        
        return newVector(
                def,
                refFrame,
                new String[] {"vx", "vy", "vz"},
                new String[] {uomCode, uomCode, uomCode},
                new String[] {"X", "Y", "Z"});
    }
    
    
    /**
     * Creates a 3D velocity with ECEF X/Y/Z axes (EPSG 4978) 
     * @param def semantic definition of velocity vector (if null, {@value #DEF_VELOCITY} is used)
     * @param uomCode unit of velocity to use on all 3 axes
     * @return the new Vector component object
     */
    public Vector newVelocityVectorECEF(String def, String uomCode)
    {
        return newVelocityVector(def, REF_FRAME_ECEF, uomCode);
    }
    
    
    /**
     * Creates a 3D velocity with ENU X/Y/Z axes
     * @param def semantic definition of velocity vector (if null, {@value #DEF_VELOCITY} is used)
     * @param uomCode unit of velocity to use on all 3 axes
     * @return the new Vector component object
     */
    public Vector newVelocityVectorENU(String def, String uomCode)
    {
        return newVelocityVector(def, REF_FRAME_ENU, uomCode);
    }
    
    
    /**
     * Creates a 3D velocity with NED X/Y/Z axes
     * @param def semantic definition of velocity vector (if null, {@value #DEF_VELOCITY} is used)
     * @param uomCode unit of velocity to use on all 3 axes
     * @return the new Vector component object
     */
    public Vector newVelocityVectorNED(String def, String uomCode)
    {
        return newVelocityVector(def, REF_FRAME_NED, uomCode);
    }
    
    
    /**
     * Creates a 3D orientation vector composed of 3 Euler angles expressed in local
     * East-North-Up (ENU) frame (order of rotations is Z, X, Y)
     * @param def semantic definition of orientation vector (if null, {@value #DEF_ORIENTATION} is used)
     * @return the new Vector component object
     */
    public Vector newEulerOrientationENU(String def)
    {
        if (def == null)
            def = DEF_ORIENTATION;
        
        return newVector(
                def,
                REF_FRAME_ENU,
                new String[] {"yaw", "pitch", "roll"},
                new String[] {"deg", "deg", "deg"},
                new String[] {"Z", "X", "Y"});
    }
    
    
    /**
     * Creates a 3D orientation vector composed of 3 Euler angles expressed in local
     * North-East-Down (NED) frame (order of rotations is Z, Y, X)
     * @param def semantic definition of orientation vector (if null, {@value #DEF_ORIENTATION} is used)
     * @return the new Vector component object
     */
    public Vector newEulerOrientationNED(String def)
    {
        if (def == null)
            def = DEF_ORIENTATION;
        
        return newVector(
                def,
                REF_FRAME_ENU,
                new String[] {"yaw", "pitch", "roll"},
                new String[] {"deg", "deg", "deg"},
                new String[] {"Z", "Y", "X"});
    }
    
    
    /**
     * Creates an orientation vector component composed of 3 Euler angles expressed in
     * Earth-Centered-Earth-Fixed (ECEF) frame (order of rotations is X, Y, Z)
     * @param def semantic definition of orientation vector (if null, {@value #DEF_ORIENTATION} is used)
     * @return the new Vector component object
     */
    public Vector newEulerOrientationECEF(String def)
    {
        if (def == null)
            def = DEF_ORIENTATION;
        
        return newVector(
                def,
                REF_FRAME_ECEF,
                new String[] {"x", "y", "z"},
                new String[] {"deg", "deg", "deg"},
                new String[] {"X", "Y", "Z"});
    }
    
    
    protected Vector newQuatOrientation(String def, String crs)
    {
        if (def == null)
            def = DEF_ORIENTATION;
        
        return newVector(
                def,
                crs,
                new String[] {"q0", "qx", "qy", "qz"},
                new String[] {"1", "1", "1", "1"},
                new String[] {null, "X", "Y", "Z"});
    }
    
    
    /**
     * Creates a  vector representing an orientation quaternion expressed in ENU frame.
     * @param def semantic definition of orientation vector (if null, {@value #DEF_ORIENTATION} is used)
     * @return the new Vector component object
     */
    public Vector newQuatOrientationENU(String def)
    {
        return newQuatOrientation(def, REF_FRAME_ENU);
    }
    
    
    /**
     * Creates a  vector representing an orientation quaternion expressed in NED frame.
     * @param def semantic definition of orientation vector (if null, {@value #DEF_ORIENTATION} is used)
     * @return the new Vector component object
     */
    public Vector newQuatOrientationNED(String def)
    {
        return newQuatOrientation(def, REF_FRAME_NED);
    }
    
    
    /**
     * Creates a  vector representing an orientation quaternion expressed in ECEF frame.
     * @param def semantic definition of orientation vector (if null, {@value #DEF_ORIENTATION} is used)
     * @return the new Vector component object
     */
    public Vector newQuatOrientationECEF(String def)
    {
        return newQuatOrientation(def, REF_FRAME_ECEF);
    }
    
    
    /**
     * Creates a variable size 1D array
     * @param sizeComponent
     * @param eltName
     * @param elementType
     * @return the new DataArray component object
     */
    public DataArray newArray(Count sizeComponent, String eltName, DataComponent elementType)
    {
        DataArray array = newDataArray();
        ((DataArrayImpl)array).setVariableSizeComponent(sizeComponent);
        array.setElementType(eltName, elementType);
        return array;
    }
    
    
    /**
     * Creates a fixed size 2D-array component representing an RGB image
     * @param width
     * @param height
     * @param dataType
     * @return the new DataArray component object
     */
    public DataArray newRgbImage(int width, int height, DataType dataType)
    {
        DataArray imgArray = newDataArray(height);
        imgArray.setDefinition(DEF_IMAGE);
        DataArray imgRow = newDataArray(width);
                
        DataRecord imgPixel = newDataRecord(3);
        if (dataType.isIntegralType())
        {
            imgPixel.addComponent("red", newCount(dataType));
            imgPixel.addComponent("green", newCount(dataType));
            imgPixel.addComponent("blue", newCount(dataType));
        }
        else
        {
            imgPixel.addComponent("red", newQuantity(dataType));
            imgPixel.addComponent("green", newQuantity(dataType));
            imgPixel.addComponent("blue", newQuantity(dataType));            
        }
        
        imgRow.addComponent("pixel", imgPixel);
        imgArray.setElementType("row", imgRow);
        return imgArray;
    }
    
    
    //////////////////////////////////////
    // Encoding and parser/writer stuff //
    //////////////////////////////////////
    
    /**
     * Creates a text encoding with the specified separators.<br/>
     * Since no escaping is supported, it is up to the programmer to make sure that
     * separator characters are not present within the encoded data.
     * @param tokenSep separator used between tokens
     * @param blockSep separator used to delimit complete tuples
     * @return the configured TextEncoding instance
     */
    public TextEncoding newTextEncoding(String tokenSep, String blockSep)
    {
        return new TextEncodingImpl(tokenSep, blockSep);
    }
    
    
    /**
     * Creates a binary encoding with the specified options
     * @param byteOrder byte ordering (endianness) of the byte stream 
     * @param byteEncoding byte encoding used (raw or base64)
     * @return the BinaryEncoding instance (not that it is not fully configured 
     * since data types of all fields have to be specified)
     */
    public BinaryEncoding newBinaryEncoding(ByteOrder byteOrder, ByteEncoding byteEncoding)
    {
        BinaryEncodingImpl encoding = new BinaryEncodingImpl();
        encoding.setByteOrder(byteOrder);
        encoding.setByteEncoding(byteEncoding);
        return encoding;
    }
    
    
    /**
     * Helper method to instantiate the proper parser for the given encoding
     * @param encoding
     * @return instance of parser capable of handling the given encoding
     */
    public static DataStreamParser createDataParser(DataEncoding encoding)
    {
        DataStreamParser parser = null;
        
        if (encoding instanceof TextEncoding)
            parser = new AsciiDataParser();
        else if (encoding instanceof BinaryEncoding)
            parser = new BinaryDataParser();
        else if (encoding instanceof XMLEncoding)
            parser = new XmlDataParser();
        else if (encoding instanceof JSONEncoding)
            parser = new JSONDataParser();
        
        parser.setDataEncoding(encoding);
        return parser;
    }
    
    
    /**
     * Helper method to instantiate the proper writer for the given encoding
     * @param encoding
     * @return instance of writer capable of handling the given encoding
     */
    public static DataStreamWriter createDataWriter(DataEncoding encoding)
    {
        DataStreamWriter writer = null;
        
        if (encoding instanceof TextEncoding)
            writer = new AsciiDataWriter();
        else if (encoding instanceof BinaryEncoding)
            writer = new BinaryDataWriter();
        else if (encoding instanceof XMLEncoding)
            writer = new XmlDataWriter();
        else if (encoding instanceof JSONEncoding)
            writer = new JSONDataWriter();
        
        writer.setDataEncoding(encoding);
        return writer;
    }
    
    
    /**
     * Gets the default encoding for the given data structure.<br/>
     * This uses BinaryEncoding if data structure contains a large array and TextEncoding
     * otherwise. 
     * @param dataComponents
     * @return an appropriately configured encoding
     */
    public static DataEncoding getDefaultEncoding(DataComponent dataComponents)
    {
        // check if one of the children is a large array
        for (DataComponent c: new DataIterator(dataComponents))
        {
            if (c instanceof DataArray)
            {
                DataArrayImpl array = (DataArrayImpl)c;
                if (array.isVariableSize() || (array.getElementCount() != null && array.getElementCount().getValue() > 10))
                    return getDefaultBinaryEncoding(dataComponents);
            }
        }
        
        // otherwise return default text encoding
        return new TextEncodingImpl();
    }
    
    
    /**
     * Get default binary encoding for the given component tree.<br/>
     * Data types used will be ones specified in each scalar component.
     * @param dataComponents component whose children will be mapped to encoding options
     * @return binary encoding instance pre-configured for the component
     */
    public static BinaryEncoding getDefaultBinaryEncoding(DataComponent dataComponents)
    {
        BinaryEncodingImpl encoding = new BinaryEncodingImpl();
        encoding.setByteEncoding(ByteEncoding.RAW);
        encoding.setByteOrder(ByteOrder.BIG_ENDIAN);
        
        // use default encoding info for each data value
        ScalarIterator it = new ScalarIterator(dataComponents);
        while (it.hasNext())
        {
            DataComponent[] nextPath = it.nextPath();
            DataValue nextScalar = (DataValue)nextPath[nextPath.length-1];
            
            // build path (just use / for root)
            StringBuffer pathString = new StringBuffer();
            pathString.append(PATH_SEPARATOR);
            for (int i = 0; i < nextPath.length; i++)
            {
                pathString.append(nextPath[i].getName());
                pathString.append(PATH_SEPARATOR);
            }
            
            BinaryComponentImpl binaryOpts = new BinaryComponentImpl();
            binaryOpts.setCdmDataType(nextScalar.getDataType());
            binaryOpts.setRef(pathString.substring(0, pathString.length()-1));
            
            encoding.addMemberAsComponent(binaryOpts);
            nextScalar.setEncodingInfo(binaryOpts);
        }
        
        return encoding;
    }
    
    
    /**
     * Ajusts encoding settings to ensures that data produced with the encoding
     * can be embedded in XML (e.g. for binary encoding this enforces base64)
     * @param encoding
     * @return copy of encoding, reconfigured appropriately
     */
    public static DataEncoding ensureXmlCompatible(DataEncoding encoding)
    {
        // if binary data, ensure encoding is set to base64
        if (encoding instanceof BinaryEncoding)
        {
            encoding = encoding.copy();
            ((BinaryEncoding) encoding).setByteEncoding(ByteEncoding.BASE_64);
        }
        
        return encoding;
    }
    
    
    /**
     * Assigns binary components and blocks definitions to the actual data component.
     * This sets the encodingInfo attribute of the component so it can be used to generate specialized datablocks.
     * For scalars, it also sets the default data type so it is the same as in the encoded stream.
     * @param dataComponents 
     * @param encoding 
     * @throws CDMException 
     */
    public static void assignBinaryEncoding(DataComponent dataComponents, BinaryEncoding encoding) throws CDMException
    {
        for (BinaryMember binaryOpts: encoding.getMemberList())
        {
            DataComponent comp = findComponentByPath(dataComponents, binaryOpts.getRef());
            ((AbstractDataComponentImpl)comp).setEncodingInfo(binaryOpts);
            
            // for scalars, also set default data type
            if (binaryOpts instanceof BinaryComponent)
                ((AbstractSimpleComponentImpl)comp).setDataType(((BinaryComponentImpl)binaryOpts).getCdmDataType());
        }
        
        // set default data type for implicit array size components if not already set
        DataIterator it = new DataIterator(dataComponents);
        while (it.hasNext())
        {
            DataComponent comp = it.next();
            if (comp instanceof DataArrayImpl)
            {
                CountImpl count = (CountImpl)((DataArrayImpl)comp).getArraySizeComponent();
                if (count != null && count.getEncodingInfo() == null)
                {
                    BinaryComponent binaryOpts = new BinaryComponentImpl();
                    binaryOpts.setCdmDataType(count.getDataType());
                    count.setEncodingInfo(binaryOpts);
                }
            }
        }
    }
    
    
    
    ///////////////////////////////////////////////// 
    // Methods for looking up components in a tree //
    ///////////////////////////////////////////////// 
    
    /**
     * Finds a component in the component tree using its name (property name)
     * @param parent component from which to start the search
     * @param name component name to look for
     * @return the first component with the specified name
     */
    public static DataComponent findComponentByName(DataComponent parent, String name)
    {
        if (parent instanceof DataArrayImpl)
            parent = ((DataArrayImpl)parent).getElementType();
        
        int childCount = parent.getComponentCount();
        for (int i=0; i<childCount; i++)
        {
            DataComponent child = parent.getComponent(i);
            String childName = child.getName();
            
            if (childName.equals(name))
                return child;
            
            // try to find it recursively!
            DataComponent desiredParam = findComponentByName(child, name);
            if (desiredParam != null)
                return desiredParam;
        }
        
        return null;
    }


    /**
     * Finds a component in the component tree using its definition
     * @param parent component from which to start the search
     * @param defUri definition URI to look for
     * @return the first component with the specified definition
     */
    public static DataComponent findComponentByDefinition(DataComponent parent, String defUri)
    {
        if (parent instanceof DataArrayImpl)
            parent = ((DataArrayImpl)parent).getElementType();
        
        int childCount = parent.getComponentCount();
        for (int i=0; i<childCount; i++)
        {
            DataComponent child = parent.getComponent(i);
            String childDef = ((DataComponent)child).getDefinition();
            
            if (childDef != null && childDef.equals(defUri))
                return child;
            
            // try to find it recursively!
            DataComponent desiredParam = findComponentByDefinition(child, defUri);
            if (desiredParam != null)
                return desiredParam;
        }
        
        return null;
    }
    
    
    /**
     * Finds a component in a component tree using a path 
     * @param parent component from which to start the search
     * @param path desired path as a String composed of component names separated by {@value #PATH_SEPARATOR} characters
     * @return the component with the given path
     * @throws CDMException if the specified path is incorrect
     */
    public static DataComponent findComponentByPath(DataComponent parent, String path) throws CDMException
    {
        try
        {
            return findComponentByPath(parent, path.split(PATH_SEPARATOR));
        }
        catch (CDMException e)
        {
            throw new CDMException("Unknown component " + path);
        }
    }
    
    
    /**
     * Finds a component in a component tree using a path 
     * @param parent component from which to start the search
     * @param path desired path as a String array cotnaining a sequence of component names
     * @return the component with the given path
     * @throws CDMException if the specified path is incorrect
     */
    public static DataComponent findComponentByPath(DataComponent parent, String [] path) throws CDMException
    {
        DataComponent data = parent;
        
        for (int i=0; i<path.length; i++)
        {
            String pathElt = path[i];
            if (pathElt.length() == 0) // a leading '/' create an empty array element
                continue;
            
            data = data.getComponent(pathElt);
            if (data == null)
                throw new CDMException("Unknown component " + pathElt);
        }
        
        return data;
    }
}
