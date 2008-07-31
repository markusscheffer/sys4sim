package sys4sim.internal_model;

public class Stock extends ModelBlock {
	private int capacity;
	private Rate failureRate;
	
	public int getCapacity() {
		return capacity;
	}
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	public Rate getFailureRate() {
		return failureRate;
	}
	public void setFailureRate(Rate failureRate) {
		this.failureRate = failureRate;
	}
	
	
}
