package sys4sim.xmi_import;

import java.util.ArrayList;

public class PackagedElement extends XmiObjectWithName {

	private String xmiType;
	private ArrayList<Port> ports;
	private ArrayList<Generalization> generalizations = new ArrayList<Generalization>();
	private ArrayList<OwnedAttribute> attributes = new ArrayList<OwnedAttribute>();
	private ArrayList<OwnedConnector> connectors = new ArrayList<OwnedConnector>();
	
	public String getXmiType() {
		return xmiType;
	}
	public void setXmiType(String xmiType) {
		this.xmiType = xmiType;
	}
	public ArrayList<Port> getPorts() {
		return ports;
	}
	public void setPorts(ArrayList<Port> ports) {
		this.ports = ports;
	}
	public ArrayList<Generalization> getGeneralizations() {
		return generalizations;
	}
	public void setGeneralizations(ArrayList<Generalization> generalizations) {
		this.generalizations = generalizations;
	}
	public ArrayList<OwnedAttribute> getAttributes() {
		return attributes;
	}
	public void setAttributes(ArrayList<OwnedAttribute> attributes) {
		this.attributes = attributes;
	}
	public ArrayList<OwnedConnector> getConnectors() {
		return connectors;
	}
	public void setConnectors(ArrayList<OwnedConnector> connectors) {
		this.connectors = connectors;
	}
}
