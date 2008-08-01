package sys4sim.export.anylogic;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import sys4sim.export.ExportInterface;
import sys4sim.internal_model.Connector;
import sys4sim.internal_model.Model;
import sys4sim.internal_model.ModelElement;

public class Exporter implements ExportInterface {

	public void writeFile(Model model, File file) {
	
	 // Wurzelelement erzeugen
   	 Element root =  new  Element("AnyLogicWorkspace")
   	 .setAttribute("WorkspaceVersion","1.9")
   	 .setAttribute("AnyLogicVersion","6.2.2.200803281613")
   	 .setAttribute("AlpVersion","6.2.2");
   	 Element Model = new Element("Model");
   	 root.addContent(Model);
   	 
   	 // definiere allgemeine Modelleigenschafften 
       Model
       .addContent(new Element ("ID").setText("1213174774546"))
   	   .addContent(new Element("Name").setText("![CDATA[Main]]"))
   	   .addContent(new Element("ExcludeFromBuild").setText("false"))
   	   .addContent(new Element("EngineVersion").setText("6"))
   	   .addContent(new Element("JavaPackageName").setText("![CDATA[mm1]]"));
       char cTest = (char) 13;
      
       Element ActiveObjectClasses = new Element("ActiveObjectClasses");
       Model.addContent(ActiveObjectClasses);
            Element ActiveObjectClass = new Element("ActiveObjectClass");
            ActiveObjectClasses.addContent(ActiveObjectClass);
              ActiveObjectClass
              .addContent(new Element("ID").setText("1213174774547"))//bea
              .addContent(new Element("Name").setText("![CDATA[MM1]]"))//bea
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
                
        //Erzeugen der anylogic IDs
        for (ModelElement element : model.getElements().values()) 
        {
        	element.setId(IDErzeugen(8, 9));
        }
                
		//FOR für die Erzeugung der Connectoren	
        int zaehlerConnector =0;
		for (ModelElement element : model.getElements().values()) 
		{
			System.out.println(element.getClass().getName());
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
			System.out.println(element.getClass().toString());
			if (element.getClass().getName().equalsIgnoreCase("sys4sim.internal_model.Sink"))
				{
				zaehlerSink=zaehlerSink+1;
				CreateSink(Connectors,element,zaehlerSink);
				}
			if (element.getClass().getName().equalsIgnoreCase("sys4sim.internal_model.Source"))
				{
				zaehlerSource=zaehlerSource+1;
				CreateSource(Connectors,element,zaehlerSource);
				}
			if (element.getClass().getName().equalsIgnoreCase("sys4sim.internal_model.Machine"))
				{
				zaehlerMachine=zaehlerMachine+1;
				CreateMachine(Connectors,element,zaehlerMachine);
				}
			if (element.getClass().getName().equalsIgnoreCase("sys4sim.internal_model.Queue"))
				{
				zaehlerQueue=zaehlerQueue+1;
				CreateQueue(Connectors,element,zaehlerQueue);
				}
			if (element.getClass().getName().equalsIgnoreCase("sys4sim.internal_model.Worker"))
				CreateWorker(Connectors,element);
			if (element.getClass().getName().equalsIgnoreCase("sys4sim.internal_model.Transporter"))
				CreateTransporter(Connectors,element);
		}

		
	// neues Dok. benötigt nur Wurzel
   	 Document doc = new Document(root);     
   	 doc.setRootElement(root);  	
   	  
   	// definierte XML Struktur in festgelegte Outputdatei übertragen
   	 try {
   		 FileOutputStream out =  new  FileOutputStream("test.xml");
   		  XMLOutputter serializer = new XMLOutputter(Format.getRawFormat());
   		  serializer.output(doc,out);
   		  out.flush();
   		  out.close();
   		}
   	 catch (IOException e) {
   		 
   	 }
			 
	}
	
