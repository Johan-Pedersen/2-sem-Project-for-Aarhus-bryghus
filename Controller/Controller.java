package controller;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

import model.CalcBehavior;
import model.Condition;
import model.Customer;
import model.Discount;
import model.GiftPackage;
import model.Observer;
import model.Order;
import model.OrderLine;
import model.Payment;
import model.PaymentType;
import model.PriceList;
import model.PriceListLine;
import model.Product;
import model.ProductGroup;
import model.Rentable;
import model.Reservation;
import model.Tour;
import storage.Storage;

public class Controller  {

	private static Controller instance;
	private static Storage storage;

	public static Controller getController() {
		if (instance == null)
			instance = new Controller();

		return instance;
	}

	private Controller() {
		storage = Storage.getStorage();
	}

	// ---------------------------------------------------------------------------------------------

	public Customer createCustomer(String name, String telNum, String adresse) {
		Customer c = new Customer(name, telNum, adresse);
		storage.addCustomer(c);
		return c;
	}

	public void removeCustomer(Customer customer) {
		storage.removeCustomer(customer);
	}

	public void updateCustomer(Customer customer, String name, String telNum, String adresse) {
		customer.setName(name);
		customer.setTelNum(telNum);
		customer.setAdresse(adresse);
	}

	public void setCustomerDiscount(Customer customer, Discount discount) {
		customer.setDiscount(discount);
	}

	public void setDiscountNullCustomer(Customer customer) {
		customer.setDiscountNull();
	}

	// ---------------------------------------------------------------------------------------------

	public Discount createDiscount(double percentage) {
		Discount d = new Discount(percentage);
		return d;
	}

	public void setDiscountPer(Discount discount, double percentage) {
		discount.setPercentage(percentage);
	}

	// ---------------------------------------------------------------------------------------------

	public Order createOrder() {
		Order o = new Order();
		storage.addOrder(o);
		return o;
	}

	public void removeOrder(Order order) {
		storage.removeOrder(order);
	}

	public double getOrderCalc(Order order, PriceList priceList) {
		return order.calcPrice(priceList);
	}

	public void setOrderDiscount(Order order, Discount discount) {
		order.setDiscount(discount);
	}

	public void setDiscountNullOrder(Order order) {
		order.setDiscountNull();
	}

	public boolean checkOrderPayment(Order order) {
		return order.checkPayment();
	}

	public void setCustomerToOrder(Order order, Customer customer) {
		order.setCustomer(customer);
	}

	// ---------------------------------------------------------------------------------------------

	public OrderLine createOrderLine(Order order, int amount, CalcBehavior calcBehavior, Product product,
			PriceList priceList) {
		OrderLine ol = order.createOrderLine(amount, calcBehavior, product, priceList);
		return ol;
	}

	// ---------------------------------------------------------------------------------------------

	public Payment createPayment(Order order, double paymentAmount, PaymentType paymentType) {
		Payment p = order.createPayment(paymentAmount, paymentType);
		return p;
	}

	public void removePayment(Order order, Payment payment) {
		order.removePayment(payment);
	}

	// ---------------------------------------------------------------------------------------------

	public PriceList createPriceList(String name) {
		PriceList pl = new PriceList(name);
		storage.addPriceList(pl);
		return pl;
	}

	public void removePriceList(PriceList priceList) {
		storage.removePriceList(priceList);
	}

	public void updatePriceList(PriceList priceList, String name) {
		priceList.setName(name);
	}

	/**
	 * Returns a set with priceList being used by a product group's products.
	 */
	public Set<PriceList> getGroupPriceList(ProductGroup productGroup) {
		Set<PriceList> set = new LinkedHashSet<>();
		for (Product p : productGroup.getProducts()) {
			for (PriceListLine pll : p.getPriceListLines()) {
				set.add(pll.getPriceList());
			}
		}
		return set;
	}

