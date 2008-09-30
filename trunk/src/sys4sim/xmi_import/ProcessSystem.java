package sys4sim.xmi_import;

import java.util.Hashtable;

import sys4sim.internal_model.ResourcePool;

public class ProcessSystem {
	private Hashtable<ResourcePool, Integer> resourcePools = new Hashtable<ResourcePool, Integer>();
	String id;
	private String name;
	
	public ProcessSystem(String id) {
		super();
		this.id = id;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Hashtable<ResourcePool, Integer> getResourcePools() {
		return resourcePools;
	}

	public void setResourcePools(Hashtable<ResourcePool, Integer> resourcePools) {
		this.resourcePools = resourcePools;
	}

}
