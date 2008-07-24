package sys4sim.gui;


public class Main {
	
	private static GUI gui;

	public static void main (String [] args) {
			setGui(new GUI());

	
	}

	public static void setGui(GUI gui) {
		Main.gui = gui;
	}

	public static GUI getGui() {
		return gui;
	}
}
