package sys4sim.export.anylogic;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Random;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import sys4sim.export.ExportInterface;
import sys4sim.internal_model.Connector;
import sys4sim.internal_model.Entity;
import sys4sim.internal_model.Model;
import sys4sim.internal_model.ModelBlock;
import sys4sim.internal_model.ModelElement;
import sys4sim.internal_model.Rate;
import sys4sim.internal_model.ResourcePool;

public class Exporter implements ExportInterface {

	public void writeFile(Model model, File file,Settings set) {
	
	
		
		
	//Modell abändern	
	
	if (set.getPortChoice()==3)
	{
		
		//Erzeuege Eingangsbuffer
	     int zsourcebuffer =0;	
	     //Model model2 = new Model();
		 Hashtable <String, ModelElement> tableElem = new Hashtable<String, ModelElement>();
	     tableElem = model.getElements();
	     	    		 
		 Hashtable<String, ModelElement> tempTable = new Hashtable<String, ModelElement>();
		 
		 for (ModelElement element : model.getElements().values()) 
	        {
			 if (element.getClass().getName().equalsIgnoreCase("sys4sim.internal_model.Source"))
				{
				 
				    ArrayList<Connector> outList = new ArrayList<Connector>();
					outList = ((ModelBlock)element).getOut();
					 for ( Iterator<Connector> i = outList.iterator(); i.hasNext(); )
				     {
						    //erzeuge Buffer
						    zsourcebuffer+=1;
						    sys4sim.internal_model.Queue queue = new sys4sim.internal_model.Queue();
							queue.setCapacity(10000);
							queue.setName("sourcebuffer"+String.valueOf(zsourcebuffer));
							tempTable.put("sourcebuffer"+String.valueOf(zsourcebuffer), queue);
				 	        // Connector abändern
							Connector s = i.next();
							ModelBlock z = s.getTarget();
							z.getIn().remove(s);
				 	        s.setTarget(queue);
				 	        s.setTargetName(queue.getName());
				 	        //neuen Connector zwischen Buffer und Elziel erzeugen
				 	       sys4sim.internal_model.Connector con = new sys4sim.internal_model.Connector();
				 	       con.setSource(queue);
				 	       con.setSourceName(queue.getName());
				 	       con.setTarget(z);
				 	       con.setTargetName(z.getName());
				 	       z.getIn().add(con);
				 	      tempTable.put("Con"+String.valueOf(zsourcebuffer), con);
				 	       //neues Teil in Buffer get.out schreiben
				 	       queue.getOut().add(con);
				        }					
				}			    
	        }
	 
	 
		 for(String s : tempTable.keySet()){
		 ModelElement m = tempTable.get(s);
		 tableElem.put(s, m);}
		 model.setElements(tableElem);
	
	 //erzeuge On Exit Code
		 
		 for (ModelElement element : model.getElements().values()) 
	        {
               CreateFunction(element);
	        
	        }
	} 
	
		
	 // Wurzelelement erzeugen
   	 Element root =  new  Element("AnyLogicWorkspace")
   	 .setAttribute("WorkspaceVersion","1.9")
   	 .setAttribute("AnyLogicVersion","6.2.2.200803281613")
   	 .setAttribute("AlpVersion","6.2.2");
   	 Element Model = new Element("Model");
   	 root.addContent(Model);
   	 
   	 // definiere allgemeine Modelleigenschafften 
   
       Model
       .addContent(new Element ("Id").setText("1213174774546"))
   	   .addContent(new Element("Name").setText("<![CDATA[MM1]]>"))//einsetzen
   	   .addContent(new Element("ExcludeFromBuild").setText("false"))
   	   .addContent(new Element("EngineVersion").setText("6"))
   	   .addContent(new Element("JavaPackageName").setText("<![CDATA[mm1]]>"));//einsetzen
       
      
       Element ActiveObjectClasses = new Element("ActiveObjectClasses");
       Model.addContent(ActiveObjectClasses);
            Element ActiveObjectClass = new Element("ActiveObjectClass");
            ActiveObjectClasses.addContent(ActiveObjectClass);
              ActiveObjectClass
              .addContent(new Element("Id").setText("1213174774547"))//bea
              .addContent(new Element("Name").setText("<![CDATA[MM1]]>"))//bea
              .addContent(new Element("ExcludeFromBuild").setText("false"))
              .addContent(new Element("ClientAreaTopLeft").addContent("<X>0</X><Y>0</Y>"))//hoffe mal das geht so
              .addContent(new Element("PresentationTopGroupPersistent").setText("true"))
              .addContent(new Element("IconTopGroupPersistent").setText("true"))
              .addContent(new Element("Generic").setText("false"))
              .addContent(new Element("GenericParameters").setText("<![CDATA[T]]>"));//bea keine Ahnung
              
              Element AgentProperties = new Element("AgentProperties");
              ActiveObjectClass.addContent(AgentProperties);
                AgentProperties
                .addContent(new Element("SpaceType").setText("CONTINUOUS"))
                .addContent(new Element("EnvironmentDefinesInitialLocation").setText("true"));
              
              Element DatasetsCreationProperties = new Element("DatasetsCreationProperties");
              ActiveObjectClass.addContent(DatasetsCreationProperties);
                DatasetsCreationProperties
                .addContent(new Element("AutoCreate").setText("true"))
                .addContent(new Element("SamplesToKeep").setText("100"))
                .addContent(new Element("FirstUpdateAtTime").setText("true"))
                .addContent(new Element("FirstUpdateTime").setText("0.0"))
                .addContent(new Element("FirstUpdateDate").setText("1213174774859"))//bea
                .addContent(new Element("RecurrenceCode").setText("<![CDATA[1]]>"));//bea
                   
               Element Connectors = new Element("Connectors");
               ActiveObjectClass.addContent(Connectors);
        
         ///Erzeugen der Ressourcepools Ids kann dann weg wenn sie Elemente sind
               
            if (!set.getDelayMode())
            {
               for (ModelElement element : model.getElements().values()) 
       		{
               	if (element.getClass().getName().equalsIgnoreCase("sys4sim.internal_model.Process"))
       			{			
       			if (!((sys4sim.internal_model.Process)element).getResourcePools().isEmpty())
       			{ 
       			 Hashtable<ResourcePool, Integer> tablePools = new Hashtable<ResourcePool, Integer>();
				 tablePools = ((sys4sim.internal_model.Process)element).getResourcePools();
				 
				 for(ResourcePool t : tablePools.keySet())
				 {
					 t.setId(IDErzeugen(8,9));
				 }
       				
       			}
       			}
       		}
            }
               
        //Erzeugen der anylogic IDs
        for (ModelElement element : model.getElements().values()) 
        {
        	element.setId(IDErzeugen(8,9));
        	System.out.println("--------------");
        	System.out.println(element.getName());
        	System.out.println(element.getId());
        }
        
       //weise den Eleementen Koordinaten zu
        int X=100; int Y=140;
        model.getEntities().get(0).getSource().setX(X);
        model.getEntities().get(0).getSource().setY(Y);
        CreateXYrekursiv(model.getEntities().get(0).getSource(),X,Y);       
           
        //erzeugen der Connectoren zu den ResourcePools
        if (!set.getDelayMode())
        {
        int zaehlerConnectorPool =0;
        for (ModelElement element : model.getElements().values()) 
		{
        	if (element.getClass().getName().equalsIgnoreCase("sys4sim.internal_model.Process"))
			{			
			if (!((sys4sim.internal_model.Process)element).getResourcePools().isEmpty())
			{ 
					 Hashtable<ResourcePool, Integer> tablePools = new Hashtable<ResourcePool, Integer>();
					 tablePools = ((sys4sim.internal_model.Process)element).getResourcePools();
					 
					 for(ResourcePool t : tablePools.keySet())
					 {
						 zaehlerConnectorPool+=1;
						 CreateConnectorForResourcePool(Connectors,element,t,zaehlerConnectorPool);
					 }
			}
			}
		}
        }
	   //FOR für die Erzeugung der Connectoren	
        int zaehlerConnector =0;
		for (ModelElement element : model.getElements().values()) 
		{
			
			if (element.getClass().getName().equalsIgnoreCase("sys4sim.internal_model.Connector"))
			{
				zaehlerConnector = zaehlerConnector+1;
				CreateConnector(Connectors,(Connector)element,zaehlerConnector);	
			}
		}
		
		Element EmbeddedObjects = new Element("EmbeddedObjects");
        ActiveObjectClass.addContent(EmbeddedObjects);
        
       //FOR für die Erzeugung der Elemente	
        int zaehlerSink = 0;int zaehlerSource = 0;int zaehlerMachine = 0;int zaehlerQueue = 0;
		for (ModelElement element : model.getElements().values()) 
		{   
			
			if (element.getClass().getName().equalsIgnoreCase("sys4sim.internal_model.Sink"))
				{
				zaehlerSink=zaehlerSink+1;
				CreateSink(EmbeddedObjects,element,zaehlerSink);
				}
			if (element.getClass().getName().equalsIgnoreCase("sys4sim.internal_model.Source"))
				{
				zaehlerSource=zaehlerSource+1;
				CreateSource(EmbeddedObjects,element,zaehlerSource);
				}
			if (element.getClass().getName().equalsIgnoreCase("sys4sim.internal_model.Process"))
				{
				zaehlerMachine=zaehlerMachine+1;
				//if the process need a resourcepool create service else create delay
				if (((sys4sim.internal_model.Process)element).getResourcePools().isEmpty())
					CreateDelay (EmbeddedObjects,element,zaehlerMachine,set);
				else if (!set.getDelayMode())CreateProcess(EmbeddedObjects,element,zaehlerMachine,set);
				else CreateDelay (EmbeddedObjects,element,zaehlerMachine,set);
				}
			if (element.getClass().getName().equalsIgnoreCase("sys4sim.internal_model.Queue"))
				{
				zaehlerQueue=zaehlerQueue+1;
				CreateQueue(EmbeddedObjects,element,zaehlerQueue,set);
				}
			if (element.getClass().getName().equalsIgnoreCase("sys4sim.internal_model.Worker"))
				CreateWorker(EmbeddedObjects,element);
			if (element.getClass().getName().equalsIgnoreCase("sys4sim.internal_model.Transporter"))
				CreateTransporter(EmbeddedObjects,element);
		}
		
		//FOR für die Erzeugung der RessourcePools	///verschiedene resourcepools brauchen verschiedene Namen dann hier testen ob schon gebaut
		if (!set.getDelayMode())
		{
		int zaehlerMachinePool =0;int zaehlerWorkerPool =0;int zaehlerTransporterPool =0;
		for (ModelElement element : model.getElements().values()) 
		{
			if (element.getClass().getName().equalsIgnoreCase("sys4sim.internal_model.Process"))
				
				if (!((sys4sim.internal_model.Process)element).getResourcePools().isEmpty())
			       { 
					 Hashtable<ResourcePool, Integer> tablePools = new Hashtable<ResourcePool, Integer>();
					 tablePools = ((sys4sim.internal_model.Process)element).getResourcePools();
					 
					 
					 for(ResourcePool t : tablePools.keySet())
					 {
						 if (t.getClass().getName().equalsIgnoreCase("sys4sim.internal_model.MachinePool"))
							{
							zaehlerMachinePool = zaehlerMachinePool+1;
							String name = "MachinePool";
							int resourcesInPool=(((sys4sim.internal_model.MachinePool)t).getElements().size());
							int needResources=(tablePools.get(t));
							//create ID for ResourcePool
							///((sys4sim.internal_model.ModelElement)t).setId(IDErzeugen(8,9));
							//create x and y value for ResourcePool
							if (t.getX()==0&&t.getY()==0)
						    {
								t.setX(element.getX()+10);
								t.setY(element.getY()+50);
						    }
							//create element 
							CreateResourcePool(EmbeddedObjects,(sys4sim.internal_model.ModelElement)t,zaehlerMachinePool,name,resourcesInPool,set);
						    }
						else if (t.getClass().getName().equalsIgnoreCase("sys4sim.internal_model.TransporterPool"))
						{
							zaehlerTransporterPool = zaehlerTransporterPool+1;
							String name = "TransporterPool";
							int resourcesInPool=(((sys4sim.internal_model.TransporterPool)t).getElements().size());
							int needResources=(tablePools.get(t));
							//create ID for ResourcePool
							///((sys4sim.internal_model.ModelElement)t).setId(IDErzeugen(8,9));
							//create x and y value for ResourcePool
							if (element.getX()==0&&element.getY()==0)
						    {
								t.setX(element.getX()+10);
								t.setY(element.getY()+50);
						    }
							//create element
							CreateResourcePool(EmbeddedObjects,(sys4sim.internal_model.ModelElement)t,zaehlerTransporterPool,name,resourcesInPool,set);
					    }
						else if (t.getClass().getName().equalsIgnoreCase("sys4sim.internal_model.WorkerPool"))
						{
							zaehlerWorkerPool = zaehlerWorkerPool+1;
							String name = "WorkerPool";
							int resourcesInPool=(((sys4sim.internal_model.WorkerPool)t).getElements().size());
							int needResources=(tablePools.get(t));
							//create ID for ResourcePool
							///((sys4sim.internal_model.ModelElement)t).setId(IDErzeugen(8,9));
							//create x and y value for ResourcePool
							if (element.getX()==0&&element.getY()==0)
						    {
								t.setX(element.getX()+10);
								t.setY(element.getY()+50);
						    }
							//create element
							CreateResourcePool(EmbeddedObjects,(sys4sim.internal_model.ModelElement)t,zaehlerWorkerPool,name,resourcesInPool,set);
						}
						else System.out.println("Error: no valid RessourcePool");
					 }
					 
			       }
		}
		}
	
	// erzeugen des Experiments
		
		Element Experiments = new Element("Experiments");
	    Model.addContent(Experiments);
	      Element Experiment = new Element("SimulationExperiment");
	      Experiments.addContent(Experiment);
	      Experiment.setAttribute("ActiveObjectClassId","1213174774547")
	      .addContent(new Element("Id").setText("1213174774548"))
	      .addContent(new Element("Name").setText("<![CDATA[Simulation]]>"))
	      .addContent(new Element("ExcludeFromBuild").setText("false"))
	      .addContent(new Element("ClientAreaTopLeft").setText("<X>0</X><Y>0</Y>"))
	      .addContent(new Element("PresentationTopGroupPersistent").setText("true"))
	      .addContent(new Element("IconTopGroupPersistent").setText("true"));
	      Element Frame = new Element("Frame");
	      Experiment.addContent(Frame);
	      Frame
	      .addContent(new Element("X").setText("0"))
	      .addContent(new Element("Y").setText("0"))
	      .addContent(new Element("Width").setText("800"))
	      .addContent(new Element("Height").setText("600"));
	      Experiment
	      .addContent(new Element("CommandLineArguments").setText("<![CDATA[]]>"))
	      .addContent(new Element("MaximumMemory").setText("64"))
	      .addContent(new Element("RandomSeed").setText("false"))
	      .addContent(new Element("SeedValue").setText("1"))
	      .addContent(new Element("VmArgs").setText("<![CDATA[]]>"))
	      .addContent(new Element("AbsoluteAccuracy").setText("1.0E-5"))
	      .addContent(new Element("RelativeAccuracy").setText("1.0E-5"))
	      .addContent(new Element("TimeAccuracy").setText("1.0E-5"))
	      .addContent(new Element("FixedTimeStep").setText("0.0010"))
	      .addContent(new Element("LoadRootFromSnapshot").setText("false"))
	      .addContent(new Element("SnapshotFile").setText(""));//
	      Element Shapes = new Element("Shapes");
	      Experiment.addContent(Shapes);
	        Element Text = new Element("Text");
	        Shapes.addContent(Text);
	        Text
	          .addContent(new Element("Id").setText("0"))
	          .addContent(new Element("Name").setText("<![CDATA[text]]>"))
	          .addContent(new Element("ExcludeFromBuild").setText("false"))
	          .addContent(new Element("X").setText("40"))
	          .addContent(new Element("Y").setText("30"))
	          .addContent(new Element("Label").setText("<X>10</X><Y>0</Y>"))
	          .addContent(new Element("PublicFlag").setText("true"))
	          .addContent(new Element("PresentationFlag").setText("true"))
	          .addContent(new Element("ShowLabel").setText("false"))
	          .addContent(new Element("AsObject").setText("true"))
	          .addContent(new Element("EmbeddedIcon").setText("false"))
	          .addContent(new Element("Rotation").setText("0.0"))
	          .addContent(new Element("Color").setText("-16777216"))
	          .addContent(new Element("Text").setText("<![CDATA[MM1]]>"));//ändern dynamisch
	          Element Font = new Element("Font");
	          Text.addContent(Font);
	          Font
	            .addContent(new Element("Name").setText("Times New Roman"))
	            .addContent(new Element("Size").setText("28"))
	            .addContent(new Element("Style").setText("1"));
	          Text
	          .addContent(new Element("Alignment").setText("LEFT"));
	        Element Text2 = new Element("Text");
		    Shapes.addContent(Text2);
		    Text2
		      .addContent(new Element("Id").setText("0"))
	          .addContent(new Element("Name").setText("<![CDATA[text1]]>"))
	          .addContent(new Element("ExcludeFromBuild").setText("false"))
	          .addContent(new Element("X").setText("40"))
	          .addContent(new Element("Y").setText("63"))
	          .addContent(new Element("Label").setText("<X>10</X><Y>0</Y>"))
	          .addContent(new Element("PublicFlag").setText("true"))
	          .addContent(new Element("PresentationFlag").setText("true"))
	          .addContent(new Element("ShowLabel").setText("false"))
	          .addContent(new Element("AsObject").setText("true"))
	          .addContent(new Element("EmbeddedIcon").setText("false"))
	          .addContent(new Element("Rotation").setText("0.0"))
	          .addContent(new Element("Color").setText("-16777216"))
	          .addContent(new Element("Text").setText("<![CDATA[Experiment setup page]]>"));
	          Element Font2 = new Element("Font");
	          Text2.addContent(Font2);
	          Font2
	            .addContent(new Element("Name").setText("Times New Roman"))
	            .addContent(new Element("Size").setText("16"))
	            .addContent(new Element("Style").setText("2"));
	          Text2
	          .addContent(new Element("Alignment").setText("LEFT"));
	      Element Controls = new Element("Controls");
		  Experiment.addContent(Controls);
		    Element Control = new Element("Control");
		    Controls.addContent(Control);
		    Control.setAttribute("Type","Button")
		    .addContent(new Element("EmbeddedIcon").setText("false"))
		    .addContent(new Element("Id").setText("0"))
		    .addContent(new Element("Name").setText("<![CDATA[button]]>"))
		    .addContent(new Element("ExcludeFromBuild").setText("false"))
		    .addContent(new Element("X").setText("40"))
		    .addContent(new Element("Y").setText("120"))
		    .addContent(new Element("Label").setText("<X>10</X><Y>0</Y>"))
		    .addContent(new Element("PublicFlag").setText("true"))
		    .addContent(new Element("PresentationFlag").setText("true"))
		    .addContent(new Element("ShowLabel").setText("false"));
		    Element BasicProperties  = new Element("BasicProperties");
		    Control.addContent(BasicProperties );
		    BasicProperties
		    .setAttribute("Width","220")
		    .setAttribute("Height","30")
		    .setAttribute("AsObject","true")
		    .addContent(new Element("EmbeddedIcon").setText("false"))
		    .addContent(new Element("FillColor").setText(""))//
		    .addContent(new Element("TextColor").setText(""))//
		    .addContent(new Element("EnableExpression").setText("<![CDATA[getState() == IDLE]]>"))
		    .addContent(new Element("ActionCode").setText("<![CDATA[run();" +
		    		"getEngine().getPresentation().setPresentable( getEngine().getRoot() );]]>"));//
		    Element ExtendedProperties  = new Element("ExtendedProperties");
		    Control.addContent(ExtendedProperties );
		      Element Font3   = new Element("Font");
		      ExtendedProperties.addContent(Font3);
		      Font3.setAttribute("Name","Tahoma")
		           .setAttribute("Size","11")
		           .setAttribute("Style","0");
		      ExtendedProperties.addContent(new Element("LabelText").setText("<![CDATA[Run the model and switch to Main view]]>"));
	 Element Parameters = new Element("Parameters");//
	 Experiment.addContent(Parameters);//
	 Element PresentationProperties  = new Element("PresentationProperties");
	 Experiment.addContent(PresentationProperties);
	   PresentationProperties 
	   .setAttribute("EnableAdaptiveFrameManagement","true")
	   .setAttribute("EnableAntiAliasing","true")
	   .setAttribute("EnablePanning","true")
	   .setAttribute("EnableZoom","true")
	   .addContent(new Element("ExecutionMode").setText("<![CDATA[realTime]]>"))
	   .addContent(new Element("CpuRatio").setText("<![CDATA[ratio_1_2]]>"))
	   .addContent(new Element("Title").setText("<![CDATA[MM1 : Simulation]]>"))
	   .addContent(new Element("FramesPerSecond").setText("<![CDATA[20.0]]>"))
	   .addContent(new Element("RealTimeScale").setText("SCALE_1"));
	   //Element UIProperty   = new Element("UIProperty");
	   //PresentationProperties.addContent(UIProperty);
	   //UIProperty
	   //.setAttribute("Experiment Progress","false");
	   Element UIProperty2   = new Element("UIProperty");
	   PresentationProperties.addContent(UIProperty2);
	   UIProperty2
	   .setAttribute("Name","Simulation Progress")
	   .setAttribute("Value","true");
	   Element UIProperty3   = new Element("UIProperty");
	   PresentationProperties.addContent(UIProperty3);
	   UIProperty3
	   .setAttribute("Name","Statusbar Events Per Second")
	   .setAttribute("Value","false");
	   Element UIProperty4   = new Element("UIProperty");
	   PresentationProperties.addContent(UIProperty4);
	   UIProperty4
	   .setAttribute("Name","Statusbar Frames Per Second")
	   .setAttribute("Value","false");
	   Element UIProperty5   = new Element("UIProperty");
	   PresentationProperties.addContent(UIProperty5);
	   UIProperty5
	   .setAttribute("Name","Statusbar Memory")
	   .setAttribute("Value","true");
	   Element UIProperty6   = new Element("UIProperty");
	   PresentationProperties.addContent(UIProperty6);
	   UIProperty6
	   .setAttribute("Name","Statusbar Model Date")
	   .setAttribute("Value","false");
	   Element UIProperty7   = new Element("UIProperty");
	   PresentationProperties.addContent(UIProperty7);
	   UIProperty7
	   .setAttribute("Name","Statusbar Model Step")
	   .setAttribute("Value","false");
	   Element UIProperty8   = new Element("UIProperty");
	   PresentationProperties.addContent(UIProperty8);
	   UIProperty8
	   .setAttribute("Name","Statusbar Model Time")
	   .setAttribute("Value","true");
	   Element UIProperty9   = new Element("UIProperty");
	   PresentationProperties.addContent(UIProperty9);
	   UIProperty9
	   .setAttribute("Name","Statusbar Real Time Of Simulation")
	   .setAttribute("Value","true");
	   Element UIProperty10   = new Element("UIProperty");
	   PresentationProperties.addContent(UIProperty10);
	   UIProperty10
	   .setAttribute("Name","Statusbar Status")
	   .setAttribute("Value","true");
	   Element UIProperty11   = new Element("UIProperty");
	   PresentationProperties.addContent(UIProperty11);
	   UIProperty11
	   .setAttribute("Name","Toolbar Animation setup")
	   .setAttribute("Value","false");
	   Element UIProperty12   = new Element("UIProperty");
	   PresentationProperties.addContent(UIProperty12);
	   UIProperty12
	   .setAttribute("Name","Toolbar Execution control")
	   .setAttribute("Value","true");
	   Element UIProperty13   = new Element("UIProperty");
	   PresentationProperties.addContent(UIProperty13);
	   UIProperty13
	   .setAttribute("Name","Toolbar File")
	   .setAttribute("Value","false");
	   Element UIProperty14   = new Element("UIProperty");
	   PresentationProperties.addContent(UIProperty14);
	   UIProperty14
	   .setAttribute("Name","Toolbar Model navigation")
	   .setAttribute("Value","true");
	   Element UIProperty15   = new Element("UIProperty");
	   PresentationProperties.addContent(UIProperty15);
	   UIProperty15
	   .setAttribute("Name","Toolbar Time scale setup")
	   .setAttribute("Value","true");
	   Element UIProperty16   = new Element("UIProperty");
	   PresentationProperties.addContent(UIProperty16);
	   UIProperty16
	   .setAttribute("Name","Toolbar View")
	   .setAttribute("Value","false");
	 Element ModelTimeProperties   = new Element("ModelTimeProperties");
	 Experiment.addContent(ModelTimeProperties );
	 ModelTimeProperties 
	 .setAttribute("UseCalendar","false")
	 .addContent(new Element("StopOption").setText("<![CDATA[Stop at specified time]]>"))
	 .addContent(new Element("InitialDate").setText("<![CDATA[1213174774546]]>"))
	 .addContent(new Element("InitialTime").setText("<![CDATA[0.0]]>"))
	 .addContent(new Element("FinalDate").setText("<![CDATA[1221814774546]]>"))
	 .addContent(new Element("FinalTime").setText("<![CDATA[100.0]]>"))
	 .addContent(new Element("ModelTimeUnit").setText("<![CDATA[Day]]>"));
    Element RequiredLibraryReference   = new Element("RequiredLibraryReference");
    Model.addContent(RequiredLibraryReference );
    RequiredLibraryReference
    .addContent(new Element("LibraryName").setText("<![CDATA[com.xj.anylogic.libraries.enterprise]]>"))
    .addContent(new Element("VersionMajor").setText("6"))
    .addContent(new Element("VersionMinor").setText("0"))
    .addContent(new Element("VersionBuild").setText("1"));
    
	      
	// neues Dok. benötigt nur Wurzel
   	 Document doc = new Document(root);     
   	 doc.setRootElement(root);  	
   	  
   	// definierte XML Struktur in festgelegte Outputdatei übertragen
   	 try {
   		 FileOutputStream out =  new  FileOutputStream("test.xml");
   		  XMLOutputter serializer = new XMLOutputter(Format.getPrettyFormat());
   		  serializer.output(doc,out);
   		  out.flush();
   		  out.close();
   		}
   	 catch (IOException e) {
   		 
   	 }
   	 
   	 String file2 = file.toString();
   	 Sonderzeichen(file2);
			 
	}
	
