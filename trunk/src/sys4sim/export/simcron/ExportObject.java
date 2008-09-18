package sys4sim.export.simcron;

import sys4sim.internal_model.ModelBlock;

public abstract class ExportObject {
	enum Type {Process,Queue,Source, Sink, Tech};
	
	abstract Type  getType();
	
	abstract ModelBlock getInternalObjekt();
	

}
