package org.vast.test;


//import java.io.InputStream;
//import org.vast.data.DataArray;
import java.util.List;

import org.ogc.cdm.common.DataComponent;
//import org.vast.data.DataValue;
import org.vast.io.xml.DOMReader;
import org.vast.process.DataProcess;
import org.vast.sensorML.metadata.Metadata;
import org.vast.sensorML.reader.*;
import org.vast.sensorML.system.SMLSystem;
//import org.vast.unit.Unit;
//import org.vast.unit.UnitParserUCUM;
import org.vast.util.*;
//import org.w3c.dom.Element;
//import org.w3c.dom.NodeList;


public class TestMain
{

	public static void main(String[] args)
	{
		try
		{
			//            DOMReader reader = new DOMReader("file:///d:/Projects/NSSTC/SensorML/instances/Airdas_Scanner2.xml", true);
			//            System.err.println(System.currentTimeMillis());
			//            System.out.println(reader.getElementValue("sml:member/sml:System/sml:definition/*/sml:description/sml:Discussion"));
			//            System.err.println(System.currentTimeMillis());

			//            DOMXPath xpath = new DOMXPath("sml:member/sml:System/sml:definition/*/sml:description/sml:Discussion");
			//            xpath.addNamespace("sml", "http://www.opengis.net/sensorML");
			//            System.err.println(System.currentTimeMillis());
			//            String text = xpath.stringValueOf(reader.getRootElement());
			//            System.err.println(text);
			//            System.err.println(System.currentTimeMillis());

			/* SensorML stuffs */
			//            SMLReader smlReader = new SMLReader("file:///d:/Projects/NSSTC/OGC%20Schemas/schemas_OGC/sensorML/1.0.30/instances/AIRDAS_GeoLocation.xml#RAY_POS");
			//            ProcessReader procReader = new ProcessReader(smlReader);
			//            DataProcess rayPosProcess = procReader.readProcess(smlReader.dom.getBaseElement());
			//            rayPosProcess.init();
			//            rayPosProcess.setName("ray position");
			//            System.out.println(rayPosProcess);
			//            
			//            smlReader = new SMLReader("file:///d:/Projects/NSSTC/OGC%20Schemas/schemas_OGC/sensorML/1.0.30/instances/AIRDAS_GeoLocation.xml#INS_POS");
			//            procReader = new ProcessReader(smlReader);
			//            DataProcess insPosProcess = procReader.readProcess(smlReader.dom.getBaseElement());
			//            insPosProcess.init();
			//            insPosProcess.setName("ins position");
			//            System.out.println(insPosProcess);
			//            
			//            DataProcess tProcess = new PosTransformProcess();
			//            tProcess.init();
			//            
			//            // execute
			//            DataValue indexData = new DataValue(DataType.DOUBLE);
			//            indexData.getData().setDoubleValue(720.0);
			//            rayPosProcess.setInputValue(new String[] {"index"}, indexData.getData());
			//            rayPosProcess.execute();
			//            DataBlockImpl rayPos = rayPosProcess.getOutputList().getComponent("position").getData();
			//            DataBlockImpl rayTime = rayPosProcess.getOutputList().getComponent("time").getData();
			//            System.out.println(rayTime);
			//            System.out.println(rayPos);
			//            System.out.println();
			//            
			//            DataValue timeData = new DataValue(DataType.DOUBLE);
			//            timeData.getData().setDoubleValue(0.0);
			//            insPosProcess.setInputValue(new String[] {"time"}, timeData.getData());
			//            insPosProcess.execute();
			//            DataBlockImpl insPos = insPosProcess.getOutputList().getComponent("position").getData();
			//            System.out.println(insPos);
			//            System.out.println();
			//            		
			//            tProcess.getInputList().getComponent("referencePosition").setData(insPos);
			//            tProcess.getInputList().getComponent("localPosition").setData(rayPos);
			//            tProcess.execute();
			//            System.out.println(tProcess.getOutputList().getComponent("position").getData());
			//        	String url = "file:///d:/Projects/NSSTC/OGC%20Schemas/schemas_OGC/sensorML/1.0.30/instances/AIRDAS_GeoLocation.xml#AIRDAS_GEOLOCATION_CHAIN";
			//        	SMLReader smlReader = new SMLReader(url);
			//            ProcessReader procReader = new ProcessReader(smlReader);
			//            DataProcess processChain = procReader.readProcess(smlReader.dom.getBaseElement());
			//            processChain.init();
			//            processChain.setName("AIRDAS GeoLocation");
			//            System.out.println(processChain);
			//            
			//            Hashtable children = ((ProcessChain)processChain).getProcessTable();
			//            Enumeration processEnum = children.elements();
			//            
			//            while (processEnum.hasMoreElements())
			//            	System.out.println(processEnum.nextElement());
			
            
			// Test Array copy vs. array clone method
            // NO DIFFERENCE -> EASIER TO USE CLONE
//            short[] src = new short[3000000];
//            short[] dest;
//            long start = System.currentTimeMillis();
//            dest = new short[3000000];
//            System.arraycopy(src,0,dest,0,3000000);
//            //dest = src.clone();
//            long stop = System.currentTimeMillis();
//            System.out.println(stop - start);
//            System.exit(0);
            
            
            // Test LinkedList iterator vs. manual loop with get(i)
            // ITERATOR IS MUCH MUCH MUCH FASTER !!
//            LinkedList<Object> list = new LinkedList<Object>();
//            Object obj = new Object();
//            Object obj2;
//            for (int i=0; i<500000; i++)
//                list.add(obj);            
//            long start = System.currentTimeMillis();
//            Iterator it = list.iterator();
//            while (it.hasNext())
//                obj2 = it.next();
//            //for (int i=0; i<30000; i++)
//            //    obj2 = list.get(i);
//            long stop = System.currentTimeMillis();
//            System.out.println(stop - start);
//            System.exit(0);
            
            
            // Writing using Xpath
//			DOMWriter writer = new DOMWriter();
//			Document dom = writer.createDocument("ProcessModel");
//			
//			JXPathContext cxt = JXPathContext.newContext(dom.getDocumentElement());
//			cxt.setFactory(new XpathDOMFactory());
//			cxt.createPathAndSetValue("inputs/InputList/input[1]/@name", "temp01");
//			cxt.createPathAndSetValue("inputs/InputList/input[2]/@name", "temp02");
//			//cxt.createPath("inputs/InputList/input[@name = 'temp02']/Quantity");
//			writer.setOutputStream(System.out);
//			writer.writeDOM(dom);
		
            
            // Writing DataComponents XML
//            DOMWriter dom = new DOMWriter();
//            dom.setOutputStream(System.out);
//            dom.createDocument("Data");
//            DataComponentsWriter writer = new DataComponentsWriter(dom);
//			  writer.setWriteInlineData(true);
//            
//            DataGroup group = new DataGroup();
//            for (int i=0; i<3; i++)
//            {
//                DataValue val = new DataValue(DataType.DOUBLE);
//                group.addComponent("elt"+i, val);
//                val.getData().setIntValue(i);
//            }
//            
//            DataGroup arrayGroup = new DataGroup();
//            arrayGroup.addComponent("count", new DataValue(DataType.INT));
//            arrayGroup.addComponent("boolean", new DataValue(DataType.BOOLEAN));
//            arrayGroup.addComponent("cat", new DataValue(DataType.ASCII_STRING));
//            
//            DataArray array = new DataArray();
//            array.addComponent(arrayGroup);
//            array.setSize(5);
//            array.assignNewDataBlock();
//            for (int i=0; i<array.getComponentCount()*3; i++)
//            {
//                if (i%3 == 0)
//                    array.getData().setIntValue(i, i);
//                else if (i%3 == 1)
//                    array.getData().setIntValue(i, i%6-1);
//                else if (i%3 == 2)
//                    array.getData().setStringValue(i, "cat"+i/3);
//            }
//            
//            group.addComponent("alarms", array);
//            
//            Element elt = writer.writeDataComponents(group);
//            dom.writeDOM(elt);
            
//            DOMReader reader = new DOMReader("file:///D:/Projects/NSSTC/OWS-Servers/WEB-INF/src/org/vast/sos_airdas/SCAN_observation.xml", false);
//            Element encPropElt = reader.getElement("resultDefinition/DataDefinition/encoding");
//            Element defPropElt = reader.getElement("resultDefinition/DataDefinition/dataComponents");
//            EncodingReader encReader = new EncodingReader(reader);
//            DataEncoding enc = encReader.readEncodingProperty(encPropElt);
//            DataComponentsReader defReader = new DataComponentsReader(reader);
//            DataComponent def = defReader.readComponentProperty(defPropElt);
//            
//            DOMWriter writer = new DOMWriter();
//            writer.setOutputStream(System.out);
//            writer.createDocument("Data");
//            EncodingWriter encWriter = new EncodingWriter(writer);
//            Element encElt = encWriter.writeDataEncoding(enc);
//            DataComponentsWriter defWriter = new DataComponentsWriter(writer);
//            defWriter.setWriteInlineData(false);
//            Element defElt = defWriter.writeDataComponents(def);
//            
//            writer.getBaseElement().appendChild(defElt);
//            writer.getBaseElement().appendChild(encElt);
//            writer.writeDOM(writer.getBaseElement());
            
//            DOMReader dom = new DOMReader("file:///d:/Projects/NSSTC/SensorML/examples/Process/GeoCentricPointingTest.xml#TEST_CHAIN", false);
//            SystemReader systemReader = new SystemReader(dom);
//            systemReader.setReadMetadata(false);
//            systemReader.setCreateExecutableProcess(true);
//            ProcessLoader.reloadMaps("file:///d:/Projects/NSSTC/SensorML/src/org/vast/test/ProcessMap.xml");
//            ProcessChain process = (ProcessChain)systemReader.readProcess(dom.getBaseElement());
//            process.setChildrenThreadsOn(false); 
//            process.init();
//            
//            process.getInputList().getComponent("location").getComponent("x").getData().setDoubleValue(-7e6);
//            process.getInputList().getComponent("location").getComponent("y").getData().setDoubleValue(7e6);
//            process.getInputList().getComponent("velocity").getComponent("z").getData().setDoubleValue(10);
//            process.execute();
//            
//            for (int i=0; i<3; i++)
//                System.out.println(process.getOutputList().getComponent("sampleLocation").getComponent(i).getData().getStringValue());
//            
//            System.out.println(process);
                
            
            /*********************************/
            /* test SPOT 5 Geolocation chain */
            /*********************************/
//            DOMReader dom = new DOMReader("file:///d:/Projects/NSSTC/SensorML/examples/Process/SPOT5_Supermode_Geolocation.xml#SPOT5_GEOLOCATION_CHAIN", true);
//            SystemReader systemReader = new SystemReader(dom);
//            systemReader.setReadMetadata(false);
//            systemReader.setCreateExecutableProcess(true);
//            ProcessLoader.reloadMaps("file:///d:/Projects/NSSTC/SensorML/src/org/vast/test/ProcessMap.xml");
//            ProcessChain process = (ProcessChain)systemReader.readProcess(dom.getBaseElement());
//            process.setChildrenThreadsOn(false);
//            process.init();
//            process.createNewInputBlocks();
//            
//            System.out.println(process.needSync() ? "sync on" : "sync off");
//            process.getInputList().getComponent("lineIndex").getData().setIntValue(12000);
//            
//            for (int i=0; i<24000; i+=500)
//            {
//                process.getInputList().getComponent("pixelIndex").getData().setIntValue(i+1);               
//                process.execute();
//                System.out.print(process.getOutputList().getComponent("sampleLocation").getComponent(0).getData().getDoubleValue() * 180.0 / Math.PI + ",");
//                System.out.print(process.getOutputList().getComponent("sampleLocation").getComponent(1).getData().getDoubleValue() * 180.0 / Math.PI - 360 + ",");
//                System.out.println(process.getOutputList().getComponent("sampleLocation").getComponent(2).getData().getDoubleValue());
//            }
//            

          DOMReader dom = new DOMReader("file:///c:/jstars.xml#jstars", false);
          SystemReader reader = new SystemReader(dom);
          reader.setReadMetadata(true);
          SMLSystem system = reader.readSystem(dom.getBaseElement());
          Metadata meta = (Metadata)system.getProperty(DataProcess.METADATA);
          
          // read characteristics list
          List<DataComponent> capList = meta.getCapabilities();
          for (int i=0; i< capList.size(); i++)
          {
             DataComponent cap = capList.get(i); 
             String carName = cap.getName();
             System.out.println(carName);
             
             for (int c=0; c<cap.getComponentCount(); c++)
             {
                DataComponent property = cap.getComponent(c);
                String propName = property.getName();
                System.out.println("\t" + propName);
             }
          }
          
          System.exit(0);

            
//          /***************************/
//          /* test sat TLE/SGP4 chain */
//          /***************************/
//          String baseFolder = "file:///d:/Projects/NSSTC/OWS-Servers/WEB-INF/src/org/vast/sos_footprints/";
//          //DOMReader dom = new DOMReader(baseFolder + "Orbital_Positioning.xml#PROCESS_CHAIN", false);
//          DOMReader dom = new DOMReader(baseFolder + "Scanner_GeoLocation.xml#PROCESS_CHAIN", false);
//          SystemReader systemReader = new SystemReader(dom);
//          systemReader.setReadMetadata(false);
//          systemReader.setCreateExecutableProcess(true);
//          ProcessLoader.reloadMaps(baseFolder + "ProcessMap.xml");
//          ProcessChain process = (ProcessChain)systemReader.readProcess(dom.getBaseElement());
//          process.setChildrenThreadsOn(false);
//          process.init();
//          process.createNewInputBlocks();
//          
//          System.out.println(process.needSync() ? "sync on" : "sync off");
//          //process.getInputList().getComponent("julianTime").getData().setDoubleValue(0.0);//86400.0 / 2);
//          process.getInputList().getComponent("scanTime").getData().setDoubleValue(0.0);
//          process.getInputList().getComponent("scanIndex").getData().setDoubleValue(0.5);
//          process.execute();
//                   
//          DataGroup llaVector = (DataGroup) process.getOutputList().getComponent("sampleLocation");
//          System.out.println(llaVector);
          
//          DataGroup eciVector = (DataGroup) process.getProcess("sgp4").getOutputList().getComponent("ECI_location");
//          for (int i=0; i<3; i++)
//              System.out.print(eciVector.getComponent(i).getData().getDoubleValue() + " ");
//          System.out.println();
          
//          DataArray outMatrix = (DataArray) process.getOutputList().getComponent("ecefPositionMatrix");
//          for (int i=0; i<4; i++)
//          {
//              for (int j=0; j<4; j++)         
//                  System.out.print(outMatrix.getComponent(i*4 + j).getData().getDoubleValue() + " ");
//              
//              System.out.println();
//          }
            
            
            //String ucumString = "-4.10*2.[pi].[ft-us]2/m2";
//            String ucumString = "2.[pi].rad/360";//"[pi].mol.kg.s-2";
//            UnitParserUCUM parser = new UnitParserUCUM();
//            parser.preloadSIUnits();
//            Unit unit = parser.getUnit(ucumString);
//            System.out.println(unit);
//                      
//            // load UCUM essence file
//            DOMReader dom = new DOMReader("file:///d:/Projects/NSSTC/DataProcessing/src/org/vast/unit/ucum-essence.xml", false);
//            NodeList eltList = dom.getElements("unit");
//            for (int i=0; i<eltList.getLength(); i++)
//            {
//                Element unitElt = (Element)eltList.item(i);                
//                String unitCode = dom.getAttributeValue(unitElt, "Code");
//                String unitName = dom.getElementValue(unitElt, "name");
//                String unitString = dom.getAttributeValue(unitElt, "value/@Unit");
//                String isMetric = dom.getAttributeValue(unitElt, "isMetric");    
//                
//                // skip units that we already have and 10*
//                if (unitCode.startsWith("10"))
//                    continue;
//                if (parser.getUnitTable().get(unitCode) != null)
//                    continue;                
//                                
//                Unit ucumUnit;
//                try
//                {
////                    if (unitCode.equals("[lbf_av]"))
////                        System.out.println("Here");
//                    ucumUnit = parser.getUnit(unitString);
//                    ucumUnit.setSymbol(unitCode);
//                    ucumUnit.setName(unitName);
//                    
//                    if (isMetric != null && isMetric.equals("yes"))
//                        ucumUnit.setMetric(true);                        
//                }
//                catch (RuntimeException e)
//                {
//                    System.out.println(unitCode + " = ? [ERROR]");
//                    continue;
//                }
//                
//                String value = dom.getAttributeValue(unitElt, "value/@value");
//                double val = Double.parseDouble(value);
//                ucumUnit.multiply(val);
//                
//                System.out.println(ucumUnit + " (" + ucumUnit.getName() + ")");
//                parser.getUnitTable().put(unitCode, ucumUnit);
//            }
//            
            
            /**************************/
            /* test chain with switch */
            /**************************/
//		    DOMReader dom = new DOMReader("file:///d:/Projects/NSSTC/SensorML/src/org/vast/test/ProcessChain.xml#PROCESSING_CHAIN", false);
//            SystemReader systemReader = new SystemReader(dom);
//            systemReader.setReadMetadata(false);
//            systemReader.setCreateExecutableProcess(true);
//            ProcessLoader.reloadMaps(TestMain.class.getResource("ProcessMap.xml").toString());//("file:///d:/Projects/NSSTC/SensorML/src/org/vast/test/ProcessMap.xml");
//            ProcessChain process = (ProcessChain)systemReader.readProcess(dom.getBaseElement());
//            process.setChildrenThreadsOn(false);
//            process.init();
//            process.createNewInputBlocks();
//            System.out.println(process.needSync() ? "sync on" : "sync off");
//            for (int i=0; i<1000; i++)
//            {
//               process.setInputReady(0);               
//               process.getInputList().getComponent("temp").getData().setDoubleValue((double)i/100);
//               process.execute();
//               String wc = process.getOutputList().getComponent("windChillTemp1").getData().getStringValue();
//               String dp = process.getOutputList().getComponent("windChillTemp2").getData().getStringValue();
//               System.out.println(i + ": " + wc + " - " + dp);
//            }

		}
		catch (Exception e)
		{
            ExceptionSystem.debug = true;
            ExceptionSystem.display(e);
		}

		//System.exit(0);
	}
}
