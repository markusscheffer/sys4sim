package sys4sim.export.simcron;

import java.util.ArrayList;
import java.util.Hashtable;

import sys4sim.internal_model.Connector;
import sys4sim.internal_model.ModelElement;

public class ExportTech {
	
	private Hashtable<String, ModelElement> connectorTable;
	private int count;
	private String name;
	
	public ExportTech(Connector connector,String name, int count){
		connectorTable = new Hashtable<String, ModelElement>();
		String s = "connector_0";
		connectorTable.put(s,connector);
		this.count=count;
		this.name=name;
	}
	
	public String getMcString(){
		return "tech create "+name;
	}
	
	public String getMaString(){
		return "appear "+name+" 0 0";
	}
	
	public String getMpString(String name){
		String result="";
		result = "tech"+count+" pass"+" append "+name;
		

		
		return result;
	}
	
	public void addConnector(Connector conn, int connectorCount){
		String s = "connector_"+connectorCount;
		connectorTable.put(s,conn);
	}
	
	public Hashtable<String, ModelElement> getconnectorTable() {
		return connectorTable;
	}
}
