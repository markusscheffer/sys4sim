package sys4sim.export;

import java.io.File;

import sys4sim.export.anylogic.Settings;
import sys4sim.internal_model.Model;

public interface ExportSimcronInterface {
	public void writeFile(Model model, File file);
}