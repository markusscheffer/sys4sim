package sys4sim.internal_model;

public class Connector extends ModelElement {
	private ModelBlock source;
	private ModelBlock target;
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
	public ModelBlock getSource() {
		return source;
	}
	public void setSource(ModelBlock source) {
		this.source = source;
	}
	public ModelBlock getTarget() {
		return target;
	}
	public void setTarget(ModelBlock target) {
		this.target = target;
	}
}
