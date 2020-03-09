package model;

import java.io.Serializable;
/**
 * @author Gruppe 1. Zia Mansoor, Oliver Wisborg, Johan Pedersen
 */
import java.time.LocalDate;

public class Tour extends Product implements Serializable {

	private LocalDate date;

	Tour(String name, ProductGroup productGroup) {
		super(name, productGroup);
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "" + getName();
	}

}
