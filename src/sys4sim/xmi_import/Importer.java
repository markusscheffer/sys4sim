package sys4sim.xmi_import;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;





import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;


import sys4sim.gui.GUI;
import sys4sim.internal_model.*;

public class Importer extends DefaultHandler{
	private GUI gui;
	private static Model model;
	
	public static Model getModel() {
		return model;
	}

	public static void setModel(Model model) {
		Importer.model = model;
	}

	public Importer(GUI gui) {
		this.gui = gui;
	}
	
	public Model importSysML(File filepath) {
		gui.message("Importiere: " + filepath.toString() + "...");
		try {
			XMLReader xr = org.xml.sax.helpers.XMLReaderFactory.createXMLReader();
			ParsingHandler handler = new ParsingHandler();
			xr.setContentHandler(handler);
			xr.setErrorHandler(handler);
			FileReader fr = new FileReader(filepath);
			xr.parse(new InputSource(fr));
			
			
			
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    gui.message("done");
	    return getModel();
	}
	
	public static void generateModel (UmlClass system, Activity first) {
		Model model = new Model();
		model.setName(system.getName());
		System.out.println("attributes: " + system.getAttributes().size());
		for (OwnedAttribute attribute : system.getAttributes()) {
			Importer.addObject(model, attribute);
		}
		int i = 0;
		for (Edge edge : first.getEdges()) {
			if (edge.getXmiType().equals("uml:ControlFlow")) {
				
				//TODO: Fork nodes!
				
				System.out.println("between here");
				OwnedAttribute source = ((Node)edge.getSource()).getInPartition().getRepresents();
				System.out.println("between here");
				System.out.println(((Node)edge.getTarget()).getXmiID());
				OwnedAttribute target = ((Node)edge.getTarget()).getInPartition().getRepresents();
				System.out.println("between here");
				if (!source.equals(target)) {
					Connector connector = new Connector();
					connector.setSource((Resource)model.getElements().get(source.getXmiID()));
					connector.setTarget((Resource)model.getElements().get(target.getXmiID()));
					connector.setId("connector_" + i);
					i++;
					System.out.println("Generating connector between " + 
							source.getName() + " and " + target.getName());
				}
				
				System.out.println("and here.");
			}

			
		}
		
		Importer.setModel(model);
	}
	public static void addObject (Model model, OwnedAttribute attribute) {
		String id = attribute.getXmiID();
		ModelBlock block;
		UmlClass classOfAtt = attribute.getType();
		if (classOfAtt.isSubclassOf("SimulationQueue")) {
			block = new Queue();
		} else if (classOfAtt.isSubclassOf("SimulationDepartureProcess")){
			block = new Sink();
		} else if (classOfAtt.isSubclassOf("SimulationArrivalProcess")){
			block = new Source();
		} else if (classOfAtt.isSubclassOf("SimulationSingleProcess")){
			block = new Machine();
		} else {
			block = new ModelBlock();
			System.out.println("Suitable ModelBlock not found: " + classOfAtt.getName());
		}
		System.out.println("Generating Block for: " + attribute.getName());
		block.setId(id);
		block.setName(attribute.getName());
		model.getElements().put(id, block);
	}
}
