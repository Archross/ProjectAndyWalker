package chula.reminder;

import java.util.Date;

public class Factor {
	private boolean isLate,isNow;

	public Factor(boolean isLate, boolean isNow) {
		super();
		this.isLate = isLate;
		this.isNow = isNow;
	}

	public boolean isLate() {
		return isLate;
	}

	public void setLate(boolean isLate) {
		this.isLate = isLate;
	}

	public boolean isNow() {
		return isNow;
	}

	public void setNow(boolean isNow) {
		this.isNow = isNow;
	}
	
}