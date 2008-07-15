package sys4sim.xmi_import;

import java.util.Hashtable;

public class OwnedAttribute extends XmiObjectWithNameAndType {
	private String aggregation;
	private String xmiType;
	
	public String getAggregation() {
		return aggregation;
	}
	public void setAggregation(String aggregation) {
		this.aggregation = aggregation;
	}
	public String getXmiType() {
		return xmiType;
	}
	public void setXmiType(String xmiType) {
		this.xmiType = xmiType;
	}
	
	public void unstringRelations (Hashtable<String, XmiObject> hash) {
		super.unstringRelations(hash);
	}
	
	public OwnedAttribute copy () {
		OwnedAttribute attribute = new OwnedAttribute();
		attribute.setName(this.getName());
		attribute.setXmiType(this.getXmiType());
		attribute.setXmiID(this.getXmiID());
		attribute.setAggregation(this.getAggregation());
		attribute.setType(this.getType());
		return attribute;
	}

}
