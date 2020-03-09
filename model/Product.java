package model;

import java.io.Serializable;

/**
 * @author Gruppe 1. Zia Mansoor, Oliver Wisborg, Johan Pedersen
 */

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.BiConsumer;

public class Product implements Observer, Serializable {

	private String name;
	private Set<PriceListLine> priceListLines = new LinkedHashSet<>();
	private ProductGroup productGroup;

	Product(String name, ProductGroup productGroup) {
		this.name = name;
		this.productGroup = productGroup;
	}

	public ProductGroup getProductGroup() {
		return productGroup;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LinkedHashSet<PriceListLine> getPriceListLines() {
		return new LinkedHashSet<>(priceListLines);
	}

	public void removePriceListLine(PriceListLine priceListLine) {
		if (priceListLines.contains(priceListLine)) {
			priceListLines.remove(priceListLine);
		}
	}

	public PriceListLine createPriceListLine(double price, PriceList priceList) {
		PriceListLine pll = new PriceListLine(price, priceList);
		priceListLines.add(pll);
		return pll;
	}

	@Override
	public String toString() {
		return name;
	}

	/**
	 * Updates the price or Discount for a priceListLine, by taking a method
	 * as @param
	 */
	@Override
	public void update(PriceList priceList, Object object, BiConsumer<PriceListLine, Object> myMethod) {

		Pre.require(object instanceof Discount || object instanceof Double);
		priceListLines.forEach(pll -> {
			if (pll.getPriceList().equals(priceList)) {
				Product.setUp(pll, object);
			}
		});
	}

	/**
	 * Sets the discount or price depending on if @param o is a Discount or not.
	 */
	public static void setUp(PriceListLine priceListLine, Object object) {
		if (object instanceof Discount) {
			Discount d = (Discount) object;
			priceListLine.setDiscount(d);
		} else {
			Double price = (Double) object;

			priceListLine.setPrice(price);
		}
	}

}