	/**
	 * Returns a set with priceList not in the product group.
	 */
	public Set<PriceList> getPriceListNotInGrp(ProductGroup productGroup) {
		Set<PriceList> set = new LinkedHashSet<>();
		for (PriceList priceList : storage.getPriceList()) {
			if (!getGroupPriceList(productGroup).contains(priceList)) {
				set.add(priceList);
			}
		}
		return set;
	}

	/**
	 * Returns a set with priceLists except fredagsbar;
	 */
	public Set<PriceList> getPriceListsExceptFriday() {
		Set<PriceList> set = new LinkedHashSet<>();
		for (PriceList priceList : storage.getPriceList()) {
			if (!priceList.getName().equals("Fredagsbar")) {
				set.add(priceList);
			}
		}
		return set;
	}

	// ---------------------------------------------------------------------------------------------

	public PriceListLine createPriceListLine(double price, PriceList priceList, Product product) {
		PriceListLine pll = product.createPriceListLine(price, priceList);
		return pll;
	}

	/**
	 * Creates a PriceListLine for every product to a chosen priceList, with a temp
	 * price of 100.
	 */
	public void setAllPriceListLinesToList(Collection<Product> products, PriceList priceList) {
		for (Product product : products) {
			product.createPriceListLine(100, priceList);
		}
	}

	// ---------------------------------------------------------------------------------------------

	public ProductGroup createProductGroup(String name) {
		ProductGroup pg = new ProductGroup(name);
		storage.addProductGroup(pg);
		return pg;
	}

	public void removeProductGroup(ProductGroup productGroup) {
		storage.removeProductGroup(productGroup);
	}

	public void updateProductGroup(ProductGroup productGroup, String name, int voucherValue, double containerDeposit) {
		productGroup.setName(name);
		productGroup.setVoucherValue(voucherValue);
		productGroup.setContainerDeposit(containerDeposit);
	}

	public void addObserver(ProductGroup productGroup, Observer observer) {
		productGroup.addObserver(observer);
	}

	public void removeObserver(ProductGroup productGroup, Observer observer) {
		productGroup.removeObserver(observer);
	}

	/**
	 * Adds the whole collection of products to the Observer set.
	 */
	public void addAllObservers(Collection<Product> products, ProductGroup productGroup) {
//		productGroup.getObservers().addAll(products);
		for (Product p : products) {
			productGroup.addObserver(p);
		}
	}

	/**
	 * Removes the whole collection of observers from the Observer set.
	 */
	public void removeAllObservers(Collection<Observer> observers, ProductGroup productGroup) {
//		productGroup.getObservers().removeAll(observers);
		for (Observer o : observers) {
			productGroup.removeObserver(o);
		}
	}

	/**
	 * Uses the Lambda method in Products and notifies all of the observers with a new discount.
	 */
	public void notifyObserversDiscount(ProductGroup productGroup, Discount discount, PriceList priceList) {
		BiConsumer<PriceListLine, Object> setMethod = Product::setUp;

		productGroup.notifyObservers(priceList, discount, setMethod);
	}

	/**
	 * Uses the Lambda method in Products and notifies all of the observers with a new price.
	 */
	public void notifyObserversPrice(ProductGroup productGroup, double price, PriceList priceList) {
		BiConsumer<PriceListLine, Object> setMethod = Product::setUp;

		productGroup.notifyObservers(priceList, price, setMethod);
	}

	/**
	 * Creates and returns a LinkedHashset with products currently not observed. 
	 */
	public Set<Product> getProductNotObserved(ProductGroup productGroup) {
		Set<Product> set = new LinkedHashSet<>();
		if (productGroup.getProducts().size() != 0) {
			set = productGroup.getProducts();
			for (Product p : productGroup.getProducts()) {
				for (Observer o : productGroup.getObservers()) {
					if (p.equals(o)) {
						set.remove(p);
					}
				}

			}
		}
		return set;
	}

	// ---------------------------------------------------------------------------------------------

	public Product createProduct(String name, ProductGroup productGroup) {
		Product p = productGroup.createProduct(name);
		return p;
	}

	public void removeProduct(ProductGroup productGroup, Product product) {
		productGroup.removeProduct(product);
	}

	public void updateProduct(Product product, String name) {
		product.setName(name);
	}

