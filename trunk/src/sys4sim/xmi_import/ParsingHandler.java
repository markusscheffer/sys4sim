package sys4sim.xmi_import;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Stack;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

public class ParsingHandler extends DefaultHandler {
	
	private Stack<XmiObject> elementStack = new Stack<XmiObject>();
	
	private Hashtable<String,XmiObject> objectHash = new Hashtable<String,XmiObject>();
	
	private boolean saveText = false;
	private String savedText = "";
	
	public ParsingHandler () {
		super();
	}
	
	public void setXmiID(XmiObject obj, String id) {
		obj.setXmiID(id);
		objectHash.put(id, obj);
	}
	public void startDocument () {
		System.out.println("Starting to parse the document.");
	}

	public void endDocument () {
		ArrayList<UmlClass> classes = new ArrayList<UmlClass>();
		ArrayList<Activity> activities = new ArrayList<Activity>();
		ArrayList<Rate> rates = new ArrayList<Rate>();
		ArrayList<Association> associations = new ArrayList<Association>();
		
		System.out.println("Finished reading of file.");
	
		for (XmiObject obj : objectHash.values()) {
			
			// turn string-ids into objects
			obj.unstringRelations(objectHash);
			
			// collect all classes 
			if (obj instanceof UmlClass) {
				classes.add((UmlClass) obj);
			}
			
			// collect all activities
			if (obj instanceof Activity) {
				activities.add((Activity) obj);
			}
			
			//collect all rates
			if (obj instanceof Rate) {
				rates.add((Rate) obj);
			}
			
			// collect all Associations
			if (obj instanceof Association) {
				associations.add((Association) obj);
			}
		}
		System.out.println("Parsed all Classes and Activities.");
		
		// import attributes, ports, and connectors from above
		for (UmlClass class1 : classes) {
			for (Generalization gen : class1.getGeneralizations()) {
				class1.importGeneralization(gen);
			}
		}
		System.out.println("Imported Attributes, Ports and Connectors.");
		
		// get the first Activity
		Activity firstActivity = Activity.getFirst(activities);
		System.out.println("First Activity: " + firstActivity.getName());
		UmlClass system = UmlClass.getSystem(classes);
		System.out.println("System: " + system.getName());
		System.out.println("Generating Java Model.");
		Importer.generateModel(system, firstActivity, rates, associations);
		System.out.println("Finished parsing.");
	}
	    
	public void startElement (String uri, String name,
		      String qName, Attributes atts) {
		splitMultiple("test");
			String methodName = "".equals(uri) ? qName : name;
		if(elementStack.isEmpty() || !elementStack.peek().getClass().equals(DeepIgnoreXmiObject.class)) {
			try {
				Class<?> cls = getClass ();
	      
				Method method = cls.getMethod (methodName, String.class, String.class, Attributes.class);
				// elementStack.push((XmiObject) 
				elementStack.push((XmiObject) method.invoke(this, methodName, uri, atts));
				//System.out.println("Elemente nach Push: " + elementStack.size());
			
			} catch (NoSuchMethodException e) {
		      System.out.println ("Method does not exist!" + methodName);
		    } catch (IllegalAccessException e) {
		      System.out.println ("Method is not accessible!" + methodName);
		    } catch (IllegalArgumentException e) {
		      System.out.println ("Invalid Argument specified!" + methodName);
		    } catch (ExceptionInInitializerError e) {
		      System.out.println ("Method call init failed!" + methodName);
		    } catch (InvocationTargetException e) {
		      System.out.println ("Method threw exception:" + methodName);
		      Throwable methodException = e.getTargetException ();
		      methodException.printStackTrace ();
		    }
		} else {
			//System.out.println("im here");
			elementStack.push(new DeepIgnoreXmiObject());
		}
	     // System.out.println("Auf den Stack gewandert: <" + qName + "> als " + elementStack.peek().getClass().toString());
		
	}

