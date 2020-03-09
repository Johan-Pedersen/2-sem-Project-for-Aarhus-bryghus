package model;

import java.io.Serializable;

/**
 *@author  Gruppe 1. Zia Mansoor, Oliver Wisborg, Johan Pedersen
 */

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.BiConsumer;

public class ProductGroup implements Serializable {
	private String name;
	private Set<Product> products = new LinkedHashSet<>();
	private Set<Observer> observers = new HashSet<>();
	private int VoucherValue;
	private double containerDeposit;

	public ProductGroup(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public int getVoucherValue() {
		return VoucherValue;
	}

	public void setVoucherValue(int voucherValue) {
		VoucherValue = voucherValue;
	}

	public Set<Product> getProducts() {
		return new LinkedHashSet<>(products);
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getContainerDeposit() {
		return containerDeposit;
	}

	public void setContainerDeposit(double containerDeposit) {
		this.containerDeposit = containerDeposit;
	}

	public HashSet<Observer> getObservers() {
		return new HashSet<>(observers);
	}

	public void addObserver(Observer observer) {
		observers.add(observer);
	}

	public void removeObserver(Observer observer) {
		observers.remove(observer);
	}

	/**
	 * Notifies the observers about change in Discount or price
	 * 
	 * @param priceList
	 * @param object, can be Discount or Double
	 * @param myMethod
	 */
	public void notifyObservers(PriceList priceList, Object object, BiConsumer<PriceListLine, Object> myMethod) {
		for (Observer ob : observers) {
			ob.update(priceList, object, myMethod);
		}
	}

	public Product createProduct(String name) {
		Product p = new Product(name, this);
		products.add(p);
		return p;
	}

	public Rentable createRentable(String name) {
		Rentable r = new Rentable(name, this);
		products.add(r);
		return r;
	}

	public Tour createTour(String name) {
		Tour t = new Tour(name, this);
		products.add(t);
		return t;
	}

	public GiftPackage createGiftPackage(String name, ProductGroup productGroup, int beerAmount) {
		GiftPackage gp = new GiftPackage(name, this, beerAmount);
		products.add(gp);
		return gp;
	}

	@Override
	public String toString() {
		return name;
	}

	public void removeProduct(Product product) {
		products.remove(product);
	}

}
