package sys4sim.export.anylogic;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import sys4sim.export.ExportInterface;
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
   	 
       Model
       .addContent(new Element ("ID").setText("1213174774546"))
   	   .addContent(new Element("Name").setText("![CDATA[MM1]]"))
   	   .addContent(new Element("ExcludeFromBuild").setText("false"))
   	   .addContent(new Element("EngineVersion").setText("6"))
   	   .addContent(new Element("JavaPackageName").setText("![CDATA[mm1]]"));
   	  
       Element ActiveObjectClasses = new Element("ActiveObjectClasses");
       Model.addContent(ActiveObjectClasses);
            Element ActiveObjectClass = new Element("ActiveObjectClass");
            ActiveObjectClasses.addContent(ActiveObjectClass);
              ActiveObjectClass
              .addContent(new Element("ID").setText("1213174774547"))//bea
              .addContent(new Element("Name").setText("![CDATA[MM1]]"))//bea
              .addContent(new Element("ExcludeFromBuild").setText("false"))
              .addContent(new Element("ClientAreaTopLeft").setText("<X>0</X><Y>0</Y>"))//hoffe mal das geht so
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
		//FOR für die Erzeugung der Connectoren		
		for (ModelElement element : model.getElements().values()) 
		{
			System.out.println(element.getClass().getName());
			if (element.getClass().getName().equalsIgnoreCase("sys4sim.internal_model.Connector"))
				CreateConnector(Connectors,element);			
		}
		
		      Element EmbeddedObjects = new Element("EmbeddedObjects");
              ActiveObjectClass.addContent(EmbeddedObjects);
       //FOR für die Erzeugung der Elemente	
		for (ModelElement element : model.getElements().values()) 
		{
			System.out.println(element.getClass().toString());
			if (element.getClass().getName().equalsIgnoreCase("sys4sim.internal_model.Sink"))
				CreateSink(Connectors,element);
			if (element.getClass().getName().equalsIgnoreCase("sys4sim.internal_model.Source"))
				CreateSource(Connectors,element);
			if (element.getClass().getName().equalsIgnoreCase("sys4sim.internal_model.Machine"))
				CreateMachine(Connectors,element);
			if (element.getClass().getName().equalsIgnoreCase("sys4sim.internal_model.Queue"))
				CreateQueue(Connectors,element);
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
   		  XMLOutputter serializer = new XMLOutputter(Format.getPrettyFormat());
   		  serializer.output(doc,out);
   		  out.flush();
   		  out.close();
   		}
   	 catch (IOException e) {
   		 
   	 }
			 
	}
	
	//Methode zur Erzeugung der einzelnen Konnektoren
	private static Element CreateConnector(Element Connectors, ModelElement element) 
	{ 
		String Name = element.getName();
		String ID = element.getId();
		
		Element Connector = new Element("Connector");
        Connectors.addContent(Connector);
        Connector
        .addContent(new Element("Id").setText(ID))//bea Zufall?
        .addContent(new Element("Name").setText("<![CDATA[connector]]>"))//bea zählt hoch
        .addContent(new Element("X").setText("170"))// A Zeilenumbruch entfernen
        .addContent(new Element("Y").setText("150"))                
        .addContent(new Element("Label").setText("<X>10</X><Y>0</Y>"))// bea ob das so geht
        .addContent(new Element("PublicFlag").setText("false"))
        .addContent(new Element("PresentationFlag").setText("true"))
        .addContent(new Element("ShowLabel").setText("false"))
        .addContent(new Element("SourceEmbeddedObject").setText("1213175436531"))
        .addContent(new Element("SourceConnectableName").setText("out"))//bea immer out oder in
        .addContent(new Element("TargetEmbeddedObject").setText("1213175452093"))//bea
        .addContent(new Element("TargetConnectableName").setText("in"));//bea sind alle in hmm...
        Element Points = new Element("Points");
        Connector.addContent(Points);
          Points
          .addContent(new Element("Point").setText("<X>0</X><Y>0</Y>"))    //bea
          .addContent(new Element("Point").setText("<X>30</X><Y>0</Y>"));    //beaja wen
        return Connectors;		
	}
	
	//Methode zur Erzeugung der einzelnen 
	private static Element test(Element Connectors, ModelElement element) 
	{ 
		Element Connector = new Element("Connector");
        Connectors.addContent(Connector);
        Connector
        .addContent(new Element("dergrosseTest").setText("Test(derGrosse)"));
        
        Element Points = new Element("testanhang");
        Connector.addContent(Points);
          Points
          .addContent(new Element("Point").setText("<X>0</X><Y>0</Y>"))    //bea
          .addContent(new Element("Point").setText("<X>30</X><Y>0</Y>"));    //beaja wen
          return Connectors;
		
	}
	
	//Methode zur Erzeugung der einzelnen 
	private static Element CreateSink(Element EmbeddedObjects, ModelElement element) 
	{
		Element EmbeddedObject = new Element("EmbeddedObject");
		EmbeddedObjects.addContent(EmbeddedObject);
			EmbeddedObject
			.addContent(new Element("Id").setText("1213175432437"))//bea
			.addContent(new Element("Name").setText("<![CDATA[sink]]>"))//bea
			.addContent(new Element("ExcludeFromBuild").setText("false"))
			.addContent(new Element("X").setText("370"))//bea nebeneinander Koordinaten
			.addContent(new Element("Y").setText("140"))
			.addContent(new Element("Label").setText("<X>10</X><Y>-20</Y>"))//bea wieder gucken Schachtelung
			.addContent(new Element("PublicFlag").setText("false"))
			.addContent(new Element("PresentationFlag").setText("true"))
			.addContent(new Element("ShowLabel").setText("true"));
			Element ActiveObjectClass2 = new Element("ActiveObjectClass"); // ActiveObjectClass2 da ohne 2 schon gibt
			EmbeddedObject.addContent(ActiveObjectClass2);
				ActiveObjectClass2
				.addContent(new Element("PackageName").setText("![CDATA[com.xj.anylogic.libraries.enterprise]]"))//bea bibliothek  in welcher element meist enterprise
				.addContent(new Element("ClassName").setText("![CDATA[Sink]]"));//bea name des Objekts in der Bibliothek
			EmbeddedObject
			.addContent(new Element("GenericParametersSubstitute").setText("![CDATA[Entity]]"));//ba
			Element Parameters = new Element("Parameters"); 
			EmbeddedObject.addContent(Parameters);
				Element Parameter = new Element("Parameter"); 
				Parameters.addContent(Parameter);
				Parameter
				.addContent(new Element("Name").setText("eigenschafft"))//bei jeden Element anders für vollständigkeit müssen elemente der Bibliothek angeguckt werden
				.addContent(new Element("Value").setText("wert"));
			return(EmbeddedObjects);
		
	}
	
	//Methode zur Erzeugung der einzelnen 
	private static Element CreateSource(Element EmbeddedObjects, ModelElement element)
	{
		return(EmbeddedObjects);
	}
	
	//Methode zur Erzeugung der einzelnen 
	private static Element CreateMachine(Element EmbeddedObjects, ModelElement element)
	{
		return(EmbeddedObjects);
	}
	
	//Methode zur Erzeugung der einzelnen 
	private static Element CreateQueue(Element EmbeddedObjects, ModelElement element)
	{
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
	
}
