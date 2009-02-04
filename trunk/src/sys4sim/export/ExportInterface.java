package sys4sim.export;

import java.io.File;

import sys4sim.export.anylogic.Settings;
import sys4sim.internal_model.Model;

public interface ExportInterface {
	public void writeFile(Model mode, File file, Settings set);
}


