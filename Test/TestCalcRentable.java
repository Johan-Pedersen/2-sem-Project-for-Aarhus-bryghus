package test;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import model.CalcBehavior;
import model.CalcRentable;
import model.Discount;
import model.Order;
import model.OrderLine;
import model.PriceList;
import model.PriceListLine;
import model.ProductGroup;
import model.Rentable;

public class TestCalcRentable {

	private OrderLine ol1;
	private Discount ti, femogtyve, halvtreds;
	private PriceList butik;
	private PriceListLine pll;

	@Before
	public void setup() {
		ProductGroup fustage = new ProductGroup("flaske");

		Rentable klosterbryg = fustage.createRentable("klosterbryg, 20 liter");
		butik = new PriceList("Fredagsbar");

		pll = klosterbryg.createPriceListLine(775, butik);

		Order o1 = new Order();

		CalcBehavior cb = new CalcRentable();

		ol1 = o1.createOrderLine(1, cb, klosterbryg, butik);

		ti = new Discount(10);
		femogtyve = new Discount(25);
		halvtreds = new Discount(50);
	}

	@Test
	public void testClacProduct() {

//		tc1
		assertEquals(775, ol1.performCalc(butik), 0.001);

// 		tc2
		ol1.setAmount(2);
		assertEquals(1550, ol1.performCalc(butik), 0.001);

//		tc3
		ol1.setAmount(5);
		assertEquals(3875, ol1.performCalc(butik), 0.001);

//		tc4
		ol1.setAmount(1);
		pll.setDiscount(ti);
		assertEquals(697.5, ol1.performCalc(butik), 0.001);

//		tc5
		ol1.setAmount(2);
		assertEquals(1395, ol1.performCalc(butik), 0.001);

//		tc6
		ol1.setAmount(2);
		pll.setDiscount(femogtyve);
		assertEquals(1162.5, ol1.performCalc(butik), 0.001);

//		tc7
		ol1.setAmount(1);
		pll.setDiscount(null);
		ol1.setPrice(100);
		assertEquals(100, ol1.performCalc(butik), 0.001);

//		tc8
		ol1.setAmount(2);
		ol1.setStudentDiscount(halvtreds);
		assertEquals(100, ol1.performCalc(butik), 0.001);

//		tc9
		try {
			ol1.setAmount(0);
			pll.setDiscount(null);
			ol1.setPrice(100);

		} catch (RuntimeException ex) {
			assertEquals(ex.getMessage(), "Pre condition violated");
		}

//		tc10
		try {
			ol1.setAmount(-1);
			ol1.setPrice(0);

		} catch (RuntimeException ex) {
			assertEquals(ex.getMessage(), "Pre condition violated");
		}

//		tc11
		ol1.setAmount(1);
		ol1.setPrice(0);
		PriceList nytårsFest = new PriceList("nytårsfest");
		assertEquals(0, ol1.performCalc(nytårsFest), 0.001);

//		tc12

		try {
			ol1.setAmount(0);

		} catch (RuntimeException ex) {
			assertEquals(ex.getMessage(), "Pre condition violated");
		}

//		tc13
		try {
			ol1.setAmount(0);
			pll.setDiscount(ti);
		} catch (RuntimeException ex) {
			assertEquals(ex.getMessage(), "Pre condition violated");
		}

	}
}
