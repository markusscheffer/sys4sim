package sys4sim.internal_model;

public class Rate {
	Distribution distribution;

	public Rate(Distribution distribution) {
		this.distribution = distribution;
	}
	
	public Distribution getDistribution() {
		return distribution;
	}

	public void setDistribution(Distribution distribution) {
		this.distribution = distribution;
	}
}
