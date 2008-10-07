package sys4sim.xmi_import;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;





import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;


import sys4sim.gui.GUI;
import sys4sim.internal_model.*;
import sys4sim.internal_model.Process;
import java.util.Stack;

public class Importer extends DefaultHandler{
	private GUI gui;
	private static Model model;
	
	private static Hashtable<String, ProcessSystem> processSystems = new Hashtable<String, ProcessSystem>();
	
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
	
	public static ArrayList<String> readElements = new ArrayList<String>();
	
	static int expandID = 0;
	
	public static InitialNode expandActivities(Activity activity) {
		System.out.println("Expanding activity (level " + expandID + "): " + activity.getName());
		InitialNode initial = activity.getFirst();
		initial =  (InitialNode) initial.expand(expandID++);
		//System.out.println("Finished expanding activity: " + activity.getName());
		return initial;
	}
	
	public static ArrayList<ActivityFinalNode> expandSubActivities(Activity activity, GeneralNode node, int superID) {
		System.out.println("Expanding activity (level " + expandID + "): " + activity.getName());
		InitialNode initial = activity.getFirst();

		initial =  (InitialNode) initial.expand(expandID++);

		Edge edge = ((Node)node).getIncoming().get(0);
		Node target = ((Node)initial.getOutgoing().get(0).getTarget());
		edge.setTarget(target);
		target.getIncoming().clear();
		target.getIncoming().add(edge);

		//System.out.println("Finished expanding activity: " + activity.getName());
		return initial.getAFNs(new ArrayList<Object>());
	}

	public static ArrayList<XmiObject> elements = new ArrayList<XmiObject>();
	
	public static XmiObject getElement(String id) {
		for (XmiObject object : elements) {
				if (object.getXmiID().equals(id)) {
					return object;
				}
			}
		return null;
	}
	
	public static void addElement(XmiObject object) {
		// TODO Auto-generated method stub
		elements.add(object);
		readElements.add(object.getXmiID());
	}
	
	private static ArrayList<Edge> connect(GeneralNode source, ArrayList<Edge> outgoing) {
		ArrayList<Edge> edges = new ArrayList<Edge>();
		
		for (Edge edge : outgoing) {
			Edge newEdge = connect(source, edge.getTarget());
			newEdge.setGuard(edge.getGuard());
			newEdge.setInPartition(edge.getInPartition());
			edges.add(newEdge);
		}

		return edges;
	}

	private static Edge connect(GeneralNode source, GeneralNode target) {
		Edge edge = new Edge();
		edge.setSource(source);
		edge.setTarget(target);
		edge.setXmiType("uml:ControlFlow");
		return edge;
	}

	private static ArrayList<Connector> queueConnectors = new ArrayList<Connector>();
	
	public static void generateModel (
			UmlClass system, 
			Activity first, 
			ArrayList<Rate> rates,
			ArrayList<Association> associations) {
		model = new Model();
		model.setName(system.getName());

		InitialNode initial = expandActivities(first);
		ArrayList<Edge> edges = initial.getEdges(new ArrayList<Edge>(), new ArrayList<GeneralNode>());
		

		
		rates = Rate.expandRates(rates, edges);
		
		System.out.println("rates: "+ rates.size());
		System.out.println("attributes: " + system.getAttributes().size());
		for (OwnedAttribute attribute : system.getAttributes()) {
			Importer.addObject(model, attribute);
		}
		for (Association association : associations) {
			addIfEntityDescription(model, association);
		}
		addRates(model, rates);

		addEdges(model, edges);
		Model model2 = model;
		System.out.println("");
	}
	
	private static Hashtable<String, Integer> processSystemInstanceCounter = new Hashtable<String, Integer>();

	//private static Hashtable<String, Process> processes = new Hashtable<String, Process>();
	
