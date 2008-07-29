package sys4sim.internal_model;

import java.util.Hashtable;

public class Process extends ModelBlock {
	private Hashtable<ResourcePool, Integer> resourcePools = new Hashtable<ResourcePool, Integer>();
	private int capacity;
	private Rate failureRate;
	
	public Hashtable<ResourcePool, Integer> getResourcePools() {
		return resourcePools;
	}
	public void setResourcePools(Hashtable<ResourcePool, Integer> resourcePools) {
		this.resourcePools = resourcePools;
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
