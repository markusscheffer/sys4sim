package sys4sim.internal_model;

public class Storage extends ModelBlock {
	private double capacity = Double.POSITIVE_INFINITY;

	public double getCapacity() {
		return capacity;
	}

	public void setCapacity(double capacity) {
		this.capacity = capacity;
	}
}