	private static void addRates(Model model, ArrayList<Rate> rates) {
		
		for (ProcessSystem ps : processSystems.values()) {
			processSystemInstanceCounter.put(ps.getName(), 0);

		}
		
		for (Rate rate : rates) {
			Node source = (Node) rate.getBaseActivityEdge().getSource();
			
			while (source.getClass().equals(ForkNode.class)) {
				
				//TODO: Hack - why first?
				source = (Node) source.getIncoming().get(0).getSource();
			}
			if (source instanceof DecisionNode) {
				ModelBlock block = (ModelBlock) model.getElement(
					((Node)((Node)rate.getBaseActivityEdge().getTarget()).getOutgoing().get(1).getTarget()).getInPartition().getRepresents().getXmiID());
				
				
					
					Entity entity = new Entity();
					//TODO: Check for entity types
					((Source)block).getEntities().put(entity, rate.getRate());
					System.out.println("Setting Rate of " + block.getName() + " to: " +
							rate.getRate().toString());
			} else {
				String idOfOwner = source.getInPartition().getRepresents().getXmiID();
				if (processSystems.containsKey(idOfOwner)) {
					ProcessSystem ps = processSystems.get(idOfOwner);
					Process block = new Process();
					source.setProcess(block);
					block.setProcessingRate(rate.getRate());
					block.setResourcePools(ps.getResourcePools());
					int idNr =  processSystemInstanceCounter.get(ps.getName());
					block.setName(ps.getName() + "_" + idNr);
					block.setId(ps.getId() + "_" + idNr);
					processSystemInstanceCounter.put(ps.getName(), idNr + 1);
					model.getElements().put(idOfOwner + "_" + idNr, block);
					System.out.println("Setting Rate of " + block.getName() + " to: " +
							rate.getRate().toString());
				} else {
				
					ModelBlock block = (ModelBlock) model.getElements().get(idOfOwner);
					if (block instanceof Process) {
						((Process)block).setProcessingRate(rate.getRate());
					} else if (block instanceof Source) {
						Entity entity = new Entity();
						//TODO: Check for entity types
						((Source)block).getEntities().put(entity, rate.getRate());
					} else {
					
						System.out.println("Rate cannot be set...");
					}
					System.out.println("Setting Rate of " + block.getName() + " to: " +
							rate.getRate().toString());
				}
			}
		}
	}
	
	private static int edgeCloneCounter = 0;
	
	private static void addEdges (Model model, ArrayList<Edge> edges) {
		ArrayList<Connector> connectors = new ArrayList<Connector>();
		ArrayList<Edge> realEdges = new ArrayList<Edge>();
		ArrayList<Edge> forkEdges = new ArrayList<Edge>();
		ArrayList<Edge> decisionEdges = new ArrayList<Edge>();
		ArrayList<Edge> possibleEdges = new ArrayList<Edge>();

		for (Edge edge : edges) {
			if (edge.getSource() instanceof ForkNode) {
				forkEdges.add(edge);
			} else if (edge.getSource() instanceof DecisionNode) {
				decisionEdges.add(edge);
			} else if (edge.getSource() instanceof CallBehaviorAction) {
				possibleEdges.add(edge);
			}
		}
		while (possibleEdges.size() > 0) {
			Edge edge = possibleEdges.get(0);
			possibleEdges.remove(edge);
				
				if (edge.getTarget() instanceof CallBehaviorAction) {
					if (((Node)edge.getSource()).getInPartition().getRepresents().equals(
							((Node)edge.getTarget()).getInPartition().getRepresents())) {
						//Start and End are in same partition
						continue;
					} else {
						//Start and End are in other partitions
						realEdges.add(edge);
						continue;
					}
				}
				if (edge.getTarget() instanceof ForkNode) {
					for (Edge edge2 : forkEdges) {
						if (edge.getTarget().getXmiID().contains(edge2.getSource().getXmiID())) {
							Edge edge3 = edge2.clone();
							edge3.setXmiID(edge2.getXmiID() + "_e" + edgeCloneCounter++);
							edge3.setSource(edge.getSource());
							possibleEdges.add(edge3);
						}
					}
				}
				if (edge.getTarget() instanceof DecisionNode) {
					for (Edge edge2 : decisionEdges) {
						if (edge.getTarget().getXmiID().contains(edge2.getSource().getXmiID())) {
							Edge edge3 = edge2.clone();
							edge3.setXmiID(edge2.getXmiID() + "_e" + edgeCloneCounter++);
							edge3.setSource(edge.getSource());
							possibleEdges.add(edge3);
						}
					}
				}
				
				
			} 
	addEdgesToModel(model, realEdges);
	}
	
	
	private static void addEdgesToModel(Model model, ArrayList<Edge> edges) {
	
		for (Edge edge : edges) {
			Node sourceNode = ((Node)edge.getSource());
			Node targetNode = ((Node)edge.getTarget());
			Connector connector = connect(sourceNode, targetNode, model);
			
			
			connector.setConditionString(edge.getGuard());
			
			if (connector.getTarget() instanceof Process) {
				Process process = (Process) connector.getTarget();
				boolean contains = false;
				
				for (String string : processQueues) {
					if (process.getId().contains(string)) {
						contains = true;
					}
				}
				if (contains) {
					boolean exists = false;

					Connector connector2 = new Connector();
					Queue queue = new Queue();
					
					for (Connector queueConnector : queueConnectors) {
						if (queueConnector.getTarget().equals(process)) {
							exists = true;
							connector2 = queueConnector;
							queue = (Queue) connector2.getSource();
							connector.setTarget(queue);
							queue.getIn().add(connector);
						}
					}

					if (!exists) {
						queueConnectors.add(connector2);
						queue.setName("q" + queueCounter++);
						queue.setId(queue.getName());
						connector.setTarget(queue);
						queue.getIn().add(connector);
						connector2.setSource(queue);
						connector2.setTarget(process);
						connector2.getSource().getOut().add(connector2);
						connector2.getTarget().getIn().add(connector2);
						connector2.setId("connector_" + connectorCounter++);
						model.addConnector(connector2);
						model.getElements().put(queue.getId(), queue);
					}
				}
			}


			if (edge.getGuard() != null) {
				connector.setConditionString(edge.getGuard());
				
			}
			
			connector.getSource().getOut().add(connector);
			connector.getTarget().getIn().add(connector);
			
			model.addConnector(connector);
		}
	
	}

