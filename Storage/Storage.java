package storage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import model.Customer;
import model.Order;
import model.PriceList;
import model.ProductGroup;

public class Storage implements Serializable {

	private static Storage instance;
	private Set<ProductGroup> productGroups;
	private List<Order> orders;
	private Set<Customer> customers;
	private List<PriceList> priceLists;

	public static Storage getStorage() {
		if (instance == null)
			instance = new Storage();
		return instance;
	}

	private Storage() {
		productGroups = new LinkedHashSet<>();
		orders = new ArrayList<>();
		customers = new HashSet<>();
		priceLists = new ArrayList<>();
	}

	// -----------------

	public Set<ProductGroup> getProductGroup() {
		return new LinkedHashSet<>(productGroups);
	}

	public void addProductGroup(ProductGroup productGroup) {
		productGroups.add(productGroup);
	}

	public void removeProductGroup(ProductGroup productGroup) {
		if (productGroups.contains(productGroup))
			productGroups.remove(productGroup);
	}

	// ------------------

	public List<Order> getOrders() {
		return new ArrayList<>(orders);
	}

	public void addOrder(Order order) {
		if (!orders.contains(order))
			orders.add(order);
	}

	public void removeOrder(Order order) {
		if (orders.contains(order))
			orders.remove(order);
	}

	// -------------------

	public Set<Customer> getCustomers() {
		return new LinkedHashSet<>(customers);
	}

	public void addCustomer(Customer c) {
		customers.add(c);
	}

	public void removeCustomer(Customer c) {
		if (customers.contains(c)) {
			customers.remove(c);
		}
	}

	// -------------------

	public List<PriceList> getPriceList() {
		return new ArrayList<>(priceLists);
	}

	public void addPriceList(PriceList pl) {
		priceLists.add(pl);
	}

	public void removePriceList(PriceList pl) {
		if (priceLists.contains(pl)) {
			priceLists.remove(pl);
		}
	}

}