	public void removePriceListLine(Product product, PriceListLine priceListLine) {
		product.removePriceListLine(priceListLine);
	}

	// ---------------------------------------------------------------------------------------------

	public Rentable createRentable(String name, ProductGroup productGroup) {
		Rentable r = productGroup.createRentable(name);
		return r;
	}

	// ----------------------------------------------------------------------------------------------

	public GiftPackage createGiftPackage(String name, int beerAmount, ProductGroup productGroup) {
		GiftPackage gp = productGroup.createGiftPackage(name, productGroup, beerAmount);
		return gp;
	}

	// ---------------------------------------------------------------------------------------------

	public Reservation createReservation(LocalDate fromDate, LocalDate toDate, Customer customer) {
		Reservation r = new Reservation(fromDate, toDate, customer);
		return r;
	}

	public void removeReservation(Reservation reservation, Customer customer) {
		customer.removeReservation(reservation);
	}

	public void updateReservation(Reservation reservation, LocalDate fromDate, LocalDate toDate) {
		reservation.setFromDate(fromDate);
		reservation.setToDate(toDate);
	}

	public void addRentableToReservation(Reservation reservation, Rentable rentable) {
		reservation.addRentable(rentable);
	}

	public void removeRentableFromoReservation(Reservation reservation, Rentable rentable) {
		reservation.removeRentable(rentable);
	}
	
	// ---------------------------------------------------------------------------------------------

	public Tour createTour(ProductGroup productGroup, String name) {
		Tour t = productGroup.createTour(name);

		return t;
	}

	public void addDateToTour(LocalDate date, Tour tour) {
		tour.setDate(date);
	}

	// ---------------------------------------------------------------------------------------------

	public Set<ProductGroup> getProductGroup() {
		return storage.getProductGroup();
	}
	
	public List<Order> getOrders() {
		return storage.getOrders();
	}
	
	public Set<Customer> getCustomers() {
		return storage.getCustomers();
	}
	
	public List<PriceList> getPriceList() {
		return storage.getPriceList();
	}
	
	// ---------------------------------------------------------------------------------------------

	/**
	 * Finds all the product groups with a product with a price list identically to
	 * the inserted and adds them to a set.
	 */
	public Set<ProductGroup> getSpecificProductGroups(PriceList priceList) {
		Set<ProductGroup> list = new LinkedHashSet<>();
		boolean found = false;
		for (ProductGroup pg : storage.getProductGroup()) {
			for (Product p : pg.getProducts()) {
				for (PriceListLine pll : p.getPriceListLines()) {
					if (pll.getPriceList().equals(priceList)) {
						found = true;
					}
				}
			}
			if (found) {
				list.add(pg);
				found = false;
			}
		}
		return list;
	}
	
	/**
	 * Returns a set with all currently Unfinished Orders
	 */
	public Set<Order> getAllUnfinishedOrders() {
		Set<Order> setOrders = new LinkedHashSet<>();
		for (Order o : storage.getOrders()) {
			if (!o.checkPayment()) {
				setOrders.add(o);
			}
		}
		return setOrders;
	}

	// ---------------------------------------------------------------------------------------------	
	
	/**
	 * Returns all of the orders registered for a given Period. (Lambda required for
	 * period condition)
	 */
	public Set<String> getPeriodicSale(Predicate<Order> filter) {
		Set<String> daliyOverview = new LinkedHashSet<>();
		for (Order o : storage.getOrders()) {
			if (filter.test(o)) {
				daliyOverview.add(o.toString() + "\n\n");
			}
		}
		return daliyOverview;
	}

	/**
	 * Calculates and returns the total of orders payment received. (Lambda required
	 * for period condition)
	 */
	public double calcTotalPayment(Predicate<Order> filter) {
		double calc = 0;
		for (Order o : storage.getOrders()) {
			if (filter.test(o)) {
				calc = calc + amountPayed(o);
				calc = calc - o.getMoneyBack();
				for (Payment p : o.getPayments()) {
					if (p.getPaymentType().equals(PaymentType.VOUCHER)) {
						calc = calc - p.getPaymentAmount();
					}
				}
			}

		}
		return calc;
	}

