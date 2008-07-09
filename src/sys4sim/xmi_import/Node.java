package sys4sim.xmi_import;

import java.util.ArrayList;

public class Node extends XmiObjectWithName {
	private String xmiType;
	private String visibility;
	private ArrayList<String> outgoing;
	private ArrayList<String> incoming;
	private String inPartition;
	private Argument argument;
	private Result result;
	
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
	public void setOutgoing(ArrayList<String> outgoing) {
		this.outgoing = outgoing;
	}
	public void setIncoming(ArrayList<String> incoming) {
		this.incoming = incoming;
	}
	public ArrayList<String> getOutgoing() {
		return outgoing;
	}
	public ArrayList<String> getIncoming() {
		return incoming;
	}
	public String getInPartition() {
		return inPartition;
	}
	public void setInPartition(String inPartition) {
		this.inPartition = inPartition;
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
