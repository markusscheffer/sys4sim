package sys4sim.internal_model;

import java.util.ArrayList;

public class Model {
	private ArrayList<ModelBlock> elements;
	private ArrayList<Connector> connectors;
	private Source source;
	
	public ArrayList<ModelBlock> getElements() {
		return elements;
	}
	public void setElements(ArrayList<ModelBlock> elements) {
		this.elements = elements;
	}
	public ArrayList<Connector> getConnectors() {
		return connectors;
	}
	public void setConnectors(ArrayList<Connector> connectors) {
		this.connectors = connectors;
	}
	public Source getSource() {
		return source;
	}
	public void setSource(Source source) {
		this.source = source;
	}
}
