package model;

import java.io.Serializable;

/**
 * @author Gruppe 1. Zia Mansoor, Oliver Wisborg, Johan Pedersen
 */

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Rentable extends Product implements Serializable {
	private List<Reservation> reservations = new ArrayList<>();
	private Condition condition = Condition.HOME;
	private int kegSize;

	Rentable(String name, ProductGroup productGroup) {
		super(name, productGroup);
	}

	public Condition getCondition() {
		return condition;
	}

	/**
	 * Sets the condition.
	 */
	public void setCondition(Condition condition) {
		if (!this.condition.equals(condition))
			this.condition = condition;
	}

	public List<Reservation> getReservations() {
		return new ArrayList<>(reservations);
	}

	/**
	 * Adds Reservation to the rentable object and adds the rentable object to the
	 * reservation.
	 * 
	 * @param reservation
	 */
	public void addReservation(Reservation reservation) {
		if (!reservations.contains(reservation)) {
			reservations.add(reservation);
			reservation.addRentable(this);
		}
	}

	/**
	 * Removes Reservation from the rentable object and removes the rentable object
	 * from the reservation.
	 * 
	 * @param reservation
	 */
	public void removeReservation(Reservation reservation) {
		if (!reservations.contains(reservation)) {
			reservations.remove(reservation);
			reservation.removeRentable(this);
		}
	}

	public int getKegSize() {
		return kegSize;
	}

	public void setKegSize(int kegSize) {
		this.kegSize = kegSize;
	}

	@Override
	public String toString() {
		if (kegSize > 0)
			return super.getName() + ", Størrelse: " + kegSize + " reservationer: " + reservations;
		else
			return super.getName();
	}

	/**
	 * Checks and changes which condition the rentable object should be in, based
	 * around its reservations. Except if the condition is currently MALFUNCTION.
	 */
	public void checkCurrentCondition() {
		if (!condition.equals(Condition.MALFUNCTION)) {
			LocalDate dateNow = LocalDate.now();
			for (Reservation r : reservations) {
				if (dateNow.compareTo(r.getToDate()) <= 0 && dateNow.compareTo(r.getFromDate()) >= 0) {
					condition = Condition.RENTED;
				} else
					condition = Condition.HOME;
			}
		}
	}

}
