package test;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;

import model.Condition;
import model.Customer;
import model.ProductGroup;
import model.Rentable;
import model.Reservation;

public class TestRentable {

	private Rentable r1;
	private Reservation reservation;

	@Before
	public void setup() {

		ProductGroup Malt = new ProductGroup("Malt");

		Customer customer = new Customer("jørgen", "12345678", "sønderhøj 30");
		r1 = Malt.createRentable("25 kg sæk");

		reservation = new Reservation(LocalDate.of(2019, 04, 03), LocalDate.of(2019, 04, 04), customer);

		r1.addReservation(reservation);
	}

	@Test
	public void testCheckCurrentCondition() {
//		data Now = 02.04.2019

//		tc1
		r1.checkCurrentCondition();
		assertEquals(Condition.HOME, r1.getCondition());

//		tc2
		reservation.setToDate(LocalDate.of(2019, 04, 03));
		reservation.setFromDate(LocalDate.of(2019, 03, 01));
		r1.checkCurrentCondition();
		assertEquals(Condition.RENTED, r1.getCondition());

//		tc3
		reservation.setToDate(LocalDate.of(2019, 04, 03));
		reservation.setFromDate(LocalDate.of(2019, 04, 03));
		r1.setCondition(Condition.MALFUNCTION);
		r1.checkCurrentCondition();
		assertEquals(Condition.MALFUNCTION, r1.getCondition());

//		tc4
		reservation.setToDate(LocalDate.of(2019, 04, 03));
		reservation.setFromDate(LocalDate.of(2019, 04, 01));
		r1.setCondition(Condition.MALFUNCTION);
		r1.checkCurrentCondition();
		assertEquals(Condition.MALFUNCTION, r1.getCondition());

	}
}
