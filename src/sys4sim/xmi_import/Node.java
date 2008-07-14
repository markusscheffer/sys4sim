package sys4sim.xmi_import;

import java.util.ArrayList;
import java.util.Hashtable;

public class Node extends GeneralNode {
	private String xmiType;
	private String visibility;
	private ArrayList<String> outgoingStrings;
	private ArrayList<String> incomingStrings;
	private String inPartitionString;
	private Argument argument;
	private Result result;
	private ArrayList<Edge> outgoing = new ArrayList<Edge>();
	private ArrayList<Edge> incoming = new ArrayList<Edge>();
	private ActivityPartition inPartition;
	
	public void unstringRelations (Hashtable<String, XmiObject> hash) {
		for (String outgoingString : outgoingStrings) {
			Edge edge = (Edge) hash.get(outgoingString);
			outgoing.add(edge);
		}
		for (String incomingString : incomingStrings) {
			Edge edge = (Edge) hash.get(incomingString);
			incoming.add(edge);
		}
		if (inPartitionString != null) {
			inPartition = (ActivityPartition) hash.get(inPartitionString);
		}
	}
	
	public ArrayList<Edge> getOutgoing() {
		return outgoing;
	}
	public void setOutgoing(ArrayList<Edge> outgoing) {
		this.outgoing = outgoing;
	}
	public ArrayList<Edge> getIncoming() {
		return incoming;
	}
	public void setIncoming(ArrayList<Edge> incoming) {
		this.incoming = incoming;
	}
	public ActivityPartition getInPartition() {
		return inPartition;
	}
	public void setInPartition(ActivityPartition inPartition) {
		this.inPartition = inPartition;
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
	public void setOutgoingStrings(ArrayList<String> outgoing) {
		this.outgoingStrings = outgoing;
	}
	public void setIncomingStrings(ArrayList<String> incoming) {
		this.incomingStrings = incoming;
	}
	public ArrayList<String> getOutgoingStrings() {
		return outgoingStrings;
	}
	public ArrayList<String> getIncomingStrings() {
		return incomingStrings;
	}
	public String getInPartitionString() {
		return inPartitionString;
	}
	public void setInPartitionString(String inPartition) {
		this.inPartitionString = inPartition;
	}
	public Argument getArgument() {
		return argument;
	}
	public void setArgument(Argument argument) {
		this.argument = argument;
	}
	public Result getResult() {
		return result;
	}
	public void setResult(Result result) {
		this.result = result;
	}
}
