package sys4sim.xmi_import;

import java.util.ArrayList;
import java.util.Hashtable;


public class Activity extends PackagedElement {
	private ArrayList<String> partitionStrings = new ArrayList<String>();
	private ArrayList<Node> nodes = new ArrayList<Node>();
	private ArrayList<Edge> edges = new ArrayList<Edge>();
	private ArrayList<ActivityPartition> partitions = new ArrayList<ActivityPartition>();
	
	public void unstringRelations(Hashtable<String, XmiObject> hash) {
		for (String partitionString : partitionStrings) {
			ActivityPartition partition = (ActivityPartition) hash.get(partitionString);
			partitions.add(partition);
		}
	}
	
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

	public static Activity getFirst(ArrayList<Activity> activities) {
		Activity first = activities.get(0);
		return getFirst(first, activities);
	}
	
	public static Activity getFirst(Activity activity, ArrayList<Activity> activities) {
		Activity first = activity;
		for (Activity activity2 : activities) {
			if (activity2.contains(first)) {
				first = activity2;
			}
		}
		if (first.equals(activity)) {
			return activity;
		} else {
			return getFirst(first, activities);
		}
	}
	
	public boolean contains (Activity activity) {
		for (Node node : getNodes()) {
			if (node.getName().equals(activity.getName())) {
				return true;
			}
		}
		return false;
		
	}
	
}
