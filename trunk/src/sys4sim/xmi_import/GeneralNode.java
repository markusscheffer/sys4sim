 	package sys4sim.xmi_import;

import java.util.ArrayList;

public class GeneralNode extends XmiObjectWithName implements java.lang.Cloneable {

	@SuppressWarnings("unchecked")
	public GeneralNode clone() {
		GeneralNode toReturn = null;
		try {
			toReturn = (GeneralNode) super.clone();
			if (toReturn instanceof Node) {
				Node node = (Node) toReturn;
				node.setIncoming((ArrayList<Edge>)node.getIncoming().clone());
				node.setOutgoing((ArrayList<Edge>)node.getOutgoing().clone());
				toReturn = (GeneralNode) node;
			}
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return toReturn;
		
	}
	
	public GeneralNode expand(int expandID) {
		System.out.println("Expanding node: " + this.getName() + "(" + this.getXmiID() + 
				", " + this.getClass().getSimpleName() + ")");
		String newID = this.getXmiID() + "_" + expandID;
		if (Importer.readElements.contains(newID)) {
			return (GeneralNode) Importer.getElement(newID);
		} else {
			
			GeneralNode node = this.clone();
		//	System.out.println("Expanding node: " + node.getName() + 
		//			" (" + node.getClass().getSimpleName() + ", "+ newID + ")");
			node.setXmiID(node.getXmiID() + "_" + expandID);
			Importer.addElement(node);
			
			if ((node instanceof CallBehaviorAction) &&
					(((CallBehaviorAction)node).getBehavior() != null)) {
				Activity sub = ((CallBehaviorAction)node).getBehavior();
				ArrayList<ActivityFinalNode> afns = Importer.expandSubActivities(sub, node, expandID);
				int i = 0;
				
				for (Edge outerEdge : ((Node)node).getOutgoing()) {
					for (ActivityFinalNode afn : afns) {
						Edge edge = new Edge(); //outerEdge.clone();
						
						Node nodeBefore = (Node) afn.getIncoming().get(0).getSource();
						edge.setSource(nodeBefore);
						nodeBefore.getOutgoing().clear();
						nodeBefore.getOutgoing().add(edge);
						
						Node nodeAfter = (Node) outerEdge.getTarget();
						edge.setTarget(nodeAfter);
						edge.expand(expandID, nodeBefore);
						//nodeAfter.getIncoming().clear();
						//nodeAfter.getIncoming().add(edge);
						
						
					}
				}
				return sub.getFirst();
			}
		
			ArrayList<Edge> edges = new ArrayList<Edge>();
		
			if (this instanceof Node) {
				for (Edge edge : ((Node)node).getOutgoing()) {
					edges.add(edge.expand(expandID, (Node) node));
				}
				((Node)node).setOutgoing(edges);
			}
			return node;
		}
	}

	public ArrayList<ActivityFinalNode> getAFNs(ArrayList<Object> tested) {
		if (tested.contains(this)) {
			return new ArrayList<ActivityFinalNode>();
		} else {
			tested.add(this);
		}
		
		ArrayList<ActivityFinalNode> afns = new ArrayList<ActivityFinalNode>();
		
		if (this instanceof ActivityFinalNode) {
			afns.add((ActivityFinalNode) this);
			return afns;
		}
		
		if (this instanceof Node) {
			for (Edge edge : ((Node)this).getOutgoing()) {
				if (tested.contains(edge)) {
					break;
				} else {
					tested.add(edge);
					afns.addAll(((Node)edge.getTarget()).getAFNs(tested));
				}
			}
		}
		return afns;
	}	
	
	public ArrayList<Edge> getEdges(ArrayList<Edge> edges, ArrayList<GeneralNode> nodes) {
		if (nodes.contains(this)) {
			return edges;
		}
		nodes.add(this);
		if (this instanceof Node) {
			edges.addAll(((Node)this).getOutgoing());
			for (Edge edge : ((Node)this).getOutgoing()) {
				edge.getTarget().getEdges(edges, nodes);
			}
		}
		return edges;
	}
}
