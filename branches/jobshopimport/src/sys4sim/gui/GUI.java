package sys4sim.gui;


import java.io.File;

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

import sys4sim.internal_model.Model;
import sys4sim.xmi_import.Importer;

public class GUI {
	
	 private Display display; 
	 private Shell shell; 
	 private Label messageLabel;
	 private Button importButton;
	 private Button anylogicExportButton;
	 private FileDialog importFileDialog;
	 private Importer importer;
	 private Model model;
	 private FileDialog anylogicExportFileDialog;
	 private sys4sim.export.anylogic.Exporter anylogicExporter = new sys4sim.export.anylogic.Exporter();
	 
	 public GUI(){
		 display = new Display();
		 shell = new Shell(display);
		 importer = new Importer(this);
		 
		 messageLabel = new Label(shell, SWT.BORDER);
		 importFileDialog = new FileDialog(shell, SWT.OPEN);
		 importButton = new Button(shell, SWT.PUSH);
		 anylogicExportButton = new Button(shell, SWT.PUSH);
		 anylogicExportFileDialog = new FileDialog(shell, SWT.SAVE);
		 
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
		 importButton.setLocation(50, 100);
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
		anylogicExporter.writeFile(model, filepath);
	}
	
}