	//Methode zur Erzeugung der einzelnen Konnektoren
	private static Element CreateConnector(Element Connectors, Connector element,int zaehlerConnector) 
	{ 
		String zname = "connector"+String.valueOf(zaehlerConnector);
		String name = "<![CDATA["+zname+"]]>";
		String name2= "<![CDATA["+element.getName()+"]]>";
		String ID = element.getId();
		String sourceID = element.getSource().getId().toString();
		String targetID = element.getTarget().getId().toString();
		
		//element.getTarget();
		
		Element Connector = new Element("Connector");
        Connectors.addContent(Connector);
        Connector
        .addContent(new Element("Id").setText(ID))//
        .addContent(new Element("Name").setText(name))//
        .addContent(new Element("ExcludeFromBuild").setText("false"))
        .addContent(new Element("X").setText("170"))//Koordinaten
        .addContent(new Element("Y").setText("150"))                
        .addContent(new Element("Label").setText("<X>10</X><Y>0</Y>"))// bea ob das so geht
        .addContent(new Element("PublicFlag").setText("false"))
        .addContent(new Element("PresentationFlag").setText("true"))
        .addContent(new Element("ShowLabel").setText("false"))
        .addContent(new Element("SourceEmbeddedObject").setText(sourceID))//
        .addContent(new Element("SourceConnectableName").setText("out"))//mal gucken ob funzt alle in  bzw. out
        .addContent(new Element("TargetEmbeddedObject").setText(targetID))//
        .addContent(new Element("TargetConnectableName").setText("in"));
        Element Points = new Element("Points");
        Connector.addContent(Points);
          Points
          .addContent(new Element("Point").setText("<X>0</X><Y>0</Y>"))    //bea
          .addContent(new Element("Point").setText("<X>30</X><Y>0</Y>"));   //bea
        return Connectors;		
	}
	
	
	private static Element CreateConnectorForResourcePool(Element Connectors, ModelElement element,ResourcePool pool,int zaehlerConnector) 
	{ 
		String zname = "connectorpool"+String.valueOf(zaehlerConnector);
		String name = "<![CDATA["+zname+"]]>";
		String ID = IDErzeugen(8,9);
		String sourceID = pool.getId().toString();
		String targetID = (element).getId().toString();
		
		//element.getTarget();
		
		Element Connector = new Element("Connector");
        Connectors.addContent(Connector);
        Connector
        .addContent(new Element("Id").setText(ID))//
        .addContent(new Element("Name").setText(name))//
        .addContent(new Element("ExcludeFromBuild").setText("false"))
        .addContent(new Element("X").setText("170"))//Koordinaten
        .addContent(new Element("Y").setText("150"))                
        .addContent(new Element("Label").setText("<X>10</X><Y>0</Y>"))// bea ob das so geht
        .addContent(new Element("PublicFlag").setText("false"))
        .addContent(new Element("PresentationFlag").setText("true"))
        .addContent(new Element("ShowLabel").setText("false"))
        .addContent(new Element("SourceEmbeddedObject").setText(sourceID))//
        .addContent(new Element("SourceConnectableName").setText("port"))//mal gucken ob funzt alle in  bzw. out
        .addContent(new Element("TargetEmbeddedObject").setText(targetID))//
        .addContent(new Element("TargetConnectableName").setText("access"));
        Element Points = new Element("Points");
        Connector.addContent(Points);
          Points
          .addContent(new Element("Point").setText("<X>0</X><Y>0</Y>"))    //bea
          .addContent(new Element("Point").setText("<X>30</X><Y>0</Y>"));   //bea
        return Connectors;		
	}
	