  public void endElement (String uri, String name, String qName) {
	  //System.out.println("Elemente vor Pop: " + elementStack.size());
	 XmiObject obj = elementStack.pop();
	 if (obj instanceof SaveTextVoidXmiObject) {
		 saveText = false;
	 } 
	 
	 String newName = "".equals(uri) ? qName : name;
	 
	 // This might just be some cruel hack...
	 if (newName.equals("guard")) {
		 saveText = false;
		 ((Edge)elementStack.peek()).setGuard(savedText);
		 savedText = "";
	 }
  }
	  
  public void characters (char ch[], int start, int length) {
	  if (saveText) {
		  for (char chr : ch) {
			  savedText += chr;
		  }
	  }
  }
  
  public XmiObject XMI(String name, String uri, Attributes atts) {
	  // done: this does not need anything to it.
  return (XmiObject) new VoidXmiObject(); 
  }
  
  public XmiObject Model(String name, String uri, Attributes atts) {
	  // done: this does not need anything to it.	  
  return (XmiObject) new VoidXmiObject(); 
  }
 
  public XmiObject ownedComment(String name, String uri, Attributes atts) {
	  // done: this does not need anything to it.  
	  return (XmiObject) new VoidXmiObject(); 
  }
  
  public XmiObject packageImport(String name, String uri, Attributes atts) {
	  // done: this does not need anything to it.  
	  return (XmiObject) new VoidXmiObject(); 
  }
  
  public XmiObject importedPackage(String name, String uri, Attributes atts) {
	  // done: this does not need anything to it.  
	  return (XmiObject) new VoidXmiObject(); 
  }
  
  public XmiObject packagedElement(String name, String uri, Attributes atts) {
	  String type = atts.getValue("xmi:type");
	  if (type == null) {
		  System.out.println("pe-typ nicht gefunden");
		  return new VoidXmiObject();
		  }
	  else if (type.equals("uml:Stereotype") ||
			  type.equals("uml:Extension")) {
		  return new DeepIgnoreXmiObject();
	  } else if (type.equals("uml:Class")) {
		  return umlClass(name, uri, atts);
	  } else if (type.equals("uml:Activity")) {
		  return activity(name, uri, atts);
	  } else if (type.equals("uml:Association")) {
		  Association association = new Association();
		  setXmiID(association, atts.getValue("xmi:id"));
		  for (String end : splitMultiple(atts.getValue("memberEnd"))) {
			  association.getEndStrings().add(end);
		  }
		  return (XmiObject) association;
		  
	  } else if (type.equals("uml:Package")) {
		  return new VoidXmiObject();
	  } else if (type.equals("uml:DataType")) {
		  return new VoidXmiObject();
	  }
	  
	  else {
		  System.out.println("Packaged Element Type not supplied! : " + type);
		  return new VoidXmiObject();	  
	  }
	  
  }
  
  public XmiObject umlClass(String name, String uri, Attributes atts) {

	  UmlClass uc = new UmlClass();
	  uc.setName(atts.getValue("name"));
	  setXmiID(uc, atts.getValue("xmi:id"));
	  uc.setXmiType(atts.getValue("xmi:type"));
	  return (XmiObject) uc; 
  }
  
  public XmiObject generalization(String name, String uri, Attributes atts) {
	  Generalization gen = new Generalization();
	  setXmiID(gen, atts.getValue("xmi:id"));
	  gen.setExtendedString(atts.getValue("general"));
	  PackagedElement pe = (PackagedElement) elementStack.peek();
	  pe.getGeneralizations().add(gen);
  return (XmiObject) gen; }
  
