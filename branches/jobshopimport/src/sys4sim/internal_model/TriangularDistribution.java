package sys4sim.internal_model;

public class TriangularDistribution extends Distribution {
	double minValue;
	String minUnit;
	double modeValue;
	String modeUnit;
	double maxValue;
	String maxUnit;
	
	public double getMinValue() {
		return minValue;
	}
	public void setMinValue(double minValue) {
		this.minValue = minValue;
	}
	public String getMinUnit() {
		return minUnit;
	}
	public void setMinUnit(String minUnit) {
		this.minUnit = minUnit;
	}
	public double getModeValue() {
		return modeValue;
	}
	public void setModeValue(double modeValue) {
		this.modeValue = modeValue;
	}
	public String getModeUnit() {
		return modeUnit;
	}
	public void setModeUnit(String modeUnit) {
		this.modeUnit = modeUnit;
	}
	public double getMaxValue() {
		return maxValue;
	}
	public void setMaxValue(double maxValue) {
		this.maxValue = maxValue;
	}
	public String getMaxUnit() {
		return maxUnit;
	}
	public void setMaxUnit(String maxUnit) {
		this.maxUnit = maxUnit;
	}
	
	
}
