package sys4sim.internal_model;

public class Transporter extends Resource {
	private double capacity = Double.POSITIVE_INFINITY;
	private double speed = Double.POSITIVE_INFINITY;
	
	public double getCapacity() {
		return capacity;
	}
	public void setCapacity(double capacity) {
		this.capacity = capacity;
	}
	public double getSpeed() {
		return speed;
	}
	public void setSpeed(double speed) {
		this.speed = speed;
	}
	
	
	public String toString() {
		return "Transporter (" + this.getName() + ")";
	}
}