  public XmiObject ownedAttribute(String name, String uri, Attributes atts) {
	  OwnedAttribute oa;
	  String type = atts.getValue("xmi:type");
	  PackagedElement pe = (PackagedElement) elementStack.peek();
	  
	  if (type != null && type.equals("uml:Port")) {
		  oa = new Port();
		  pe.getPorts().add((Port)oa);
	  }
	  else {
		  oa = new OwnedAttribute();
		  pe.getAttributes().add(oa);
	  }
	  
	  setXmiID(oa, atts.getValue("xmi:id"));
	  oa.setXmiType(atts.getValue("xmi:type"));
	  oa.setName(atts.getValue("name"));
	  oa.setTypeString(atts.getValue("type"));
	  oa.setAggregation(atts.getValue("aggregation"));
	  oa.setOwner(pe);
	  return (XmiObject) oa;
  }
  
  public XmiObject ownedConnector(String name, String uri, Attributes atts) {
	  OwnedConnector oc = new OwnedConnector();
	  setXmiID(oc, atts.getValue("xmi:id"));
	  oc.setName(atts.getValue("name"));
	  XmiObject stackTop = elementStack.peek();
	  PackagedElement pe = (PackagedElement) stackTop;
	  pe.getConnectors().add(oc);
	  return (XmiObject) oc; 
  }
  
  public XmiObject end(String name, String uri, Attributes atts) {
	  End end = new End();
	  setXmiID(end, atts.getValue("xmi:id"));
	  end.setPartWithPortString(atts.getValue("partWithPort"));
	  end.setRoleString(atts.getValue("role"));
	  XmiObject stackTop = elementStack.peek();
	  OwnedConnector oc = (OwnedConnector) stackTop;
	  oc.getEnds().add(end);
	  return (XmiObject) end; 
  }
  
  public XmiObject activity(String name, String uri, Attributes atts) {
	  Activity act = new Activity();
	  setXmiID(act, atts.getValue("xmi:id"));
	  act.setXmiType(atts.getValue("xmi:type"));
	  act.setName(atts.getValue("name"));
	  if (atts.getValue("partition") != null) {
		  act.setPartitionStrings(splitMultiple(atts.getValue("partition")));
	  }
	  
	  return (XmiObject) act;
  }
  
  public XmiObject ownedBehavior(String name, String uri, Attributes atts) {
	  Activity act = (Activity) activity(name, uri, atts);
	  act.setOwner((PackagedElement)elementStack.peek());
	  return (XmiObject) act;
  }
  
  
  public ArrayList<String> splitMultiple(String string) {
	  ArrayList<String> list = new ArrayList<String>();
	  String[] temp = string.split(" ");
	  for (String sub : temp) {
		  list.add(sub);
	  }
	  return list;
  }
  
  public XmiObject general(String name, String uri, Attributes atts) {
	  // done: this does not need anything done to it.
	  return (XmiObject) new VoidXmiObject(); 
  }
  
  public XmiObject type(String name, String uri, Attributes atts) {
	  // done: this does not need anything done to it.
	  return (XmiObject) new VoidXmiObject(); 
  }
  
  public XmiObject icon(String name, String uri, Attributes atts) {
	  // done: this does not need anything done to it.
	  return (XmiObject) new VoidXmiObject(); 
  }
  
  public XmiObject ownedEnd(String name, String uri, Attributes atts) {
	  // done: this does not need anything done to it.
	  return (XmiObject) new VoidXmiObject(); 
  }
  
  public XmiObject upperValue(String name, String uri, Attributes atts) {
	  // done: this does not need anything done to it.
	  return (XmiObject) new VoidXmiObject(); 
  }
  
  public XmiObject lowerValue(String name, String uri, Attributes atts) {
	  // done: this does not need anything done to it.
	  return (XmiObject) new VoidXmiObject(); 
  }
  
  public XmiObject nestedClassifier(String name, String uri, Attributes atts) {
	  // done: this does not need anything done to it.
	  return (XmiObject) new VoidXmiObject(); 
  }
	  
