package sys4sim.internal_model;

import java.util.ArrayList;
import java.util.Hashtable;

public class Model {
	private Hashtable<String, ModelElement> elements = new Hashtable<String, ModelElement>();
	private Source source;
	private String name;
	private ArrayList<Entity> entities = new ArrayList<Entity>();
	
	public ArrayList<Entity> getEntities() {
		return entities;
	}
	public void setEntities(ArrayList<Entity> entities) {
		this.entities = entities;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setElements(Hashtable<String, ModelElement> elements) {
		this.elements = elements;
	}
	public Hashtable<String, ModelElement> getElements() {
		return elements;
	}
	public Source getSource() {
		return source;
	}
	public void setSource(Source source) {
		this.source = source;
	}
	
	public void addConnector(Connector connector) {
		boolean doNotAdd = false;
		for (ModelElement elem : this.getElements().values()) {
			if (elem instanceof Connector) {
				Connector c = (Connector) elem;
				if (c.getSource().equals(connector.getSource()) &&
						c.getTarget().equals(connector.getTarget())) {
					doNotAdd = true;
				}
			}
		}
		if (!doNotAdd) {
			this.getElements().put(connector.getId(), connector);
		
			String outString = "Generating Connector between " + 
				connector.getSource().getName() + " and " 
				+ connector.getTarget().getName();
		
			if (connector.getConditionString() != null) {
				outString = outString + " [condition: " + connector.getConditionString() + "]";
			}
		
			System.out.println(outString);
		}
	}
	
	public ModelBlock getElement(String xmiID) {
		for (ModelElement element : this.getElements().values()) {
			if (element.getId().contains(xmiID)) {
				return (ModelBlock) element;
			}
		}
		return null;
	}
}
