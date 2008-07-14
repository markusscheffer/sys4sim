package sys4sim.internal_model;

import java.util.Hashtable;

public class Model {
	private Hashtable<String, ModelElement> elements = new Hashtable<String, ModelElement>();
	private Source source;
	
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
