package model;

import java.io.Serializable;

/**
 *@author  Gruppe 1. Zia Mansoor, Oliver Wisborg, Johan Pedersen
 */

import java.util.ArrayList;
import java.util.List;

public class Customer implements Serializable{

	private String name;
	private String telNum;
	private String adresse;
	private Discount discount;
	private List<Order> orders = new ArrayList<>();
	private List<Reservation> reservations = new ArrayList<>();

	public Customer(String name, String telNum, String adresse) {
		this.name = name;
		this.telNum = telNum;
		this.adresse = adresse;
	}

	public String getName() {
		return name;
	}

	public String getTelNum() {
		return telNum;
	}

	public String getAdresse() {
		return adresse;
	}

	public Discount getDiscount() {
		return discount;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setTelNum(String telNum) {
		this.telNum = telNum;
	}

	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}

	public void setDiscount(Discount discount) {
		if (this.discount != discount)
			this.discount = discount;
	}

	public void setDiscountNull() {
		if (this.discount != null)
			this.discount = null;
	}

	public List<Order> getOrders() {
		return new ArrayList<>(orders);
	}

	public void addOrder(Order order) {
		if (!orders.contains(order)) {
			orders.add(order);
			order.setCustomer(this);
		}
	}

	public void removeOrder(Order order) {
		if (orders.contains(order)) {
			orders.remove(order);
		}
	}

	public List<Reservation> getReservations() {
		return new ArrayList<>(reservations);
	}

	public void addReservation(Reservation reservation) {
		if (!reservations.contains(reservation)) {
			reservations.add(reservation);
			reservation.setCustomer(this);
		}
	}

	public void removeReservation(Reservation reservation) {
		if (!reservations.contains(reservation)) {
			reservations.remove(reservation);
			reservation.setCustomerNull();
		}
	}

	@Override
	public String toString() {
		return name + ", Tlf: " + telNum + ", Adresse: " + adresse;
	}

}
