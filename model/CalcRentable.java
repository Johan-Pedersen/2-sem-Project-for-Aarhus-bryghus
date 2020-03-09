package model;

import java.io.Serializable;

/**
 * @author Gruppe 1. Zia Mansoor, Oliver Wisborg, Johan Pedersen
 */

import java.util.Iterator;
import java.util.LinkedHashSet;

public class CalcRentable implements CalcBehavior, Serializable {

	/**
	 * Calculates the total price of an orderLine with a Rentable object
	 * OrderLine.getAmount() >= 1
	 */

	@Override
	public double calc(PriceList priceList, OrderLine orderLine) {
		Rentable rentable = (Rentable) orderLine.getProduct();
		double calcedPrice = 0;
		LinkedHashSet<PriceListLine> plls = rentable.getPriceListLines();
		Iterator<PriceListLine> i = plls.iterator();

		if (orderLine.getPrice() == 0) {
			while (i.hasNext()) {
				PriceListLine pll = i.next();
				if (priceList.equals(pll.getPriceList())) {

					if (orderLine.getTempKegSize() > 0) {
						calcedPrice += (pll.getPrice() / rentable.getKegSize() * orderLine.getTempKegSize())
								* orderLine.getAmount();
					} else {
						calcedPrice += pll.getPrice() * orderLine.getAmount();
					}

					if (pll.getDiscount() != null) {
						calcedPrice -= calcedPrice * pll.getDiscount().getPercentage();
					}
					calcedPrice += rentable.getProductGroup().getContainerDeposit() * orderLine.getAmount();
				}
			}
		} else {
			calcedPrice = orderLine.getPrice();
		}

		return calcedPrice;
	}

}
