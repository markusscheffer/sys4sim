package sys4sim.xmi_import;

public class Port extends OwnedAttribute{
	public Port copy() {
		Port port = new Port();
		OwnedAttribute attribute = super.copy();
		port.setAggregation(attribute.getAggregation());
		port.setName(attribute.getName());
		port.setType(attribute.getType());
		port.setXmiID(attribute.getXmiID());
		port.setXmiType(attribute.getXmiType());
		
		
		return port;
	}
}