	//Methode zur Erzeugung der einzelnen Konnektoren
	private static Element CreateConnector(Element Connectors, Connector element,int zaehlerConnector) 
	{ 
		String zname = "connector"+String.valueOf(zaehlerConnector);
		String name = "<![CDATA["+zname+"]]>";
		String ID = element.getId();
		String sourceID = element.getSource().getId().toString();
		String targetID = element.getTarget().getId().toString();
		
		//element.getTarget();
		
		Element Connector = new Element("Connector");
        Connectors.addContent(Connector);
        Connector
        .addContent(new Element("Id").setText(ID))//
        .addContent(new Element("Name").setText(name))//
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
          .addContent(new Element("Point").setText("<X>30</X><Y>0</Y>"));    //bea
        return Connectors;		
	}
	
		
	
	//Methode zur Erzeugung der einzelnen Senken
	private static Element CreateSink(Element EmbeddedObjects, ModelElement element,int zaehlerElement) 
	{
		String zname = "Sink"+String.valueOf(zaehlerElement);
		String name = "<![CDATA["+zname+"]]>";
		String ID = element.getId();
		
		Element EmbeddedObject = new Element("EmbeddedObject");
		EmbeddedObjects.addContent(EmbeddedObject);
			EmbeddedObject
			.addContent(new Element("Id").setText(ID))//
			.addContent(new Element("Name").setText(name))//
			.addContent(new Element("ExcludeFromBuild").setText("false"))
			.addContent(new Element("X").setText("370"))//bea nebeneinander Koordinaten
			.addContent(new Element("Y").setText("140"))
			.addContent(new Element("Label").setText("<X>10</X><Y>-20</Y>"))//bea wieder gucken Schachtelung
			.addContent(new Element("PublicFlag").setText("false"))
			.addContent(new Element("PresentationFlag").setText("true"))
			.addContent(new Element("ShowLabel").setText("true"));
			Element ActiveObjectClass2 = new Element("ActiveObjectClass"); /// ActiveObjectClass2 da ohne 2 schon gibt
			EmbeddedObject.addContent(ActiveObjectClass2);
				ActiveObjectClass2
				.addContent(new Element("PackageName").setText("![CDATA[com.xj.anylogic.libraries.enterprise]]"))//
				.addContent(new Element("ClassName").setText("![CDATA[Sink]]"));//bea name des Objekts in der Bibliothek
			EmbeddedObject
			.addContent(new Element("GenericParametersSubstitute").setText("![CDATA[Entity]]"));//ba
			Element Parameters = new Element("Parameters"); 
			EmbeddedObject.addContent(Parameters);
				Element Parameter = new Element("Parameter"); 
				Parameters.addContent(Parameter);
				Parameter
				.addContent(new Element("Name").setText("<![CDATA[onEnter]]>"))//bei jeden Element anders für vollständigkeit müssen elemente der Bibliothek angeguckt werden
				.addContent(new Element("Value").setText("<![CDATA[]]>"));
			return(EmbeddedObjects);
		
	}
	