	//Methode zur Erzeugung der einzelnen Senken
	private static Element CreateSink(Element EmbeddedObjects, ModelElement element,int zaehlerElement) 
	{
		String zname = "sink"+String.valueOf(zaehlerElement);
		String name2 = "<![CDATA["+zname+"]]>";
		String name= "<![CDATA["+element.getName()+"]]>";
		String ID = element.getId();
		String X = String.valueOf(element.getX()); 
		String Y = String.valueOf(element.getY()); 
		System.out.println(X);
		System.out.println(Y);
		System.out.println("--sink");
		
		Element EmbeddedObject = new Element("EmbeddedObject");
		EmbeddedObjects.addContent(EmbeddedObject);
			EmbeddedObject
			.addContent(new Element("Id").setText(ID))//
			.addContent(new Element("Name").setText(name))//
			.addContent(new Element("ExcludeFromBuild").setText("false"))
			.addContent(new Element("X").setText(X))//bea nebeneinander Koordinaten
			.addContent(new Element("Y").setText(Y))
			.addContent(new Element("Label").setText("<X>10</X><Y>-20</Y>"))//bea wieder gucken Schachtelung
			.addContent(new Element("PublicFlag").setText("false"))
			.addContent(new Element("PresentationFlag").setText("true"))
			.addContent(new Element("ShowLabel").setText("true"));
			Element ActiveObjectClass2 = new Element("ActiveObjectClass"); /// ActiveObjectClass2 da ohne 2 schon gibt
			EmbeddedObject.addContent(ActiveObjectClass2);
				ActiveObjectClass2
				.addContent(new Element("PackageName").setText("<![CDATA[com.xj.anylogic.libraries.enterprise]]>"))//
				.addContent(new Element("ClassName").setText("<![CDATA[Sink]]>"));//bea name des Objekts in der Bibliothek
			EmbeddedObject
			.addContent(new Element("GenericParametersSubstitute").setText("<![CDATA[Entity]]>"));//ba
			Element Parameters = new Element("Parameters"); 
			EmbeddedObject.addContent(Parameters);
				Element Parameter = new Element("Parameter"); 
				Parameters.addContent(Parameter);
				Parameter
				.addContent(new Element("Name").setText("<![CDATA[onEnter]]>"))//bei jeden Element anders für vollständigkeit müssen elemente der Bibliothek angeguckt werden
				.addContent(new Element("Value").setText("<![CDATA[]]>"));
			return EmbeddedObjects;
		
	}
	
