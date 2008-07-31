package sys4sim.xmi_import;

import java.util.Hashtable;


public class End extends XmiObject {
	private String partWithPortString;
	private String roleString;
	
	private OwnedAttribute partWithPort;
	private Port role;
	
	public void unstringRelations (Hashtable<String, XmiObject> hash) {
		if (partWithPortString != null) {
			//System.out.println(partWithPortString);
			partWithPort = (OwnedAttribute) hash.get(partWithPortString);
		}
		if (roleString != null) {
			//System.out.println(roleString);
			role = (Port) hash.get(roleString);
		}
	}
	
	public String getPartWithPortString() {
		return partWithPortString;
	}
	public void setPartWithPortString(String partWithPort) {
		this.partWithPortString = partWithPort;
	}
	public String getRoleString() {
		return roleString;
	}
	public void setRoleString(String role) {
		this.roleString = role;
	}
	public OwnedAttribute getPartWithPort() {
		return partWithPort;
	}
	public void setPartWithPort(OwnedAttribute partWithPort) {
		this.partWithPort = partWithPort;
	}
	public Port getRole() {
		return role;
	}
	public void setRole(Port role) {
		this.role = role;
	}
}
