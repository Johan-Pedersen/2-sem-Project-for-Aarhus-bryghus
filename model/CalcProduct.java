package model;

import java.io.Serializable;

/**
 *@author  Gruppe 1. Zia Mansoor, Oliver Wisborg, Johan Pedersen
 */

import java.util.Iterator;
import java.util.LinkedHashSet;

public class CalcProduct implements CalcBehavior, Serializable {

	/**
	 * Calculates the total price of an orderLine with discount. If the OrderLine
	 * has a price already decided, then it will just return the decided price.
	 */

	@Override
	public double calc(PriceList priceList, OrderLine orderLine) {
		double calcedPrice = 0;
		LinkedHashSet<PriceListLine> plls = orderLine.getProduct().getPriceListLines();
		ProductGroup productGroup = orderLine.getProduct().getProductGroup();
		Iterator<PriceListLine> i = plls.iterator();

		if (orderLine.getPrice() == 0) {
			while (i.hasNext()) {
				PriceListLine pll = i.next();
				if (priceList.equals(pll.getPriceList())) {
					calcedPrice = pll.getPrice() * orderLine.getAmount();

					if (pll.getDiscount() != null) {
						calcedPrice -= calcedPrice * pll.getDiscount().getPercentage();
					}

					if (productGroup.getContainerDeposit() > 0) {
						calcedPrice += productGroup.getContainerDeposit() * orderLine.getAmount();
					}
				}
			}
		} else {
			calcedPrice = orderLine.getPrice();
		}

		return calcedPrice;
	}

}