  public XmiObject node(String name, String uri, Attributes atts) {
	  Node node;
	  String type = atts.getValue("xmi:type");
	  if (type.equals("uml:InitialNode")) {
		  node = (Node) initialNode(atts);
	  } else if (type.equals("uml:CallBehaviorAction")) {
		  node = (Node) callBehaviorAction(atts);
	  } else if (type.equals("uml:ActivityFinalNode")) {
		  node = (Node) activityFinalNode(atts);
	  } else if (type.equals("uml:ForkNode")) {
		  node = (Node) forkNode(atts);
	  } else if (type.equals("uml:DecisionNode")) {
		  node = (Node) decisionNode(atts);
	  } else if (type.equals("uml:CentralBufferNode")) {
		  return new VoidXmiObject();
	  } else if (type.equals("uml:FlowFinalNode")) {
		  node = (Node) new FlowFinalNode();
	  } else {
	  
		  node = new Node();
		  System.out.println("Node Class not supplied: " + type);
	  }
	  
	  setXmiID(node, atts.getValue("xmi:id"));
	  node.setXmiType(atts.getValue("xmi:type"));
	  node.setName(atts.getValue("name"));
	  node.setVisibility(atts.getValue("visibility"));
	  node.setInPartitionString(atts.getValue("inPartition"));
	  String incoming = atts.getValue("incoming");
	  String outgoing = atts.getValue("outgoing");
	  if (incoming != null) {
		  node.setIncomingStrings(splitMultiple(incoming));
	  } else {
		  node.setIncomingStrings(new ArrayList<String>());
	  }
	  if (outgoing != null) {
		  node.setOutgoingStrings(splitMultiple(outgoing));
	  } else {
		  node.setOutgoingStrings(new ArrayList<String>());
	  }
	  
	  XmiObject stackTop = elementStack.peek();
	  if (stackTop.getClass().equals(Activity.class)) {
		  Activity act = (Activity) stackTop;
		  act.getNodes().add(node);
	  } else {
		  System.out.println("Node not within Activity");
	  }
	  
	  return (XmiObject) node; 
  }
  
  public InitialNode initialNode (Attributes atts) {
	  return new InitialNode();
  }
  
  public CallBehaviorAction callBehaviorAction (Attributes atts) {
	  return new CallBehaviorAction();
  }
  
  public ActivityFinalNode activityFinalNode (Attributes atts) {
	  return new ActivityFinalNode();
  }
  
  public ForkNode forkNode (Attributes atts) {
	  return new ForkNode();
  }
  
  public DecisionNode decisionNode (Attributes atts) {
	  return new DecisionNode();
  }
  
  public XmiObject result(String name, String uri, Attributes atts) {
	  Result result = new Result();
	  setXmiID(result, atts.getValue("xmi:id"));
	  result.setName(atts.getValue("name"));
	  result.setVisibility(atts.getValue("visibility"));
	  result.setOutgoingString(atts.getValue("outgoing"));
	  
	  XmiObject stackTop = elementStack.peek();
	  if (stackTop.getClass().equals(CallBehaviorAction.class)) {
		  CallBehaviorAction cba = (CallBehaviorAction) stackTop;
		  cba.setResult(result);
	  } else {
		  System.out.println("result not within call behaviour action node");
	  }
	  return (XmiObject) result; 
  }
  
  public XmiObject argument(String name, String uri, Attributes atts) {
	  Argument argument  = new Argument();
	  setXmiID(argument, atts.getValue("xmi:id"));
	  argument.setName(atts.getValue("name"));
	  argument.setVisibility(atts.getValue("visibility"));
	  argument.setIncomingString(atts.getValue("incoming"));
	  CallBehaviorAction cba = (CallBehaviorAction) elementStack.peek();
	  cba.setArgument(argument);
	  
	  return (XmiObject) argument; 
  }
  