	//Methode zur Erzeugung der einzelnen 
	private static Element CreateSource(Element EmbeddedObjects, ModelElement element,int zaehlerElement)
	{
		//initialize of the name, x-value, y-value, id, capayity of inputbuffer 
		String zname = "source"+String.valueOf(zaehlerElement);
		String name2 = "<![CDATA["+zname+"]]>";
		String name= "<![CDATA["+element.getName()+"]]>";
		String ID = ((ModelElement) element).getId();
		String X = String.valueOf(((ModelElement) element).getX()); 
		String Y = String.valueOf(((ModelElement) element).getY()); 
		//int capacity =((sys4sim.internal_model.Source) element).getCapacity();
		int batchSizeValue=((sys4sim.internal_model.Source) element).getBatchSize();
		String batchSize = "<![CDATA["+String.valueOf(batchSizeValue)+"]]>";
		//get distribution
		Hashtable<Entity, Rate> tableRate = new Hashtable<Entity, Rate>();
		tableRate = ((sys4sim.internal_model.Source) element).getEntities();
		String distribution ="";
		for(Entity t : tableRate.keySet())
		 {
			 Rate rate=(tableRate.get(t));
			 distribution=CreateDistributionForSource(rate);
		 }
		if (distribution=="") System.out.println("Error no distribution, look at function CreateSource.");
		System.out.println(X);
		System.out.println(Y);
		System.out.println("--source");
		
    	
		Element EmbeddedObject = new Element("EmbeddedObject");
		EmbeddedObjects.addContent(EmbeddedObject);
			EmbeddedObject
			.addContent(new Element("Id").setText(ID))
			.addContent(new Element("Name").setText(name))
			.addContent(new Element("ExcludeFromBuild").setText("false"))
			.addContent(new Element("X").setText(X))
			.addContent(new Element("Y").setText(Y))
			.addContent(new Element("Label").setText("<X>10</X><Y>-20</Y>"))
			.addContent(new Element("PublicFlag").setText("false"))
			.addContent(new Element("PresentationFlag").setText("true"))
			.addContent(new Element("ShowLabel").setText("true"));
			Element ActiveObjectClass2 = new Element("ActiveObjectClass"); 
			EmbeddedObject.addContent(ActiveObjectClass2);
				ActiveObjectClass2
				.addContent(new Element("PackageName").setText("<![CDATA[com.xj.anylogic.libraries.enterprise]]>"))//
				.addContent(new Element("ClassName").setText("<![CDATA[Source]]>"));//
			EmbeddedObject
			.addContent(new Element("GenericParametersSubstitute").setText("<![CDATA[Entity]]>"));//
			Element Parameters = new Element("Parameters"); 
			EmbeddedObject.addContent(Parameters);
				Element Parameter = new Element("Parameter"); 
				Parameters.addContent(Parameter);
				Parameter
				.addContent(new Element("Name").setText("<![CDATA[arrivalType]]>"))//bleibt aussen vor
				.addContent(new Element("Value").setText("<![CDATA[Source.INTERARRIVAL_TIME]]>"));//
				Element Parameter2 = new Element("Parameter"); 
				Parameters.addContent(Parameter2);
				Parameter2
				.addContent(new Element("Name").setText("<![CDATA[rate]]>"))//wenn on enter code da ist
				.addContent(new Element("Value").setText("<![CDATA[]]>"));//wenn on enter code da ist jetzt bsp
				Element Parameter3 = new Element("Parameter"); 
				Parameters.addContent(Parameter3);
				Parameter3
				.addContent(new Element("Name").setText("<![CDATA[interarrivalTime]]>"))//bleibt aussen vor
				.addContent(new Element("Value").setText(distribution));
				Element Parameter4 = new Element("Parameter"); 
				Parameters.addContent(Parameter4);
				Parameter4
				.addContent(new Element("Name").setText("<![CDATA[rateTable]]>"))//bleibt aussen vor
				.addContent(new Element("Value").setText("<![CDATA[]]>"));
				Element Parameter5 = new Element("Parameter"); 
				Parameters.addContent(Parameter5);
				Parameter5
				.addContent(new Element("Name").setText("<![CDATA[arrivalTable]]>"))//bleibt aussen vor
				.addContent(new Element("Value").setText("<![CDATA[]]>"));
				Element Parameter6 = new Element("Parameter"); 
				Parameters.addContent(Parameter6);
				Parameter6
				.addContent(new Element("Name").setText("<![CDATA[entitiesPerArrival]]>"))//wenn code da
				.addContent(new Element("Value").setText(batchSize));//hab mal 1
				Element Parameter7 = new Element("Parameter"); 
				Parameters.addContent(Parameter7);
				Parameter7
				.addContent(new Element("Name").setText("<![CDATA[limitArrivals]]>"))//bleibt aussen vor
				.addContent(new Element("Value").setText("<![CDATA[]]>"));
				Element Parameter8 = new Element("Parameter"); 
				Parameters.addContent(Parameter8);
				Parameter8
				.addContent(new Element("Name").setText("<![CDATA[maxArrivals]]>"))
				.addContent(new Element("Value").setText("<![CDATA[]]>"));
				Element Parameter9 = new Element("Parameter"); 
				Parameters.addContent(Parameter9);
				Parameter9
				.addContent(new Element("Name").setText("<![CDATA[newEntity]]>"))
				.addContent(new Element("Value").setText("<![CDATA[]]>"));
				Element Parameter10 = new Element("Parameter"); 
				Parameters.addContent(Parameter10);
				Parameter10
				.addContent(new Element("Name").setText("<![CDATA[onExit]]>"))
				.addContent(new Element("Value").setText("<![CDATA[]]>"));
				Element Parameter11 = new Element("Parameter"); 
				Parameters.addContent(Parameter11);
				Parameter11
				.addContent(new Element("Name").setText("<![CDATA[entityShape]]>"))
				.addContent(new Element("Value").setText("<![CDATA[]]>"));
				Element Parameter12 = new Element("Parameter"); 
				Parameters.addContent(Parameter12);
				Parameter12
				.addContent(new Element("Name").setText("<![CDATA[uniqueShape]]>"))
				.addContent(new Element("Value").setText("<![CDATA[]]>"));
				Element Parameter13 = new Element("Parameter"); 
				Parameters.addContent(Parameter13);
				Parameter13
				.addContent(new Element("Name").setText("<![CDATA[enableRotation]]>"))
				.addContent(new Element("Value").setText("<![CDATA[]]>"));
			return EmbeddedObjects;
		
	}
	
