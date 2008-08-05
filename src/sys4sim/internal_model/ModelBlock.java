package sys4sim.internal_model;

import java.util.ArrayList;

public class ModelBlock extends ModelElement {
	private ArrayList<Connector> in = new ArrayList<Connector>();
	private ArrayList<Connector> out = new ArrayList<Connector>();
	private String onEnter = "";
	private String onExit = "";
	private int priority = 0;
	private boolean isFork = false;
	private int x, y = 0;
	
	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public boolean isFork() {
		return isFork;
	}

	public void setFork(boolean isFork) {
		this.isFork = isFork;
	}

	public String getOnEnter() {
		return onEnter;
	}

	public void setOnEnter(String onEnter) {
		this.onEnter = onEnter;
	}

	public String getOnExit() {
		return onExit;
	}

	public void setOnExit(String onExit) {
		this.onExit = onExit;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public boolean hasIn() {
		return true;
	}
	
	public boolean hasOut() {
		return true;
	}

	public ArrayList<Connector> getIn() {
		return in;
	}

	public void setIn(ArrayList<Connector> in) {
		this.in = in;
	}

	public ArrayList<Connector> getOut() {
		return out;
	}

	public void setOut(ArrayList<Connector> out) {
		this.out = out;
	}
}
