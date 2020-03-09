package model;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedHashSet;

/**
 * @author Gruppe 1. Zia Mansoor, Oliver Wisborg, Johan Pedersen
 */

public class CalcTour implements CalcBehavior, Serializable {

	/**
	 * Calculates the total price of an orderLine with a Tour object
	 * OrderLine.getAmount() >= 1
	 */
	@Override
	public double calc(PriceList priceList, OrderLine orderLine) {
		// TODO Auto-generated method stub

		LinkedHashSet<PriceListLine> plls = orderLine.getProduct().getPriceListLines();
		Iterator<PriceListLine> i = plls.iterator();

		double calcedPrice = 0;
		while (i.hasNext()) {
			PriceListLine pll = i.next();
			if (orderLine.getPrice() == 0) {
				calcedPrice += pll.getPrice() * orderLine.getAmount();
				if (orderLine.getStudentDiscount() != null) {
					calcedPrice -= calcedPrice * orderLine.getStudentDiscount().getPercentage();
				}
			} else {
				calcedPrice = orderLine.getPrice();
			}
		}

		return calcedPrice;
	}

}