	//Methode zur Erzeugung der einzelnen 
	private static Element CreateDelay(Element EmbeddedObjects, ModelElement element,int zaehlerElement,Settings set)
	{
		String zname = "delay"+String.valueOf(zaehlerElement);
		String name2 = "<![CDATA["+zname+"]]>";
		String name= "<![CDATA["+element.getName()+"]]>";
		String ID = element.getId();
		String X = String.valueOf(element.getX()); 
		String Y = String.valueOf(element.getY()); 
		String distribution = CreateDistribution(element);
		int capacityValue =((sys4sim.internal_model.Process) element).getCapacity();
		if (capacityValue==0) capacityValue=1;
		String capacity = "<![CDATA["+String.valueOf(capacityValue)+"]]>";
		if (set.getPortChoice()==2) capacity = "<![CDATA["+String.valueOf(10000)+"]]>";

		
		String onExit = "<![CDATA["+((ModelBlock)element).getOnExit()+"]]>";
		System.out.println(X);
		System.out.println(Y);
		System.out.println("--process");
		
		
		Element EmbeddedObject = new Element("EmbeddedObject");
		EmbeddedObjects.addContent(EmbeddedObject);
			EmbeddedObject
			.addContent(new Element("Id").setText(ID))//
			.addContent(new Element("Name").setText(name))//
			.addContent(new Element("ExcludeFromBuild").setText("false"))
			.addContent(new Element("X").setText(X))//
			.addContent(new Element("Y").setText(Y))//
			.addContent(new Element("Label").setText("<X>10</X><Y>-20</Y>"))//
			.addContent(new Element("PublicFlag").setText("false"))
			.addContent(new Element("PresentationFlag").setText("true"))
			.addContent(new Element("ShowLabel").setText("true"));
			Element ActiveObjectClass2 = new Element("ActiveObjectClass"); /// ActiveObjectClass2 da ohne 2 schon gibt
			EmbeddedObject.addContent(ActiveObjectClass2);
				ActiveObjectClass2
				.addContent(new Element("PackageName").setText("<![CDATA[com.xj.anylogic.libraries.enterprise]]>"))//
				.addContent(new Element("ClassName").setText("<![CDATA[Delay]]>"));//
			EmbeddedObject
			.addContent(new Element("GenericParametersSubstitute").setText("<![CDATA[Entity]]>"));//
			Element Parameters = new Element("Parameters"); 
			EmbeddedObject.addContent(Parameters);
				Element Parameter = new Element("Parameter"); 
				Parameters.addContent(Parameter);
				Parameter
				.addContent(new Element("Name").setText("<![CDATA[delayTimeDefinedByPath]]>"))//muss dann gemacht werden aus dem modell
				.addContent(new Element("Value").setText("<![CDATA[]]>"));//gleiches
				Element Parameter2 = new Element("Parameter"); 
				Parameters.addContent(Parameter2);
				Parameter2
				.addContent(new Element("Name").setText("<![CDATA[delayTime]]>"))//muss dann gemacht werden
				.addContent(new Element("Value").setText(distribution));//wenn on enter code da ist jetzt bsp
				Element Parameter3 = new Element("Parameter"); 
				Parameters.addContent(Parameter3);
				Parameter3
				.addContent(new Element("Name").setText("<![CDATA[speed]]>"))//bleibt aussen vor
				.addContent(new Element("Value").setText("<![CDATA[]]>"));
				Element Parameter4 = new Element("Parameter"); 
				Parameters.addContent(Parameter4);
				Parameter4
				.addContent(new Element("Name").setText("<![CDATA[capacity]]>"))//muss dann gemacht werden
				.addContent(new Element("Value").setText(capacity));
				Element Parameter5 = new Element("Parameter"); 
				Parameters.addContent(Parameter5);
				Parameter5
				.addContent(new Element("Name").setText("<![CDATA[maximumCapacity]]>"))//dann wenn modell
				.addContent(new Element("Value").setText("<![CDATA[]]>"));
				Element Parameter6 = new Element("Parameter"); 
				Parameters.addContent(Parameter6);
				Parameter6
				.addContent(new Element("Name").setText("<![CDATA[onEnter]]>"))//dann wenn modell
				.addContent(new Element("Value").setText("<![CDATA[]]>"));//
				Element Parameter7 = new Element("Parameter"); 
				Parameters.addContent(Parameter7);
				Parameter7
				.addContent(new Element("Name").setText("<![CDATA[onExit]]>"))//dann wenn modell
				.addContent(new Element("Value").setText(onExit));
				Element Parameter8 = new Element("Parameter"); 
				Parameters.addContent(Parameter8);
				Parameter8
				.addContent(new Element("Name").setText("<![CDATA[animationGuide]]>"))//bleibt aussen vor 
				.addContent(new Element("Value").setText("<![CDATA[]]>"));
				Element Parameter9 = new Element("Parameter"); 
				Parameters.addContent(Parameter9);
				Parameter9
				.addContent(new Element("Name").setText("<![CDATA[animationType]]>"))//bleibt aussen vor 
				.addContent(new Element("Value").setText("<![CDATA[]]>"));
				Element Parameter10 = new Element("Parameter"); 
				Parameters.addContent(Parameter10);
				Parameter10  
				.addContent(new Element("Name").setText("<![CDATA[animationForward]]>"))//bleibt aussen vor
				.addContent(new Element("Value").setText("<![CDATA[]]>"));
				Element Parameter11 = new Element("Parameter"); 
				Parameters.addContent(Parameter11);
				Parameter11
				.addContent(new Element("Name").setText("<![CDATA[enableStats]]>"))//bleibt aussen vor
				.addContent(new Element("Value").setText("<![CDATA[]]>"));
		return EmbeddedObjects;
	}
	
	private static Element CreateProcess(Element EmbeddedObjects, ModelElement element,int zaehlerElement,Settings set)
	{
		//initialize of the name, x-value, y-value, id, capayity of inputbuffer and distribution
		String zname = "service"+String.valueOf(zaehlerElement);
		String name2 = "<![CDATA["+zname+"]]>";
		String name= "<![CDATA["+element.getName()+"]]>";
		String ID = element.getId();
		String X = String.valueOf(element.getX()); 
		String Y = String.valueOf(element.getY()-20); 
		int capacityValue = ((sys4sim.internal_model.Process) element).getCapacity();
		String capacity = "<![CDATA["+String.valueOf(capacityValue)+"]]>";
		if (capacityValue==0) capacityValue=1;
		if (set.getPortChoice()==2) capacity = "<![CDATA["+String.valueOf(10000)+"]]>";
		String distribution = CreateDistribution(element);
		
		String onExit = "<![CDATA["+((ModelBlock)element).getOnExit()+"]]>";
		
		//search number of needed resources for one entity, now only one resourcepool per process is realized
		int needResources=0;
		if (!((sys4sim.internal_model.Process)element).getResourcePools().isEmpty())
	       { 
			 Hashtable<ResourcePool, Integer> tablePools = new Hashtable<ResourcePool, Integer>();
			 tablePools = ((sys4sim.internal_model.Process)element).getResourcePools();
			 
			 int z=0;
			 for(ResourcePool t : tablePools.keySet())
			 {
				 z=z+1;
				 if (z > 1) System.out.println("Error by creating process, more then one resourcepool for this process. Please check the function Create Process");
				 needResources=(tablePools.get(t));
			 }
	       }
		String needResourcesSrting = "<![CDATA["+String.valueOf(needResources)+"]]>";
		
		
		//create the XML file
		Element EmbeddedObject = new Element("EmbeddedObject");
		EmbeddedObjects.addContent(EmbeddedObject);
			EmbeddedObject
			.addContent(new Element("Id").setText(ID))
			.addContent(new Element("Name").setText(name))
			.addContent(new Element("ExcludeFromBuild").setText("false"))
			.addContent(new Element("X").setText(X))
			.addContent(new Element("Y").setText(Y))
			.addContent(new Element("Label").setText("<X>10</X><Y>0</Y>"))
			.addContent(new Element("PublicFlag").setText("false"))
			.addContent(new Element("PresentationFlag").setText("true"))
			.addContent(new Element("ShowLabel").setText("true"));
			Element ActiveObjectClass2 = new Element("ActiveObjectClass"); 
			EmbeddedObject.addContent(ActiveObjectClass2);
				ActiveObjectClass2
				.addContent(new Element("PackageName").setText("<![CDATA[com.xj.anylogic.libraries.enterprise]]>"))
				.addContent(new Element("ClassName").setText("<![CDATA[Service]]>"));
			EmbeddedObject
			.addContent(new Element("GenericParametersSubstitute").setText("<![CDATA[Entity]]>"));
			Element Parameters = new Element("Parameters"); 
			EmbeddedObject.addContent(Parameters);
				Element Parameter = new Element("Parameter"); 
				Parameters.addContent(Parameter);
				Parameter
				.addContent(new Element("Name").setText("<![CDATA[quantity]]>"))//muss dann gemacht werden aus dem modell
				.addContent(new Element("Value").setText(needResourcesSrting));//wie viele einheiten vom resourcepool gebraucht werden
				Element Parameter2 = new Element("Parameter"); 
				Parameters.addContent(Parameter2);
				Parameter2
				.addContent(new Element("Name").setText("<![CDATA[delayTime]]>"))//muss dann gemacht werden
				.addContent(new Element("Value").setText(distribution));//wenn on enter code da ist jetzt bsp
				Element Parameter3 = new Element("Parameter"); 
				Parameters.addContent(Parameter3);
				Parameter3
				.addContent(new Element("Name").setText("<![CDATA[resourcePool]]>"))//bleibt aussen vor
				.addContent(new Element("Value").setText("<![CDATA[]]>"));
				Element Parameter4 = new Element("Parameter"); 
				Parameters.addContent(Parameter4);
				Parameter4
				.addContent(new Element("Name").setText("<![CDATA[onEnter]]>"))//muss dann gemacht werden
				.addContent(new Element("Value").setText("<![CDATA[]]>"));
				Element Parameter5 = new Element("Parameter"); 
				Parameters.addContent(Parameter5);
				Parameter5
				.addContent(new Element("Name").setText("<![CDATA[onEnterDelay]]>"))//dann wenn modell
				.addContent(new Element("Value").setText("<![CDATA[]]>"));
				Element Parameter6 = new Element("Parameter"); 
				Parameters.addContent(Parameter6);
				Parameter6
				.addContent(new Element("Name").setText("<![CDATA[onExit]]>"))//dann wenn modell
				.addContent(new Element("Value").setText(onExit));//
				Element Parameter7 = new Element("Parameter"); 
				Parameters.addContent(Parameter7);
				Parameter7
				.addContent(new Element("Name").setText("<![CDATA[queueCapacity]]>"))//dann wenn modell
				.addContent(new Element("Value").setText(capacity));
				Element Parameter8 = new Element("Parameter"); 
				Parameters.addContent(Parameter8);
				Parameter8
				.addContent(new Element("Name").setText("<![CDATA[maximumCapacity]]>"))//bleibt aussen vor 
				.addContent(new Element("Value").setText("<![CDATA[]]>"));
				Element Parameter9 = new Element("Parameter"); 
				Parameters.addContent(Parameter9);
				Parameter9
				.addContent(new Element("Name").setText("<![CDATA[enableTimeout]]>"))//bleibt aussen vor 
				.addContent(new Element("Value").setText("<![CDATA[]]>"));
				Element Parameter10 = new Element("Parameter"); 
				Parameters.addContent(Parameter10);
				Parameter10  
				.addContent(new Element("Name").setText("<![CDATA[timeout]]>"))//bleibt aussen vor
				.addContent(new Element("Value").setText("<![CDATA[]]>"));
				Element Parameter11 = new Element("Parameter"); 
				Parameters.addContent(Parameter11);
				Parameter11
				.addContent(new Element("Name").setText("<![CDATA[onExitTimeout]]>"))//bleibt aussen vor
				.addContent(new Element("Value").setText("<![CDATA[]]>"));
				Element Parameter12 = new Element("Parameter"); 
				Parameters.addContent(Parameter12);
				Parameter12
				.addContent(new Element("Name").setText("<![CDATA[enablePreemption]]>"))//bleibt aussen vor
				.addContent(new Element("Value").setText("<![CDATA[]]>"));
				Element Parameter13 = new Element("Parameter"); 
				Parameters.addContent(Parameter13);
				Parameter13
				.addContent(new Element("Name").setText("<![CDATA[onExitTimeout]]>"))//bleibt aussen vor
				.addContent(new Element("Value").setText("<![CDATA[]]>"));
				Element Parameter14 = new Element("Parameter"); 
				Parameters.addContent(Parameter14);
				Parameter14
				.addContent(new Element("Name").setText("<![CDATA[priority]]>"))//bleibt aussen vor
				.addContent(new Element("Value").setText("<![CDATA[]]>"));
				Element Parameter15 = new Element("Parameter"); 
				Parameters.addContent(Parameter15);
				Parameter15
				.addContent(new Element("Name").setText("<![CDATA[onExitPreempted]]>"))//bleibt aussen vor
				.addContent(new Element("Value").setText("<![CDATA[]]>"));
				Element Parameter16 = new Element("Parameter"); 
				Parameters.addContent(Parameter16);
				Parameter16
				.addContent(new Element("Name").setText("<![CDATA[animationGuideQueue]]>"))//bleibt aussen vor
				.addContent(new Element("Value").setText("<![CDATA[]]>"));
				Element Parameter17 = new Element("Parameter"); 
				Parameters.addContent(Parameter17);
				Parameter17
				.addContent(new Element("Name").setText("<![CDATA[animationTypeQueue]]>"))//bleibt aussen vor
				.addContent(new Element("Value").setText("<![CDATA[]]>"));
				Element Parameter18 = new Element("Parameter"); 
				Parameters.addContent(Parameter18);
				Parameter18
				.addContent(new Element("Name").setText("<![CDATA[animationForwardQueue]]>"))//bleibt aussen vor
				.addContent(new Element("Value").setText("<![CDATA[]]>"));
				Element Parameter19 = new Element("Parameter"); 
				Parameters.addContent(Parameter19);
				Parameter19
				.addContent(new Element("Name").setText("<![CDATA[animationGuideDelay]]>"))//bleibt aussen vor
				.addContent(new Element("Value").setText("<![CDATA[]]>"));
				Element Parameter20 = new Element("Parameter"); 
				Parameters.addContent(Parameter20);
				Parameter20
				.addContent(new Element("Name").setText("<![CDATA[animationTypeDelay]]>"))//bleibt aussen vor
				.addContent(new Element("Value").setText("<![CDATA[]]>"));
				Element Parameter21 = new Element("Parameter"); 
				Parameters.addContent(Parameter21);
				Parameter21
				.addContent(new Element("Name").setText("<![CDATA[animationForwardDelay]]>"))//bleibt aussen vor
				.addContent(new Element("Value").setText("<![CDATA[]]>"));
				Element Parameter22 = new Element("Parameter"); 
				Parameters.addContent(Parameter22);
				Parameter22
				.addContent(new Element("Name").setText("<![CDATA[enableStats]]>"))//bleibt aussen vor
				.addContent(new Element("Value").setText("<![CDATA[]]>"));
		return EmbeddedObjects;
	}
	
