package sys4sim.xmi_import;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;





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
	
	public static void generateModel (UmlClass system, Activity first, ArrayList<Rate> rates) {
		model = new Model();
		model.setName(system.getName());
		System.out.println("rates: "+ rates.size());
		System.out.println("attributes: " + system.getAttributes().size());
		for (OwnedAttribute attribute : system.getAttributes()) {
			Importer.addObject(model, attribute);
		}
		ArrayList<Edge> edges = eliminateForkNodes(first.getEdges());
		for (Edge edge : edges) {
			if (edge.getXmiType().equals("uml:ControlFlow")) {
				Connector connector = connect(edge.getSource(), edge.getTarget(), model);
				if (!connector.getName().equals("void")) {
					boolean add = true;
					for (ModelElement con : model.getElements().values()) {
						if (con.getClass().equals(Connector.class)) {
							if (((Connector)con).getSource().equals(connector.getSource()) &&
									((Connector)con).getTarget().equals(connector.getTarget())) {
								add = false;
							}
						}
					}
					if (add) {
						System.out.println("Generating Connector between " + 
								connector.getSource().getName() + " and " 
								+ connector.getTarget().getName());
						
						model.getElements().put(connector.getId(), connector);
					}
				}
			}
		}
		for (Rate rate : rates) {
			Node source = (Node) rate.getBaseActivityEdge().getSource();
			
			while (source.getClass().equals(ForkNode.class)) {
				
				//TODO: Hack - why first?
				source = (Node) source.getIncoming().get(0).getSource();
			}
			
			String idOfOwner = source.getInPartition().getRepresents().getXmiID();
			ModelBlock block = (ModelBlock) model.getElements().get(idOfOwner);
			block.setRate(rate.getRate());
			System.out.println("Setting Rate of " + block.getName() + " to: " +
					rate.getRate().toString());
		}
	}

	private static int connectorCounter = 0;

	public static Connector connect (GeneralNode source, GeneralNode target, Model model){
		
		OwnedAttribute sourceAtt = ((Node) source).getInPartition().getRepresents();
		OwnedAttribute targetAtt = ((Node) target).getInPartition().getRepresents();
		
		if (!sourceAtt.equals(targetAtt)) {
			Connector connector = new Connector();
			connector.setSource((ModelBlock)model.getElements().get(sourceAtt.getXmiID()));
			connector.setTarget((ModelBlock)model.getElements().get(targetAtt.getXmiID()));
			connector.setId("connector_" + connectorCounter);
			connectorCounter++;
			return connector;
		}
		
		Connector connector = new Connector();
		connector.setName("void");
		return connector;
	}
	
	public static ArrayList<Edge> eliminateForkNodes (ArrayList<Edge> edges) {
		ArrayList<Edge> cleanEdges = new ArrayList<Edge>();
		for (Edge edge : edges) {
			if (edge.getSource().getClass().equals(ForkNode.class)) {
				ArrayList<Edge> newEdges = new ArrayList<Edge>();
				for (Edge incoming : ((ForkNode) edge.getSource()).getIncoming()) {
					Edge newEdge = new Edge();
					newEdge.setSource(incoming.getSource());
					newEdge.setTarget(edge.getTarget());
					newEdge.setXmiType(edge.getXmiType());
					newEdges.add(newEdge);
				}
				for (Edge newEdge : eliminateForkNodes(newEdges)) {
					cleanEdges.add(newEdge);
				}
			} else if (edge.getTarget().getClass().equals(ForkNode.class)) {
				ArrayList<Edge> newEdges = new ArrayList<Edge>();
				for (Edge outgoing : ((ForkNode) edge.getTarget()).getOutgoing()) {
					Edge newEdge = new Edge();
					newEdge.setSource(edge.getSource()); 
					newEdge.setTarget(outgoing.getTarget());
					newEdge.setXmiType(edge.getXmiType());
					newEdges.add(newEdge);
				}
				for (Edge newEdge : eliminateForkNodes(newEdges)) {
					cleanEdges.add(newEdge);
				}
			} else {
				cleanEdges.add(edge);
			}
		}	
		
		return cleanEdges;
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
		System.out.println("Generating Block (" + block.getClass().getName().substring(23) + ") for: " + attribute.getName());
		block.setId(id);
		block.setName(attribute.getName());
		model.getElements().put(id, block);
	}
}
