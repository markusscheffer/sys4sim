package sys4sim.xmi_import;

import java.util.ArrayList;


public class Activity extends PackagedElement {
	private ArrayList<String> partitionStrings = new ArrayList<String>();
	private ArrayList<Node> nodes = new ArrayList<Node>();
	private ArrayList<Edge> edges = new ArrayList<Edge>();
	private ArrayList<ActivityPartition> partitions = new ArrayList<ActivityPartition>();
	
	public ArrayList<ActivityPartition> getPartitions() {
		return partitions;
	}
	public void setPartitions(ArrayList<ActivityPartition> partitions) {
		this.partitions = partitions;
	}
	public ArrayList<String> getPartitionStrings() {
		return partitionStrings;
	}
	public void setPartitionStrings(ArrayList<String> partitions) {
		this.partitionStrings = partitions;
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
}