	private static Element CreateResourcePool(Element EmbeddedObjects, ModelElement element,int zaehlerElement,String zName,int zResourcesInPool,Settings set) 
	{
		String zname = zName+String.valueOf(zaehlerElement);
		String name = "<![CDATA["+zname+"]]>";
		String name2= "<![CDATA["+element.getName()+"]]>";
		String ID = element.getId();
		String X = String.valueOf(element.getX()); 
		String Y = String.valueOf(element.getY()); 
		String resourcesInPool = "<![CDATA["+zResourcesInPool+"]]>";
		
		System.out.println(X);
		System.out.println(Y);
		System.out.println("--resourcePool");
		
		Element EmbeddedObject = new Element("EmbeddedObject");
		EmbeddedObjects.addContent(EmbeddedObject);
			EmbeddedObject
			.addContent(new Element("Id").setText(ID))//
			.addContent(new Element("Name").setText(name))//
			.addContent(new Element("ExcludeFromBuild").setText("false"))
			.addContent(new Element("X").setText(X))//bea nebeneinander Koordinaten
			.addContent(new Element("Y").setText(Y))
			.addContent(new Element("Label").setText("<X>-10</X><Y>30</Y>"))//bea wieder gucken Schachtelung
			.addContent(new Element("PublicFlag").setText("false"))
			.addContent(new Element("PresentationFlag").setText("true"))
			.addContent(new Element("ShowLabel").setText("true"));
			Element ActiveObjectClass2 = new Element("ActiveObjectClass"); /// ActiveObjectClass2 da ohne 2 schon gibt
			EmbeddedObject.addContent(ActiveObjectClass2);
				ActiveObjectClass2
				.addContent(new Element("PackageName").setText("<![CDATA[com.xj.anylogic.libraries.enterprise]]>"))//
				.addContent(new Element("ClassName").setText("<![CDATA[ResourcePool]]>"));//bea name des Objekts in der Bibliothek
			EmbeddedObject
			.addContent(new Element("GenericParametersSubstitute").setText("<![CDATA[ResourceUnit]]>"));//ba
			Element Parameters = new Element("Parameters"); 
			EmbeddedObject.addContent(Parameters);
			Element Parameter = new Element("Parameter"); 
			Parameters.addContent(Parameter);
			Parameter
			.addContent(new Element("Name").setText("<![CDATA[unitsAreObjects]]>"))//muss dann gemacht werden aus dem modell
			.addContent(new Element("Value").setText("<![CDATA[]]>"));//wie viele einheiten vom resourcepool gebraucht werden
			Element Parameter2 = new Element("Parameter"); 
			Parameters.addContent(Parameter2);
			Parameter2
			.addContent(new Element("Name").setText("<![CDATA[capacityDefinedByTable]]>"))//muss dann gemacht werden
			.addContent(new Element("Value").setText("<![CDATA[]]>"));//wenn on enter code da ist jetzt bsp
			Element Parameter3 = new Element("Parameter"); 
			Parameters.addContent(Parameter3);
			Parameter3
			.addContent(new Element("Name").setText("<![CDATA[capacity]]>"))//bleibt aussen vor
			.addContent(new Element("Value").setText(resourcesInPool));
			Element Parameter4 = new Element("Parameter"); 
			Parameters.addContent(Parameter4);
			Parameter4
			.addContent(new Element("Name").setText("<![CDATA[capacityTable]]>"))//muss dann gemacht werden
			.addContent(new Element("Value").setText("<![CDATA[]]>"));
			Element Parameter5 = new Element("Parameter"); 
			Parameters.addContent(Parameter5);
			Parameter5
			.addContent(new Element("Name").setText("<![CDATA[newUnit]]>"))//dann wenn modell
			.addContent(new Element("Value").setText("<![CDATA[]]>"));
			Element Parameter6 = new Element("Parameter"); 
			Parameters.addContent(Parameter6);
			Parameter6
			.addContent(new Element("Name").setText("<![CDATA[onNewUnit]]>"))//dann wenn modell
			.addContent(new Element("Value").setText("<![CDATA[]]>"));//
			Element Parameter7 = new Element("Parameter"); 
			Parameters.addContent(Parameter7);
			Parameter7
			.addContent(new Element("Name").setText("<![CDATA[onSeize]]>"))//dann wenn modell
			.addContent(new Element("Value").setText("<![CDATA[]]>"));
			Element Parameter8 = new Element("Parameter"); 
			Parameters.addContent(Parameter8);
			Parameter8
			.addContent(new Element("Name").setText("<![CDATA[onRelease]]>"))//bleibt aussen vor 
			.addContent(new Element("Value").setText("<![CDATA[]]>"));
			Element Parameter9 = new Element("Parameter"); 
			Parameters.addContent(Parameter9);
			Parameter9
			.addContent(new Element("Name").setText("<![CDATA[enablePriorities]]>"))//bleibt aussen vor 
			.addContent(new Element("Value").setText("<![CDATA[]]>"));
			Element Parameter10 = new Element("Parameter"); 
			Parameters.addContent(Parameter10);
			Parameter10  
			.addContent(new Element("Name").setText("<![CDATA[priority]]>"))//bleibt aussen vor
			.addContent(new Element("Value").setText("<![CDATA[]]>"));
			Element Parameter11 = new Element("Parameter"); 
			Parameters.addContent(Parameter11);
			Parameter11
			.addContent(new Element("Name").setText("<![CDATA[idleUnitShape]]>"))//bleibt aussen vor
			.addContent(new Element("Value").setText("<![CDATA[]]>"));
			Element Parameter12 = new Element("Parameter"); 
			Parameters.addContent(Parameter12);
			Parameter12
			.addContent(new Element("Name").setText("<![CDATA[busyUnitShape]]>"))//bleibt aussen vor
			.addContent(new Element("Value").setText("<![CDATA[]]>"));
			Element Parameter13 = new Element("Parameter"); 
			Parameters.addContent(Parameter13);
			Parameter13
			.addContent(new Element("Name").setText("<![CDATA[uniqueShape]]>"))//bleibt aussen vor
			.addContent(new Element("Value").setText("<![CDATA[]]>"));
			Element Parameter14 = new Element("Parameter"); 
			Parameters.addContent(Parameter14);
			Parameter14
			.addContent(new Element("Name").setText("<![CDATA[enableRotation]]>"))//bleibt aussen vor
			.addContent(new Element("Value").setText("<![CDATA[]]>"));
			Element Parameter15 = new Element("Parameter"); 
			Parameters.addContent(Parameter15);
			Parameter15
			.addContent(new Element("Name").setText("<![CDATA[animationGuide]]>"))//bleibt aussen vor
			.addContent(new Element("Value").setText("<![CDATA[]]>"));
			Element Parameter16 = new Element("Parameter"); 
			Parameters.addContent(Parameter16);
			Parameter16
			.addContent(new Element("Name").setText("<![CDATA[animationType]]>"))//bleibt aussen vor
			.addContent(new Element("Value").setText("<![CDATA[]]>"));
			Element Parameter17 = new Element("Parameter"); 
			Parameters.addContent(Parameter17);
			Parameter17
			.addContent(new Element("Name").setText("<![CDATA[enableStats]]>"))//bleibt aussen vor
			.addContent(new Element("Value").setText("<![CDATA[]]>"));
		return EmbeddedObjects;
	}
	