  public XmiObject edge(String name, String uri, Attributes atts) {
	  Edge edge;
	  String type = atts.getValue("xmi:type");
	  if (type.equals("uml:ControlFlow")) {
		  edge = (Edge) controlFlow(atts);
	  } else if (type.equals("uml:ObjectFlow")) {
		  edge = (Edge) objectFlow(atts);
	  } else {
		  edge = new Edge();
		  System.out.println("Edge Class not supplied! : " + type);
	  }
	  
	  setXmiID(edge, atts.getValue("xmi:id"));
	  edge.setXmiType(atts.getValue("xmi:type"));
	  edge.setName(atts.getValue("name"));
	  edge.setVisibility(atts.getValue("visibility"));
	  edge.setSourceString(atts.getValue("source"));
	  edge.setTargetString(atts.getValue("target"));
	  
	  Activity act = (Activity) elementStack.peek();
	  act.getEdges().add(edge);
	  
	  return (XmiObject) edge; 
  }
  
  public XmiObject guard (String name, String uri, Attributes atts) {
	  // we do not need the guard element. however, look at the body element
	  return new VoidXmiObject();
  }
  
  public XmiObject body (String name, String uri, Attributes atts) {
	  saveText = true;
	  return new SaveTextVoidXmiObject();
  }
  
  
  public ControlFlow controlFlow (Attributes atts) {
	  return new ControlFlow();
  }
  
  public ObjectFlow objectFlow (Attributes atts) {
	  return new ObjectFlow();
  }
  
  public XmiObject weight(String name, String uri, Attributes atts) {
	  Edge edge = (Edge) elementStack.peek();
	  edge.setWeight(Integer.parseInt(atts.getValue("value")));
	  
	  // we return a void object because the weight is already expressed in the edge.
	  return (XmiObject) new VoidXmiObject(); 
  }
  
  public XmiObject group(String name, String uri, Attributes atts) {
	  String xmiType = atts.getValue("xmi:type");
	  if (xmiType.equals("uml:ActivityPartition")) {
		  ActivityPartition ap = new ActivityPartition();
		  ap.setXmiType(xmiType);
		  setXmiID(ap, atts.getValue("xmi:id"));
		  ap.setVisibility(atts.getValue("visibility"));
		  ap.setNodeStrings(splitMultiple(atts.getValue("node")));
		  ap.setRepresentsString(atts.getValue("represents"));
		  ap.setEdgeStrings(splitMultiple(atts.getValue("edge")));
		  return ap;
	  } else {
		  System.out.println("Group type not known: " + xmiType);
		  return (XmiObject) new VoidXmiObject(); 
	  }
  }
  
  public XmiObject Allocated(String name, String uri, Attributes atts) {
	  //TODO Possibly, this should not be ignored
	  return (XmiObject) new VoidXmiObject();
  }
  
  public XmiObject profileApplication(String name, String uri, Attributes atts) {
	  // Ignore the whole tree.
	  return (XmiObject) new DeepIgnoreXmiObject(); 
  }
  
  public XmiObject eAnnotations(String name, String uri, Attributes atts) {
	  // nothing needed here.
	  return (XmiObject) new VoidXmiObject(); 
  }
  
  public XmiObject references(String name, String uri, Attributes atts) {
	  //nothing needed here.
	  return (XmiObject) new VoidXmiObject(); 
  }
  
  public XmiObject appliedProfile(String name, String uri, Attributes atts) {
	  //nothing needed here.
	  return (XmiObject) new VoidXmiObject(); 
  }
  
  public XmiObject FlowPort(String name, String uri, Attributes atts) {
	  //TODO: Not really sure here.
	  return (XmiObject) new VoidXmiObject(); 
  }
  
  public XmiObject ModelLibrary(String name, String uri, Attributes atts) {
	  //nothing needed here.
	  return (XmiObject) new DeepIgnoreXmiObject(); 
  }
  
  public XmiObject modelLibrary(String name, String uri, Attributes atts) {
	  //nothing needed here.
	  return (XmiObject) new DeepIgnoreXmiObject(); 
  }

