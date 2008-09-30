 	package sys4sim.xmi_import;

import java.util.ArrayList;

public class GeneralNode extends XmiObjectWithName implements java.lang.Cloneable {

	public GeneralNode clone() {
		GeneralNode toReturn = null;
		try {
			toReturn = (GeneralNode) super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return toReturn;
		
	}
	
	public GeneralNode expand(int expandID) {
		String newID = this.getXmiID() + "_" + expandID;
		if (Importer.readElements.contains(newID)) {
			return (GeneralNode) Importer.getElement(newID);
		} else {
			/*
			ArrayList<Edge> edges = (ArrayList<Edge>) ((Node)node).getIncoming().clone();
			
			for (Edge edge : edges) {
				((Node)node).getIncoming().remove(edge);
				((Node)node).getIncoming().add(Importer.getElement(edge.getXmiID() + "_" + ))
			}*/
			GeneralNode node = this.clone();
			System.out.println("Expanding node: " + node.getName() + 
					" (" + node.getClass().getSimpleName() + ", "+ newID + ")");
			node.setXmiID(node.getXmiID() + "_" + expandID);
			Importer.addElement(node);
			
			if ((node instanceof CallBehaviorAction) &&
					(((CallBehaviorAction)node).getBehavior() != null)) {
				Activity sub = ((CallBehaviorAction)node).getBehavior();
				ArrayList<ActivityFinalNode> afns = Importer.expandSubActivities(sub, node, expandID);
				int i = 0;
				for (Edge outerEdge : ((Node)node).getOutgoing()) {
					i++;
					((Node)node).getOutgoing().remove(outerEdge);
					for (ActivityFinalNode afn : afns) {
						i++;
						for (Edge innerEdge : afn.getIncoming()) {
							i++;
							afn.getIncoming().remove(innerEdge);
							Edge newEdge = new Edge();
							newEdge.setXmiID(innerEdge.getXmiID() + "_" + i);
							newEdge.setSource(innerEdge.getSource());
							newEdge.setTarget(outerEdge.getTarget());
							if (outerEdge.getGuard() != null) {
								newEdge.setGuard(outerEdge.getGuard());
							}
							newEdge.setXmiType(outerEdge.getXmiType());
							((Node)node).getOutgoing().add(newEdge);
							afn.getIncoming().add(newEdge);
							//System.out.println("Expanding over boundaries.");
						}
						
					}
				}
				
				ArrayList<Edge> edges = new ArrayList<Edge>();
				
				if (this instanceof Node) {
					for (Edge edge : ((Node)node).getOutgoing()) {
						edges.add(edge.expand(expandID));
					}
					((Node)node).setOutgoing(edges);
				}
				return sub.getFirst();
			}
			
			
			ArrayList<Edge> edges = new ArrayList<Edge>();
		
			if (this instanceof Node) {
				for (Edge edge : ((Node)node).getOutgoing()) {
					edges.add(edge.expand(expandID));
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
