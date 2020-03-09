package test;

import static org.junit.Assert.assertEquals;

import java.util.function.BiConsumer;

import org.junit.Before;
import org.junit.Test;

import model.Discount;
import model.PriceList;
import model.PriceListLine;
import model.Product;
import model.ProductGroup;

public class TestProductGroup {

	Discount femogtyve;
	private PriceList fredagsbar;
	private PriceListLine pll;
	private Product klosterbryg;
	private BiConsumer<PriceListLine, Object> setMethod;
	private ProductGroup flaske;

	@Before
	public void setup() {

		flaske = new ProductGroup("flaske");

		klosterbryg = flaske.createProduct("klosterbryg");
		fredagsbar = new PriceList("Fredagsbar");

		pll = klosterbryg.createPriceListLine(50, fredagsbar);

		flaske.addObserver(klosterbryg);

		femogtyve = new Discount(25);
		setMethod = Product::setUp;

	}

	@Test
	public void testUpdate() {

//		tc1
		flaske.notifyObservers(fredagsbar, femogtyve, setMethod);
		assertEquals(0.25, pll.getDiscount().getPercentage(), 0.001);

//		tc2
		flaske.notifyObservers(fredagsbar, 79.00, setMethod);
		assertEquals(79, pll.getPrice(), 0.001);

//		tc3
		flaske.notifyObservers(fredagsbar, 79.5, setMethod);
		assertEquals(79.5, pll.getPrice(), 0.001);

//		tc4
		try {
			flaske.notifyObservers(fredagsbar, 79, setMethod);

		} catch (RuntimeException ex) {
			assertEquals(ex.getMessage(), "Pre condition violated");
		}

//		tc5
		try {
			flaske.notifyObservers(null, 79.00, setMethod);

		} catch (RuntimeException ex) {
			assertEquals(ex.getMessage(), "Pre condition violated");
		}

//		tc6
		try {
			flaske.notifyObservers(fredagsbar, "hej", setMethod);

		} catch (RuntimeException ex) {
			assertEquals(ex.getMessage(), "Pre condition violated");
		}

//		tc7
		try {
			flaske.notifyObservers(fredagsbar, 79.00, null);

		} catch (RuntimeException ex) {
			assertEquals(ex.getMessage(), "Pre condition violated");
		}
	}
}
