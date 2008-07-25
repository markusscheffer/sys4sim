package sys4sim.export.simcron;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;


import sys4sim.export.ExportInterface;
import sys4sim.internal_model.Connector;
import sys4sim.internal_model.Machine;
import sys4sim.internal_model.Model;
import sys4sim.internal_model.ModelElement;
import sys4sim.internal_model.Queue;
import sys4sim.internal_model.Sink;
import sys4sim.internal_model.Source;

public class ExportToSimcron implements ExportInterface{

	private String mcFileOutput="";
	private String mpFileOutput="";
	private String maFileOutput="";
	private File file;
	
	public void writeFile(Model model, File file) {
		this.file=file;
		int machineCount=1;
		int queueCount=1;
		int connectorCount=1;
		int rateCount=1;
		int jobCount=1;

		ExportTech et = null;
		ExportSink eSi = null;
		
		for (ModelElement element : model.getElements().values()){
			if(element.getClass().getName().equalsIgnoreCase("sys4sim.internal_model.Machine")){
				ExportMachine em = new ExportMachine((Machine)element,machineCount);			
				mcFileOutput=mcFileOutput+em.getMcString()+"\n";
				maFileOutput=maFileOutput+em.getMaString()+"\n";
				machineCount++;
			}
			if(element.getClass().getName().equalsIgnoreCase("sys4sim.internal_model.Queue")){
				ExportQueue eq= new ExportQueue((Queue)element,queueCount);
				mcFileOutput=mcFileOutput+eq.getMcString()+"\n";
				maFileOutput=maFileOutput+eq.getMaString()+"\n";	
				queueCount++;
			}
			if(element.getClass().getName().equalsIgnoreCase("sys4sim.internal_model.Connector")){
				
				if (connectorCount>1) {
			//	mcFileOutput=mcFileOutput+et.getMcString()+"\n";
			//	maFileOutput=maFileOutput+et.getMaString()+"\n";
				}
				else {
					et= new ExportTech((Connector)element,connectorCount);
					mcFileOutput=mcFileOutput+et.getMcString()+"\n";
					maFileOutput=maFileOutput+et.getMaString()+"\n";
				//	mpFileOutput=mpFileOutput+et.getMpString(eSi.getName())+"\n";
				}
				connectorCount++;
			}
			if(element.getClass().getName().equalsIgnoreCase("sys4sim.internal_model.Sink")){
				eSi= new ExportSink((Sink)element,queueCount);	
				mcFileOutput=mcFileOutput+eSi.getMcString()+"\n";
				maFileOutput=maFileOutput+eSi.getMaString()+"\n";
				queueCount++;
			}
			if(element.getClass().getName().equalsIgnoreCase("sys4sim.internal_model.Source")){
				ExportSource eSo= new ExportSource((Source)element,queueCount);	
				mcFileOutput=mcFileOutput+eSo.getMcString()+"\n";
				maFileOutput=maFileOutput+eSo.getMaString()+"\n";
				ExportJob eJ= new ExportJob((Source)element,jobCount);	
				mcFileOutput=mcFileOutput+eJ.getMcString()+"\n";
				maFileOutput=maFileOutput+eJ.getMaString()+"\n";
				mpFileOutput=mpFileOutput+eJ.getMpLeaveString()+"\n";
				if (eSo.getContainsRate()) {
					ExportRate eR= new ExportRate(((Source)element).getRate(),rateCount);	
					mcFileOutput=mcFileOutput+eR.getMcString()+"\n";
					maFileOutput=maFileOutput+eR.getMaString()+"\n";
					mpFileOutput=mpFileOutput+eR.getMpString()+"\n";
					eSo.setRateName(eR.getName());
					rateCount++;
				}
				mpFileOutput=mpFileOutput+eJ.getMpSampleString()+"\n";
				mpFileOutput=mpFileOutput+eSo.getMpString()+"\n";
				jobCount++;
				queueCount++;
			}
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
