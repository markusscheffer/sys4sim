package sys4sim.internal_model;

public class ModelBlock extends ModelElement {
	private Connector in;
	private Connector out;
	private Rate rate;
	
	public Rate getRate() {
		return rate;
	}

	public void setRate(Rate rate) {
		this.rate = rate;
	}

	public boolean hasIn() {
		return true;
	}
	
	public boolean hasOut() {
		return true;
	}

	public Connector getIn() {
		return in;
	}

	public void setIn(Connector in) {
		this.in = in;
	}

	public Connector getOut() {
		return out;
	}

	public void setOut(Connector out) {
		this.out = out;
	}
}
