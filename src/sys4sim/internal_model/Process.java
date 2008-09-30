package sys4sim.internal_model;

import java.util.Hashtable;

public class Process extends ModelBlock {
	private Hashtable<ResourcePool, Integer> resourcePools = new Hashtable<ResourcePool, Integer>();
	private int capacity;
	private Rate failureRate;
	private Rate processingRate;
	public Rate getProcessingRate() {
		return processingRate;
	}
	public void setProcessingRate(Rate processingRate) {
		this.processingRate = processingRate;
	}
	public Rate getSetUpRate() {
		return setUpRate;
	}
	public void setSetUpRate(Rate setUpRate) {
		this.setUpRate = setUpRate;
	}
	private Rate setUpRate;
	
	public Hashtable<ResourcePool, Integer> getResourcePools() {
		return resourcePools;
	}
	public void setResourcePools(Hashtable<ResourcePool, Integer> pools) {
		 resourcePools = pools;
	}
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
