package sys4sim.export.simcron;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Hashtable;


import sys4sim.export.ExportInterface;
import sys4sim.export.simcron.ExportObject.Type;
import sys4sim.internal_model.Connector;
import sys4sim.internal_model.Entity;
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
	private String mpFilePartOutput="";
	private String mpFileEndPartOutput="";
	private File file;
	
//	private enum Type {Process,Queue,Source, Sink};
	
	
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
		ExportSource eSo = null;
		
		Hashtable<String, ExportObject> mpFileTable = new Hashtable<String, ExportObject>();
		ArrayList<ExportProcess> ProcessList = new ArrayList<ExportProcess>();
		for (ModelElement element : model.getElements().values()){
			if(element.getClass().getName().equalsIgnoreCase("sys4sim.internal_model.Process")){
				String Processname="process"+(processCount);
				String name;
				element.setName(Processname);
				ExportProcess eP = new ExportProcess((Process)element,Processname,processCount,Type.Process);			
				mcFileOutput=mcFileOutput+eP.getMcString()+"\n";
				maFileOutput=maFileOutput+eP.getMaString()+"\n";
				Rate rate = (Rate) ((Process)element).getProcessingRate();
				name="distrib"+(rateCount);
			//	rate.setName(name);
				ExportRate eRate = new ExportRate(rate,name);
				mcFileOutput=mcFileOutput+eRate.getMcString()+"\n";
				maFileOutput=maFileOutput+eRate.getMaString()+"\n";
				eP.addRate(eRate);
				mpFileTable.put(Processname, eP);
				ProcessList.add(eP);
				rateCount++;
				processCount++;
			}
			if(element.getClass().getName().equalsIgnoreCase("sys4sim.internal_model.Queue")){
				String name="queue"+(queueCount);
				element.setName(name);
				ExportQueue eq= new ExportQueue((Queue)element,name,queueCount,Type.Queue);
				mcFileOutput=mcFileOutput+eq.getMcString()+"\n";
				maFileOutput=maFileOutput+eq.getMaString()+"\n";	
				mpFileTable.put(name, eq);
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
					et= new ExportTech((Connector)element,name,techCount,Type.Tech);
					mcFileOutput=mcFileOutput+et.getMcString()+"\n";
					maFileOutput=maFileOutput+et.getMaString()+"\n";
					
				}
				
				techCount++;
			}
			if(element.getClass().getName().equalsIgnoreCase("sys4sim.internal_model.Sink")){
				String name="sink"+(queueCount);
				element.setName(name);
				eSi= new ExportSink((Sink)element,name,queueCount,Type.Sink);	
				mcFileOutput=mcFileOutput+eSi.getMcString()+"\n";
				maFileOutput=maFileOutput+eSi.getMaString()+"\n";
				mpFileTable.put(name, eSi);
				queueCount++;
			}
			if(element.getClass().getName().equalsIgnoreCase("sys4sim.internal_model.Source")){
				String Sourcename="source"+(queueCount);
				String name;
				element.setName(Sourcename);
				eSo= new ExportSource((Source)element,Sourcename, queueCount, Type.Source);	
				mcFileOutput=mcFileOutput+eSo.getMcString()+"\n";
				maFileOutput=maFileOutput+eSo.getMaString()+"\n";
				for (Entity entity : ((Source) element).getEntities().keySet()) {
					Rate rate = (Rate) ((Source) element).getEntities().get(entity);
					name="distrib"+(rateCount);
				//	rate.setName(name);
					ExportRate eRate = new ExportRate(rate,name);
					mcFileOutput=mcFileOutput+eRate.getMcString()+"\n";
					maFileOutput=maFileOutput+eRate.getMaString()+"\n";
					name="job"+(entityCount);
					entity.setName(name);
					ExportEntity eJ= new ExportEntity((Entity)entity,name,entityCount);
					mcFileOutput=mcFileOutput+eJ.getMcString()+"\n";
					maFileOutput=maFileOutput+eJ.getMaString()+"\n";
					rateCount++;
					entityCount++;
					eSo.addEntity(eJ, eRate);
					mpFileTable.put(Sourcename, eSo);
		    	}
				queueCount++;
			}
			
			
		}
		
		// alles nötige für das mp-File berechen
		// hier wird die Technologieausgaben ermittelt
		String status ="sys4sim.internal_model.Source";
		String ModellObjectName  ="source3";
		int stepCount=1;
		for (Entity entity : model.getEntities()){
			ModellObjectName = entity.getSource().getName();
			ExportObject element = (ExportObject) mpFileTable.get(ModellObjectName);		
			mpFileOutput=mpFileOutput+ et.getMpString(ModellObjectName)+"\n";
			mpFilePartOutput=mpFilePartOutput+"tech1 pass "+stepCount+" spec 0 tag 0"+"\n";
			mpFileEndPartOutput=mpFileEndPartOutput+((ExportSource)element).getMpSourceString();
			stepCount++;
			element = mpFileTable.get(element.getInternalObjekt().getOut().get(0).getTarget().getName());
			while (status!=null){
				ModellObjectName = element.getInternalObjekt().getName();
				if (element.getType()==Type.Process) {
					mpFileOutput=mpFileOutput+ et.getMpString(ModellObjectName)+"\n";
					mpFilePartOutput=mpFilePartOutput+"tech1 pass "+stepCount+" spec 0 time "+
					((ExportProcess)element).getMpString()+"\n";
					stepCount++;
					element = mpFileTable.get(element.getInternalObjekt().getOut().get(0).getTarget().getName());
					
				} else			
					if (element.getType()==Type.Sink) {
						mpFileOutput=mpFileOutput+ et.getMpString(ModellObjectName)+"\n";
						mpFilePartOutput=mpFilePartOutput+"tech1 pass "+stepCount+" spec 0 tag 0"+"\n";
						//in der Sink befindet sich nur so viele Jobs wie die Capazität es zulässt der 
						//Rest wird gekillt (Bei Sink Kapazität =1)
						mpFilePartOutput=mpFilePartOutput+((ExportSink)element).getName()+" kill 1\n";
						stepCount++;
						status=null;
					} else {
						mpFileOutput=mpFileOutput+ et.getMpString(ModellObjectName)+"\n";
						mpFilePartOutput=mpFilePartOutput+"tech1 pass "+stepCount+" spec 0 tag 0"+"\n";
						stepCount++;
						element = mpFileTable.get(element.getInternalObjekt().getOut().get(0).getTarget().getName());
						
					}
				
				
			}
		}

		mpFileOutput=mpFileOutput+mpFilePartOutput;

		for (ExportEntity entity : eSo.getEntityTable().keySet()) {
			ExportRate rate = (ExportRate) eSo.getEntityTable().get(entity);
			mpFileOutput=mpFileOutput+ rate.getMpString()+"\n";
			mpFileOutput=mpFileOutput+ entity.getMpString()+"\n";
			mpFileOutput=mpFileOutput+ eSo.getMpSampleString(rate.getName())+"\n";
			mpFileOutput=mpFileOutput+ eSo.getMpSourceString()+"\n";		
		}
		
		for (ExportProcess process : ProcessList) {
			mpFileOutput=mpFileOutput+process.getMpDistrString()+"\n";
		}
		
		
		writeMcFile();
		writeMaFile();
		writeMpFile();
		
		
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
	
	private void writeMpFile() {
		try {
			PrintWriter pw = new PrintWriter(new FileWriter(file+".mp"));
			pw.println("stop inf");
			pw.println("watch 0");
			pw.print(mpFileOutput);
			pw.println("cost100 name Zykluszeit");
			pw.println("namespace eval ::cost100 {");
			pw.println("variable this cost100}");
			pw.println("cost100 type makespan");
			pw.println("cost100 all 1");
			pw.println("cost100 name Durchlaufzeit");
			pw.println("namespace eval ::cost101 {");
			pw.println("variable this cost101}");
			pw.println("cost101 type jobtimeavrg");
			pw.println("cost101 all 1");
			pw.println("cost102 name Liegezeit");
			pw.println("namespace eval ::cost102 {");
			pw.println("variable this cost102}");
			pw.println("cost102 type jobwaitavrgon");
			pw.println("cost102 all 1");
			pw.println("cost103 name Verspätung");
			pw.println("namespace eval ::cost103 {");
			pw.println("variable this cost103}");
			pw.println("cost103 type datelate");
			pw.println("cost103 all 1");
			pw.println("cost104 name Auslastung");
			pw.println("namespace eval ::cost104 {");
			pw.println("variable this cost104}");
			pw.println("cost104 type machworkrateon");
			pw.println("cost104 all 1");
			pw.println("cost105 name Bestand");
			pw.println("namespace eval ::cost105 {");
			pw.println("variable this cost105}");
			pw.println("cost105 type contavrg");
			pw.println("cost105 all 1");
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
