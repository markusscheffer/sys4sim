package sys4sim.internal_model;

import java.util.Hashtable;

public class Source extends ModelBlock{
	private int batchSize = 1;
	private int capacity;
	private Hashtable<Entity, Rate> entities = new Hashtable<Entity, Rate>();


	public Hashtable<Entity, Rate> getEntities() {
		return entities;
	}


	public void setEntities(Hashtable<Entity, Rate> entities) {
		this.entities = entities;
	}


	public int getBatchSize() {
		return batchSize;
	}


	public void setBatchSize(int batchSize) {
		this.batchSize = batchSize;
	}


	public int getCapacity() {
		return capacity;
	}


	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}


	public boolean hasIn () {
		return false;
	}
	
	
}
