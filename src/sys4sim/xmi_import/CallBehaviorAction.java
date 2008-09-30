package sys4sim.xmi_import;

import java.util.Hashtable;

public class CallBehaviorAction extends Node {
	String behaviorString;
	Activity behavior;
	public String getBehaviorString() {
		return behaviorString;
	}
	public void setBehaviorString(String behaviourString) {
		this.behaviorString = behaviourString;
	}
	public Activity getBehavior() {
		return behavior;
	}
	public void setBehavior(Activity behavior) {
		this.behavior = behavior;
	}
	
	public void unstringRelations(Hashtable<String, XmiObject> objects) {
		super.unstringRelations(objects);
		if (behaviorString != null) {
			behavior = (Activity) objects.get(behaviorString);
		}
	}
}
