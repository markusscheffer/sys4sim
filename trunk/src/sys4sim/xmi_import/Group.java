package sys4sim.xmi_import;

import java.util.ArrayList;

public class Group extends XmiObjectWithName {
	private String xmiType;
	private String visibility;
	private ArrayList<String> nodes;
	private String represents;
	private ArrayList<String> edges;
	
	public String getXmiType() {
		return xmiType;
	}
	public void setXmiType(String xmiType) {
		this.xmiType = xmiType;
	}
	public String getVisibility() {
		return visibility;
	}
	public void setVisibility(String visibility) {
		this.visibility = visibility;
	}
	public ArrayList<String> getNodes() {
		return nodes;
	}
	public void setNodes(ArrayList<String> nodes) {
		this.nodes = nodes;
	}
	public String getRepresents() {
		return represents;
	}
	public void setRepresents(String represents) {
		this.represents = represents;
	}
	public ArrayList<String> getEdges() {
		return edges;
	}
	public void setEdges(ArrayList<String> edges) {
		this.edges = edges;
	}
}
