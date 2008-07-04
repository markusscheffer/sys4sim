package sys4sim.xmi_import;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Stack;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

public class ParsingHandler extends DefaultHandler {
	
	private Stack<XmiObject> elementStack = new Stack<XmiObject>();
	
	public ParsingHandler () {
		super();
	}
	public void startDocument () {
		System.out.println("Start document");
	}

	public void endDocument () {
		System.out.println("End document");
	}
	    
	public void startElement (String uri, String name,
		      String qName, Attributes atts) {
			String methodName = "".equals(uri) ? qName : name;
			
		try {
			Class<?> cls = getClass ();
	      
			Method method = cls.getMethod (methodName, String.class, String.class, Attributes.class);
			// elementStack.push((XmiObject) 
			elementStack.push((XmiObject) method.invoke(this, methodName, uri, atts));
			
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
	      
		
	}

  public void endElement (String uri, String name, String qName) {
	 /* elementStack.pop(); */
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
	  PackagedElement pe = new PackagedElement();
	  pe.setName(atts.getValue("name"));
	  pe.setXmiID(atts.getValue("xmi:id"));
	  pe.setXmiType(atts.getValue("xmi:type"));
	  return (XmiObject) pe; 
  }
  
  public XmiObject generalization(String name, String uri, Attributes atts) {
	  Generalization gen = new Generalization();
	  gen.setXmiID(atts.getValue("xmi:id"));
	  gen.setExtended(atts.getValue("general"));
	  PackagedElement pe = (PackagedElement) elementStack.peek();
	  pe.getGeneralizations().add(gen);
  return (XmiObject) gen; }
  
  public XmiObject ownedAttribute(String name, String uri, Attributes atts) {
	  OwnedAttribute oa = new OwnedAttribute();
	  oa.setXmiID(atts.getValue("xmi:id"));
	  oa.setXmiType(atts.getValue("xmi:type"));
	  oa.setName(atts.getValue("name"));
	  oa.setType(atts.getValue("type"));
	  oa.setAggregation(atts.getValue("aggregation"));
	  PackagedElement pe = (PackagedElement) elementStack.peek();
	  pe.getAttributes().add(oa);
	  return (XmiObject) oa;
  }
  
  public XmiObject ownedConnector(String name, String uri, Attributes atts) {
	  
  return (XmiObject) new VoidXmiObject(); }
  
  public XmiObject end(String name, String uri, Attributes atts) {
	  
  return (XmiObject) new VoidXmiObject(); }
  
  public XmiObject general(String name, String uri, Attributes atts) {
	  
  return (XmiObject) new VoidXmiObject(); }
  
  public XmiObject type(String name, String uri, Attributes atts) {
	  
  return (XmiObject) new VoidXmiObject(); }
  
  public XmiObject icon(String name, String uri, Attributes atts) {
	  
  return (XmiObject) new VoidXmiObject(); }
  
  public XmiObject ownedEnd(String name, String uri, Attributes atts) {
	  
  return (XmiObject) new VoidXmiObject(); }
  
  public XmiObject upperValue(String name, String uri, Attributes atts) {
	  
  return (XmiObject) new VoidXmiObject(); }
  
  public XmiObject lowerValue(String name, String uri, Attributes atts) {
	  
  return (XmiObject) new VoidXmiObject(); }
  
  public XmiObject nestedClassifier(String name, String uri, Attributes atts) {
	  
  return (XmiObject) new VoidXmiObject(); }
  
  public XmiObject node(String name, String uri, Attributes atts) {
	  
  return (XmiObject) new VoidXmiObject(); }
  
  public XmiObject result(String name, String uri, Attributes atts) {
	  
  return (XmiObject) new VoidXmiObject(); }
  
  public XmiObject argument(String name, String uri, Attributes atts) {
	  
  return (XmiObject) new VoidXmiObject(); }
  
  public XmiObject weight(String name, String uri, Attributes atts) {
	  
  return (XmiObject) new VoidXmiObject(); }
  
  public XmiObject group(String name, String uri, Attributes atts) {
	  
  return (XmiObject) new VoidXmiObject(); }
  
  public XmiObject profileApplication(String name, String uri, Attributes atts) {
	  
  return (XmiObject) new VoidXmiObject(); }
  
  public XmiObject eAnnotations(String name, String uri, Attributes atts) {
	  
  return (XmiObject) new VoidXmiObject(); }
  
  public XmiObject references(String name, String uri, Attributes atts) {
	  
  return (XmiObject) new VoidXmiObject(); }
  
  public XmiObject appliedProfile(String name, String uri, Attributes atts) {
	  
  return (XmiObject) new VoidXmiObject(); }
  
  public XmiObject FlowPort(String name, String uri, Attributes atts) {
	  
  return (XmiObject) new VoidXmiObject(); }
  
  public XmiObject ModelLibrary(String name, String uri, Attributes atts) {
	  
  return (XmiObject) new VoidXmiObject(); }
  
  public XmiObject modelLibrary(String name, String uri, Attributes atts) {
	  
  return (XmiObject) new VoidXmiObject(); }

  public XmiObject customizationTarget(String name, String uri, Attributes atts) {
	  
  return (XmiObject) new VoidXmiObject(); }
  
  public XmiObject Customization(String name, String uri, Attributes atts) {
	  
  return (XmiObject) new VoidXmiObject(); }
  
  public XmiObject typesForSource(String name, String uri, Attributes atts) {
	  
  return (XmiObject) new VoidXmiObject(); }
  
  public XmiObject typesForTarget(String name, String uri, Attributes atts) {
	  
  return (XmiObject) new VoidXmiObject(); }
  
  public XmiObject edge(String name, String uri, Attributes atts) {
	  
  return (XmiObject) new VoidXmiObject(); }
  
  public XmiObject applyToSource(String name, String uri, Attributes atts) {
	  
  return (XmiObject) new VoidXmiObject(); }
  
  public XmiObject standardExpertConfiguration(String name, String uri, Attributes atts) {
	  
  return (XmiObject) new VoidXmiObject(); }
  
  public XmiObject suggestedOwnedTypes(String name, String uri, Attributes atts) {
	  
  return (XmiObject) new VoidXmiObject(); }  
  
  public XmiObject hiddenOwnedTypes(String name, String uri, Attributes atts) {
	  
  return (XmiObject) new VoidXmiObject(); }
  
  public XmiObject multiLineTextProperties(String name, String uri, Attributes atts) {
	  
  return (XmiObject) new VoidXmiObject(); }
  
  public XmiObject possibleOwners(String name, String uri, Attributes atts) {
	  
  return (XmiObject) new VoidXmiObject(); }
  
  public XmiObject disallowedRelationships(String name, String uri, Attributes atts) {
	  
  return (XmiObject) new VoidXmiObject(); }
  
  public XmiObject quickApplyingFor(String name, String uri, Attributes atts) {
	  
  return (XmiObject) new VoidXmiObject(); }
  
  public XmiObject suggestedOwnedDiagrams(String name, String uri, Attributes atts) {
	  
  return (XmiObject) new VoidXmiObject(); }
  
  public XmiObject hiddenOwnedDiagrams(String name, String uri, Attributes atts) {
	  
  return (XmiObject) new VoidXmiObject(); }
  
  public XmiObject usedUMLProperties(String name, String uri, Attributes atts) {
	  
  return (XmiObject) new VoidXmiObject(); }
  
  public XmiObject inShortcutMenu(String name, String uri, Attributes atts) {
	  
  return (XmiObject) new VoidXmiObject(); }
  
  public XmiObject applyToTarget(String name, String uri, Attributes atts) {
	  
  return (XmiObject) new VoidXmiObject(); }
  
  public XmiObject metaProperty(String name, String uri, Attributes atts) {
	  
  return (XmiObject) new VoidXmiObject(); }
  
  public XmiObject newTypes(String name, String uri, Attributes atts) {
	  
  return (XmiObject) new VoidXmiObject(); }
  
  public XmiObject propertyGroup(String name, String uri, Attributes atts) {
	  
  return (XmiObject) new VoidXmiObject(); }
  
  public XmiObject properties(String name, String uri, Attributes atts) {
	  
  return (XmiObject) new VoidXmiObject(); }
  
  public XmiObject columns(String name, String uri, Attributes atts) {
	  
  return (XmiObject) new VoidXmiObject(); }
  
  public XmiObject auxiliaryResource(String name, String uri, Attributes atts) {
	  
  return (XmiObject) new VoidXmiObject(); }
  
  public XmiObject matrixtemplate(String name, String uri, Attributes atts) {
	  
  return (XmiObject) new VoidXmiObject(); }
  
  public XmiObject DependencyCriteria(String name, String uri, Attributes atts) {
	  
  return (XmiObject) new VoidXmiObject(); }
  
  public XmiObject UMLRelationTypeandStereotype(String name, String uri, Attributes atts) {
	  
  return (XmiObject) new VoidXmiObject(); }
  
  public XmiObject column_filter(String name, String uri, Attributes atts) {
	  
  return (XmiObject) new VoidXmiObject(); }
  
  public XmiObject row_filter(String name, String uri, Attributes atts) {
	  
  return (XmiObject) new VoidXmiObject(); }

  public XmiObject ElementType(String name, String uri, Attributes atts) {
	  
  return (XmiObject) new VoidXmiObject(); }
  
  public XmiObject Block(String name, String uri, Attributes atts) {
	  
  return (XmiObject) new VoidXmiObject(); }
  
  public XmiObject Unit(String name, String uri, Attributes atts) {
	  
  return (XmiObject) new VoidXmiObject(); }
  
  public XmiObject Dimension(String name, String uri, Attributes atts) {
	  
  return (XmiObject) new VoidXmiObject(); }
  
  public XmiObject NestedConnectorEnd(String name, String uri, Attributes atts) {
	  
  return (XmiObject) new VoidXmiObject(); }
  
  public XmiObject validationSuite(String name, String uri, Attributes atts) {
	  
  return (XmiObject) new VoidXmiObject(); }
  
  public XmiObject AppliedStereotype(String name, String uri, Attributes atts) {
	  
  return (XmiObject) new VoidXmiObject(); }

  
  





  




  
  
  


  
  
}