  public XmiObject customizationTarget(String name, String uri, Attributes atts) {
	  //nothing needed here.
	  return (XmiObject) new DeepIgnoreXmiObject(); 
  }
  
  public XmiObject Customization(String name, String uri, Attributes atts) {
	  //nothing needed here.
	  return (XmiObject) new DeepIgnoreXmiObject(); 
  }
  
  public XmiObject typesForSource(String name, String uri, Attributes atts) {
	  //nothing needed here.
	  return (XmiObject) new VoidXmiObject(); 
  }
  
  public XmiObject typesForTarget(String name, String uri, Attributes atts) {
	  //nothing needed here.
	  return (XmiObject) new VoidXmiObject(); 
  }
  
  public XmiObject applyToSource(String name, String uri, Attributes atts) {
	  //nothing needed here.
	  return (XmiObject) new VoidXmiObject(); 
  }
  
  public XmiObject standardExpertConfiguration(String name, String uri, Attributes atts) {
	  //nothing needed here.
	  return (XmiObject) new VoidXmiObject(); 
  }
  
  public XmiObject suggestedOwnedTypes(String name, String uri, Attributes atts) {
	  //nothing needed here.
	  return (XmiObject) new VoidXmiObject(); 
  }
  
  public XmiObject hiddenOwnedTypes(String name, String uri, Attributes atts) {
	  //nothing needed here.
	  return (XmiObject) new VoidXmiObject(); 
  }
  
  public XmiObject multiLineTextProperties(String name, String uri, Attributes atts) {
	  //nothing needed here.
	  return (XmiObject) new VoidXmiObject(); 
  }
  
  public XmiObject possibleOwners(String name, String uri, Attributes atts) {
	  //nothing needed here.
	  return (XmiObject) new VoidXmiObject(); 
  }
  
  public XmiObject disallowedRelationships(String name, String uri, Attributes atts) {
	  //nothing needed here.
	  return (XmiObject) new VoidXmiObject(); 
  }
  
  public XmiObject quickApplyingFor(String name, String uri, Attributes atts) {
	  //nothing needed here.
	  return (XmiObject) new VoidXmiObject(); 
  }
  
  public XmiObject suggestedOwnedDiagrams(String name, String uri, Attributes atts) {
	  //nothing needed here.
	  return (XmiObject) new VoidXmiObject(); 
  }
  
  public XmiObject hiddenOwnedDiagrams(String name, String uri, Attributes atts) {
	  //nothing needed here.
	  return (XmiObject) new VoidXmiObject(); 
  }
  
  public XmiObject usedUMLProperties(String name, String uri, Attributes atts) {
	  //nothing needed here.
	  return (XmiObject) new VoidXmiObject(); 
  }
  
  public XmiObject inShortcutMenu(String name, String uri, Attributes atts) {
	  //nothing needed here.
	  return (XmiObject) new VoidXmiObject(); 
  }
  
  public XmiObject applyToTarget(String name, String uri, Attributes atts) {
	  //nothing needed here.
	  return (XmiObject) new VoidXmiObject(); 
  }
  
  public XmiObject metaProperty(String name, String uri, Attributes atts) {
	  //nothing needed here.
	  return (XmiObject) new DeepIgnoreXmiObject(); 
  }
  
  public XmiObject newTypes(String name, String uri, Attributes atts) {
	  //nothing needed here.
	  return (XmiObject) new VoidXmiObject(); 
  }
  
  public XmiObject propertyGroup(String name, String uri, Attributes atts) {
	  //nothing needed here.
	  return (XmiObject) new DeepIgnoreXmiObject(); 
  }
  
  public XmiObject properties(String name, String uri, Attributes atts) {
	  //nothing needed here.
	  return (XmiObject) new VoidXmiObject(); 
  }
  
  public XmiObject columns(String name, String uri, Attributes atts) {
	  //nothing needed here.
	  return (XmiObject) new VoidXmiObject(); 
  }
  
