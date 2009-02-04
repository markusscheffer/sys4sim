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
	
		
		// Function to read out the import model, (only informatic)
		/*
		System.out.println("start------------");
		for (ModelElement element : model.getElements().values()) 
		{   
        	
			
			if (element.getClass().getName().equalsIgnoreCase("sys4sim.internal_model.Sink"))
				{
				System.out.println("Sink");
				}
			if (element.getClass().getName().equalsIgnoreCase("sys4sim.internal_model.Source"))
				{
				System.out.println("Source");
				}
			if (element.getClass().getName().equalsIgnoreCase("sys4sim.internal_model.Outputer"))
				{
				System.out.println("Outputer");
				}
			if (element.getClass().getName().equalsIgnoreCase("sys4sim.internal_model.Hold"))
			{
				System.out.println("Hold");
			}
			if (element.getClass().getName().equalsIgnoreCase("sys4sim.internal_model.Process"))
				{
				
				System.out.println("Process");
				System.out.println(element.getName());
				System.out.println("Resourcepools of this process");
				if (!((sys4sim.internal_model.Process)element).getResourcePools().isEmpty())
       			{ 
       			 Hashtable<ResourcePool, Integer> tablePools = new Hashtable<ResourcePool, Integer>();
				 tablePools = ((sys4sim.internal_model.Process)element).getResourcePools();
				 
				 for(ResourcePool t : tablePools.keySet())
				 {
					 
					 System.out.println(t.getClass().getName());
					 System.out.println(t.getId());
					 System.out.println(t.getName());
					 //t.setId(CreateID(8,9));
				 }
       				
       			}
				System.out.println("------------");
				}
			if (element.getClass().getName().equalsIgnoreCase("sys4sim.internal_model.Queue"))
				{
				System.out.println("Queue");
				}
			if (element.getClass().getName().equalsIgnoreCase("sys4sim.internal_model.Worker"))
				System.out.println("Worker");
			if (element.getClass().getName().equalsIgnoreCase("sys4sim.internal_model.Transporter"))
				System.out.println("Transporter");
		}
		System.out.println("Import model---------End");	
		*/
		
		
	//Change model for Protectiomode (PortChoice 4 is now ignored)	
	if (set.getPortChoice()==4)
    ChangeModelforProtectiomode (model,set); 
	if (set.getPortChoice()==3)
    {		
    ChangeModelforProtectiomode2partzero (model,set);
    
    System.out.println("elm in list");
    for (ModelElement element : model.getElements().values()) 
    {
		 if (element.getClass().getName().equalsIgnoreCase("sys4sim.internal_model.Connector"))
			{
			 System.out.println("----");
			 System.out.println(element.getName());
			 System.out.println(element.getId());
			 System.out.println(element.getClass().getName());
			 System.out.println("----");
			 
			}
    }
	 System.out.println("elm in list end");

 
	ChangeModelforProtectiomode2part2 (model,set);
    ChangeModelforProtectiomode2 (model,set);    
    ChangeModelforProtectiomode2part3 (model,set);
    }
	
	
	
	// Create Outputsplits
	CreateOutputs(model);
			
	 // Create root element 
   	 Element root =  new  Element("AnyLogicWorkspace")
   	 .setAttribute("WorkspaceVersion","1.9")
   	 .setAttribute("AnyLogicVersion","6.2.2.200803281613")
   	 .setAttribute("AlpVersion","6.2.2");
   	 Element Model = new Element("Model");
   	 root.addContent(Model);
   	 
       Model
       .addContent(new Element ("Id").setText("1213174774546"))
   	   .addContent(new Element("Name").setText("<![CDATA[MM1]]>"))//Name of the model
   	   .addContent(new Element("ExcludeFromBuild").setText("false"))
   	   .addContent(new Element("EngineVersion").setText("6"))
   	   .addContent(new Element("JavaPackageName").setText("<![CDATA[mm1]]>"));//Name of the JavaPackage
       
       Element ActiveObjectClasses = new Element("ActiveObjectClasses");
       Model.addContent(ActiveObjectClasses);
            Element ActiveObjectClass = new Element("ActiveObjectClass");
            ActiveObjectClasses.addContent(ActiveObjectClass);
              ActiveObjectClass
              .addContent(new Element("Id").setText("1213174774547"))//ID of the main class in anylogic
              .addContent(new Element("Name").setText("<![CDATA[MM1]]>"))//Name of the main class in anylogic
              .addContent(new Element("ExcludeFromBuild").setText("false"))
              .addContent(new Element("ClientAreaTopLeft").addContent("<X>0</X><Y>0</Y>"))
              .addContent(new Element("PresentationTopGroupPersistent").setText("true"))
              .addContent(new Element("IconTopGroupPersistent").setText("true"))
              .addContent(new Element("Generic").setText("false"))
              .addContent(new Element("GenericParameters").setText("<![CDATA[T]]>"));
              
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
                .addContent(new Element("FirstUpdateDate").setText("1213174774859"))
                .addContent(new Element("RecurrenceCode").setText("<![CDATA[1]]>"));
                   
               Element Connectors = new Element("Connectors");
               ActiveObjectClass.addContent(Connectors);
        
               
                  
             // copy the name of resourcepool from ID to Name, (change from importmodel)
             // create array list of the different resource pools (that is needed why resourcepool is not a model element)
               Hashtable<ResourcePool, String> difTablePools = new Hashtable<ResourcePool, String>();
               
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
   					 t.setName(t.getId());
   					 difTablePools.put(t, t.getName());
   				 }
          				
          			}
          			}
          		}
               }
               
           // all resourcepools get a ID that function is not needed 
               
               for(ResourcePool t : difTablePools.keySet())
 				 {
 					t.setId(CreateID(8,9));
 				 }
               
              
               
         //create same IDs for same resourcepools              
               
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
					 for(ResourcePool t2 : difTablePools.keySet())
	 				 {
						 //gucken ob Elemente gleich und dann Ids tauschen
						 if (t.getName()==t2.getName())
						 t.setId(t2.getId());
	 				 }
					 
				 }
       				
       			}
       			}
       		}
            }
            
                 
            
        //Create the anylogic IDs for the other elements
        for (ModelElement element : model.getElements().values()) 
        {
        	element.setId(CreateID(8,9));
        	
        }
        
        
 
 
       //create coordinates for the Elements 
       //for every source one way 
        int X=100; int Y=140;
        int countSources=0;
        for (ModelElement element : model.getElements().values()) 
  		{
          	if (element.getClass().getName().equalsIgnoreCase("sys4sim.internal_model.Source"))
  			{	
          		model.getEntities().get(countSources).getSource().setX(X);
                model.getEntities().get(countSources).getSource().setY(Y);
                CreateXYrekursiv(model.getEntities().get(countSources).getSource(),X,Y);           		
          		countSources+=1;   
          		X=100; 
          		Y=(140-(70*countSources));
  			}
  		}
        
       
        
           
        //create the connectors for the resourcepools        
        if (!set.getDelayMode())
        {
        int countConnectorPool =0;
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
						 countConnectorPool+=1;
						 CreateConnectorForResourcePool(Connectors,element,t,countConnectorPool);
					 }
			}
			}
		}
        }
        
	   //create the other Connectors
        int countConnector =0;boolean countbool = false;
		for (ModelElement element : model.getElements().values()) 
		{
			
			if (element.getClass().getName().equalsIgnoreCase("sys4sim.internal_model.Connector"))
			{
				countConnector = countConnector+1;
				if (((Connector)element).getSource().getClass().getName().equalsIgnoreCase("sys4sim.internal_model.Outputer"))
					{ if (countbool == false)countbool = true;
					  else countbool = false;
					if (countbool == true)
					{CreateConnectorForOutputT(Connectors,(Connector)element,countConnector);
					}
					else
					{CreateConnectorForOutputF(Connectors,(Connector)element,countConnector);
					}
					}
				else CreateConnector(Connectors,(Connector)element,countConnector);	
			}
		}
		
		Element EmbeddedObjects = new Element("EmbeddedObjects");
        ActiveObjectClass.addContent(EmbeddedObjects);
        
       //create the anylogic elemente	
        int countSink = 0;int countSource = 0;int countMachine = 0;int countQueue = 0;
		int countOutputer=0;int countHold=0;
        for (ModelElement element : model.getElements().values()) 
		{   
        	
			
			if (element.getClass().getName().equalsIgnoreCase("sys4sim.internal_model.Sink"))
				{
				countSink=countSink+1;
				CreateSink(EmbeddedObjects,element,countSink);
				}
			if (element.getClass().getName().equalsIgnoreCase("sys4sim.internal_model.Source"))
				{
				countSource=countSource+1;
				CreateSource(EmbeddedObjects,element,countSource);
				
				}
			if (element.getClass().getName().equalsIgnoreCase("sys4sim.internal_model.Outputer"))
				{
				countOutputer=countOutputer+1;
				CreateSelectOutput(EmbeddedObjects,element,countOutputer,set);
				
				}
			if (element.getClass().getName().equalsIgnoreCase("sys4sim.internal_model.Hold"))
			{
		    countHold=countHold+1;
			CreateHold(EmbeddedObjects,element,countHold);
			
			}
			if (element.getClass().getName().equalsIgnoreCase("sys4sim.internal_model.Process"))
				{
				countMachine=countMachine+1;
				//if the process need a resourcepool create service else create delay
				if (((sys4sim.internal_model.Process)element).getResourcePools().isEmpty())
					CreateDelay (EmbeddedObjects,element,countMachine,set);
				else if (!set.getDelayMode())CreateProcess(EmbeddedObjects,element,countMachine,set);
				else CreateDelay (EmbeddedObjects,element,countMachine,set);
				}
			if (element.getClass().getName().equalsIgnoreCase("sys4sim.internal_model.Queue"))
				{
				countQueue=countQueue+1;
				CreateQueue(EmbeddedObjects,element,countQueue,set);
				}
			if (element.getClass().getName().equalsIgnoreCase("sys4sim.internal_model.Worker"))
				CreateWorker(EmbeddedObjects,element);
			if (element.getClass().getName().equalsIgnoreCase("sys4sim.internal_model.Transporter"))
				CreateTransporter(EmbeddedObjects,element);
		}
		
		//create the RessourcePools	
		if (!set.getDelayMode())
			CreateRessourcepoolsNoDelayMode (difTablePools, EmbeddedObjects, model, set);
		
	
	    //create the anylogic experimental		
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
	          .addContent(new Element("Text").setText("<![CDATA[MM1]]>"));//the name of the package needed, now its static mm1!
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
    
	      
	// new doc needs a root
   	 Document doc = new Document(root);     
   	 doc.setRootElement(root);  	
   	  
   	// defining the XML structure
   	 try {
   		 FileOutputStream out =  new  FileOutputStream("test.xml");
   		  XMLOutputter serializer = new XMLOutputter(Format.getPrettyFormat());
   		  serializer.output(doc,out);
   		  out.flush();
   		  out.close();
   	   	}
   	 catch (IOException e) {}
   	 
   	 // change the special symbols from JDOM
   	 String file2 = file.toString();
   	 Sonderzeichen(file2);
	}
	
	
	
	
	
	
	
	
	//method for creating connectors
	private static Element CreateConnector(Element Connectors, Connector element,int countConnector) 
	{ 
		String zname = "connector"+String.valueOf(countConnector);
		String name = "<![CDATA["+zname+"]]>";
		String name2= "<![CDATA["+element.getName()+"]]>";
		String ID = element.getId();
		String sourceID = element.getSource().getId().toString();
		String targetID = element.getTarget().getId().toString();
		
		
		Element Connector = new Element("Connector");
        Connectors.addContent(Connector);
        Connector
        .addContent(new Element("Id").setText(ID))
        .addContent(new Element("Name").setText(name))
        .addContent(new Element("ExcludeFromBuild").setText("false"))
        .addContent(new Element("X").setText("170"))
        .addContent(new Element("Y").setText("150"))                
        .addContent(new Element("Label").setText("<X>10</X><Y>0</Y>"))
        .addContent(new Element("PublicFlag").setText("false"))
        .addContent(new Element("PresentationFlag").setText("true"))
        .addContent(new Element("ShowLabel").setText("false"))
        .addContent(new Element("SourceEmbeddedObject").setText(sourceID))
        .addContent(new Element("SourceConnectableName").setText("out"))
        .addContent(new Element("TargetEmbeddedObject").setText(targetID))
        .addContent(new Element("TargetConnectableName").setText("in"));
        Element Points = new Element("Points");
        Connector.addContent(Points);
          Points
          .addContent(new Element("Point").setText("<X>0</X><Y>0</Y>"))   
          .addContent(new Element("Point").setText("<X>30</X><Y>0</Y>"));   
        return Connectors;		
	}
	
	//method for creating connectors for spliter output T
	private static Element CreateConnectorForOutputT(Element Connectors, Connector element,int countConnector) 
	{ 
		String zname = "connector"+String.valueOf(countConnector);
		String name = "<![CDATA["+zname+"]]>";
		String name2= "<![CDATA["+element.getName()+"]]>";
		String ID = element.getId();
		String sourceID = element.getSource().getId().toString();
		String targetID = element.getTarget().getId().toString();
		
		
		Element Connector = new Element("Connector");
        Connectors.addContent(Connector);
        Connector
        .addContent(new Element("Id").setText(ID))
        .addContent(new Element("Name").setText(name))
        .addContent(new Element("ExcludeFromBuild").setText("false"))
        .addContent(new Element("X").setText("170"))
        .addContent(new Element("Y").setText("150"))                
        .addContent(new Element("Label").setText("<X>10</X><Y>0</Y>"))
        .addContent(new Element("PublicFlag").setText("false"))
        .addContent(new Element("PresentationFlag").setText("true"))
        .addContent(new Element("ShowLabel").setText("false"))
        .addContent(new Element("SourceEmbeddedObject").setText(sourceID))
        .addContent(new Element("SourceConnectableName").setText("outT"))
        .addContent(new Element("TargetEmbeddedObject").setText(targetID))
        .addContent(new Element("TargetConnectableName").setText("in"));
        Element Points = new Element("Points");
        Connector.addContent(Points);
          Points
          .addContent(new Element("Point").setText("<X>0</X><Y>0</Y>"))    
          .addContent(new Element("Point").setText("<X>30</X><Y>0</Y>"));   
        return Connectors;		
	}
	
	//method for creating connectors for spliter output F
	private static Element CreateConnectorForOutputF(Element Connectors, Connector element,int countConnector) 
	{ 
		String zname = "connector"+String.valueOf(countConnector);
		String name = "<![CDATA["+zname+"]]>";
		String name2= "<![CDATA["+element.getName()+"]]>";
		String ID = element.getId();
		String sourceID = element.getSource().getId().toString();
		String targetID = element.getTarget().getId().toString();
		
		
		Element Connector = new Element("Connector");
        Connectors.addContent(Connector);
        Connector
        .addContent(new Element("Id").setText(ID))
        .addContent(new Element("Name").setText(name))
        .addContent(new Element("ExcludeFromBuild").setText("false"))
        .addContent(new Element("X").setText("170"))
        .addContent(new Element("Y").setText("150"))                
        .addContent(new Element("Label").setText("<X>10</X><Y>0</Y>"))
        .addContent(new Element("PublicFlag").setText("false"))
        .addContent(new Element("PresentationFlag").setText("true"))
        .addContent(new Element("ShowLabel").setText("false"))
        .addContent(new Element("SourceEmbeddedObject").setText(sourceID))
        .addContent(new Element("SourceConnectableName").setText("outF"))
        .addContent(new Element("TargetEmbeddedObject").setText(targetID))
        .addContent(new Element("TargetConnectableName").setText("in"));
        Element Points = new Element("Points");
        Connector.addContent(Points);
          Points
          .addContent(new Element("Point").setText("<X>0</X><Y>0</Y>"))    
          .addContent(new Element("Point").setText("<X>30</X><Y>0</Y>"));   
        return Connectors;		
	}
	
	//method for creating connectors for resource pools
	private static Element CreateConnectorForResourcePool(Element Connectors, ModelElement element,ResourcePool pool,int countConnector) 
	{ 
		String zname = "connectorpool"+String.valueOf(countConnector);
		String name = "<![CDATA["+zname+"]]>";
		String ID = CreateID(8,9);
		String sourceID = pool.getId().toString();
		String targetID = (element).getId().toString();
		
		
		Element Connector = new Element("Connector");
        Connectors.addContent(Connector);
        Connector
        .addContent(new Element("Id").setText(ID))
        .addContent(new Element("Name").setText(name))
        .addContent(new Element("ExcludeFromBuild").setText("false"))
        .addContent(new Element("X").setText("170"))
        .addContent(new Element("Y").setText("150"))                
        .addContent(new Element("Label").setText("<X>10</X><Y>0</Y>"))
        .addContent(new Element("PublicFlag").setText("false"))
        .addContent(new Element("PresentationFlag").setText("true"))
        .addContent(new Element("ShowLabel").setText("false"))
        .addContent(new Element("SourceEmbeddedObject").setText(sourceID))
        .addContent(new Element("SourceConnectableName").setText("port"))
        .addContent(new Element("TargetEmbeddedObject").setText(targetID))
        .addContent(new Element("TargetConnectableName").setText("access"));
        Element Points = new Element("Points");
        Connector.addContent(Points);
          Points
          .addContent(new Element("Point").setText("<X>0</X><Y>0</Y>"))    
          .addContent(new Element("Point").setText("<X>30</X><Y>0</Y>"));  
        return Connectors;		
	}
	
	//method for creating element sink
	private static Element CreateSink(Element EmbeddedObjects, ModelElement element,int countElement) 
	{
		String zname = "sink"+String.valueOf(countElement);
		String name2 = "<![CDATA["+zname+"]]>";
		String name= "<![CDATA["+element.getName()+"]]>";
		String ID = element.getId();
		String X = String.valueOf(element.getX()); 
		String Y = String.valueOf(element.getY()); 
		System.out.println("create sink");
		
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
				.addContent(new Element("PackageName").setText("<![CDATA[com.xj.anylogic.libraries.enterprise]]>"))
				.addContent(new Element("ClassName").setText("<![CDATA[Sink]]>"));
			EmbeddedObject
			.addContent(new Element("GenericParametersSubstitute").setText("<![CDATA[Entity]]>"));
			Element Parameters = new Element("Parameters"); 
			EmbeddedObject.addContent(Parameters);
				Element Parameter = new Element("Parameter"); 
				Parameters.addContent(Parameter);
				Parameter
				.addContent(new Element("Name").setText("<![CDATA[onEnter]]>"))
				.addContent(new Element("Value").setText("<![CDATA[]]>"));
			return EmbeddedObjects;
		
	}
	
	//method for creating element hold
	private static Element CreateHold(Element EmbeddedObjects, ModelElement element,int countElement) 
	{
		String zname = "hold"+String.valueOf(countElement);
		String name2 = "<![CDATA["+zname+"]]>";
		String name= "<![CDATA["+element.getName()+"]]>";
		String ID = element.getId();
		String X = String.valueOf(element.getX()); 
		String Y = String.valueOf(element.getY()); 
		System.out.println("create hold");
		String onEnter = "<![CDATA["+((ModelBlock)element).getOnEnter()+"]]>";
		
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
				.addContent(new Element("PackageName").setText("<![CDATA[com.xj.anylogic.libraries.enterprise]]>"))
				.addContent(new Element("ClassName").setText("<![CDATA[Hold]]>"));
			EmbeddedObject
			.addContent(new Element("GenericParametersSubstitute").setText("<![CDATA[Entity]]>"));
			Element Parameters = new Element("Parameters"); 
			EmbeddedObject.addContent(Parameters);
				Element Parameter = new Element("Parameter"); 
				Parameters.addContent(Parameter);
				Parameter
				.addContent(new Element("Name").setText("<![CDATA[onEnter]]>"))
				.addContent(new Element("Value").setText(onEnter));
				Element Parameter2 = new Element("Parameter"); 
				Parameters.addContent(Parameter2);
				Parameter2
				.addContent(new Element("Name").setText("<![CDATA[initiallyBlocked]]>"))
				.addContent(new Element("Value").setText("<![CDATA[]]>"));
			return EmbeddedObjects;
		
	}
	
	//method for creating element source
	private static Element CreateSource(Element EmbeddedObjects, ModelElement element,int countElement)
	{
		//initialize of the name, x-value, y-value, id, capayity of inputbuffer 
		String zname = "source"+String.valueOf(countElement);
		String name2 = "<![CDATA["+zname+"]]>";
		String name= "<![CDATA["+element.getName()+"]]>";
		String ID = ((ModelElement) element).getId();
		String X = String.valueOf(((ModelElement) element).getX()); 
		String Y = String.valueOf(((ModelElement) element).getY()); 
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
		
		System.out.println("create source");
		
    	
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
				.addContent(new Element("Name").setText("<![CDATA[arrivalType]]>"))
				.addContent(new Element("Value").setText("<![CDATA[Source.INTERARRIVAL_TIME]]>"));
				Element Parameter2 = new Element("Parameter"); 
				Parameters.addContent(Parameter2);
				Parameter2
				.addContent(new Element("Name").setText("<![CDATA[rate]]>"))
				.addContent(new Element("Value").setText("<![CDATA[]]>"));
				Element Parameter3 = new Element("Parameter"); 
				Parameters.addContent(Parameter3);
				Parameter3
				.addContent(new Element("Name").setText("<![CDATA[interarrivalTime]]>"))
				.addContent(new Element("Value").setText(distribution));
				Element Parameter4 = new Element("Parameter"); 
				Parameters.addContent(Parameter4);
				Parameter4
				.addContent(new Element("Name").setText("<![CDATA[rateTable]]>"))
				.addContent(new Element("Value").setText("<![CDATA[]]>"));
				Element Parameter5 = new Element("Parameter"); 
				Parameters.addContent(Parameter5);
				Parameter5
				.addContent(new Element("Name").setText("<![CDATA[arrivalTable]]>"))
				.addContent(new Element("Value").setText("<![CDATA[]]>"));
				Element Parameter6 = new Element("Parameter"); 
				Parameters.addContent(Parameter6);
				Parameter6
				.addContent(new Element("Name").setText("<![CDATA[entitiesPerArrival]]>"))
				.addContent(new Element("Value").setText(batchSize));
				Element Parameter7 = new Element("Parameter"); 
				Parameters.addContent(Parameter7);
				Parameter7
				.addContent(new Element("Name").setText("<![CDATA[limitArrivals]]>"))
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
	
	///method for creating element delay (delay is a process how needs no resources)
	private static Element CreateDelay(Element EmbeddedObjects, ModelElement element,int countElement,Settings set)
	{
		String zname = "delay"+String.valueOf(countElement);
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
		System.out.println("create delay");
		
		
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
				.addContent(new Element("PackageName").setText("<![CDATA[com.xj.anylogic.libraries.enterprise]]>"))
				.addContent(new Element("ClassName").setText("<![CDATA[Delay]]>"));
			EmbeddedObject
			.addContent(new Element("GenericParametersSubstitute").setText("<![CDATA[Entity]]>"));
			Element Parameters = new Element("Parameters"); 
			EmbeddedObject.addContent(Parameters);
				Element Parameter = new Element("Parameter"); 
				Parameters.addContent(Parameter);
				Parameter
				.addContent(new Element("Name").setText("<![CDATA[delayTimeDefinedByPath]]>"))
				.addContent(new Element("Value").setText("<![CDATA[]]>"));
				Element Parameter2 = new Element("Parameter"); 
				Parameters.addContent(Parameter2);
				Parameter2
				.addContent(new Element("Name").setText("<![CDATA[delayTime]]>"))
				.addContent(new Element("Value").setText(distribution));
				Element Parameter3 = new Element("Parameter"); 
				Parameters.addContent(Parameter3);
				Parameter3
				.addContent(new Element("Name").setText("<![CDATA[speed]]>"))
				.addContent(new Element("Value").setText("<![CDATA[]]>"));
				Element Parameter4 = new Element("Parameter"); 
				Parameters.addContent(Parameter4);
				Parameter4
				.addContent(new Element("Name").setText("<![CDATA[capacity]]>"))
				.addContent(new Element("Value").setText(capacity));
				Element Parameter5 = new Element("Parameter"); 
				Parameters.addContent(Parameter5);
				Parameter5
				.addContent(new Element("Name").setText("<![CDATA[maximumCapacity]]>"))

				.addContent(new Element("Value").setText("<![CDATA[]]>"));
				Element Parameter6 = new Element("Parameter"); 
				Parameters.addContent(Parameter6);
				Parameter6
				.addContent(new Element("Name").setText("<![CDATA[onEnter]]>"))
				.addContent(new Element("Value").setText("<![CDATA[]]>"));//
				Element Parameter7 = new Element("Parameter"); 
				Parameters.addContent(Parameter7);
				Parameter7
				.addContent(new Element("Name").setText("<![CDATA[onExit]]>"))
				.addContent(new Element("Value").setText(onExit));
				Element Parameter8 = new Element("Parameter"); 
				Parameters.addContent(Parameter8);
				Parameter8
				.addContent(new Element("Name").setText("<![CDATA[animationGuide]]>")) 
				.addContent(new Element("Value").setText("<![CDATA[]]>"));
				Element Parameter9 = new Element("Parameter"); 
				Parameters.addContent(Parameter9);
				Parameter9
				.addContent(new Element("Name").setText("<![CDATA[animationType]]>")) 
				.addContent(new Element("Value").setText("<![CDATA[]]>"));
				Element Parameter10 = new Element("Parameter"); 
				Parameters.addContent(Parameter10);
				Parameter10  
				.addContent(new Element("Name").setText("<![CDATA[animationForward]]>"))
				.addContent(new Element("Value").setText("<![CDATA[]]>"));
				Element Parameter11 = new Element("Parameter"); 
				Parameters.addContent(Parameter11);
				Parameter11
				.addContent(new Element("Name").setText("<![CDATA[enableStats]]>"))
				.addContent(new Element("Value").setText("<![CDATA[]]>"));
		return EmbeddedObjects;
	}
	
	///method for creating element select output/output split
	private static Element CreateSelectOutput(Element EmbeddedObjects, ModelElement element,int countElement,Settings set)
	{
		String zname = "delay"+String.valueOf(countElement);
		String name2 = "<![CDATA["+zname+"]]>";
		String name= "<![CDATA["+element.getName()+"]]>";
		String ID = element.getId();
		String X = String.valueOf(element.getX()); 
		String Y = String.valueOf(element.getY()); 
		
		String probability = CreateProbability (element);
	    probability = "<![CDATA["+probability+"]]>";	    
				
		
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
				.addContent(new Element("PackageName").setText("<![CDATA[com.xj.anylogic.libraries.enterprise]]>"))
				.addContent(new Element("ClassName").setText("<![CDATA[SelectOutput]]>"));
			EmbeddedObject
			.addContent(new Element("GenericParametersSubstitute").setText("<![CDATA[Entity]]>"));
			Element Parameters = new Element("Parameters"); 
			EmbeddedObject.addContent(Parameters);
				Element Parameter = new Element("Parameter"); 
				Parameters.addContent(Parameter);
				Parameter
				.addContent(new Element("Name").setText("<![CDATA[arrivalType]]>")) 
				.addContent(new Element("Value").setText("<![CDATA[]]>"));
				Element Parameter2 = new Element("Parameter"); 
				Parameters.addContent(Parameter2);
				Parameter2
				.addContent(new Element("Name").setText("<![CDATA[rate]]>"))
				.addContent(new Element("Value").setText("<![CDATA[]]>"));
				Element Parameter3 = new Element("Parameter"); 
				Parameters.addContent(Parameter3);
				Parameter3
				.addContent(new Element("Name").setText("<![CDATA[conditionIsProbabilistic]]>"))
				.addContent(new Element("Value").setText("<![CDATA[]]>"));
				Element Parameter4 = new Element("Parameter"); 
				Parameters.addContent(Parameter4);
				Parameter4
				.addContent(new Element("Name").setText("<![CDATA[condition]]>"))
				.addContent(new Element("Value").setText("<![CDATA[]]>"));
				Element Parameter5 = new Element("Parameter"); 
				Parameters.addContent(Parameter5);
				Parameter5
				.addContent(new Element("Name").setText("<![CDATA[probability]]>"))
				.addContent(new Element("Value").setText(probability));
				Element Parameter6 = new Element("Parameter"); 
				Parameters.addContent(Parameter6);
				Parameter6
				.addContent(new Element("Name").setText("<![CDATA[onEnter]]>"))
				.addContent(new Element("Value").setText("<![CDATA[]]>"));//
				Element Parameter7 = new Element("Parameter"); 
				Parameters.addContent(Parameter7);
				Parameter7
				.addContent(new Element("Name").setText("<![CDATA[onExitTrue]]>"))
				.addContent(new Element("Value").setText("<![CDATA[]]>"));
				Element Parameter8 = new Element("Parameter"); 
				Parameters.addContent(Parameter8);
				Parameter8
				.addContent(new Element("Name").setText("<![CDATA[onExitFalse]]>")) 
				.addContent(new Element("Value").setText("<![CDATA[]]>"));
				
		return EmbeddedObjects;
	}
	
	///method for creating element process
	private static Element CreateProcess(Element EmbeddedObjects, ModelElement element,int countElement,Settings set)
	{
		//initialize of the name, x-value, y-value, id, capayity of inputbuffer and distribution
		String zname = "service"+String.valueOf(countElement);
		String name2 = "<![CDATA["+zname+"]]>";
		String name= "<![CDATA["+element.getName()+"]]>";
		String ID = element.getId();
		String X = String.valueOf(element.getX()); 
		String Y = String.valueOf(element.getY()-20); 
		int capacityValue = ((sys4sim.internal_model.Process) element).getCapacity();
		if (capacityValue==0) capacityValue=1;
		String capacity = "<![CDATA["+String.valueOf(capacityValue)+"]]>";
		System.out.println("create process");
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
				.addContent(new Element("Name").setText("<![CDATA[quantity]]>")) 
				.addContent(new Element("Value").setText(needResourcesSrting));
				Element Parameter2 = new Element("Parameter"); 
				Parameters.addContent(Parameter2);
				Parameter2
				.addContent(new Element("Name").setText("<![CDATA[delayTime]]>"))
				.addContent(new Element("Value").setText(distribution));
				Element Parameter3 = new Element("Parameter"); 
				Parameters.addContent(Parameter3);
				Parameter3
				.addContent(new Element("Name").setText("<![CDATA[resourcePool]]>"))
				.addContent(new Element("Value").setText("<![CDATA[]]>"));
				Element Parameter4 = new Element("Parameter"); 
				Parameters.addContent(Parameter4);
				Parameter4
				.addContent(new Element("Name").setText("<![CDATA[onEnter]]>"))
				.addContent(new Element("Value").setText("<![CDATA[]]>"));
				Element Parameter5 = new Element("Parameter"); 
				Parameters.addContent(Parameter5);
				Parameter5
				.addContent(new Element("Name").setText("<![CDATA[onEnterDelay]]>"))
				.addContent(new Element("Value").setText("<![CDATA[]]>"));
				Element Parameter6 = new Element("Parameter"); 
				Parameters.addContent(Parameter6);
				Parameter6
				.addContent(new Element("Name").setText("<![CDATA[onExit]]>"))
				.addContent(new Element("Value").setText(onExit));//
				Element Parameter7 = new Element("Parameter"); 
				Parameters.addContent(Parameter7);
				Parameter7
				.addContent(new Element("Name").setText("<![CDATA[queueCapacity]]>"))
				.addContent(new Element("Value").setText(capacity));
				Element Parameter8 = new Element("Parameter"); 
				Parameters.addContent(Parameter8);
				Parameter8
				.addContent(new Element("Name").setText("<![CDATA[maximumCapacity]]>")) 
				.addContent(new Element("Value").setText("<![CDATA[]]>"));
				Element Parameter9 = new Element("Parameter"); 
				Parameters.addContent(Parameter9);
				Parameter9
				.addContent(new Element("Name").setText("<![CDATA[enableTimeout]]>")) 
				.addContent(new Element("Value").setText("<![CDATA[]]>"));
				Element Parameter10 = new Element("Parameter"); 
				Parameters.addContent(Parameter10);
				Parameter10  
				.addContent(new Element("Name").setText("<![CDATA[timeout]]>"))
				.addContent(new Element("Value").setText("<![CDATA[]]>"));
				Element Parameter11 = new Element("Parameter"); 
				Parameters.addContent(Parameter11);
				Parameter11
				.addContent(new Element("Name").setText("<![CDATA[onExitTimeout]]>"))
				.addContent(new Element("Value").setText("<![CDATA[]]>"));
				Element Parameter12 = new Element("Parameter"); 
				Parameters.addContent(Parameter12);
				Parameter12
				.addContent(new Element("Name").setText("<![CDATA[enablePreemption]]>"))
				.addContent(new Element("Value").setText("<![CDATA[]]>"));
				Element Parameter13 = new Element("Parameter"); 
				Parameters.addContent(Parameter13);
				Parameter13
				.addContent(new Element("Name").setText("<![CDATA[onExitTimeout]]>"))
				.addContent(new Element("Value").setText("<![CDATA[]]>"));
				Element Parameter14 = new Element("Parameter"); 
				Parameters.addContent(Parameter14);
				Parameter14
				.addContent(new Element("Name").setText("<![CDATA[priority]]>"))
				.addContent(new Element("Value").setText("<![CDATA[]]>"));
				Element Parameter15 = new Element("Parameter"); 
				Parameters.addContent(Parameter15);
				Parameter15
				.addContent(new Element("Name").setText("<![CDATA[onExitPreempted]]>"))
				.addContent(new Element("Value").setText("<![CDATA[]]>"));
				Element Parameter16 = new Element("Parameter"); 
				Parameters.addContent(Parameter16);
				Parameter16
				.addContent(new Element("Name").setText("<![CDATA[animationGuideQueue]]>"))
				.addContent(new Element("Value").setText("<![CDATA[]]>"));
				Element Parameter17 = new Element("Parameter"); 
				Parameters.addContent(Parameter17);
				Parameter17
				.addContent(new Element("Name").setText("<![CDATA[animationTypeQueue]]>"))
				.addContent(new Element("Value").setText("<![CDATA[]]>"));
				Element Parameter18 = new Element("Parameter"); 
				Parameters.addContent(Parameter18);
				Parameter18
				.addContent(new Element("Name").setText("<![CDATA[animationForwardQueue]]>"))
				.addContent(new Element("Value").setText("<![CDATA[]]>"));
				Element Parameter19 = new Element("Parameter"); 
				Parameters.addContent(Parameter19);
				Parameter19
				.addContent(new Element("Name").setText("<![CDATA[animationGuideDelay]]>"))
				.addContent(new Element("Value").setText("<![CDATA[]]>"));
				Element Parameter20 = new Element("Parameter"); 
				Parameters.addContent(Parameter20);
				Parameter20
				.addContent(new Element("Name").setText("<![CDATA[animationTypeDelay]]>"))
				.addContent(new Element("Value").setText("<![CDATA[]]>"));
				Element Parameter21 = new Element("Parameter"); 
				Parameters.addContent(Parameter21);
				Parameter21
				.addContent(new Element("Name").setText("<![CDATA[animationForwardDelay]]>"))
				.addContent(new Element("Value").setText("<![CDATA[]]>"));
				Element Parameter22 = new Element("Parameter"); 
				Parameters.addContent(Parameter22);
				Parameter22
				.addContent(new Element("Name").setText("<![CDATA[enableStats]]>"))
				.addContent(new Element("Value").setText("<![CDATA[]]>"));
		return EmbeddedObjects;
	}
	
	///method for creating element resourcepool
	private static Element CreateResourcePool(Element EmbeddedObjects, ModelElement element,int countElement,String zName,int zResourcesInPool,Settings set) 
	{
		String zname = zName+String.valueOf(countElement);
		String name = "<![CDATA["+zname+"]]>";
		String name2= "<![CDATA["+element.getName()+"]]>";
		String ID = element.getId();
		String X = String.valueOf(element.getX()); 
		String Y = String.valueOf(element.getY()); 
		String resourcesInPool = "<![CDATA["+zResourcesInPool+"]]>";
		
		System.out.println("create resourcePool");
		
		Element EmbeddedObject = new Element("EmbeddedObject");
		EmbeddedObjects.addContent(EmbeddedObject);
			EmbeddedObject
			.addContent(new Element("Id").setText(ID))
			.addContent(new Element("Name").setText(name))
			.addContent(new Element("ExcludeFromBuild").setText("false"))
			.addContent(new Element("X").setText(X))
			.addContent(new Element("Y").setText(Y))
			.addContent(new Element("Label").setText("<X>-10</X><Y>30</Y>"))
			.addContent(new Element("PublicFlag").setText("false"))
			.addContent(new Element("PresentationFlag").setText("true"))
			.addContent(new Element("ShowLabel").setText("true"));
			Element ActiveObjectClass2 = new Element("ActiveObjectClass"); 
			EmbeddedObject.addContent(ActiveObjectClass2);
				ActiveObjectClass2
				.addContent(new Element("PackageName").setText("<![CDATA[com.xj.anylogic.libraries.enterprise]]>"))
				.addContent(new Element("ClassName").setText("<![CDATA[ResourcePool]]>"));
			EmbeddedObject
			.addContent(new Element("GenericParametersSubstitute").setText("<![CDATA[ResourceUnit]]>"));
			Element Parameters = new Element("Parameters"); 
			EmbeddedObject.addContent(Parameters);
			Element Parameter = new Element("Parameter"); 
			Parameters.addContent(Parameter);
			Parameter
			.addContent(new Element("Name").setText("<![CDATA[unitsAreObjects]]>")) 
			.addContent(new Element("Value").setText("<![CDATA[]]>"));
			Element Parameter2 = new Element("Parameter"); 
			Parameters.addContent(Parameter2);
			Parameter2
			.addContent(new Element("Name").setText("<![CDATA[capacityDefinedByTable]]>"))
			.addContent(new Element("Value").setText("<![CDATA[]]>"));
			Element Parameter3 = new Element("Parameter"); 
			Parameters.addContent(Parameter3);
			Parameter3
			.addContent(new Element("Name").setText("<![CDATA[capacity]]>"))
			.addContent(new Element("Value").setText(resourcesInPool));
			Element Parameter4 = new Element("Parameter"); 
			Parameters.addContent(Parameter4);
			Parameter4
			.addContent(new Element("Name").setText("<![CDATA[capacityTable]]>"))
			.addContent(new Element("Value").setText("<![CDATA[]]>"));
			Element Parameter5 = new Element("Parameter"); 
			Parameters.addContent(Parameter5);
			Parameter5
			.addContent(new Element("Name").setText("<![CDATA[newUnit]]>"))
			.addContent(new Element("Value").setText("<![CDATA[]]>"));
			Element Parameter6 = new Element("Parameter"); 
			Parameters.addContent(Parameter6);
			Parameter6
			.addContent(new Element("Name").setText("<![CDATA[onNewUnit]]>"))
			.addContent(new Element("Value").setText("<![CDATA[]]>"));//
			Element Parameter7 = new Element("Parameter"); 
			Parameters.addContent(Parameter7);
			Parameter7
			.addContent(new Element("Name").setText("<![CDATA[onSeize]]>"))
			.addContent(new Element("Value").setText("<![CDATA[]]>"));
			Element Parameter8 = new Element("Parameter"); 
			Parameters.addContent(Parameter8);
			Parameter8
			.addContent(new Element("Name").setText("<![CDATA[onRelease]]>")) 
			.addContent(new Element("Value").setText("<![CDATA[]]>"));
			Element Parameter9 = new Element("Parameter"); 
			Parameters.addContent(Parameter9);
			Parameter9
			.addContent(new Element("Name").setText("<![CDATA[enablePriorities]]>")) 
			.addContent(new Element("Value").setText("<![CDATA[]]>"));
			Element Parameter10 = new Element("Parameter"); 
			Parameters.addContent(Parameter10);
			Parameter10  
			.addContent(new Element("Name").setText("<![CDATA[priority]]>"))
			.addContent(new Element("Value").setText("<![CDATA[]]>"));
			Element Parameter11 = new Element("Parameter"); 
			Parameters.addContent(Parameter11);
			Parameter11
			.addContent(new Element("Name").setText("<![CDATA[idleUnitShape]]>"))
			.addContent(new Element("Value").setText("<![CDATA[]]>"));
			Element Parameter12 = new Element("Parameter"); 
			Parameters.addContent(Parameter12);
			Parameter12
			.addContent(new Element("Name").setText("<![CDATA[busyUnitShape]]>"))
			.addContent(new Element("Value").setText("<![CDATA[]]>"));
			Element Parameter13 = new Element("Parameter"); 
			Parameters.addContent(Parameter13);
			Parameter13
			.addContent(new Element("Name").setText("<![CDATA[uniqueShape]]>"))
			.addContent(new Element("Value").setText("<![CDATA[]]>"));
			Element Parameter14 = new Element("Parameter"); 
			Parameters.addContent(Parameter14);
			Parameter14
			.addContent(new Element("Name").setText("<![CDATA[enableRotation]]>"))
			.addContent(new Element("Value").setText("<![CDATA[]]>"));
			Element Parameter15 = new Element("Parameter"); 
			Parameters.addContent(Parameter15);
			Parameter15
			.addContent(new Element("Name").setText("<![CDATA[animationGuide]]>"))
			.addContent(new Element("Value").setText("<![CDATA[]]>"));
			Element Parameter16 = new Element("Parameter"); 
			Parameters.addContent(Parameter16);
			Parameter16
			.addContent(new Element("Name").setText("<![CDATA[animationType]]>"))
			.addContent(new Element("Value").setText("<![CDATA[]]>"));
			Element Parameter17 = new Element("Parameter"); 
			Parameters.addContent(Parameter17);
			Parameter17
			.addContent(new Element("Name").setText("<![CDATA[enableStats]]>"))
			.addContent(new Element("Value").setText("<![CDATA[]]>"));
		return EmbeddedObjects;
	}
	
	///method for creating element queue
	private static Element CreateQueue(Element EmbeddedObjects, ModelElement element, int countElement,Settings set)
	{
		String zname = "queue"+String.valueOf(countElement);
		String name2 = "<![CDATA["+zname+"]]>";
		String name= "<![CDATA["+element.getName()+"]]>";
		String ID = element.getId();
		String X = String.valueOf(element.getX()); 
		String Y = String.valueOf(element.getY()-20); 
		int capacityValue = ((sys4sim.internal_model.Queue) element).getCapacity();
		if (capacityValue==0) capacityValue=100;
		String capacity = "<![CDATA["+String.valueOf(capacityValue)+"]]>";
		if (set.getPortChoice()==2) capacity = "<![CDATA["+String.valueOf(10000)+"]]>";
		System.out.println("create queue");
		String onExit = "<![CDATA["+((ModelBlock)element).getOnExit()+"]]>";
		
		
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
				.addContent(new Element("ClassName").setText("<![CDATA[Queue]]>"));
			EmbeddedObject
			.addContent(new Element("GenericParametersSubstitute").setText("<![CDATA[Entity]]>"));//
			Element Parameters = new Element("Parameters"); 
			EmbeddedObject.addContent(Parameters);
				Element Parameter = new Element("Parameter"); 
				Parameters.addContent(Parameter);
				Parameter
				.addContent(new Element("Name").setText("<![CDATA[capacity]]>"))
				.addContent(new Element("Value").setText(capacity));
				Element Parameter2 = new Element("Parameter"); 
				Parameters.addContent(Parameter2);
				Parameter2
				.addContent(new Element("Name").setText("<![CDATA[maximumCapacity]]>"))
				.addContent(new Element("Value").setText("<![CDATA[]]>"));
				Element Parameter3 = new Element("Parameter"); 
				Parameters.addContent(Parameter3);
				Parameter3
				.addContent(new Element("Name").setText("<![CDATA[onEnter]]>"))
				.addContent(new Element("Value").setText("<![CDATA[]]>"));
				Element Parameter4 = new Element("Parameter"); 
				Parameters.addContent(Parameter4);
				Parameter4
				.addContent(new Element("Name").setText("<![CDATA[onAtExit]]>"))
				.addContent(new Element("Value").setText("<![CDATA[]]>"));
				Element Parameter5 = new Element("Parameter"); 
				Parameters.addContent(Parameter5);
				Parameter5
				.addContent(new Element("Name").setText("<![CDATA[onExit]]>"))
				.addContent(new Element("Value").setText(onExit));
				Element Parameter6 = new Element("Parameter"); 
				Parameters.addContent(Parameter6);
				Parameter6
				.addContent(new Element("Name").setText("<![CDATA[enableTimeout]]>"))
				.addContent(new Element("Value").setText("<![CDATA[]]>"));//
				Element Parameter7 = new Element("Parameter"); 
				Parameters.addContent(Parameter7);
				Parameter7
				.addContent(new Element("Name").setText("<![CDATA[timeout]]>"))
				.addContent(new Element("Value").setText("<![CDATA[]]>"));
				Element Parameter8 = new Element("Parameter"); 
				Parameters.addContent(Parameter8);
				Parameter8
				.addContent(new Element("Name").setText("<![CDATA[onExitTimeout]]>")) 
				.addContent(new Element("Value").setText("<![CDATA[]]>"));
				Element Parameter9 = new Element("Parameter"); 
				Parameters.addContent(Parameter9);
				Parameter9
				.addContent(new Element("Name").setText("<![CDATA[enablePreemption]]>")) 
				.addContent(new Element("Value").setText("<![CDATA[]]>"));
				Element Parameter10 = new Element("Parameter"); 
				Parameters.addContent(Parameter10);
				Parameter10
				.addContent(new Element("Name").setText("<![CDATA[priority]]>"))
				.addContent(new Element("Value").setText("<![CDATA[]]>"));
				Element Parameter11 = new Element("Parameter"); 
				Parameters.addContent(Parameter11);
				Parameter11
				.addContent(new Element("Name").setText("<![CDATA[onExitPreempted]]>"))
				.addContent(new Element("Value").setText("<![CDATA[]]>"));
				Element Parameter12 = new Element("Parameter"); 
				Parameters.addContent(Parameter12);
				Parameter12
				.addContent(new Element("Name").setText("<![CDATA[animationGuide]]>"))
				.addContent(new Element("Value").setText("<![CDATA[]]>"));
				Element Parameter13 = new Element("Parameter"); 
				Parameters.addContent(Parameter13);
				Parameter13
				.addContent(new Element("Name").setText("<![CDATA[animationType]]>"))
				.addContent(new Element("Value").setText("<![CDATA[]]>"));
				Element Parameter14 = new Element("Parameter"); 
				Parameters.addContent(Parameter14);
				Parameter14
				.addContent(new Element("Name").setText("<![CDATA[animationForward]]>"))
				.addContent(new Element("Value").setText("<![CDATA[]]>"));
				Element Parameter15 = new Element("Parameter"); 
				Parameters.addContent(Parameter15);
				Parameter15
				.addContent(new Element("Name").setText("<![CDATA[enableStats]]>"))
				.addContent(new Element("Value").setText("<![CDATA[]]>"));
		return EmbeddedObjects;
	}
	
	///method for creating element worker
	private static Element CreateWorker(Element EmbeddedObjects, ModelElement element)
	{
		System.out.println("create worker");
		return(EmbeddedObjects);
	}
	
	///method for creating element transporter
	private static Element CreateTransporter(Element EmbeddedObjects, ModelElement element)
	{
		System.out.println("create transporter");
		return(EmbeddedObjects);
	}
	
	///method for creating the Ids of the elements 
	private static String CreateID(int anzahl, int bereich) {

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
	
	
	///method for change the special symbols for JDOM
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
    
	///method for creating the on exit/onenter code for protection mode II (Code mode)
    private static void CreateFunction(ModelElement element)
    {
    	
    	if (element.getClass().getName().equalsIgnoreCase("sys4sim.internal_model.Process"))
		{
		     ArrayList<Connector> outList = new ArrayList<Connector>();
			 outList = ((ModelBlock)element).getOut();
			 
			 for ( Iterator<Connector> i = outList.iterator(); i.hasNext(); )
		     { 
				 Connector con = i.next();
				 ModelBlock next = con.getTarget();
				 if (!next.getClass().getName().equalsIgnoreCase("sys4sim.internal_model.Sink"))
					{
			        ArrayList<String> inListString = new ArrayList<String>();
				    ArrayList<Connector> inList = new ArrayList<Connector>();
					inList = ((ModelBlock)element).getIn();
					
					 
					 for ( Iterator<Connector> it = inList.iterator(); it.hasNext(); )
				     {   
						 Connector con2 = it.next();
						 ModelBlock next2 = con2.getSource();
						 inListString.add(next2.getName());
				     }
					
				 String function = "if ("+next.getName()+".size() >="+next.getName()+".capacity-1){"+element.getName()+".in.disconnectAndUnmapAll();"+element.getName()+".in.refreshConnections();}";
				 function = ((ModelBlock)element).getOnExit()+function;
				 ((ModelBlock)element).setOnExit(function);
				 
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
    
	///method for creating the cordinates
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
     		
    		if (Z==1) {
     		s.getTarget().setX(X);
     		s.getTarget().setY(Y);
     		
     		CreateXYrekursiv(s.getTarget(),X,Y);}
     		s = i.next();
     		Z=0;
     		if (s.getTarget().getX()==0&&s.getTarget().getY()==0)
    			Z=1;
     		
    		if (Z==1) {
    		s.getTarget().setX(X);
     		s.getTarget().setY(Y2);
     		
     		CreateXYrekursiv(s.getTarget(),X,Y2);}
     		
     	}
     	else
     	{
     		X=X+80;
     		
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
    
	///method for creating the distribution
    private static String CreateDistribution(ModelElement element)
    {
    	String distribution;
    	
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
			distribution = "<![CDATA[triangular( "+String.valueOf(minDeviationValue)+", "+String.valueOf(modeDeviationValue)+", "+String.valueOf(maxValue)+" )]]>";
		}
		else {distribution = "<![CDATA[1]]>";}
		return distribution;
    }
    
	///method for creating the probabilitys
    private static String CreateProbability (ModelElement element)
    {
    	String probability1=""; 
		String probability2="";
		ArrayList<Connector> outList = new ArrayList<Connector>();
	    outList = ((ModelBlock)element).getOut();
	    int zz=0;
	    for ( Iterator<Connector> i = outList.iterator(); i.hasNext(); )
    	{
	    	zz =zz+1;
    		Connector s = i.next();	
    		if (zz==1)
    		probability1 = s.getConditionString();
    		else probability2 = s.getConditionString();
    	}
	    
	    char test = probability2.charAt(0); double prob1; double prob2;
	    if (test == 101 )
	    {	   
	      char first = probability1.charAt(0);
	      if ( first == 101) System.out.println("Error: double else by probability");
	      else if ( probability1.length()==2) 
	    	{
	    	char chars[] = new char[1];
	    	probability1.getChars(0, 0 + 1, chars, 0 );
	    	probability1=String.valueOf(chars);
	    	
	    	}	    
	      else if ( probability1.length()==3) 
	      {
    	  char chars[] = new char[2];
    	  probability1.getChars(0, 0 + 2, chars, 0 );
    	  probability1=String.valueOf(chars);
    	  
    	  }	  
	      else if ( probability1.length()==4) 
	      {
    	  char chars[] = new char[3];
    	  probability1.getChars(0, 0 + 3, chars, 0 );
    	  probability1=String.valueOf(chars);
    	  
    	  }	  
	      else if ( probability1.length()>4) 
	      System.out.println("Error: no probability by output connector");
	      
	      //prob1 = Integer.parseInt(probability1);
	      prob1 = Double.valueOf(probability1);
	      prob2 = 100-prob1;
	      prob2 = prob2/100;
	      prob1 = prob1/100;
	      probability1=String.valueOf(prob1);
	      probability2=String.valueOf(prob2);
	     
	    }
	    else
	    {
	    	  char first = probability2.charAt(0);
		      if ( first == 101) System.out.println("Error: double else by probability");
		      else if ( probability2.length()==2) 
		    	{
		    	char chars[] = new char[1];
		    	probability2.getChars(0, 0 + 1, chars, 0 );
		    	probability2=String.valueOf(chars);
		    	
		    	}	    
		      else if ( probability2.length()==3) 
		      {
	    	  char chars[] = new char[2];
	    	  probability2.getChars(0, 0 + 2, chars, 0 );
	    	  probability2=String.valueOf(chars);
	    	  
	    	  }	  
		      else if ( probability2.length()==4) 
		      {
	    	  char chars[] = new char[3];
	    	  probability2.getChars(0, 0 + 3, chars, 0 );
	    	  probability2=String.valueOf(chars);
	    	  
	    	  }	  
		      else if ( probability2.length()>4) 
		      System.out.println("Error: no probability by output connector");
		      
		      //prob1 = Integer.parseInt(probability1);
		      prob2 = Double.valueOf(probability2);
		      prob1 = 100-prob2;
		      prob1 = prob1/100;
		      prob2 = prob2/100;
		      probability1=String.valueOf(prob1);
		      probability2=String.valueOf(prob2);
		     
	    }
	    return probability2;
    }
    
	///method for creating the ditribution for element source
    private static String CreateDistributionForSource(Rate rate)
    {
    	String distribution;
    	
		if (((sys4sim.internal_model.Rate) rate).getClass().toString().equalsIgnoreCase("class sys4sim.internal_model.ConstantRate"))
		{
			sys4sim.internal_model.Rate zw= rate;
			String unit = ((sys4sim.internal_model.ConstantRate) zw).getUnit();
			double value = ((sys4sim.internal_model.ConstantRate) zw).getValue();
			
			char first = unit.charAt(0);
			if (first == 115)
			value=value/60;			
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
			distribution = "<![CDATA[triangular( "+String.valueOf(minDeviationValue)+", "+String.valueOf(modeDeviationValue)+", "+String.valueOf(maxValue)+" )]]>";
		}
		else {distribution = "<![CDATA[1]]>";}
		return distribution;
    }
    
	///method for creating the output (not the element, now we create the logic for creating elements)
    private static void CreateOutputs(Model model)
    {
    	
    	int countOutputer=0;
    	Hashtable<String, ModelElement> tempTable = new Hashtable<String, ModelElement>();
    	for (ModelElement element : model.getElements().values()) 
		{
    		if (!element.getClass().getName().equalsIgnoreCase("sys4sim.internal_model.Connector"))
    		{
    			ArrayList<Connector> outList = new ArrayList<Connector>();
			    outList = ((ModelBlock)element).getOut();
			    if (outList.size()==2)
			    {
			    	countOutputer=countOutputer+1; 
			    	sys4sim.internal_model.Outputer outputer = new sys4sim.internal_model.Outputer();
			    	outputer.setName("outputer"+String.valueOf(countOutputer));
			    	outputer.setId(CreateID(8,9));			    	
					tempTable.put("outputer"+String.valueOf(countOutputer), outputer);
			    	
					Hashtable<String, ModelElement> tempTable2 = new Hashtable<String, ModelElement>();
			    	int zz=0;
					for ( Iterator<Connector> i = outList.iterator(); i.hasNext(); )
			    	{
			    		zz=zz+1;
			    		Connector s = i.next();		
			   
			    		tempTable2.put("connector"+String.valueOf(zz), s);
			    		
						outputer.getOut().add(s);
						s.setSource(outputer);
			 	        s.setSourceName(outputer.getName());
			 	       
			 	       		    		
			    	}
			    	
			    	for(String s : tempTable2.keySet()){
			       		ModelElement m = tempTable2.get(s);
			       		if (((ModelBlock)element).getOut().remove(m)); }
			    	
			    	sys4sim.internal_model.Connector con = new sys4sim.internal_model.Connector();
			 	    con.setSource((ModelBlock)element);
			 	    con.setSourceName(element.getName());
			 	    con.setTarget((ModelBlock)outputer);
			 	    con.setTargetName(outputer.getName());
			 	    outputer.getIn().add(con);
			 	    ((ModelBlock)element).getOut().add(con);
			 	   tempTable.put("connectorout"+String.valueOf(countOutputer), con);
    		    }
    		}
    	}
    	for(String s : tempTable.keySet()){
   		ModelElement m = tempTable.get(s);
    	model.getElements().put(s,m);} 
    	
    	    	
    }
    
	//method for creating  resourcepool (not the element but the logical function to create it)
    private static Element CreateRessourcepoolsNoDelayMode (Hashtable difTablePools,Element EmbeddedObjects, Model model, Settings set)
    {
    		
    	Hashtable<ResourcePool, String> difTablePools2 = new Hashtable<ResourcePool, String>();
		difTablePools2=difTablePools; 
		
		 int X=40;int Y=200;
		 int countMachinePool =0;int countWorkerPool =0;int countTransporterPool =0;
		 for(ResourcePool t : difTablePools2.keySet())
		 {
			 
			 
			 if (t.getClass().getName().equalsIgnoreCase("sys4sim.internal_model.MachinePool"))
				{
				countMachinePool = countMachinePool+1;
				String name = "MachinePool";
				int resourcesInPool=(((sys4sim.internal_model.MachinePool)t).getElements().size());
				int needResources=1;
				
				t.setX(X);
				t.setY(Y);
				X=X+80;
				
				CreateResourcePool(EmbeddedObjects,(sys4sim.internal_model.ModelElement)t,countMachinePool,name,resourcesInPool,set);
			    }
			else if (t.getClass().getName().equalsIgnoreCase("sys4sim.internal_model.TransporterPool"))
			{
				countTransporterPool = countTransporterPool+1;
				String name = "TransporterPool";
				int resourcesInPool=(((sys4sim.internal_model.TransporterPool)t).getElements().size());
				int needResources=1;
				
				t.setX(X);
				t.setY(Y);
				X=X+80;
								
				CreateResourcePool(EmbeddedObjects,(sys4sim.internal_model.ModelElement)t,countTransporterPool,name,resourcesInPool,set);
		    }
			else if (t.getClass().getName().equalsIgnoreCase("sys4sim.internal_model.WorkerPool"))
			{
				countWorkerPool = countWorkerPool+1;
				String name = "WorkerPool";
				int resourcesInPool=(((sys4sim.internal_model.WorkerPool)t).getElements().size());
				int needResources=1;
				
				t.setX(X);
				t.setY(Y);
				X=X+80;
				
				CreateResourcePool(EmbeddedObjects,(sys4sim.internal_model.ModelElement)t,countWorkerPool,name,resourcesInPool,set);
			}
			else System.out.println("Error: no valid RessourcePool");
		 }
		
		
		
		return EmbeddedObjects;
    }
    
	
    ///method for change the anylogic port protection mode I
    private static void ChangeModelforProtectiomode (Model model,Settings set)
    {
		 int zsourcebuffer =0;	
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
						    //create Buffer
						    zsourcebuffer+=1;
						    sys4sim.internal_model.Queue queue = new sys4sim.internal_model.Queue();
							queue.setCapacity(10000);
							queue.setName("sourcebuffer"+String.valueOf(zsourcebuffer));
							tempTable.put("sourcebuffer"+String.valueOf(zsourcebuffer), queue);
				 	        // change Connector 
							Connector s = i.next();
							ModelBlock z = s.getTarget();
							z.getIn().remove(s);
				 	        s.setTarget(queue);
				 	        s.setTargetName(queue.getName());
				 	        //create new ocnnector between buffer and elziel 
				 	       sys4sim.internal_model.Connector con = new sys4sim.internal_model.Connector();
				 	       con.setSource(queue);
				 	       con.setSourceName(queue.getName());
				 	       con.setTarget(z);
				 	       con.setTargetName(z.getName());
				 	       z.getIn().add(con);
				 	       tempTable.put("Con"+String.valueOf(zsourcebuffer), con);
				 	       //write new connector in buffer get.out 
				 	       queue.getOut().add(con);
				        }					
				}			    
	        }
	 	 
		 for(String s : tempTable.keySet()){
		 ModelElement m = tempTable.get(s);
		 tableElem.put(s, m);}
		 model.setElements(tableElem);	
	     //create On Exit Code		 
		 for (ModelElement element : model.getElements().values()) 
	        {
               CreateFunction(element);
	        
	        }	
    }
    
    ///method for change the anylogic port protection mode II (element mode)
    private static void ChangeModelforProtectiomode2part3 (Model model,Settings set)
    {
    	
    	for (ModelElement element : model.getElements().values()) 
        {
    	if (element.getClass().getName().equalsIgnoreCase("sys4sim.internal_model.Process"))
		{
		     ArrayList<Connector> outList = new ArrayList<Connector>();
			 outList = ((ModelBlock)element).getOut();
			
			 ArrayList<Connector> inList = new ArrayList<Connector>();
			 inList = ((ModelBlock)element).getIn();
			 
			 ModelBlock block1=((ModelBlock)element);
			 for ( Iterator<Connector> it = inList.iterator(); it.hasNext(); )
		     {  
				 Connector con =it.next();
				 block1 = con.getSource();
		     }
			 
			 ModelBlock block2=((ModelBlock)element);
			 for ( Iterator<Connector> i = outList.iterator(); i.hasNext(); )
		     { 
				 
				 Connector con = i.next();
				 block2 = con.getTarget();
				 
		     }
			 
				 
			 String function1 = block1.getName()+".setBlocked(true)";
			 String function2 = block1.getName()+".setBlocked(false)";
			 block1.setOnEnter(function1);
			 block2.setOnExit(function2);
			 
			
        }
    }
    }
    
  ///method for change the anylogic port protection mode II
    private static void ChangeModelforProtectiomode2 (Model model,Settings set)
    {
    	
    	   	   
	     int count =1;	int countqueue=0;
	     
		 Hashtable <String, ModelElement> tableElem = new Hashtable<String, ModelElement>();
	     tableElem = model.getElements();
	     
	     	     	    		 
		 Hashtable<String, ModelElement> tempTable = new Hashtable<String, ModelElement>();
		 
		 Hashtable<String, ModelElement> tempTableConnector = new Hashtable<String, ModelElement>();
		 
		 Hashtable<String, ModelElement> tempTableConnectorEntferntElement = new Hashtable<String, ModelElement>();
		 
		 Hashtable<String, ModelElement> tempTableElem = new Hashtable<String, ModelElement>();
		 
		 for (ModelElement element : model.getElements().values()) 
	        {
			 if (element.getClass().getName().equalsIgnoreCase("sys4sim.internal_model.Process"))
				{
				    countqueue+=1;
				    tempTableElem.put(element.getId(),element);
				    ArrayList<Connector> outList = new ArrayList<Connector>();
					outList = ((ModelBlock)element).getOut();
										
					//create Queue
				    count+=1;
				    sys4sim.internal_model.Queue queue = new sys4sim.internal_model.Queue();
					queue.setCapacity(1);
					queue.setName("protectqueue"+String.valueOf(countqueue));
					
					 
					
					//change connector of follow element, 
					// set first queue, the end is the same like before, also save the connectors to delete them from the  process
					for ( Iterator<Connector> i = outList.iterator(); i.hasNext(); )
				     {		count+=1;
						    Connector connector = i.next();
						    connector.setSource(queue);
							connector.setSourceName(queue.getName());
							queue.getOut().add(connector);
							tempTableConnector.put("ConZ"+String.valueOf(count),connector);						    
						    
				     }
					tempTable.put("protectqueuez"+String.valueOf(count), queue);
					
					// save element in the delete list
					ModelElement zwischenelement = element;	
					for(String s : tempTableConnector.keySet())
					{					
					ModelElement m = tempTableConnector.get(s);
					((ModelBlock)zwischenelement).getOut().remove(m);					
					}					
					
					 ArrayList<Connector> outList2 = new ArrayList<Connector>();
					 outList2 = ((ModelBlock)zwischenelement).getOut();
					 int zz=0;
					 for ( Iterator<Connector> i = outList2.iterator(); i.hasNext(); )
				     {
						 zz+=1;						 
						 Connector con = i.next();
				     }
					 
					tempTableConnectorEntferntElement.put(zwischenelement.getId(),zwischenelement);
					
					
					//create connectors from machine to queue
					sys4sim.internal_model.Connector con = new sys4sim.internal_model.Connector();
					con.setSource((ModelBlock)element);
			 	    con.setSourceName(element.getName());
			 	    con.setTarget(queue);
			 	    con.setTargetName(queue.getName());
			 	    queue.getIn().add(con);
			 	    if (((ModelBlock)element).getOut().add(con)) ;
			 	    else ;
			 	    tempTable.put("ConZZ"+String.valueOf(count), con);			
				}							    
	        }
		 
			 
		 for(String s : tempTable.keySet()){
		 ModelElement m = tempTable.get(s);
		 tableElem.put(s, m);}
		// System.out.println("");
	     for(String s : tableElem.keySet()){
			 ModelElement m = tableElem.get(s);
			// System.out.println(m.getClass().getName());
			 }
	    // System.out.println("");
		 model.setElements(tableElem);
		 
				 
		 for(String s : tempTableElem.keySet()){
	     ModelElement m = tempTableElem.get(s);
		 model.getElements().remove(s);}		 
		 
		 for(String s : tempTableConnectorEntferntElement.keySet()){
		 ModelElement m = tempTableConnectorEntferntElement.get(s);
		 model.getElements().put(s,m);} 
		 			         	 
    }
    
    
    private static void ChangeModelforProtectiomode2part2 (Model model,Settings set)
    {
    	
    	
	     int count =0;	int counthold =0;
	     
		 Hashtable <String, ModelElement> tableElem2 = new Hashtable<String, ModelElement>();
	     tableElem2 = model.getElements();
	     	    		 
		 Hashtable<String, ModelElement> tempTable2 = new Hashtable<String, ModelElement>();
		 
		 Hashtable<String, ModelElement> tempTableConnector2 = new Hashtable<String, ModelElement>();
		 
		 Hashtable<String, ModelElement> tempTableConnectorEntferntElement2 = new Hashtable<String, ModelElement>();
		 
		 Hashtable<String, ModelElement> tempTableElem2 = new Hashtable<String, ModelElement>();
		 
		 for (ModelElement element : model.getElements().values()) 
	        {
			 if (element.getClass().getName().equalsIgnoreCase("sys4sim.internal_model.Process"))
				{
				 
				    tempTableElem2.put(element.getId(),element);
				    ArrayList<Connector> inList = new ArrayList<Connector>();
					inList = ((ModelBlock)element).getIn();
										
					//create Queue
				    count+=1;
				    counthold+=1;
				    sys4sim.internal_model.Hold hold = new sys4sim.internal_model.Hold();
					hold.setName("protecthold"+String.valueOf(counthold));
					
					//change connectors from successor element, 
					//set as first the queue, the end remains in,also save connectors to delete from process
					for ( Iterator<Connector> i = inList.iterator(); i.hasNext(); )
				     {		count+=1;
						    Connector connector = i.next();
						    connector.setTarget(hold);
							connector.setTargetName(hold.getName());
							hold.getIn().add(connector);
							tempTableConnector2.put("Con"+String.valueOf(count),connector);						    
				     }
					tempTable2.put("protectqueue"+String.valueOf(count), hold);
					
					//save elements in the list to delete them later
					
					ModelElement zwischenelement = element;	
					for(String s : tempTableConnector2.keySet())
					{					
					ModelElement m = tempTableConnector2.get(s);
					//System.out.println(((Connector)m).getSource().getName());
					((ModelBlock)zwischenelement).getIn().remove(m);
					}
										
					
					tempTableConnectorEntferntElement2.put(zwischenelement.getId(),zwischenelement);
					
					//create ocnnectors from hold to maschine
					sys4sim.internal_model.Connector con = new sys4sim.internal_model.Connector();
					con.setTarget((ModelBlock)element);
			 	    con.setTargetName(element.getName());
			 	    con.setSource(hold);
			 	    con.setSourceName(hold.getName());
			 	    hold.getOut().add(con);
			 	    if (((ModelBlock)element).getIn().add(con)) ;
			 	    else ;
			 	    tempTable2.put("Con"+String.valueOf(count), con);			
				}							    
	        }
	 
	 
		 for(String s : tempTable2.keySet()){
		 ModelElement m = tempTable2.get(s);
		 tableElem2.put(s, m);}
		 model.setElements(tableElem2);
		 
		 
		 for(String s : tempTableElem2.keySet()){
	     ModelElement m = tempTableElem2.get(s);
		 model.getElements().remove(s);}		 
		 
		 for(String s : tempTableConnectorEntferntElement2.keySet()){
		 ModelElement m = tempTableConnectorEntferntElement2.get(s);
		 model.getElements().put(s,m);} 
		 
		 
		
    }
    
    
    private static void ChangeModelforProtectiomode2partzero (Model model,Settings set)
    {
    	 int zsourcebuffer =0;	
    	 
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
						    //create Buffer
						    zsourcebuffer+=1;
						    sys4sim.internal_model.Queue queue = new sys4sim.internal_model.Queue();
							queue.setCapacity(10000);
							queue.setName("sourcebuffer"+String.valueOf(zsourcebuffer));
							tempTable.put("sourcebuffer"+String.valueOf(zsourcebuffer), queue);
				 	        // change Connector 
							Connector s = i.next();
							ModelBlock z = s.getTarget();
							z.getIn().remove(s);
				 	        s.setTarget(queue);
				 	        s.setTargetName(queue.getName());
				 	        //create new ocnnector between buffer and elziel 
				 	       sys4sim.internal_model.Connector con = new sys4sim.internal_model.Connector();
				 	       con.setSource(queue);
				 	       con.setSourceName(queue.getName());
				 	       con.setTarget(z);
				 	       con.setTargetName(z.getName());
				 	       con.setName("Constartq"+String.valueOf(zsourcebuffer));
				 	       con.setId("Constartq"+String.valueOf(zsourcebuffer));
				 	       z.getIn().add(con);
				 	       tempTable.put("Constartq"+String.valueOf(zsourcebuffer), con);
				 	       //write new connector in buffer get.out 
				 	       queue.getOut().add(con);
				        }					
				}			    
	        }
		 
	 	 
		 for(String s : tempTable.keySet()){
		 ModelElement m = tempTable.get(s);
		 tableElem.put(s, m);}
		 model.setElements(tableElem);		
		 
    }
    
    
    private static String readFileContent (File oldFile) {
	    StringBuilder sb = new StringBuilder();
	    try {   
	            FileReader reader = new FileReader(oldFile);
	            int character;
	          
	            while ((character=reader.read()) >-1) 
	            {
	            	// filtering the spezial symbols  
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
