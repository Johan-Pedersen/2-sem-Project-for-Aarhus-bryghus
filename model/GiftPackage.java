package model;

import java.io.Serializable;

public class GiftPackage extends Product implements Serializable {

	private int beerAmount;

	public GiftPackage(String name, ProductGroup productGroup, int beerAmount) {
		super(name, productGroup);
		this.beerAmount = beerAmount;
	}

	public int getBeerAmount() {
		return beerAmount;
	}

	public void setBeerAmount(int beerAmount) {
		this.beerAmount = beerAmount;
	}

	@Override
	public String toString() {
		return super.getName();
	}

}
