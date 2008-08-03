package sys4sim.internal_model;


public class ExponentialDistribution extends Distribution {
	double meanValue;
	String meanUnit;
	
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
}
