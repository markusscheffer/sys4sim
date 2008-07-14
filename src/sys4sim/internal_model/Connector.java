package sys4sim.internal_model;

public class Connector extends ModelElement {
	private Resource source;
	private Resource target;
	private String sourceName;
	private String targetName;
	
	public String getSourceName() {
		return sourceName;
	}
	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}
	public String getTargetName() {
		return targetName;
	}
	public void setTargetName(String targetName) {
		this.targetName = targetName;
	}
	public Resource getSource() {
		return source;
	}
	public void setSource(Resource source) {
		this.source = source;
	}
	public Resource getTarget() {
		return target;
	}
	public void setTarget(Resource target) {
		this.target = target;
	}
}