	/**
	 * Calculates and returns the total of products sold. (Lambda required for
	 * period condition)
	 */
	public int calcTotalProductsSold(Predicate<Order> filter) {
		int calc = 0;
		for (Order o : storage.getOrders()) {
			if (filter.test(o)) {
				for (OrderLine ol : o.getOrderLines()) {
					calc = calc + ol.getAmount();
				}
			}
		}
		return calc;
	}

	/**
	 * Calculates and returns the total of draft beer sold. (Lambda required for
	 * period condition)
	 */
	public int calcTotalDraftBeerSold(Predicate<Order> filter) {
		int calc = 0;
		for (Order o : storage.getOrders()) {
			if (filter.test(o)) {
				for (OrderLine ol : o.getOrderLines()) {
					if (ol.getProduct().getProductGroup().getName().equals("Fadøl, 40cl")) {
						calc = calc + ol.getAmount();
					}
				}
			}
		}
		return calc;
	}

	/**
	 * Calculates and returns the total of beer bottles sold. (Lambda required for
	 * period condition)
	 */
	public int calcTotalBeerBottlesSold(Predicate<Order> filter) {
		int calc = 0;
		for (Order o : storage.getOrders()) {
			if (filter.test(o)) {
				for (OrderLine ol : o.getOrderLines()) {
					if (ol.getProduct().getProductGroup().getName().equals("Flaske")) {
						calc = calc + ol.getAmount();
					}
				}
			}
		}
		return calc;
	}

	/**
	 * Calculates and returns the total of vouchers sold. (Lambda required for
	 * period condition)
	 */
	public int calcTotalVouchersSold(Predicate<Order> filter) {
		int calc = 0;
		for (Order o : storage.getOrders()) {
			if (filter.test(o)) {
				for (OrderLine ol : o.getOrderLines()) {
					if (ol.getProduct().getProductGroup().getName().equals("Klippekort")) {
						calc = calc + ol.getAmount();
					}
				}
			}
		}
		return calc;
	}

	/**
	 * Calculates and returns the total of voucher clips used. (Lambda required for
	 * period condition)
	 */
	public int calcTotalVoucherUsed(Predicate<Order> filter) {
		int calc = 0;
		for (Order o : storage.getOrders()) {
			if (filter.test(o)) {
				for (Payment p : o.getPayments()) {
					if (p.getPaymentType().equals(PaymentType.VOUCHER)) {
						calc = (int) (calc + p.getPaymentAmount());
					}
				}
			}
		}
		return calc;
	}

	/**
	 * Returns a set with all currently rented products
	 */
	public Set<String> getAllRentedProducts() {
		Set<String> setRented = new LinkedHashSet<>();
		for (ProductGroup gp : storage.getProductGroup()) {
			for (Product p : gp.getProducts()) {
				if (p instanceof Rentable) {
					Rentable r = (Rentable) p;
					r.checkCurrentCondition();
					if (r.getCondition().equals(Condition.RENTED)) {
						setRented.add(r.toString() + ", Tilstand: " + r.getCondition());
					}
				}
			}
		}
		return setRented;
	}


	public int beerSoldInPackages(Predicate<Order> filter) {
		int amount = 0;
		for (Order o : storage.getOrders()) {
			if (filter.test(o)) {
				for (OrderLine ol : o.getOrderLines()) {
					if (ol.getProduct() instanceof GiftPackage) {
						GiftPackage gp = (GiftPackage) ol.getProduct();
						amount = ol.getAmount() * gp.getBeerAmount();
					}
				}
			}
		}
		return amount;
	}
	
	// ---------------------------------------------------------------------------------------------

		/**
		 * Calls the real totalOrderPant method.
		 */
		public double totalOrderDeposit(List<OrderLine> list) {
			if (list.size() == 0)
				return 0;
			else
				return totalOrderDeposit(list, 0, list.size() - 1);
		}

