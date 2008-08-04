package sys4sim.internal_model;

public class Entity extends ModelElement {
	private String type;
	private Source source;
	
	public Source getSource() {
		return source;
	}

	public void setSource(Source source) {
		this.source = source;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	
}