	//Methode zur Erzeugung der einzelnen 
	private static Element CreateQueue(Element EmbeddedObjects, ModelElement element, int zaehlerElement,Settings set)
	{
		String zname = "queue"+String.valueOf(zaehlerElement);
		String name2 = "<![CDATA["+zname+"]]>";
		String name= "<![CDATA["+element.getName()+"]]>";
		String ID = element.getId();
		String X = String.valueOf(element.getX()); 
		String Y = String.valueOf(element.getY()-20); 
		int capacityValue = ((sys4sim.internal_model.Queue) element).getCapacity();
		if (capacityValue==0) capacityValue=100;
		String capacity = "<![CDATA["+String.valueOf(capacityValue)+"]]>";
		if (set.getPortChoice()==2) capacity = "<![CDATA["+String.valueOf(10000)+"]]>";
		System.out.println(X);
		System.out.println(Y);
		System.out.println("--queue");
		
		
		Element EmbeddedObject = new Element("EmbeddedObject");
		EmbeddedObjects.addContent(EmbeddedObject);
			EmbeddedObject
			.addContent(new Element("Id").setText(ID))//
			.addContent(new Element("Name").setText(name))//
			.addContent(new Element("ExcludeFromBuild").setText("false"))
			.addContent(new Element("X").setText(X))//bea nebeneinander Koordinaten
			.addContent(new Element("Y").setText(Y))
			.addContent(new Element("Label").setText("<X>10</X><Y>-20</Y>"))//
			.addContent(new Element("PublicFlag").setText("false"))
			.addContent(new Element("PresentationFlag").setText("true"))
			.addContent(new Element("ShowLabel").setText("true"));
			Element ActiveObjectClass2 = new Element("ActiveObjectClass"); /// ActiveObjectClass2 da ohne 2 schon gibt
			EmbeddedObject.addContent(ActiveObjectClass2);
				ActiveObjectClass2
				.addContent(new Element("PackageName").setText("<![CDATA[com.xj.anylogic.libraries.enterprise]]>"))//
				.addContent(new Element("ClassName").setText("<![CDATA[Queue]]>"));//
			EmbeddedObject
			.addContent(new Element("GenericParametersSubstitute").setText("<![CDATA[Entity]]>"));//
			Element Parameters = new Element("Parameters"); 
			EmbeddedObject.addContent(Parameters);
				Element Parameter = new Element("Parameter"); 
				Parameters.addContent(Parameter);
				Parameter
				.addContent(new Element("Name").setText("<![CDATA[capacity]]>"))//muss dann gemacht werden aus dem modell
				.addContent(new Element("Value").setText(capacity));//gleiches
				Element Parameter2 = new Element("Parameter"); 
				Parameters.addContent(Parameter2);
				Parameter2
				.addContent(new Element("Name").setText("<![CDATA[maximumCapacity]]>"))//wenn on enter code da ist
				.addContent(new Element("Value").setText("<![CDATA[]]>"));//wenn on enter code da ist jetzt bsp
				Element Parameter3 = new Element("Parameter"); 
				Parameters.addContent(Parameter3);
				Parameter3
				.addContent(new Element("Name").setText("<![CDATA[onEnter]]>"))//dann wenn modell
				.addContent(new Element("Value").setText("<![CDATA[]]>"));
				Element Parameter4 = new Element("Parameter"); 
				Parameters.addContent(Parameter4);
				Parameter4
				.addContent(new Element("Name").setText("<![CDATA[onAtExit]]>"))//bleibt aussen vor
				.addContent(new Element("Value").setText("<![CDATA[]]>"));
				Element Parameter5 = new Element("Parameter"); 
				Parameters.addContent(Parameter5);
				Parameter5
				.addContent(new Element("Name").setText("<![CDATA[onExit]]>"))//dann wenn modell
				.addContent(new Element("Value").setText("<![CDATA[]]>"));
				Element Parameter6 = new Element("Parameter"); 
				Parameters.addContent(Parameter6);
				Parameter6
				.addContent(new Element("Name").setText("<![CDATA[enableTimeout]]>"))//bleibt aussen vor
				.addContent(new Element("Value").setText("<![CDATA[]]>"));//
				Element Parameter7 = new Element("Parameter"); 
				Parameters.addContent(Parameter7);
				Parameter7
				.addContent(new Element("Name").setText("<![CDATA[timeout]]>"))//bleibt aussen vor
				.addContent(new Element("Value").setText("<![CDATA[]]>"));
				Element Parameter8 = new Element("Parameter"); 
				Parameters.addContent(Parameter8);
				Parameter8
				.addContent(new Element("Name").setText("<![CDATA[onExitTimeout]]>"))//bleibt aussen vor 
				.addContent(new Element("Value").setText("<![CDATA[]]>"));
				Element Parameter9 = new Element("Parameter"); 
				Parameters.addContent(Parameter9);
				Parameter9
				.addContent(new Element("Name").setText("<![CDATA[enablePreemption]]>"))//bleibt aussen vor 
				.addContent(new Element("Value").setText("<![CDATA[]]>"));
				Element Parameter10 = new Element("Parameter"); 
				Parameters.addContent(Parameter10);
				Parameter10
				.addContent(new Element("Name").setText("<![CDATA[priority]]>"))//bleibt aussen vor
				.addContent(new Element("Value").setText("<![CDATA[]]>"));
				Element Parameter11 = new Element("Parameter"); 
				Parameters.addContent(Parameter11);
				Parameter11
				.addContent(new Element("Name").setText("<![CDATA[onExitPreempted]]>"))//bleibt aussen vor
				.addContent(new Element("Value").setText("<![CDATA[]]>"));
				Element Parameter12 = new Element("Parameter"); 
				Parameters.addContent(Parameter12);
				Parameter12
				.addContent(new Element("Name").setText("<![CDATA[animationGuide]]>"))//bleibt aussen vor
				.addContent(new Element("Value").setText("<![CDATA[]]>"));
				Element Parameter13 = new Element("Parameter"); 
				Parameters.addContent(Parameter13);
				Parameter13
				.addContent(new Element("Name").setText("<![CDATA[animationType]]>"))//bleibt aussen vor
				.addContent(new Element("Value").setText("<![CDATA[]]>"));
				Element Parameter14 = new Element("Parameter"); 
				Parameters.addContent(Parameter14);
				Parameter14
				.addContent(new Element("Name").setText("<![CDATA[animationForward]]>"))//bleibt aussen vor
				.addContent(new Element("Value").setText("<![CDATA[]]>"));
				Element Parameter15 = new Element("Parameter"); 
				Parameters.addContent(Parameter15);
				Parameter15
				.addContent(new Element("Name").setText("<![CDATA[enableStats]]>"))//bleibt aussen vor
				.addContent(new Element("Value").setText("<![CDATA[]]>"));
		return EmbeddedObjects;
	}
	
	//Methode zur Erzeugung der einzelnen 
	private static Element CreateWorker(Element EmbeddedObjects, ModelElement element)
	{
		return(EmbeddedObjects);
	}
	
	//Methode zur Erzeugung der einzelnen 
	private static Element CreateTransporter(Element EmbeddedObjects, ModelElement element)
	{
		return(EmbeddedObjects);
	}
	
	private static String IDErzeugen(int anzahl, int bereich) {

        Random zufallsgenerator;
        zufallsgenerator = new Random();
        String b="12169";
        for(int i=1; i<=anzahl; i++)
        {	    
        int index = zufallsgenerator.nextInt(bereich);
        String a = String.valueOf(index);
        b =b+a;	        
        }
        return b;
    }
	
	
	
    private static void Sonderzeichen(String file)
    {
    	try
		 {
			File oldFile = new File("C:/Dokumente und Einstellungen/Administrator/workspace/sys4sim/test.xml");
		    File newFile = new File(file);
		    newFile.createNewFile();
		    String sb =readFileContent(oldFile);
		    FileWriter writer = new FileWriter(newFile);
		    writer.write(sb);
		    writer.flush();
		    writer.close();
		    oldFile.delete();
		 }
		 catch (IOException e) {
	   		 
	   	 }    	
    }
    
    private static void CreateFunction(ModelElement element)
    {
    	///muss noch für queues
    	if (element.getClass().getName().equalsIgnoreCase("sys4sim.internal_model.Process"))
		{
		     ArrayList<Connector> outList = new ArrayList<Connector>();
			 outList = ((ModelBlock)element).getOut();
			 // Hole Ziele als next
			 for ( Iterator<Connector> i = outList.iterator(); i.hasNext(); )
		     { 
				 Connector con = i.next();
				 ModelBlock next = con.getTarget();
				 if (!next.getClass().getName().equalsIgnoreCase("sys4sim.internal_model.Sink"))
					{
			        ArrayList<String> inListString = new ArrayList<String>();
				    ArrayList<Connector> inList = new ArrayList<Connector>();
					inList = ((ModelBlock)element).getIn();
					
					 //hole Vorgänger als next2 in Sring Liste
					 for ( Iterator<Connector> it = inList.iterator(); it.hasNext(); )
				     {   
						 Connector con2 = it.next();
						 ModelBlock next2 = con2.getSource();
						 inListString.add(next2.getName());
				     }
					
				 String function = "if ("+next.getName()+".size() >="+next.getName()+".capacity-1){"+element.getName()+".in.disconnectAndUnmapAll();"+element.getName()+".in.refreshConnections();}";
				 function = ((ModelBlock)element).getOnExit()+function;
				 ((ModelBlock)element).setOnExit(function);
				 //((ModelBlock)element).setOnExit("if ("+next.getName()+".size() >="+next.getName()+".capacity-1){"+element.getName()+".in.disconnectAndUnmapAll();"+element.getName()+".in.refreshConnections();}");
				 
				 function = "if ("+next.getName()+".size() < "+next.getName()+".capacity){";
				 
				 for ( Iterator<String> iter = inListString.iterator(); iter.hasNext(); )
			     { 
					 String str = iter.next();
					 String zus = element.getName()+".in.connect("+str+".out);"+element.getName()+".in.refreshConnections();"+str+".out.refreshConnections();";
					 function = function+zus;
			     }
				 function = function+"}";
				 function = ((ModelBlock)next).getOnExit()+function;
				 ((ModelBlock)next).setOnExit(function);
		     }
		     }
		
		}
    }
    
