package sys4sim.gui;


import java.io.File;

import javax.swing.ButtonGroup;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import sys4sim.export.anylogic.Settings;
import sys4sim.internal_model.Model;
import sys4sim.xmi_import.Importer;

public class GUI {
	
	 private Display display; 
	 private Shell shell; 
	 private Label messageLabel;
	 private Button importButton;
	 private Button anylogicExportButton;	
	 private Button anylogicChoiceButton;
	 private Button anylogicChoiceButton2;
	 private Button anylogicChoiceButton3;
	 private Button anylogicDelayButton;
	 private Button simcronExportButton;
	 private Settings set = new Settings();
	 private FileDialog importFileDialog;
	 private Importer importer;
	 private Model model;
	 private FileDialog anylogicExportFileDialog;
	 private FileDialog simcronExportFileDialog;
	 private sys4sim.export.anylogic.Exporter anylogicExporter = new sys4sim.export.anylogic.Exporter();
	 
	 private sys4sim.export.simcron.ExportToSimcron simcronExporter = new sys4sim.export.simcron.ExportToSimcron();
	 
	 public GUI(){
		 display = new Display();
		 shell = new Shell(display);
		 importer = new Importer(this);
		 
		 messageLabel = new Label(shell, SWT.BORDER);
		 importFileDialog = new FileDialog(shell, SWT.OPEN);
		 importButton = new Button(shell, SWT.PUSH);
		 anylogicExportButton = new Button(shell, SWT.PUSH);
		 simcronExportButton = new Button(shell, SWT.PUSH);
		 anylogicExportFileDialog = new FileDialog(shell, SWT.SAVE);
		 simcronExportFileDialog = new FileDialog(shell, SWT.SAVE);
		 
		 
		 anylogicChoiceButton = new Button(shell, SWT.CHECK);
		 anylogicChoiceButton2 = new Button(shell, SWT.CHECK);
		 anylogicChoiceButton3 = new Button(shell, SWT.CHECK);
		 anylogicDelayButton = new Button(shell, SWT.CHECK);
		 
		 shell.setText("SysML Simulation Converter");
		 shell.setSize(800,400);
		 shell.setLocation(200, 150);

		 shell.addListener(13, new Listener() {
			 public void handleEvent(Event event) {
			 }});		 
		 messageLabel.setText("Bitte importieren Sie ein SysML-Modell.");
		 messageLabel.setSize(400, 20);
		 messageLabel.setLocation(180, 300);
		 messageLabel.setBackground(new Color(display, 255, 255, 255));

		 importButton.setText("SysML-Modell importieren...");
		 importButton.setSize(220,30);
		 importButton.setLocation(200, 50);
		 importButton.addSelectionListener(new SelectionAdapter() { 
			  public void widgetSelected(SelectionEvent e) { 
				   importSysML(new File(importFileDialog.open())); 
				  }
		 });
		 
		 anylogicExportButton.setText("Anylogic exportieren...");
		 anylogicExportButton.setSize(220,30);
		 anylogicExportButton.setLocation(350, 100);
		 anylogicExportButton.addSelectionListener(new SelectionAdapter() { 
			  public void widgetSelected(SelectionEvent e) { 
				   exportAnylogic(new File(anylogicExportFileDialog.open())); 
				  }
		 });
		 
		 simcronExportButton.setText("Simcron exportieren...");
		 simcronExportButton.setSize(220,30);
		 simcronExportButton.setLocation(50, 100);
		 simcronExportButton.addSelectionListener(new SelectionAdapter() { 
			  public void widgetSelected(SelectionEvent e) { 
				   exportSimcron(new File(simcronExportFileDialog.open())); 
				  }
		 });
		 
		 anylogicChoiceButton.setText("original");
		 anylogicChoiceButton.setSize(60,30);
		 anylogicChoiceButton.setLocation(350, 150);
		 anylogicChoiceButton.addSelectionListener(new SelectionAdapter() { 
			  public void widgetSelected(SelectionEvent e) { 
				  int choice=1;
				  anylogicChoiceSet(choice); 
				  }
		 });
		 
		 anylogicChoiceButton2.setText("high buffers");
		 anylogicChoiceButton2.setSize(80,30);
		 anylogicChoiceButton2.setLocation(420, 150);
		 anylogicChoiceButton2.addSelectionListener(new SelectionAdapter() { 
			  public void widgetSelected(SelectionEvent e) { 
				  int choice=2;
				  anylogicChoiceSet(choice); 
				  }
		 });
		 
		 anylogicChoiceButton3.setText("port protection");
		 anylogicChoiceButton3.setSize(100,30);
		 anylogicChoiceButton3.setLocation(500, 150);
		 anylogicChoiceButton3.addSelectionListener(new SelectionAdapter() { 
			  public void widgetSelected(SelectionEvent e) { 
				  int choice=3;
				  anylogicChoiceSet(choice); 
				  }
		 });
		 
		 anylogicDelayButton.setText("delay mode");
		 anylogicDelayButton.setSize(100,30);
		 anylogicDelayButton.setLocation(350, 180);
		 anylogicDelayButton.addSelectionListener(new SelectionAdapter() { 
			  public void widgetSelected(SelectionEvent e) { 
				  boolean choice=true;
				  anylogicDelayModeSet(choice); 
				  }
		 });
		 
		 shell.open ();
		 while (!shell.isDisposed()) {
			 if (!display.readAndDispatch())
				 display.sleep ();
		 }
		 display.dispose ();	
	 }


	public void message(String messageString){
		messageLabel.setText(messageString);
	}
	
	public void setImporter(Importer importer) {
		this.importer = importer;
	}
	
	public void importSysML(File filepath) {
		model = importer.importSysML(filepath);
	}
	
	public void exportAnylogic(File filepath) {
		
		anylogicExporter.writeFile(model, filepath, set);
	}
	
	
    public void exportSimcron(File filepath) {
		
		simcronExporter.writeFile(model, filepath, set);
	}
	
	public void anylogicChoiceSet(int choice) {
		
		set.setPortChoice(choice);
	}
	public void anylogicDelayModeSet (boolean choice){
		
		set.setDelayMode(choice);
	}
	
}

