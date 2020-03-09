package test;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import model.CalcBehavior;
import model.CalcTour;
import model.Discount;
import model.Order;
import model.OrderLine;
import model.PriceList;
import model.ProductGroup;
import model.Tour;

public class TestCalcTour {

	private OrderLine ol1;
	private Discount ti, femogtyve, halvtreds;
	private PriceList butik;

	@Before
	public void setup() {

		ProductGroup rundvisning = new ProductGroup("rundvisning");

		Tour tour = rundvisning.createTour("Rundvisning");

		tour.createPriceListLine(100, butik);
		butik = new PriceList("butik");

		Order o1 = new Order();

		CalcBehavior cb = new CalcTour();

		ol1 = o1.createOrderLine(1, cb, tour, butik);

		ti = new Discount(10);
		femogtyve = new Discount(25);
		halvtreds = new Discount(50);
	}

	@Test
	public void testClacProduct() {

//			tc1
		assertEquals(100, ol1.performCalc(butik), 0.001);

//	 		tc2
		ol1.setAmount(2);
		assertEquals(200, ol1.performCalc(butik), 0.001);

//			tc3
		ol1.setAmount(5);
		assertEquals(500, ol1.performCalc(butik), 0.001);

//			tc4
		ol1.setAmount(1);
		ol1.setStudentDiscount(ti);
		assertEquals(90, ol1.performCalc(butik), 0.001);

//			tc5
		ol1.setAmount(2);
		assertEquals(180, ol1.performCalc(butik), 0.001);

//			tc6
		ol1.setAmount(2);
		ol1.setStudentDiscount(femogtyve);
		assertEquals(150, ol1.performCalc(butik), 0.001);

//			tc7
		ol1.setAmount(1);
		ol1.setStudentDiscount(null);
		ol1.setPrice(35);
		assertEquals(35, ol1.performCalc(butik), 0.001);

//			tc8
		ol1.setAmount(2);
		ol1.setStudentDiscount(halvtreds);
		assertEquals(35, ol1.performCalc(butik), 0.001);

//			tc9
		ol1.setAmount(1);
		ol1.setPrice(0);
		ol1.setStudentDiscount(null);
		PriceList nytårsFest = new PriceList("nytårsfest");
		assertEquals(100, ol1.performCalc(nytårsFest), 0.001);

//			tc10
		try {
			ol1.setAmount(-1);
			ol1.setPrice(0);

		} catch (RuntimeException ex) {
			assertEquals(ex.getMessage(), "Pre condition violated");
		}

//			tc11
		try {
			ol1.setAmount(0);
			ol1.setStudentDiscount(null);
			ol1.setPrice(100);

		} catch (RuntimeException ex) {
			assertEquals(ex.getMessage(), "Pre condition violated");
		}

//			tc12

		try {
			ol1.setAmount(0);
		} catch (RuntimeException ex) {
			assertEquals(ex.getMessage(), "Pre condition violated");
		}

//			tc13
		try {
			ol1.setAmount(0);
			ol1.setStudentDiscount(ti);
		} catch (RuntimeException ex) {
			assertEquals(ex.getMessage(), "Pre condition violated");
		}

	}
}