	//Methode zur Erzeugung der einzelnen 
	private static Element CreateSource(Element EmbeddedObjects, ModelElement element,int zaehlerElement)
	{
		String zname = "Source"+String.valueOf(zaehlerElement);
		String name = "<![CDATA["+zname+"]]>";
		String ID = element.getId();
		
		Element EmbeddedObject = new Element("EmbeddedObject");
		EmbeddedObjects.addContent(EmbeddedObject);
			EmbeddedObject
			.addContent(new Element("Id").setText(ID))//
			.addContent(new Element("Name").setText(name))//
			.addContent(new Element("ExcludeFromBuild").setText("false"))
			.addContent(new Element("X").setText("370"))//bea nebeneinander Koordinaten
			.addContent(new Element("Y").setText("140"))
			.addContent(new Element("Label").setText("<X>10</X><Y>-20</Y>"))//
			.addContent(new Element("PublicFlag").setText("false"))
			.addContent(new Element("PresentationFlag").setText("true"))
			.addContent(new Element("ShowLabel").setText("true"));
			Element ActiveObjectClass2 = new Element("ActiveObjectClass"); /// ActiveObjectClass2 da ohne 2 schon gibt
			EmbeddedObject.addContent(ActiveObjectClass2);
				ActiveObjectClass2
				.addContent(new Element("PackageName").setText("![CDATA[com.xj.anylogic.libraries.enterprise]]"))//
				.addContent(new Element("ClassName").setText("![CDATA[Source]]"));//
			EmbeddedObject
			.addContent(new Element("GenericParametersSubstitute").setText("![CDATA[Entity]]"));//
			Element Parameters = new Element("Parameters"); 
			EmbeddedObject.addContent(Parameters);
				Element Parameter = new Element("Parameter"); 
				Parameters.addContent(Parameter);
				Parameter
				.addContent(new Element("Name").setText("<![CDATA[arrivalType]]>"))//bleibt aussen vor
				.addContent(new Element("Value").setText("<![CDATA[]]>"));//
				Element Parameter2 = new Element("Parameter"); 
				Parameters.addContent(Parameter2);
				Parameter2
				.addContent(new Element("Name").setText("<![CDATA[rate]]>"))//wenn on enter code da ist
				.addContent(new Element("Value").setText("<![CDATA[poisson(0.45)]]>"));//wenn on enter code da ist jetzt bsp
				Element Parameter3 = new Element("Parameter"); 
				Parameters.addContent(Parameter3);
				Parameter3
				.addContent(new Element("Name").setText("<![CDATA[interarrivalTime]]>"))//bleibt aussen vor
				.addContent(new Element("Value").setText("<![CDATA[]]>"));
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
				.addContent(new Element("Value").setText("<![CDATA[1]]>"));//hab mal 1
				Element Parameter7 = new Element("Parameter"); 
				Parameters.addContent(Parameter7);
				Parameter7
				.addContent(new Element("Name").setText("<![CDATA[limitArrivals]]>"))//bleibt aussen vor
				.addContent(new Element("Value").setText("<![CDATA[]]>"));
				Element Parameter8 = new Element("Parameter"); 
				Parameters.addContent(Parameter8);
				Parameter8
				.addContent(new Element("Name").setText("<![CDATA[maxArrivals]]>"))//bleibt aussen vor 
				.addContent(new Element("Value").setText("<![CDATA[]]>"));
				Element Parameter9 = new Element("Parameter"); 
				Parameters.addContent(Parameter9);
				Parameter9
				.addContent(new Element("Name").setText("<![CDATA[newEntity]]>"))//bleibt aussen vor vielleicht neue eigenschaft
				.addContent(new Element("Value").setText("<![CDATA[]]>"));
				Element Parameter10 = new Element("Parameter"); 
				Parameters.addContent(Parameter10);
				Parameter10
				.addContent(new Element("Name").setText("<![CDATA[onExit]]>"))//bis code da dann rein
				.addContent(new Element("Value").setText("<![CDATA[]]>"));
				Element Parameter11 = new Element("Parameter"); 
				Parameters.addContent(Parameter11);
				Parameter11
				.addContent(new Element("Name").setText("<![CDATA[entityShape]]>"))//bleibt aussen vor
				.addContent(new Element("Value").setText("<![CDATA[]]>"));
				Element Parameter12 = new Element("Parameter"); 
				Parameters.addContent(Parameter12);
				Parameter12
				.addContent(new Element("Name").setText("<![CDATA[uniqueShape]]>"))//bleibt aussen vor
				.addContent(new Element("Value").setText("<![CDATA[]]>"));
				Element Parameter13 = new Element("Parameter"); 
				Parameters.addContent(Parameter13);
				Parameter13
				.addContent(new Element("Name").setText("<![CDATA[enableRotation]]>"))//bleibt aussen vor
				.addContent(new Element("Value").setText("<![CDATA[]]>"));
			return(EmbeddedObjects);
		
	}
	
