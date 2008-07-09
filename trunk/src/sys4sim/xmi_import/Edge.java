package sys4sim.xmi_import;

import java.util.Hashtable;

public class Edge extends XmiObjectWithName {
	private String xmiType;
	private String visibility;
	private String sourceString;
	private String targetString;
	private String inPartitionString;
	private int weight;
	
	private Node source;
	private Node target;
	private ActivityPartition inPartition;
	
	public void unstringRelations (Hashtable<String, XmiObject> hash) {
		source = (Node) hash.get(sourceString);
		target = (Node) hash.get(targetString);
		inPartition = (ActivityPartition) hash.get(inPartitionString);
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
	public String getSourceString() {
		return sourceString;
	}
	public void setSourceString(String source) {
		this.sourceString = source;
	}
	public String getTargetString() {
		return targetString;
	}
	public void setTargetString(String target) {
		this.targetString = target;
	}
	public String getInPartitionString() {
		return inPartitionString;
	}
	public void setInPartitionString(String inPartition) {
		this.inPartitionString = inPartition;
	}
	public int getWeight() {
		return weight;
	}
	public void setWeight(int weight) {
		this.weight = weight;
	}
	public Node getSource() {
		return source;
	}
	public void setSource(Node source) {
		this.source = source;
	}
	public Node getTarget() {
		return target;
	}
	public void setTarget(Node target) {
		this.target = target;
	}
	public ActivityPartition getInPartition() {
		return inPartition;
	}
	public void setInPartition(ActivityPartition inPartition) {
		this.inPartition = inPartition;
	}
}