	private static int connectorCounter = 0;

	public static Connector connect (GeneralNode source, GeneralNode target, Model model){
		
		OwnedAttribute sourceAtt = ((Node) source).getInPartition().getRepresents();
		OwnedAttribute targetAtt = ((Node) target).getInPartition().getRepresents();
		
		if (!sourceAtt.equals(targetAtt)) {
			Connector connector = new Connector();
			if (((Node) source).getProcess() != null) {
				connector.setSource(((Node)source).getProcess());
			} else {
				connector.setSource((ModelBlock)model.getElement(sourceAtt.getXmiID()));
			}
			if (((Node) target).getProcess() != null) {
				connector.setTarget(((Node)target).getProcess());
			} else {
				connector.setTarget((ModelBlock)model.getElement(targetAtt.getXmiID()));
			}
			
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
				//System.out.println("foo");
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
			ProcessSystem ps = new ProcessSystem(id);
			ps.setName(attribute.getName());
			generateResourcePool(ps, attribute);
			System.out.println("Generating Processing System (" + attribute.getName() + ")");
			processSystems.put(id, ps);
			return;
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
	private static int queueCounter = 0;
	
	private static ArrayList<String> processQueues = new ArrayList<String>();
	
	private static void generateResourcePool(ProcessSystem process,
			OwnedAttribute attribute) {
		MachinePool mp = new MachinePool();
		mp.setId("mp" + poolCounter);
		
		TransporterPool tp = new TransporterPool();
		tp.setId("tp" + poolCounter);
		
		WorkerPool wp = new WorkerPool();
		wp.setId("wp" + poolCounter);
		
		poolCounter++;
		
		UmlClass cls = attribute.getType();
		for (OwnedAttribute att : cls.getAttributes()) {
			if (att.getType() == null) {
				
			} else if (att.getType().isSubclassOf("SimulationQueue")) {
				Queue queue = new Queue();
				processQueues.add(process.getId());
				System.out.println("Adding Queue to system: " + process.getName());
			} else {
				Resource resource = addAttribute(process, att);
				addToPools(resource, mp, tp, wp);
			}
		}
		if (mp.getElements().size() > 0) {
			process.getResourcePools().put(mp, mp.getElements().size());
			//model.getElements().put(mp.getId(), mp);
		}
		if (wp.getElements().size() > 0) {
			process.getResourcePools().put(wp, wp.getElements().size());
			//model.getElements().put(wp.getId(), wp);
		}
		if (tp.getElements().size() > 0) {
			process.getResourcePools().put(tp, tp.getElements().size());
			//model.getElements().put(tp.getId(), tp);
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
	

	private static Resource addAttribute(ProcessSystem process, OwnedAttribute att) {
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