  public XmiObject auxiliaryResource(String name, String uri, Attributes atts) {
	  //nothing needed here.
	  return (XmiObject) new VoidXmiObject(); 
  }
  
  public XmiObject matrixtemplate(String name, String uri, Attributes atts) {
	  //nothing needed here.
	  return (XmiObject) new VoidXmiObject(); 
  }
  
  public XmiObject DependencyCriteria(String name, String uri, Attributes atts) {
	  //nothing needed here.
	  return (XmiObject) new VoidXmiObject(); 
  }
  
  public XmiObject UMLRelationTypeandStereotype(String name, String uri, Attributes atts) {
	  //nothing needed here.
	  return (XmiObject) new VoidXmiObject(); 
  }
  
  public XmiObject column_filter(String name, String uri, Attributes atts) {
	  //nothing needed here.
	  return (XmiObject) new VoidXmiObject(); 
  }
  
  public XmiObject row_filter(String name, String uri, Attributes atts) {
	  //nothing needed here.
	  return (XmiObject) new VoidXmiObject(); 
  }

  public XmiObject ElementType(String name, String uri, Attributes atts) {
	  //nothing needed here.
	  return (XmiObject) new VoidXmiObject(); 
  }
  
  public XmiObject Block(String name, String uri, Attributes atts) {
	  //nothing needed here.
	  return (XmiObject) new VoidXmiObject(); 
  }
  
  public XmiObject Unit(String name, String uri, Attributes atts) {
	  //nothing needed here.
	  return (XmiObject) new VoidXmiObject(); 
  }
  
  public XmiObject Dimension(String name, String uri, Attributes atts) {
	  //nothing needed here.
	  return (XmiObject) new VoidXmiObject(); 
  }
  
  public XmiObject NestedConnectorEnd(String name, String uri, Attributes atts) {
	  //nothing needed here.
	  return (XmiObject) new VoidXmiObject(); 
  }
  
  public XmiObject validationSuite(String name, String uri, Attributes atts) {
	  //nothing needed here.
	  return (XmiObject) new VoidXmiObject(); 
  }
  
  public XmiObject AppliedStereotype(String name, String uri, Attributes atts) {
	  //nothing needed here.
	  return (XmiObject) new VoidXmiObject(); 
  }

  public XmiObject details(String name, String uri, Attributes atts) {
	  //nothing needed here.
	  return (XmiObject) new VoidXmiObject(); 
  }
  
  public XmiObject importedProfile(String name, String uri, Attributes atts) {
	  //nothing needed here.
	  return (XmiObject) new VoidXmiObject(); 
  }
  
  public XmiObject Rate(String name, String uri, Attributes atts) {
	  if  (atts.getValue("rate") == null) {
		  return new VoidXmiObject();
	  }
	  Rate rate = new Rate();
	  setXmiID(rate, atts.getValue("xmi:id"));
	  rate.setRate(atts.getValue("rate"));
	  rate.setBaseActivityEdgeString(atts.getValue("base_ActivityEdge"));
	  
	  
	  
	  return (XmiObject) rate;
  }

  public XmiObject TODO_Owner(String name, String uri, Attributes atts) {
	  return new VoidXmiObject();
  }

  public XmiObject Info(String name, String uri, Attributes atts) {
	  return new VoidXmiObject();
  }


  public XmiObject validationRule(String name, String uri, Attributes atts) {
	  return new VoidXmiObject();
  }


  public XmiObject ownedRule(String name, String uri, Attributes atts) {
	  return new DeepIgnoreXmiObject();
  }
  
  public XmiObject constrainedElement(String name, String uri, Attributes atts) {
	  return new VoidXmiObject();
  }
  
  public XmiObject specification(String name, String uri, Attributes atts) {
	  return new DeepIgnoreXmiObject();
  }
  
  public XmiObject activeValidationSuite(String name, String uri, Attributes atts) {
	  return new VoidXmiObject();
  }
  




  
  
  


  
  
}
