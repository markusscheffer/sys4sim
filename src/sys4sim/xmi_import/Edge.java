package sys4sim.xmi_import;

public class Edge extends XmiObjectWithName {
	private String xmiType;
	private String visibility;
	private String source;
	private String target;
	private String inPartition;
	private int weight;
	
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
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public String getInPartition() {
		return inPartition;
	}
	public void setInPartition(String inPartition) {
		this.inPartition = inPartition;
	}
	public int getWeight() {
		return weight;
	}
	public void setWeight(int weight) {
		this.weight = weight;
	}
}