    private static void CreateXYrekursiv(ModelBlock target,int X,int Y)
    {
    ArrayList<Connector> outList = new ArrayList<Connector>();
	outList = target.getOut();
	 for ( Iterator<Connector> i = outList.iterator(); i.hasNext(); )
     {
 	    
     	Connector s = i.next();
     	if (i.hasNext())
     	{ 
     		X=X+80;Y=Y+60;int Y2=Y-120;int Z=0;
     		if (s.getTarget().getX()==0&&s.getTarget().getY()==0)
    			Z=1;
     		System.out.println("Target");
    		System.out.println(s.getTarget().getName());
    		System.out.println(X);
    		System.out.println(Y);
    		if (Z==1) {
     		s.getTarget().setX(X);
     		s.getTarget().setY(Y);
     		CreateXYrekursiv(s.getTarget(),X,Y);}
     		s = i.next();
     		Z=0;
     		if (s.getTarget().getX()==0&&s.getTarget().getY()==0)
    			Z=1;
     		System.out.println("Target");
    		System.out.println(s.getTarget().getName());
    		System.out.println(X);
    		System.out.println(Y);
    		if (Z==1) {
    		s.getTarget().setX(X);
     		s.getTarget().setY(Y2);
     		CreateXYrekursiv(s.getTarget(),X,Y2);}
     		
     	}
     	else
     	{
     		X=X+80;
     		System.out.println("Target");
    		System.out.println(s.getTarget().getName());
    		System.out.println(X);
    		System.out.println(Y);
     		int Z=0;
     		if (s.getTarget().getX()==0&&s.getTarget().getY()==0)
    			Z=1;
     		if (Z==1) {
     		s.getTarget().setX(X);
     		s.getTarget().setY(Y);
     		CreateXYrekursiv(s.getTarget(),X,Y);}
     		
     	}
     	
     }
    }
    
    private static String CreateDistribution(ModelElement element)
    {
    	String distribution;
    	//System.out.println(((sys4sim.internal_model.Process) element).getProcessingRate().getClass().toString());
		if (((sys4sim.internal_model.Process) element).getProcessingRate().getClass().toString().equalsIgnoreCase("class sys4sim.internal_model.ConstantRate"))
		{
			sys4sim.internal_model.Rate zw=((sys4sim.internal_model.Process) element).getProcessingRate();
			String unit = ((sys4sim.internal_model.ConstantRate) zw).getUnit();
			double value = ((sys4sim.internal_model.ConstantRate) zw).getValue();
			distribution = "<![CDATA["+String.valueOf(value)+"]]>";
		}
		else if (((sys4sim.internal_model.Process) element).getProcessingRate().getClass().toString().equalsIgnoreCase("class sys4sim.internal_model.NormalDistribution"))
		{
			sys4sim.internal_model.Rate zw=((sys4sim.internal_model.Process) element).getProcessingRate();
			String meanUnit =((sys4sim.internal_model.NormalDistribution) zw).getMeanUnit();
			double meanValue =((sys4sim.internal_model.NormalDistribution) zw).getMeanValue();
			String standardDeviationUnit =((sys4sim.internal_model.NormalDistribution) zw).getStandardDeviationUnit();
			double standardDeviationValue =((sys4sim.internal_model.NormalDistribution) zw).getStandardDeviationValue();
			distribution = "<![CDATA[normal("+String.valueOf(standardDeviationValue)+", "+String.valueOf(meanValue)+" )]]>";
		}
		else if (((sys4sim.internal_model.Process) element).getProcessingRate().getClass().toString().equalsIgnoreCase("class sys4sim.internal_model.ExponentialDistribution"))
		{
			sys4sim.internal_model.Rate zw=((sys4sim.internal_model.Process) element).getProcessingRate();
			String meanUnit = ((sys4sim.internal_model.ExponentialDistribution) zw).getMeanUnit();
			double meanValue = ((sys4sim.internal_model.ExponentialDistribution) zw).getMeanValue();
			distribution = "<![CDATA[exponential("+String.valueOf(meanValue)+")]]>";
		}
		else if (((sys4sim.internal_model.Process) element).getProcessingRate().getClass().toString().equalsIgnoreCase("class sys4sim.internal_model.PoissonDistribution"))
		{
			sys4sim.internal_model.Rate zw=((sys4sim.internal_model.Process) element).getProcessingRate();
			String expectedUnit = ((sys4sim.internal_model.PoissonDistribution) zw).getExpectedUnit();
			double expectedValue = ((sys4sim.internal_model.PoissonDistribution) zw).getExpectedValue();
			distribution = "<![CDATA[poisson("+String.valueOf(expectedValue)+")]]>";
		}
		else if (((sys4sim.internal_model.Process) element).getProcessingRate().getClass().toString().equalsIgnoreCase("class sys4sim.internal_model.TriangularDistribution"))
		{
			sys4sim.internal_model.Rate zw=((sys4sim.internal_model.Process) element).getProcessingRate();
			String maxUnit =((sys4sim.internal_model.TriangularDistribution) zw).getMaxUnit();
			double maxValue =((sys4sim.internal_model.TriangularDistribution) zw).getMaxValue();
			String minDeviationUnit =((sys4sim.internal_model.TriangularDistribution) zw).getMinUnit();
			double minDeviationValue =((sys4sim.internal_model.TriangularDistribution) zw).getMinValue();
			String modeDeviationUnit =((sys4sim.internal_model.TriangularDistribution) zw).getModeUnit();
			double modeDeviationValue =((sys4sim.internal_model.TriangularDistribution) zw).getModeValue();
			distribution = "<![CDATA[tringular( "+String.valueOf(minDeviationValue)+", "+String.valueOf(modeDeviationValue)+", "+String.valueOf(maxValue)+" )]]>";
		}
		else {distribution = "<![CDATA[1]]>";}
		return distribution;
    }
    
    private static String CreateDistributionForSource(Rate rate)
    {
    	String distribution;
    	//System.out.println(((sys4sim.internal_model.Process) element).getProcessingRate().getClass().toString());
		if (((sys4sim.internal_model.Rate) rate).getClass().toString().equalsIgnoreCase("class sys4sim.internal_model.ConstantRate"))
		{
			sys4sim.internal_model.Rate zw= rate;
			String unit = ((sys4sim.internal_model.ConstantRate) zw).getUnit();
			double value = ((sys4sim.internal_model.ConstantRate) zw).getValue();
			distribution = "<![CDATA["+String.valueOf(value)+"]]>";
		}
		else if (((sys4sim.internal_model.Rate) rate).getClass().toString().equalsIgnoreCase("class sys4sim.internal_model.NormalDistribution"))
		{
			sys4sim.internal_model.Rate zw=rate;
			String meanUnit =((sys4sim.internal_model.NormalDistribution) zw).getMeanUnit();
			double meanValue =((sys4sim.internal_model.NormalDistribution) zw).getMeanValue();
			String standardDeviationUnit =((sys4sim.internal_model.NormalDistribution) zw).getStandardDeviationUnit();
			double standardDeviationValue =((sys4sim.internal_model.NormalDistribution) zw).getStandardDeviationValue();
			distribution = "<![CDATA[normal("+String.valueOf(standardDeviationValue)+", "+String.valueOf(meanValue)+" )]]>";
		}
		else if (((sys4sim.internal_model.Rate) rate).getClass().toString().equalsIgnoreCase("class sys4sim.internal_model.ExponentialDistribution"))
		{
			sys4sim.internal_model.Rate zw=rate;
			String meanUnit = ((sys4sim.internal_model.ExponentialDistribution) zw).getMeanUnit();
			double meanValue = ((sys4sim.internal_model.ExponentialDistribution) zw).getMeanValue();
			distribution = "<![CDATA[exponential("+String.valueOf(meanValue)+")]]>";
		}
		else if (((sys4sim.internal_model.Rate) rate).getClass().toString().equalsIgnoreCase("class sys4sim.internal_model.PoissonDistribution"))
		{
			sys4sim.internal_model.Rate zw=rate;
			String expectedUnit = ((sys4sim.internal_model.PoissonDistribution) zw).getExpectedUnit();
			double expectedValue = ((sys4sim.internal_model.PoissonDistribution) zw).getExpectedValue();
			distribution = "<![CDATA[poisson("+String.valueOf(expectedValue)+")]]>";
		}
		else if (((sys4sim.internal_model.Rate) rate).getClass().toString().equalsIgnoreCase("class sys4sim.internal_model.TriangularDistribution"))
		{
			sys4sim.internal_model.Rate zw=rate;
			String maxUnit =((sys4sim.internal_model.TriangularDistribution) zw).getMaxUnit();
			double maxValue =((sys4sim.internal_model.TriangularDistribution) zw).getMaxValue();
			String minDeviationUnit =((sys4sim.internal_model.TriangularDistribution) zw).getMinUnit();
			double minDeviationValue =((sys4sim.internal_model.TriangularDistribution) zw).getMinValue();
			String modeDeviationUnit =((sys4sim.internal_model.TriangularDistribution) zw).getModeUnit();
			double modeDeviationValue =((sys4sim.internal_model.TriangularDistribution) zw).getModeValue();
			distribution = "<![CDATA[tringular( "+String.valueOf(minDeviationValue)+", "+String.valueOf(modeDeviationValue)+", "+String.valueOf(maxValue)+" )]]>";
		}
		else {distribution = "<![CDATA[1]]>";}
		return distribution;
    }
    
    private static String readFileContent (File oldFile) {
	    StringBuilder sb = new StringBuilder();
	    try {   
	            FileReader reader = new FileReader(oldFile);
	            int character;
	            //boolean phase=false;
	            while ((character=reader.read()) >-1) 
	            {
	            	// filtern der Sonderzeichen 
	            	if (character == 38)
	            	{   
	            		character=reader.read();
	            		if (character == 108)
	            		{
	            			character=reader.read();
	            			if (character == 116)
	            			{   
	            				character=reader.read();
	            				if (character == 59)
	            				{
	            					character=60;
	            				}
	            				else {   sb.append((char) 38);
	            			             sb.append((char) 108);
	            				         sb.append((char) 116);}
	            			}
	            			else 	{   sb.append((char) 38);
	            			            sb.append((char) 108);}
	            		}
	            		
	            		else if (character == 103)
	            		{   
	            			character=reader.read();
	            			if (character == 116)
	            			{   
	            				character=reader.read();
	            				if (character == 59)
	            				{
	            					character=62;
	            				}
	            				else {sb.append((char) 38);
	            				      sb.append((char) 103);
	            				      sb.append((char) 116);}
	            			}
	            			else 	   {sb.append((char) 38);
	            			            sb.append((char) 103);}
	            		}
	            		
	            		else sb.append((char) 38);
	            	}
	            	
	            	
	                sb.append((char) character);
	            }
	            reader.close();
	        } catch(FileNotFoundException fnfe) {
	            fnfe.printStackTrace();
	        } catch (IOException ioe) {
	            ioe.printStackTrace();
	        }
	        return sb.toString();
	    } 
	
}