		/**
		 * Calculates and returns the total deposit of the order.
		 */
		private double totalOrderDeposit(List<OrderLine> list, int l, int r) {
			if (l == r) {
				Product p = list.get(l).getProduct();
				if (p.getProductGroup().getContainerDeposit() > 0) {
					return p.getProductGroup().getContainerDeposit() * list.get(l).getAmount();
				} else
					return 0;
			} else {
				int m = (l + r) / 2;
				double sum = totalOrderDeposit(list, l, m);
				sum += totalOrderDeposit(list, m + 1, r);
				return sum;
			}
		}

		/**
		 * Calculates and return the amount that has been payed so far
		 */
		public double amountPayed(Order order) {
			double payed = 0.0;
			for (Payment payments : order.getPayments()) {
				payed += payments.getPaymentAmount();
			}
			return payed;
		}

		/**
		 * Calculates the order's products which is not of the instance of Rentable and Tour. 
		 */
		public double calcOrderProducts(Order order, PriceList priceList) {

			double check = 0;
			for (OrderLine ol : order.getOrderLines()) {
				if (!(ol.getProduct() instanceof Rentable) && !(ol.getProduct() instanceof Tour)) {
					check += ol.performCalc(priceList);
				}
			}
			check += totalOrderDeposit(order.getOrderLines());
			return check;
		}

		/**
		 * calculates the amount left of an order.
		 */

		public double amountLeft(PriceList priceList, Order order) {

			return order.calcPrice(priceList) - amountPayed(order);
		}

	// ---------------------------------------------------------------------------------------------

