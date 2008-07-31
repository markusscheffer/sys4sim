package sys4sim.internal_model;

public class PoissonDistribution extends Distribution {
	double expectedValue;
	String expectedUnit;
	
	public double getExpectedValue() {
		return expectedValue;
	}
	public void setExpectedValue(double expectedValue) {
		this.expectedValue = expectedValue;
	}
	public String getExpectedUnit() {
		return expectedUnit;
	}
	public void setExpectedUnit(String expectedUnit) {
		this.expectedUnit = expectedUnit;
	}
	
	public String toString() {
		return ("Poisson Distribution (Mean: " + getExpectedValue() + getExpectedUnit() +")");
	}
	
}
