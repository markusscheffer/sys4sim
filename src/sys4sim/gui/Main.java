package sys4sim.gui;

import javax.xml.parsers.FactoryConfigurationError;
import org.apache.xerces.jaxp.SAXParserImpl;

public class Main {
	
	private static GUI gui;

	public static void main (String [] args) {
		try {
			System.setProperty("javax.xml.parsers.SAXParserFactory","org.apache.xerces.jaxp.SAXParserFactoryImpl");
			gui = new GUI();
		} catch (FactoryConfigurationError fce) {
			System.out.println(fce.getMessage());
			fce.printStackTrace();
		}

	
	}
}
