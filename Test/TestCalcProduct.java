package test;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import model.CalcBehavior;
import model.CalcProduct;
import model.Discount;
import model.Order;
import model.OrderLine;
import model.PriceList;
import model.PriceListLine;
import model.Product;
import model.ProductGroup;

public class TestCalcProduct {

	private OrderLine ol1;
	private Discount ti, femogtyve, halvtreds;
	private PriceList fredagsbar;
	private PriceListLine pll;

	@Before
	public void setup() {

		ProductGroup flaske = new ProductGroup("flaske");

		Product klosterbryg = flaske.createProduct("klosterbryg");
		fredagsbar = new PriceList("Fredagsbar");

		pll = klosterbryg.createPriceListLine(50, fredagsbar);

		Order o1 = new Order();

		CalcBehavior cb = new CalcProduct();

		ol1 = o1.createOrderLine(1, cb, klosterbryg, fredagsbar);

		ti = new Discount(10);
		femogtyve = new Discount(25);
		halvtreds = new Discount(50);
	}

	@Test
	public void testClacProduct() {

//		tc1
		assertEquals(50, ol1.performCalc(fredagsbar), 0.001);

// 		tc2
		ol1.setAmount(2);
		assertEquals(100, ol1.performCalc(fredagsbar), 0.001);

//		tc3
		ol1.setAmount(5);
		assertEquals(250, ol1.performCalc(fredagsbar), 0.001);

//		tc4
		ol1.setAmount(1);
		pll.setDiscount(ti);
		assertEquals(45, ol1.performCalc(fredagsbar), 0.001);

//		tc5
		ol1.setAmount(2);
		assertEquals(90, ol1.performCalc(fredagsbar), 0.001);

//		tc6
		ol1.setAmount(2);
		pll.setDiscount(femogtyve);
		assertEquals(75, ol1.performCalc(fredagsbar), 0.001);

//		tc7
		ol1.setAmount(1);
		pll.setDiscount(null);
		ol1.setPrice(35);
		assertEquals(35, ol1.performCalc(fredagsbar), 0.001);

//		tc8
		ol1.setAmount(2);
		ol1.setStudentDiscount(halvtreds);
		assertEquals(35, ol1.performCalc(fredagsbar), 0.001);

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
