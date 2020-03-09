package model;

import java.io.Serializable;

/**
 * @author Gruppe 1. Zia Mansoor, Oliver Wisborg, Johan Pedersen
 */

public class Payment implements Serializable {

	private double paymentAmount;
	private PaymentType paymentType;

	Payment(double paymentAmount, PaymentType paymentType) {
		this.paymentAmount = paymentAmount;
		this.paymentType = paymentType;
	}

	public double getPaymentAmount() {
		return paymentAmount;
	}

	public void setPaymentAmount(double paymentAmount) {
		this.paymentAmount = paymentAmount;
	}

	public PaymentType getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(PaymentType paymentType) {
		this.paymentType = paymentType;
	}

	@Override
	public String toString() {
		return "Beløb: " + paymentAmount + ", Betalings type: " + paymentType;
	}

}
