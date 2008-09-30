package sys4sim.internal_model;

public class Worker extends Resource {
	private double speed = Double.POSITIVE_INFINITY;

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}
	
	public String toString() {
		return "Worker (" + this.getName() + ")";
	}
}
