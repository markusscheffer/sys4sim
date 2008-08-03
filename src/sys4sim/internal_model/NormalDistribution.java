package sys4sim.internal_model;

public class NormalDistribution extends Distribution {
	double meanValue;
	String meanUnit;
	double standardDeviationValue;
	String standardDeviationUnit;
	
	public double getMeanValue() {
		return meanValue;
	}
	public void setMeanValue(double meanValue) {
		this.meanValue = meanValue;
	}
	public String getMeanUnit() {
		return meanUnit;
	}
	public void setMeanUnit(String meanUnit) {
		this.meanUnit = meanUnit;
	}
	public double getStandardDeviationValue() {
		return standardDeviationValue;
	}
	public void setStandardDeviationValue(double standardDeviationValue) {
		this.standardDeviationValue = standardDeviationValue;
	}
	public String getStandardDeviationUnit() {
		return standardDeviationUnit;
	}
	public void setStandardDeviationUnit(String standardDeviationUnit) {
		this.standardDeviationUnit = standardDeviationUnit;
	}
	public String toString () {
		return "Normal [" + getMeanValue() + getMeanUnit() + "±" + 
		getStandardDeviationValue() + getStandardDeviationUnit() + "]";
	}
}