	public void initStorage() {

		ProductGroup klippekortGrp = getController().createProductGroup("Klippekort");
		ProductGroup flaskeGrp = getController().createProductGroup("Flaske");
		flaskeGrp.setVoucherValue(2);
		ProductGroup fadølGrp = getController().createProductGroup("Fadøl, 40cl");
		fadølGrp.setVoucherValue(1);
		ProductGroup spiritusGrp = getController().createProductGroup("Spiritus");
		ProductGroup fustageGrp = getController().createProductGroup("Fustage");
		ProductGroup kulsyreGrp = getController().createProductGroup("Kulsyre");
		ProductGroup maltGrp = getController().createProductGroup("Malt");
		ProductGroup beklædningGrp = getController().createProductGroup("Beklædning");
		ProductGroup anlægGrp = getController().createProductGroup("Anlæg");
		ProductGroup glasGrp = getController().createProductGroup("Glas");
		ProductGroup sampakningerGrp = getController().createProductGroup("Sampakninger");
		ProductGroup rundvisningGrp = getController().createProductGroup("Rundvisning");

		PriceList fredagsbar = getController().createPriceList("Fredagsbar");
		PriceList butik = getController().createPriceList("Butik");

		Product klippekort = getController().createProduct("Klippekort, 4 klip", klippekortGrp);

		Product klosterFlaske = getController().createProduct("Klosterbryg", flaskeGrp);
		Product SGBFlaske = getController().createProduct("Sweet Georgia Brown", flaskeGrp);
		Product ePilsnerFlaske = getController().createProduct("Extra Pilsner", flaskeGrp);
		Product celebrationFlaske = getController().createProduct("Celebration", flaskeGrp);
		Product blondieFlaske = getController().createProduct("Blondie", flaskeGrp);
		Product forårsbrygFlaske = getController().createProduct("Forårsbryg", flaskeGrp);
		Product indiaPaleFlaske = getController().createProduct("India Pale Ale", flaskeGrp);
		Product juleBrygFlaske = getController().createProduct("Julebryg", flaskeGrp);
		Product juletøndenFlaske = getController().createProduct("Juletønden", flaskeGrp);
		Product oldStrongFlaske = getController().createProduct("Old Strong Ale", flaskeGrp);
		Product fregattenFlaske = getController().createProduct("Fregatten Jylland", flaskeGrp);
		Product imperialFlaske = getController().createProduct("Imperial Stout", flaskeGrp);
		Product tributeFlaske = getController().createProduct("Tribute", flaskeGrp);
		Product blackMFlaske = getController().createProduct("Black Monster", flaskeGrp);

		Product klosterFadøl = getController().createProduct("Klosterbryg", fadølGrp);
		Product jazzFadøl = getController().createProduct("Jazz Classic", fadølGrp);
		Product ePilsnerFadøl = getController().createProduct("Extra Pilsner", fadølGrp);
		Product celebrationFadøl = getController().createProduct("Celebration", fadølGrp);
		Product blondieFadøl = getController().createProduct("Blondie", fadølGrp);
		Product forårsbrygFadøl = getController().createProduct("Forårsbryg", fadølGrp);
		Product indiaPaleFadøl = getController().createProduct("India Pale Ale", fadølGrp);
		Product juleBrygFadøl = getController().createProduct("Julebryg", fadølGrp);
		Product specialFadøl = getController().createProduct("Special", fadølGrp);
		Product imperialFadøl = getController().createProduct("Imperial Stout", fadølGrp);
		Product æblebrus = getController().createProduct("Æblebrus", fadølGrp);
		Product chips = getController().createProduct("Chips", fadølGrp);
		Product peanuts = getController().createProduct("Peanuts", fadølGrp);
		Product cola = getController().createProduct("Cola", fadølGrp);
		Product nikoline = getController().createProduct("Nikoline", fadølGrp);
		Product sevenUp = getController().createProduct("7-Up", fadølGrp);
		Product vand = getController().createProduct("Vand", fadølGrp);

		Product sOASpiritus = getController().createProduct("Spirit of Aarhus", spiritusGrp);
		Product sOAPindSpiritus = getController().createProduct("SOA med pind", spiritusGrp);
		Product whiskySpiritus = getController().createProduct("Whisky", spiritusGrp);
		Product lOASpiritus = getController().createProduct("Liqour of Aarhus", spiritusGrp);

		Rentable klosterFustage = getController().createRentable("Klosterbryg", fustageGrp);
		Rentable jazzClassicFustage = getController().createRentable("Jazz Classic", fustageGrp);
		Rentable extraPilsFustage = getController().createRentable("Extra Pilsner", fustageGrp);
		Rentable celebrationFustage = getController().createRentable("Celebration", fustageGrp);
		Rentable blondieFustage = getController().createRentable("Blondie", fustageGrp);
		Rentable forårsbrygFustage = getController().createRentable("ForårsBryg", fustageGrp);
		Rentable indiaPaleFustage = getController().createRentable("India Pale Ale", fustageGrp);
		Rentable juleBrygFustage = getController().createRentable("Julebryg", fustageGrp);
		Rentable imperialFustage = getController().createRentable("Imperial Stout", fustageGrp);

		Product kulyre = getController().createProduct("Kulsyre, 6kg", kulsyreGrp);

		Product malt = getController().createProduct("Malt, 25kg sæk", maltGrp);

		Product tshirt = getController().createProduct("t-shirt", beklædningGrp);
		Product polo = getController().createProduct("Polo", beklædningGrp);
		Product cap = getController().createProduct("Cap", beklædningGrp);

		Rentable hanex1 = getController().createRentable("1-hane", anlægGrp);
		Rentable hanex2 = getController().createRentable("2-haner", anlægGrp);
		Rentable barHaner = getController().createRentable("Bar med flere haner", anlægGrp);
		Rentable levering = getController().createRentable("Levering", anlægGrp);
		Rentable krus = getController().createRentable("Krus", anlægGrp);

		Product glas = getController().createProduct("Glas", glasGrp);

		GiftPackage gave2øl2gl = getController().createGiftPackage("Gaveæske 2 øl, 2 glas", 2, sampakningerGrp);
		GiftPackage gave4øl = getController().createGiftPackage("Gaveæske 4 øl", 4, sampakningerGrp);
		GiftPackage trækas6øl = getController().createGiftPackage("Trækasse 6 øl", 6, sampakningerGrp);
		GiftPackage gave6øl2gl = getController().createGiftPackage("Gavekurv 6 øl", 6, sampakningerGrp);
		GiftPackage trækas6øl6gl = getController().createGiftPackage("Trækasse 6 øl, 6 glas", 6, sampakningerGrp);
		GiftPackage trækas12øl = getController().createGiftPackage("Trækasse 6 øl, 6 glas", 6, sampakningerGrp);
		GiftPackage papkas12øl = getController().createGiftPackage("Papkasse 6 øl, 6 glas", 6, sampakningerGrp);

		Tour rundvisningDag = getController().createTour(rundvisningGrp, "Rundvisning Dag");
		Tour rundvisningNat = getController().createTour(rundvisningGrp, "Rundvisning Aften");
		rundvisningDag.createPriceListLine(100, butik);
		rundvisningNat.createPriceListLine(120, butik);

		klippekort.createPriceListLine(100, fredagsbar);
		klippekort.createPriceListLine(100, butik);

		klosterFlaske.createPriceListLine(50, fredagsbar);
		klosterFlaske.createPriceListLine(36, butik);
		SGBFlaske.createPriceListLine(50, fredagsbar);
		SGBFlaske.createPriceListLine(36, butik);
		ePilsnerFlaske.createPriceListLine(50, fredagsbar);
		ePilsnerFlaske.createPriceListLine(36, butik);
		celebrationFlaske.createPriceListLine(50, fredagsbar);
		celebrationFlaske.createPriceListLine(36, butik);
		blondieFlaske.createPriceListLine(50, fredagsbar);
		blondieFlaske.createPriceListLine(36, butik);
		forårsbrygFlaske.createPriceListLine(50, fredagsbar);
		forårsbrygFlaske.createPriceListLine(36, butik);
		indiaPaleFlaske.createPriceListLine(50, fredagsbar);
		indiaPaleFlaske.createPriceListLine(36, butik);
		juleBrygFlaske.createPriceListLine(50, fredagsbar);
		juleBrygFlaske.createPriceListLine(36, butik);
		juletøndenFlaske.createPriceListLine(50, fredagsbar);
		juletøndenFlaske.createPriceListLine(36, butik);
		oldStrongFlaske.createPriceListLine(50, fredagsbar);
		oldStrongFlaske.createPriceListLine(36, butik);
		fregattenFlaske.createPriceListLine(50, fredagsbar);
		fregattenFlaske.createPriceListLine(36, butik);
		imperialFlaske.createPriceListLine(50, fredagsbar);
		imperialFlaske.createPriceListLine(36, butik);
		tributeFlaske.createPriceListLine(50, fredagsbar);
		tributeFlaske.createPriceListLine(36, butik);
		blackMFlaske.createPriceListLine(50, fredagsbar);
		blackMFlaske.createPriceListLine(50, butik);

		klosterFadøl.createPriceListLine(30, fredagsbar);
		jazzFadøl.createPriceListLine(30, fredagsbar);
		ePilsnerFadøl.createPriceListLine(30, fredagsbar);
		celebrationFadøl.createPriceListLine(30, fredagsbar);
		blondieFadøl.createPriceListLine(30, fredagsbar);
		forårsbrygFadøl.createPriceListLine(30, fredagsbar);
		indiaPaleFadøl.createPriceListLine(30, fredagsbar);
		juleBrygFadøl.createPriceListLine(30, fredagsbar);
		imperialFadøl.createPriceListLine(30, fredagsbar);
		specialFadøl.createPriceListLine(30, fredagsbar);
		æblebrus.createPriceListLine(15, fredagsbar);
		chips.createPriceListLine(10, fredagsbar);
		peanuts.createPriceListLine(10, fredagsbar);
		cola.createPriceListLine(15, fredagsbar);
		nikoline.createPriceListLine(15, fredagsbar);
		sevenUp.createPriceListLine(15, fredagsbar);
		vand.createPriceListLine(10, fredagsbar);

		sOASpiritus.createPriceListLine(300, fredagsbar);
		sOASpiritus.createPriceListLine(300, butik);
		sOAPindSpiritus.createPriceListLine(350, fredagsbar);
		sOAPindSpiritus.createPriceListLine(350, butik);
		whiskySpiritus.createPriceListLine(500, fredagsbar);
		whiskySpiritus.createPriceListLine(500, butik);
		lOASpiritus.createPriceListLine(175, fredagsbar);
		lOASpiritus.createPriceListLine(175, butik);

		fustageGrp.setContainerDeposit(200);
		klosterFustage.createPriceListLine(775, butik);
		klosterFustage.setKegSize(20);
		jazzClassicFustage.createPriceListLine(625, butik);
		jazzClassicFustage.setKegSize(25);
		extraPilsFustage.createPriceListLine(575, butik);
		extraPilsFustage.setKegSize(25);
		celebrationFustage.createPriceListLine(775, butik);
		celebrationFustage.setKegSize(20);
		blondieFustage.createPriceListLine(700, butik);
		blondieFustage.setKegSize(25);
		forårsbrygFustage.createPriceListLine(775, butik);
		forårsbrygFustage.setKegSize(20);
		indiaPaleFustage.createPriceListLine(775, butik);
		indiaPaleFustage.setKegSize(20);
		juleBrygFustage.createPriceListLine(775, butik);
		juleBrygFustage.setKegSize(20);
		imperialFustage.createPriceListLine(775, butik);
		imperialFustage.setKegSize(20);

		kulsyreGrp.setContainerDeposit(1000);
		kulyre.createPriceListLine(400, fredagsbar);
		kulyre.createPriceListLine(400, butik);

		malt.createPriceListLine(300, butik);

		tshirt.createPriceListLine(70, fredagsbar);
		tshirt.createPriceListLine(70, butik);
		polo.createPriceListLine(100, fredagsbar);
		polo.createPriceListLine(100, butik);
		cap.createPriceListLine(30, fredagsbar);
		cap.createPriceListLine(30, butik);

		hanex1.createPriceListLine(250, butik);
		hanex2.createPriceListLine(400, butik);
		barHaner.createPriceListLine(500, butik);
		levering.createPriceListLine(500, butik);
		krus.createPriceListLine(60, butik);

		glas.createPriceListLine(15, butik);

		gave2øl2gl.createPriceListLine(100, fredagsbar);
		gave2øl2gl.createPriceListLine(100, butik);
		gave4øl.createPriceListLine(130, fredagsbar);
		gave4øl.createPriceListLine(130, butik);
		trækas6øl.createPriceListLine(240, fredagsbar);
		trækas6øl.createPriceListLine(240, butik);
		gave6øl2gl.createPriceListLine(250, fredagsbar);
		gave6øl2gl.createPriceListLine(250, butik);
		trækas6øl6gl.createPriceListLine(290, fredagsbar);
		trækas6øl6gl.createPriceListLine(290, butik);
		trækas12øl.createPriceListLine(390, fredagsbar);
		trækas12øl.createPriceListLine(390, butik);
		papkas12øl.createPriceListLine(360, fredagsbar);
		papkas12øl.createPriceListLine(360, butik);

	}
	
// ---------------------------------------------------------------------------------------------

	/**
	 * Loads the storage (including all objects in storage).
	 */

	public void loadStorage() {
		try (FileInputStream fileIn = new FileInputStream("storage.ser")) {
			try (ObjectInputStream in = new ObjectInputStream(fileIn);) {
				storage = (Storage) in.readObject();

				System.out.println("Storage loaded from file storage.ser.");
			} catch (ClassNotFoundException ex) {
				System.out.println("Error loading storage object.");
				throw new RuntimeException(ex);
			}
		} catch (IOException ex) {
			System.out.println("Error loading storage object.");
			throw new RuntimeException(ex);
		}

	}

	public void saveStorage() {
		try (FileOutputStream fileOut = new FileOutputStream("storage.ser")) {
			try (ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
				out.writeObject(storage);
				System.out.println("Storage saved in file storage.ser.");
			}
		} catch (IOException ex) {
			System.out.println("Error saving storage object.");
			throw new RuntimeException(ex);
		}
	}

}
