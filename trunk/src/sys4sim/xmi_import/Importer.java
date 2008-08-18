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
import sys4sim.internal_model.Process;


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
	
	public static void generateModel (
			UmlClass system, 
			Activity first, 
			ArrayList<Rate> rates,
			ArrayList<Association> associations) {
		model = new Model();
		model.setName(system.getName());
		System.out.println("rates: "+ rates.size());
		System.out.println("attributes: " + system.getAttributes().size());
		for (OwnedAttribute attribute : system.getAttributes()) {
			Importer.addObject(model, attribute);
		}
		for (Association association : associations) {
			addIfEntityDescription(model, association);
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
						if (edge.getGuard() != null) {
							connector.setConditionString(edge.getGuard());
							System.out.println("Generating Connector between " + 
									connector.getSource().getName() + " and " + 
									connector.getTarget().getName() +
									" [condition: " + connector.getConditionString() + "]");
						} else {
							System.out.println("Generating Connector between " + 
								connector.getSource().getName() + " and " 
								+ connector.getTarget().getName());
						}
						connector.getSource().getOut().add(connector);
						connector.getTarget().getIn().add(connector);
						
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
			if (block.getClass().equals(Process.class)) {
				((Process)block).setProcessingRate(rate.getRate());
			} else if (block.getClass().equals(Source.class)) {
				Entity entity = new Entity();
				//TODO: Check for entity types
				((Source)block).getEntities().put(entity, rate.getRate());
			}
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
			} else if (edge.getTarget() == null) {
				System.out.println("foo");
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
			((Source) block).setTypeID(classOfAtt.getXmiID());
		} else if (classOfAtt.isSubclassOf("SimulationSingleProcess")){
			block = new Process();
			generateResourcePool((Process) block, attribute);
		} else {
			block = new ModelBlock();
			System.out.println("Suitable ModelBlock not found: " + classOfAtt.getName());
		}
		System.out.println("Generating Block (" + block.getClass().getName().substring(23) + ") for: " + attribute.getName());
		block.setId(id);
		block.setName(attribute.getName());
		model.getElements().put(id, block);
	}
	
	private static int poolCounter = 0;
	
	private static void generateResourcePool(Process process,
			OwnedAttribute attribute) {
		MachinePool mp = new MachinePool();
		mp.setId("mp" + poolCounter);
		model.getElements().put(mp.getId(), mp);
		
		TransporterPool tp = new TransporterPool();
		tp.setId("tp" + poolCounter);
		model.getElements().put(tp.getId(), tp);
		
		WorkerPool wp = new WorkerPool();
		wp.setId("wp" + poolCounter);
		model.getElements().put(wp.getId(), wp);
		
		poolCounter++;
		
		UmlClass cls = attribute.getType();
		for (OwnedAttribute att : cls.getAttributes()) {
			if (att.getType() == null) {
				
			} else if (att.getType().isSubclassOf("SimulationQueue")) {
				// TODO
			} else {
				Resource resource = addAttribute(process, att);
				addToPools(resource, mp, tp, wp);
			}
		}
		if (mp.getElements().size() > 0) {
			process.getResourcePools().put(mp, mp.getElements().size());
		}
		if (wp.getElements().size() > 0) {
			process.getResourcePools().put(wp, wp.getElements().size());
		}
		if (tp.getElements().size() > 0) {
			process.getResourcePools().put(tp, tp.getElements().size());
		}
	}


	
	private static void addToPools(Resource resource, MachinePool mp,
			TransporterPool tp, WorkerPool wp) {
		if (resource == null) {
			System.out.println("Sorry: Null resource");
			return;
		}
		System.out.println("Adding Resource: " + resource.toString());
		if (resource instanceof Machine) {
			mp.add((Machine) resource);
		} else if (resource instanceof Transporter) {
			tp.add((Transporter) resource);
		} else if (resource instanceof Worker) {
			wp.add((Worker) resource);
		} else {
			System.out.println("Sorry: Do not know the correct resource.");
		}
	}
	

	private static Resource addAttribute(Process process, OwnedAttribute att) {
		if (att.getType().isSubclassOf("SimulationMachine")) {
			Machine machine = new Machine();
			setName(machine, att);
			return machine;
		} else if (att.getType().isSubclassOf("SimulationWorker")) {
			Worker worker = new Worker();
			setName(worker, att);
			return worker;
		} else if (att.getType().isSubclassOf("SimulationTransporter")) {
			Transporter transporter = new Transporter();
			setName(transporter, att);
			return transporter;
		} else {
			System.out.println("No matching Resource found for: " + att.getType().getName());
			return null;
		}
	}

	private static void setName(Resource resource, OwnedAttribute att) {
		resource.setName(att.getName() + " (" + att.getType().getName() + ")");	
	}

	public static void addIfEntityDescription (Model model, Association association) {
		if (association.getEnds().size() < 2) {
			return;
		}
		
		if (association.getEnds().get(0).isSubclassOf("SimulationArrivalProcess")) {
			addIfEntityDescription(
					model, 
					association.getEnds().get(0), 
					association.getEnds().get(1));
		} else if (association.getEnds().get(1).isSubclassOf("SimulationArrivalProcess")) {
			addIfEntityDescription(
					model, 
					association.getEnds().get(1), 
					association.getEnds().get(0));
		} else {
			System.out.println("Could not generate Entity Description for Association: " + association.getXmiID());
		}
	}

	private static void addIfEntityDescription(Model model, UmlClass source,
			UmlClass umlEntity) {
		if (umlEntity.isSubclassOf("SimulationEntity")) {
			Entity entity = new Entity();
			entity.setName(umlEntity.getName());
			entity.setSource(findSource(model, source));
			model.getEntities().add(entity);
			System.out.println("Adding entity: " + entity.getName() + " [Source: " + entity.getSource().getName() + "]");
		} else {
			System.out.println("Could not generate Entity Description for: " + source.getXmiID());
		}
	}

	private static Source findSource(Model model, UmlClass source) {
		for (ModelElement element : model.getElements().values()) {
			if (element instanceof Source) {
				if (source.getXmiID().equals(((Source) element).getTypeID())) {
					return (Source) element;
				}
			}
		}
		System.out.println("Could not find source.");
		return null;

	}
}
