package sys4sim.export.simcron;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;


import sys4sim.export.ExportInterface;
import sys4sim.internal_model.Connector;
import sys4sim.internal_model.Entity;
import sys4sim.internal_model.Machine;
import sys4sim.internal_model.Model;
import sys4sim.internal_model.ModelElement;
import sys4sim.internal_model.Queue;
import sys4sim.internal_model.Process;
import sys4sim.internal_model.Rate;
import sys4sim.internal_model.Sink;
import sys4sim.internal_model.Source;

public class ExportToSimcron implements ExportInterface{

	private String mcFileOutput="";
	private String mpFileOutput="";
	private String maFileOutput="";
	private File file;
	
	private ArrayList aList = new ArrayList();
	
	public void writeFile(Model model, File file) {
		this.file=file;
		int processCount=1;
		int queueCount=1;
		int connectorCount=1;
		int techCount=1;
		int rateCount=1;
		int entityCount=1;

		ExportTech et = null;
		ExportSink eSi = null;
		
		for (ModelElement element : model.getElements().values()){
			if(element.getClass().getName().equalsIgnoreCase("sys4sim.internal_model.Process")){
				String name="process"+(processCount);
				element.setName(name);
				ExportProcess eP = new ExportProcess((Process)element,name,processCount);			
				mcFileOutput=mcFileOutput+eP.getMcString()+"\n";
				maFileOutput=maFileOutput+eP.getMaString()+"\n";
				processCount++;
			}
			if(element.getClass().getName().equalsIgnoreCase("sys4sim.internal_model.Queue")){
				String name="queue"+(queueCount);
				element.setName(name);
				ExportQueue eq= new ExportQueue((Queue)element,name,queueCount);
				mcFileOutput=mcFileOutput+eq.getMcString()+"\n";
				maFileOutput=maFileOutput+eq.getMaString()+"\n";	
				queueCount++;
			}
			if(element.getClass().getName().equalsIgnoreCase("sys4sim.internal_model.Connector")){
				
				if (techCount>1) {
					et.addConnector((Connector)element, connectorCount);
					connectorCount++;
			//	mcFileOutput=mcFileOutput+et.getMcString()+"\n";
			//	maFileOutput=maFileOutput+et.getMaString()+"\n";
				}
				else {
					String name="tech"+(techCount);
					element.setName(name);
					et= new ExportTech((Connector)element,name,techCount);
					mcFileOutput=mcFileOutput+et.getMcString()+"\n";
					maFileOutput=maFileOutput+et.getMaString()+"\n";
					aList.add(et);
				}
				
				techCount++;
			}
			if(element.getClass().getName().equalsIgnoreCase("sys4sim.internal_model.Sink")){
				String name="sink"+(queueCount);
				element.setName(name);
				eSi= new ExportSink((Sink)element,name,queueCount);	
				mcFileOutput=mcFileOutput+eSi.getMcString()+"\n";
				maFileOutput=maFileOutput+eSi.getMaString()+"\n";
				queueCount++;
			}
			if(element.getClass().getName().equalsIgnoreCase("sys4sim.internal_model.Source")){
				String name="source"+(queueCount);
				element.setName(name);
				ExportSource eSo= new ExportSource((Source)element,name, queueCount);	
				mcFileOutput=mcFileOutput+eSo.getMcString()+"\n";
				maFileOutput=maFileOutput+eSo.getMaString()+"\n";
				for (Entity entity : ((Source) element).getEntities().keySet()) {
					Rate rate = (Rate) ((Source) element).getEntities().get(entity);
					name="distrib"+(rateCount);
				//	rate.setName(name);
					ExportRate eRate = new ExportRate(rate,name,rateCount);
					mcFileOutput=mcFileOutput+eRate.getMcString()+"\n";
					maFileOutput=maFileOutput+eRate.getMaString()+"\n";
					name="job"+(entityCount);
					entity.setName(name);
					ExportEntity eJ= new ExportEntity((Entity)entity,name,entityCount);
					mcFileOutput=mcFileOutput+eJ.getMcString()+"\n";
					maFileOutput=maFileOutput+eJ.getMaString()+"\n";
					rateCount++;
					entityCount++;
		    	}
				aList.add(eSo);
				queueCount++;
			}
			
			
		}
		int i=0;
		while (i<aList.size()){
			ExportTech tech1 = (ExportTech) aList.get(i);
			Hashtable<String, ModelElement> table = (Hashtable<String, ModelElement>) tech1.getconnectorTable();
			int j =0;
			String status ="sys4sim.internal_model.Source";
			while (status!=null){
				
				boolean gefunden=false;
				int conValue=0;
				
				while(gefunden==false){
				 String s="connector_"+conValue;
				 Connector conn = (Connector) table.get(s);
				  if (conn.getSource().getClass().getName().equalsIgnoreCase(status)){
						String st= tech1.getMpString(conn.getSource().getName());
						System.out.println(st);
						status=conn.getTarget().getClass().getName();
						if (conn.getTarget().getClass().getName().equalsIgnoreCase("sys4sim.internal_model.Sink")){
							st= tech1.getMpString(conn.getTarget().getName());
							System.out.println(st);
							status=null;
						}
					    gefunden=true;
				 } else{
					 
				 }
				  conValue++;
				 
				}
			
			
			//	System.out.println(test);
		//		Connector conn = (Connector) table.get(s);
				j++;
				
			}
		    i++;
		}
		
		writeMcFile();
		writeMaFile();
		
		
	}

	private void writeMaFile() {
		try {
			PrintWriter pw = new PrintWriter(new FileWriter(file+".ma"));
			pw.println(maFileOutput);
			pw.flush();
			pw.close();
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void writeMcFile() {
		try {
			PrintWriter pw = new PrintWriter(new FileWriter(file+".mc"));
			pw.println("RC {}");
			pw.print(mcFileOutput);
			pw.println("cost create cost100");
			pw.println("cost create cost101");
			pw.println("cost create cost102");
			pw.println("cost create cost103");
			pw.println("cost create cost104");
			pw.println("cost create cost105");
			pw.flush();
			pw.close();
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
