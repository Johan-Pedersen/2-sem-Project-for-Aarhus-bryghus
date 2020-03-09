package model;

import java.io.Serializable;

/**
 * @author Gruppe 1. Zia Mansoor, Oliver Wisborg, Johan Pedersen
 */

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import storage.Storage;

public class Order implements Serializable {
	private int id;
	private LocalDate date;
	private List<OrderLine> orderLines = new ArrayList<>();
	private Set<Payment> payments = new LinkedHashSet<>();
	private Discount discount;
	private double sum;
	private double moneyBack;
	private Customer customer;

	public Order() {
		this.date = LocalDate.now();
		createId();
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		if (this.customer != customer) {
			this.customer = customer;
			customer.addOrder(this);
		}
	}

	public Discount getDiscount() {
		return discount;
	}

	public void setDiscount(Discount discount) {
		if (this.discount != discount)
			this.discount = discount;
	}

	public void setDiscountNull() {
		if (this.discount != null)
			this.discount = null;
	}

	public OrderLine createOrderLine(int amount, CalcBehavior calcBehavior, Product product, PriceList priceList) {
		OrderLine ol = new OrderLine(amount, calcBehavior, product, priceList);
		orderLines.add(ol);
		return ol;
	}

	public void removeOrderLine(OrderLine ol) {
		if (orderLines.contains(ol)) {
			orderLines.remove(ol);
		}
	}

	@Override
	public String toString() {
		String orderlineS = null;
		for (OrderLine ol : orderLines) {
			orderlineS = "\n- " + ol;
		}

		String paymentS = null;
		for (Payment p : payments) {
			paymentS = "\n- " + p;
		}

		String isPayed = null;
		if (this.checkPayment())
			isPayed = "Ja";
		else
			isPayed = "Nej";

		return "ID: " + id + ", Dato: " + date + "\nOrder linjer: " + orderlineS + "\nRabat: " + discount + "\nTotal: "
				+ sum + "\nBetalinger: " + paymentS + "\nEr ordre færdig betalt? " + isPayed + "\nPenge tilbage: "
				+ moneyBack + ", Kunde: " + customer;
	}

	public LocalDate getDate() {
		return date;
	}

	public List<OrderLine> getOrderLines() {
		return new ArrayList<>(orderLines);
	}

	public void setDato(LocalDate date) {
		this.date = date;
	}

	public Set<Payment> getPayments() {
		return new LinkedHashSet<>(payments);
	}

	/**
	 * if amount < sum a payment can be made
	 * 
	 * @pre cannot addPayment if sum == 0
	 * @param p
	 */
	public Payment createPayment(double paymentAmount, PaymentType paymentType) {
		Payment p = new Payment(paymentAmount, paymentType);
		Pre.require(sum > 0);
		if (!checkPayment()) {
			payments.add(p);
			// in order to update moneyBack
//			checkPayment();
		}
		return p;
	}

	public void removePayment(Payment payment) {
		payments.remove(payment);
	}

	/**
	 * checks if the payments have reached to sum, if payment overreach the remaning
	 * amount will be put into monyBack
	 * 
	 * @return
	 */
	public boolean checkPayment() {

		double amount = 0;
		for (Payment p : payments) {
			amount += p.getPaymentAmount();
		}

		if (amount >= sum) {
			moneyBack = amount - sum;
			return true;
		}

		return false;
	}

	public int getId() {
		return id;
	}

	public double getSum() {
		return sum;
	}

	public double getMoneyBack() {
		return moneyBack;
	}

	/**
	 * Creates the order Id. If the order is the first to be created then it will be
	 * 1000001. Otherwise it will just be 1 more than the previous order.
	 */
	public void createId() {
		Storage storage = Storage.getStorage();
		if (storage.getOrders().size() == 0) {
			this.id = 1000001;
		} else {
			this.id = storage.getOrders().get(storage.getOrders().size() - 1).getId() + 1;
		}
	}

	/**
	 * Calculates the total price of the whole order.
	 * Takes into account order discount and customer discount.
	 * Sets the order sum to be equal to the price.
	 */
	public double calcPrice(PriceList priceList) {
		double price = 0;
		for (OrderLine ol : orderLines) {
			price += ol.performCalc(priceList);
		}
		if (this.discount != null) {
			price -= price * discount.getPercentage();
		}
		if (customer != null) {
			if (customer.getDiscount() != null) {
				price -= price * customer.getDiscount().getPercentage();
			}
		}
		sum = price;
		return price;
	}

	/**
	 * 
	 * @param priceList
	 * @return The total price of all the orderlines in the order in amount of
	 *         vouchers clips
	 */

	public int calcVoucher(PriceList priceList) {
		int value = 0;

		for (OrderLine ol : orderLines) {
			value += ol.getProduct().getProductGroup().getVoucherValue() * ol.getAmount();
		}
		sum = value;
		return value;
	}
}
