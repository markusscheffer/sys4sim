package sys4sim.internal_model;

import java.util.Hashtable;

public class Model {
	private Hashtable<String, ModelElement> elements = new Hashtable<String, ModelElement>();
	private Source source;
	private String name;
	
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
}
