package model;

import java.io.Serializable;

/**
 * @author Gruppe 1. Zia Mansoor, Oliver Wisborg, Johan Pedersen
 */

public class OrderLine implements Serializable {

	private double price;
	/**
	 * counts for the amount of products or the number of people on a tour
	 */
	private int amount;
	private Product product;
	private CalcBehavior calcBehavior;
	private int tempKegSize;
	private Discount studentDiscount;
	private PriceList priceList;

	OrderLine(int amount, CalcBehavior calcBehavior, Product product, PriceList priceList) {
		setAmount(amount);
		this.calcBehavior = calcBehavior;
		this.product = product;
		this.priceList = priceList;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		Pre.require(amount >= 1);
		this.amount = amount;
	}

	public Product getProduct() {
		return product;
	}

	public PriceList getPriceList() {
		return priceList;
	}

	public void setTempKegSize(int size) {
		tempKegSize = size;
	}

	public int getTempKegSize() {
		return tempKegSize;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public CalcBehavior getCalcBehavior() {
		return calcBehavior;
	}

	public Discount getStudentDiscount() {
		return studentDiscount;
	}

	public void setStudentDiscount(Discount studentDiscount) {
		this.studentDiscount = studentDiscount;
	}

	/**
	 * This method uses a strategy pattern to properly calculate the orderLine.
	 * Therefore the calcBehavior must set wisely. 
	 * @return The sum of this orderLine
	 */

	public double performCalc(PriceList priceList) {
		return calcBehavior.calc(priceList, this);
	}

	@Override
	public String toString() {
		String normalPrice = null;
		if (price == 0) {
			for (PriceListLine pll : product.getPriceListLines()) {
				if (pll.getPriceList().equals(priceList)) {
					normalPrice = "" + pll.getPrice() * amount;
				}
			}
		} else
			normalPrice = "" + price;

		return product + ", Antal: " + amount + ", Pris: " + normalPrice;
	}

}
