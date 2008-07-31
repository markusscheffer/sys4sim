package sys4sim.export;

import java.io.File;

import sys4sim.internal_model.Model;

public interface ExportInterface {
	public void writeFile(Model model, File file);
}


