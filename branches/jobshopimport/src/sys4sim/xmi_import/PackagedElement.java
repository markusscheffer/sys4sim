package sys4sim.xmi_import;

import java.util.ArrayList;

public class PackagedElement extends XmiObjectWithName {

	private String xmiType;
	private ArrayList<Port> ports = new ArrayList<Port>();
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
	public void copyPorts (PackagedElement element) {
		for (Port port : element.getPorts()) {
			this.getPorts().add(port.copy());
		}
	}
	public void copyAttributes (PackagedElement element) {
		for (OwnedAttribute attribute : element.getAttributes()) {
			if (isNotMeta(attribute)) {
				this.getAttributes().add(attribute.copy());	
			} 
		}
		
	}
	
	public boolean isNotMeta (OwnedAttribute attribute) {
		if (attribute.getType() == null) {
			return true;
		} else if (attribute.getType().getName().equals("SimulationSingleProcess")) {
			return false;
		} else if (attribute.getType().getName().equals("SimulationArrivalProcess")) {
			return false;
		} else if (attribute.getType().getName().equals("SimulationDepartureProcess")) {
			return false;
		} else if (attribute.getType().getName().equals("SimulationQueue")) {
			return false;
		} else {
			return true;
		}
	}
	
	public void copyConnectors (PackagedElement element) {
		for (OwnedConnector connector : element.getConnectors()) {
			this.getConnectors().add(connector.copy());
		}
	}
	
	
}
