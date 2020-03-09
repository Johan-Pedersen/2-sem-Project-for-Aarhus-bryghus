package model;

import java.io.Serializable;

/**
 * @author Gruppe 1. Zia Mansoor, Oliver Wisborg, Johan Pedersen
 */

public class Discount implements Serializable {

	private double percentage;

	public Discount(double percentage) {
		setPercentage(percentage);
	}

	public double getPercentage() {
		return percentage;
	}

	/**
	 * Sets a double between 0 and 100 and divides the number by 100
	 * @Pre percentage must be a double between 0 and 100
	 */
	public void setPercentage(double percentage) {
		Pre.require(percentage > 0 && percentage <= 100);
		this.percentage = percentage / 100;
	}

	@Override
	public String toString() {
		return "Discount [percentage=" + percentage + "]";
	}
}
