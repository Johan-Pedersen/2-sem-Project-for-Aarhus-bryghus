package test;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import controller.Controller;
import model.CalcProduct;
import model.Customer;
import model.Discount;
import model.Order;
import model.OrderLine;
import model.PriceList;
import model.Product;
import model.ProductGroup;

public class TestOrder {

	private OrderLine ol1, ol2, ol3;
	private Discount ti, femogtyve, halvtreds;
	private PriceList fredagsbar;
	private Customer customer;
	private Product sortMonster, klosterbryg;
	private CalcProduct cb;
	private Order o1, o2, o3, o4;
	private ProductGroup flaske;

	@Before
	public void setup() {
		o1 = Controller.getController().createOrder();
		o2 = Controller.getController().createOrder();
		o3 = Controller.getController().createOrder();
		o4 = Controller.getController().createOrder();

		flaske = new ProductGroup("flaske");
		fredagsbar = new PriceList("Fredagsbar");

		klosterbryg = flaske.createProduct("klosterbryg");
		klosterbryg.createPriceListLine(50, fredagsbar);
		sortMonster = flaske.createProduct("sortMonster");
		sortMonster.createPriceListLine(50, fredagsbar);

		cb = new CalcProduct();

		ol1 = o1.createOrderLine(1, cb, klosterbryg, fredagsbar);
		ol1 = o2.createOrderLine(1, cb, klosterbryg, fredagsbar);
		ol2 = o2.createOrderLine(1, cb, sortMonster, fredagsbar);
		ol3 = o3.createOrderLine(5, cb, klosterbryg, fredagsbar);

		ti = new Discount(10);
		femogtyve = new Discount(25);
		halvtreds = new Discount(50);

		customer = new Customer("jørgen", "12345678", "sønderhøj 30");

		customer.setDiscount(ti);

	}

	@Test
	public void testCreateId() {

//		tc1
		assertEquals(1000001, o1.getId());

//		tc2
		assertEquals(1000004, o4.getId());
	}

	@Test
	public void testCalcPrice() {

//		tc1
		assertEquals(50, o1.calcPrice(fredagsbar), 0.001);

//		tc2 
		assertEquals(100, o2.calcPrice(fredagsbar), 0.001);

//		tc3
		o2.setCustomer(customer);
		assertEquals(90, o2.calcPrice(fredagsbar), 0.001);

//		tc4
		o1.setDiscount(halvtreds);
		assertEquals(25, o1.calcPrice(fredagsbar), 0.001);

//		tc5
		try {

			ol1.setAmount(0);

		} catch (RuntimeException ex) {
			assertEquals(ex.getMessage(), "Pre condition violated");
		}
	}

	@Test
	public void testCalcVoucher() {

//		tc1

		flaske.setVoucherValue(1);
		assertEquals(1, o1.calcVoucher(fredagsbar));

//		tc2
		assertEquals(2, o2.calcVoucher(fredagsbar));

//		tc3 
		assertEquals(5, o3.calcVoucher(fredagsbar));

//		tc4
		flaske.setVoucherValue(2);
		assertEquals(2, o1.calcVoucher(fredagsbar));

//		tc5
		assertEquals(4, o2.calcVoucher(fredagsbar));

//		tc6
		flaske.setVoucherValue(5);
		assertEquals(10, o2.calcVoucher(fredagsbar));

//		---------------------------------------------------------

//		tc7

		try {
			ol1.setAmount(-1);
			flaske.setVoucherValue(0);
		} catch (RuntimeException ex) {
			assertEquals(ex.getMessage(), "Pre condition violated");
		}

//		tc8

		try {
			ol1.setAmount(0);

		} catch (RuntimeException ex) {
			assertEquals(ex.getMessage(), "Pre condition violated");
		}

//		tc9

		try {
			flaske.setVoucherValue(1);

		} catch (RuntimeException ex) {
			assertEquals(ex.getMessage(), "Pre condition violated");
		}

	}

}
