package sys4sim.xmi_import;

import java.util.ArrayList;
import java.util.Hashtable;

public class Group extends XmiObjectWithName {
	private String xmiType;
	private String visibility;
	private ArrayList<String> nodeStrings;
	private String representsString;
	private ArrayList<String> edgeStrings;
	
	private ArrayList<Node> nodes = new ArrayList<Node>();
	private ArrayList<Edge> edges = new ArrayList<Edge>();
	private OwnedAttribute represents;
	
	public void destringRelations (Hashtable<String, XmiObject> hash) {
		for (String nodeString : nodeStrings) {
			Node node = (Node) hash.get(nodeString);
			nodes.add(node);
		}
		for (String edgeString : edgeStrings) {
			Edge edge = (Edge) hash.get(edgeString);
			edges.add(edge);
		}
		represents = (OwnedAttribute) hash.get(representsString);
	}
	
	public ArrayList<Node> getNodes() {
		return nodes;
	}
	public void setNodes(ArrayList<Node> nodes) {
		this.nodes = nodes;
	}
	public ArrayList<Edge> getEdges() {
		return edges;
	}
	public void setEdges(ArrayList<Edge> edges) {
		this.edges = edges;
	}
	public OwnedAttribute getRepresents() {
		return represents;
	}
	public void setRepresents(OwnedAttribute represents) {
		this.represents = represents;
	}
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
	public ArrayList<String> getNodeStrings() {
		return nodeStrings;
	}
	public void setNodeStrings(ArrayList<String> nodes) {
		this.nodeStrings = nodes;
	}
	public String getRepresentsString() {
		return representsString;
	}
	public void setRepresentsString(String represents) {
		this.representsString = represents;
	}
	public ArrayList<String> getEdgeStrings() {
		return edgeStrings;
	}
	public void setEdgeStrings(ArrayList<String> edges) {
		this.edgeStrings = edges;
	}
}
