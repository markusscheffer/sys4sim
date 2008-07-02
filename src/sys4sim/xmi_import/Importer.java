package sys4sim.xmi_import;

import java.io.File;



import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.apache.xerces.jaxp.SAXParserImpl;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import org.xml.sax.helpers.DefaultHandler;


import sys4sim.gui.GUI;

public class Importer extends DefaultHandler{
	private GUI gui;
	
	public Importer(GUI gui) {
		this.gui = gui;
	}
	
	public void importSysML(File filepath) {
		gui.message("Importiere: " + filepath.toString() + "...");

		//get a factory
		SAXParserFactory spf = SAXParserFactory.newInstance();
		try {
		
			//get a new instance of parser
			SAXParser sp = spf.newSAXParser();
			
			//parse the file and also register this class for call backs
			sp.parse(filepath, this);
			
		}catch(SAXException se) {
			se.printStackTrace();
		}catch(ParserConfigurationException pce) {
			pce.printStackTrace();
		}catch (IOException ie) {
			ie.printStackTrace();
		}catch (FactoryConfigurationError fce) {
			fce.printStackTrace();
		}
		

		
	}
}
