package model;

import java.io.Serializable;

/**
 * @author Gruppe 1. Zia Mansoor, Oliver Wisborg, Johan Pedersen
 */

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class Reservation implements Serializable {

	private LocalDate fromDate;
	private LocalDate toDate;
	private Set<Rentable> rentables = new HashSet<>();
	private Customer customer;

	public Reservation(LocalDate fromDate, LocalDate toDate, Customer customer) {
		Pre.require(fromDate.compareTo(toDate) <= 0);
		this.fromDate = fromDate;
		this.toDate = toDate;
		setCustomer(customer);

	}

	public LocalDate getToDate() {
		return toDate;
	}

	public void setToDate(LocalDate toDate) {
		this.toDate = toDate;
	}

	public LocalDate getFromDate() {
		return fromDate;
	}

	public void setFromDate(LocalDate fromDate) {
		this.fromDate = fromDate;
	}

	public Set<Rentable> getRentables() {
		return rentables;
	}

	public void addRentable(Rentable rentable) {
		rentables.add(rentable);
		rentable.addReservation(this);
	}

	public void removeRentable(Rentable rentable) {
		if (rentables.contains(rentable)) {
			rentables.remove(rentable);
			rentable.removeReservation(this);
		}
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		if (this.customer != customer) {
			this.customer = customer;
			customer.addReservation(this);
		}
	}

	public void setCustomerNull() {
		if (this.customer != null) {
			Customer oldCustomer = this.customer;
			this.customer = null;
			oldCustomer.removeReservation(this);

		}
	}

	@Override
	public String toString() {
		return " Fra dato: " + fromDate + ", Til dato: " + toDate + ", Kunde: " + customer;
	}

}