	//Methode zur Erzeugung der einzelnen 
	private static Element CreateMachine(Element EmbeddedObjects, ModelElement element,int zaehlerElement)
	{
		String zname = "delay"+String.valueOf(zaehlerElement);
		String name = "<![CDATA["+zname+"]]>";
		String ID = element.getId();
		
		Element EmbeddedObject = new Element("EmbeddedObject");
		EmbeddedObjects.addContent(EmbeddedObject);
			EmbeddedObject
			.addContent(new Element("Id").setText(ID))//
			.addContent(new Element("Name").setText(name))//
			.addContent(new Element("ExcludeFromBuild").setText("false"))
			.addContent(new Element("X").setText("370"))//bea nebeneinander Koordinaten
			.addContent(new Element("Y").setText("140"))
			.addContent(new Element("Label").setText("<X>10</X><Y>-20</Y>"))//
			.addContent(new Element("PublicFlag").setText("false"))
			.addContent(new Element("PresentationFlag").setText("true"))
			.addContent(new Element("ShowLabel").setText("true"));
			Element ActiveObjectClass2 = new Element("ActiveObjectClass"); /// ActiveObjectClass2 da ohne 2 schon gibt
			EmbeddedObject.addContent(ActiveObjectClass2);
				ActiveObjectClass2
				.addContent(new Element("PackageName").setText("![CDATA[com.xj.anylogic.libraries.enterprise]]"))//
				.addContent(new Element("ClassName").setText("![CDATA[Delay]]"));//
			EmbeddedObject
			.addContent(new Element("GenericParametersSubstitute").setText("![CDATA[Entity]]"));//
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
				.addContent(new Element("Value").setText("<![CDATA[]]>"));//wenn on enter code da ist jetzt bsp
				Element Parameter3 = new Element("Parameter"); 
				Parameters.addContent(Parameter3);
				Parameter3
				.addContent(new Element("Name").setText("<![CDATA[speed]]>"))//bleibt aussen vor
				.addContent(new Element("Value").setText("<![CDATA[]]>"));
				Element Parameter4 = new Element("Parameter"); 
				Parameters.addContent(Parameter4);
				Parameter4
				.addContent(new Element("Name").setText("<![CDATA[capacity]]>"))//muss dann gemacht werden
				.addContent(new Element("Value").setText("<![CDATA[]]>"));
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
				.addContent(new Element("Value").setText("<![CDATA[]]>"));
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
		return(EmbeddedObjects);
	}
	
	//Methode zur Erzeugung der einzelnen 
	private static Element CreateQueue(Element EmbeddedObjects, ModelElement element, int zaehlerElement)
	{
		String zname = "Queue"+String.valueOf(zaehlerElement);
		String name = "<![CDATA["+zname+"]]>";
		String ID = element.getId();
		
		Element EmbeddedObject = new Element("EmbeddedObject");
		EmbeddedObjects.addContent(EmbeddedObject);
			EmbeddedObject
			.addContent(new Element("Id").setText(ID))//
			.addContent(new Element("Name").setText(name))//
			.addContent(new Element("ExcludeFromBuild").setText("false"))
			.addContent(new Element("X").setText("370"))//bea nebeneinander Koordinaten
			.addContent(new Element("Y").setText("140"))
			.addContent(new Element("Label").setText("<X>10</X><Y>-20</Y>"))//
			.addContent(new Element("PublicFlag").setText("false"))
			.addContent(new Element("PresentationFlag").setText("true"))
			.addContent(new Element("ShowLabel").setText("true"));
			Element ActiveObjectClass2 = new Element("ActiveObjectClass"); /// ActiveObjectClass2 da ohne 2 schon gibt
			EmbeddedObject.addContent(ActiveObjectClass2);
				ActiveObjectClass2
				.addContent(new Element("PackageName").setText("![CDATA[com.xj.anylogic.libraries.enterprise]]"))//
				.addContent(new Element("ClassName").setText("![CDATA[queue]]"));//
			EmbeddedObject
			.addContent(new Element("GenericParametersSubstitute").setText("![CDATA[Entity]]"));//
			Element Parameters = new Element("Parameters"); 
			EmbeddedObject.addContent(Parameters);
				Element Parameter = new Element("Parameter"); 
				Parameters.addContent(Parameter);
				Parameter
				.addContent(new Element("Name").setText("<![CDATA[capacity]]"))//muss dann gemacht werden aus dem modell
				.addContent(new Element("Value").setText("<![CDATA[]]>"));//gleiches
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
		return(EmbeddedObjects);
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
	
	public String IDErzeugen(int anzahl, int bereich) {

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
	
}
