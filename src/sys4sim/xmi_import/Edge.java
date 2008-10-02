package sys4sim.xmi_import;

import java.util.Hashtable;

public class Edge extends XmiObjectWithName implements java.lang.Cloneable {
	private String xmiType;
	private String visibility;
	private String sourceString;
	private String targetString;
	private String inPartitionString;
	private int weight;
	private String guard;
	
	public String getGuard() {
		return guard;
	}

	public void setGuard(String guard) {
		this.guard = guard;
	}
	private GeneralNode source;
	private GeneralNode target;
	private ActivityPartition inPartition;
	
	public void unstringRelations (Hashtable<String, XmiObject> hash) {
		if (sourceString != null) {
			//System.out.println(sourceString);
			source = (GeneralNode) hash.get(sourceString);
		}
		if (targetString != null) {
			//System.out.println(targetString);
			target = (GeneralNode) hash.get(targetString);
		}
		if (inPartitionString != null) {
			//System.out.println(inPartitionString);
			inPartition = (ActivityPartition) hash.get(inPartitionString);
		}
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
	public GeneralNode getSource() {
		return source;
	}
	public void setSource(GeneralNode source) {
		this.source = source;
	}
	public GeneralNode getTarget() {
		return target;
	}
	public void setTarget(GeneralNode target) {
		this.target = target;
	}
	public ActivityPartition getInPartition() {
		return inPartition;
	}
	public void setInPartition(ActivityPartition inPartition) {
		this.inPartition = inPartition;
	}
	
	public Edge clone() {
		Edge toReturn = null;
		try {
			toReturn = (Edge) super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		return toReturn;
	}
	
	public Edge expand(int expandID, Node node) {
		System.out.println("Expanding Edge between " + this.getSource().getClass().getSimpleName() + " and " +
				this.getTarget().getClass().getSimpleName());
		String newID = this.getXmiID() + "_" + expandID;
		if (Importer.readElements.contains(newID)) {
			return (Edge) Importer.getElement(newID);
		} else {
			//System.out.println("Expanding edge: " + newID);
			Edge edge = this.clone();
			edge.setXmiID(newID);
			Importer.addElement(edge);
			edge.setSource(node);
			edge.setTarget(edge.getTarget().expand(expandID));
			
		//	((Node)edge.getTarget()).getIncoming().remove(this);
		//	((Node)edge.getTarget()).getIncoming().add(edge);
			
			return edge;
		}
	}
}
