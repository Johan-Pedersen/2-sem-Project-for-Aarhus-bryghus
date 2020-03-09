package model;

import java.io.Serializable;

/**
 * @author Gruppe 1. Zia Mansoor, Oliver Wisborg, Johan Pedersen
 */

public class PriceListLine implements Serializable {

	private double price;
	private PriceList priceList;
	private Discount discount;

	PriceListLine(double price, PriceList priceList) {
		this.price = price;
		this.priceList = priceList;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public PriceList getPriceList() {
		return priceList;
	}

	public void setPriceList(PriceList priceList) {
		this.priceList = priceList;
	}

	public Discount getDiscount() {
		return discount;
	}

	public void setDiscount(Discount discount) {
		if (this.discount != discount)
			this.discount = discount;
	}

	public void setDiscountNull() {
		if (this.discount != null)
			this.discount = null;
	}

	@Override
	public String toString() {
		return price + " " + priceList;
	}

}
