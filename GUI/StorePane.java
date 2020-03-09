package gui;

import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.time.LocalDate;

import controller.Controller;
import javafx.animation.PauseTransition;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;
import model.CalcProduct;
import model.CalcRentable;
import model.CalcTour;
import model.Condition;
import model.Customer;
import model.Discount;
import model.Order;
import model.OrderLine;
import model.PaymentType;
import model.PriceList;
import model.Product;
import model.ProductGroup;
import model.Rentable;
import model.Reservation;
import model.Tour;
import storage.Storage;

public class StorePane extends GridPane {

	private Button btnCancel, btnFinish, btnCreate, btnAddToOrder, btnAdd, btnPlus, btnMinus, btnSelect,
			btnCreateCustomer, btnDiscount, btnPayAll, btnCalcDeposit;
	private TextField txfPayment, txfAmount, txfCustomer, txfAgreedPrice, txfDiscount, txfKegSize;
	private static ListView<ProductGroup> lvwProductGroup;
	private static ListView<Product> lvwProducts;
	private TextArea txaOrderList;
	private VBox box;
	private ToggleGroup group;
	private PaymentType type;
	private DatePicker calenderFrom;
	private DatePicker calenderTo;
	private LocalDate dateFrom, dateTo;
	private GridPane pane = new GridPane();
	private Order order;
	private Product product;
	private Rentable rentable;
	private Tour tour;
	private static Customer customer;
	private Reservation reservation;
	private PriceList priceList;
	private CalcProduct calcProduct = new CalcProduct();
	private CalcRentable calcRent = new CalcRentable();
	private CalcTour calcTour = new CalcTour();
	private Label lblError, lblAmountLeft, lblReturn, lblCalcPrice;
	private DecimalFormat format = new DecimalFormat("#");
	private ImageView image1;
	private Image imagePay1, imagePay2;
	private ComboBox<PriceList> comboPL;

	public StorePane() {

		this.setPadding(new Insets(20));
		this.setHgap(20);
		this.setVgap(10);
		this.setGridLinesVisible(false);

		this.add(pane, 0, 1);
		pane.setDisable(true);
		pane.setPadding(new Insets(10));
		pane.setHgap(10);
		pane.setVgap(10);
		pane.setGridLinesVisible(false);

		btnCreate = new Button("Opret Ordre");
		this.add(btnCreate, 0, 0);
		btnCreate.setOnAction(e -> createOrderAction());

		btnSelect = new Button("Vælg Kunde");
		pane.add(btnSelect, 0, 0);
		btnSelect.setTranslateX(230);
		btnSelect.setOnAction(e -> selectCustomerAction());

		btnCreateCustomer = new Button("Opret Kunde");
		pane.add(btnCreateCustomer, 0, 0);
		btnCreateCustomer.setTranslateX(320);
		btnCreateCustomer.setOnAction(e -> createCustomerAction());

		txfCustomer = new TextField();
		pane.add(txfCustomer, 0, 0);
		txfCustomer.setTranslateX(435);
		txfCustomer.setEditable(false);

		comboPL = new ComboBox<>();
		pane.add(comboPL, 1, 1);
		comboPL.setTranslateY(35);
		ChangeListener<PriceList> listener4 = (ov, oldValue, newValue) -> priceListChanged();
		comboPL.getSelectionModel().selectedItemProperty().addListener(listener4);

		Label lblGroup = new Label("Produkt Gruppe");
		pane.add(lblGroup, 0, 1);

		lvwProductGroup = new ListView<>();
		pane.add(lvwProductGroup, 0, 2);
		lvwProductGroup.setPrefHeight(100);
		lvwProductGroup.setPrefWidth(200);
		ChangeListener<ProductGroup> listener = (ov, oldGroup, newGroup) -> this.selectedGroupChanged();
		lvwProductGroup.getSelectionModel().selectedItemProperty().addListener(listener);

		Label lblProducts = new Label("Produkter");
		pane.add(lblProducts, 0, 3);

		lvwProducts = new ListView<>();
		pane.add(lvwProducts, 0, 4);
		lvwProducts.setPrefHeight(100);
		lvwProducts.setPrefWidth(200);
		ChangeListener<Product> listener2 = (ov, oldProduct, newProduct) -> this.selectedProductChanged();
		lvwProducts.getSelectionModel().selectedItemProperty().addListener(listener2);

		ChangeListener<Product> listener3 = (ov, oldProduct, newProduct) -> this.selectSize();
		lvwProducts.getSelectionModel().selectedItemProperty().addListener(listener3);

		ChangeListener<Product> listener6 = (ov, oldProduct, newProduct) -> this.selectedTour();
		lvwProducts.getSelectionModel().selectedItemProperty().addListener(listener6);

		btnMinus = new Button("-");
		pane.add(btnMinus, 3, 4);
		btnMinus.setTranslateY(-10);
		btnMinus.setOnAction(e -> decreaseAmount());

		btnPlus = new Button("+");
		pane.add(btnPlus, 1, 4);
		btnPlus.setTranslateY(-10);
		btnPlus.setTranslateX(27);
		btnPlus.setOnAction(e -> increaseAmount());

		txfAmount = new TextField();
		pane.add(txfAmount, 2, 4);
		txfAmount.setTranslateY(-10);
		txfAmount.setPrefWidth(35);
		txfAmount.setTextFormatter(new TextFormatter<>(c -> {
			if (c.getControlNewText().isEmpty()) {
				return c;
			}

			ParsePosition parsePosition = new ParsePosition(0);
			Object object = format.parse(c.getControlNewText(), parsePosition);

			if (object == null || parsePosition.getIndex() < c.getControlNewText().length()) {
				return null;
			} else {
				return c;
			}
		}));

		Label lblKegSize = new Label("Indtast størrelse:");
		pane.add(lblKegSize, 0, 4);
		lblKegSize.setTranslateX(270);
		lblKegSize.setTranslateY(20);

		txfKegSize = new TextField();
		pane.add(txfKegSize, 2, 5);
		txfKegSize.setPrefWidth(35);
		txfKegSize.setTranslateY(-30);
		txfKegSize.setDisable(true);
		txfKegSize.setTextFormatter(new TextFormatter<>(c -> {
			if (c.getControlNewText().isEmpty()) {
				return c;
			}

			ParsePosition parsePosition = new ParsePosition(0);
			Object object = format.parse(c.getControlNewText(), parsePosition);

			if (object == null || parsePosition.getIndex() < c.getControlNewText().length()) {
				return null;
			} else {
				return c;
			}
		}));

		Label lblPrice = new Label("Indtast aftalt pris:");
		pane.add(lblPrice, 2, 5);
		lblPrice.setTranslateY(10);

		txfAgreedPrice = new TextField();
		pane.add(txfAgreedPrice, 2, 5);
		txfAgreedPrice.setTranslateY(30);

		btnAddToOrder = new Button("Tilføj til kurv");
		pane.add(btnAddToOrder, 2, 5);
		btnAddToOrder.setTranslateY(60);
		btnAddToOrder.setOnAction(e -> addToOrderAction());

		Label lblOrder = new Label("Kvittering");
		pane.add(lblOrder, 4, 1);

		txaOrderList = new TextArea();
		pane.add(txaOrderList, 4, 2, 2, 1);
		txaOrderList.setPrefWidth(350);
		txaOrderList.setEditable(false);

		Label lblTypes = new Label("Betalings typer");
		pane.add(lblTypes, 4, 3);

		box = new VBox();
		group = new ToggleGroup();
		String[] typer = { "Dankort", "Kontant", "MobilePay", "Regning" };
		PaymentType[] type = { PaymentType.DANKORT, PaymentType.CASH, PaymentType.MOBILEPAY, PaymentType.BILL };
		RadioButton rb = null;
		for (int i = 0; i < typer.length; i++) {

			rb = new RadioButton();
			rb.setToggleGroup(group);
			rb.setText(typer[i]);
			rb.setUserData(type[i]);
			box.getChildren().add(rb);
		}
		pane.add(box, 4, 4, 1, 1);
		group.selectedToggleProperty().addListener(e -> toggleRadioButton());
		rb.setSelected(true);

		imagePay1 = new Image("PaymentStore.png");
		imagePay2 = new Image("PaymentStore2.png");
		image1 = new ImageView(imagePay2);
		image1.setFitHeight(50);
		image1.setFitWidth(20);
		pane.add(image1, 5, 4);
		image1.setTranslateY(-20);

		Label lblAdd = new Label("Beløb betalt");
		lblAdd.setTranslateY(-30);
		pane.add(lblAdd, 4, 5);

		txfPayment = new TextField();
		pane.add(txfPayment, 4, 6);
		txfPayment.setTranslateY(-35);
		txfPayment.setPrefWidth(80);

		btnAdd = new Button("Tilføj betaling");
		pane.add(btnAdd, 4, 6);
		btnAdd.setOnAction(e -> ammountPayAction());

		btnPayAll = new Button("Betal alt");
		pane.add(btnPayAll, 4, 6);
		btnPayAll.setTranslateX(200);
		btnPayAll.setOnAction(e -> PayAllAction());

		btnCalcDeposit = new Button("betal pant");
		pane.add(btnCalcDeposit, 4, 6);
		btnCalcDeposit.setTranslateX(270);
		btnCalcDeposit.setOnAction(e -> payDeposit());

		Label lblFrom = new Label("Dato fra:");
		pane.add(lblFrom, 0, 5);
		calenderFrom = new DatePicker();
		pane.add(calenderFrom, 0, 5);
		calenderFrom.setTranslateY(20);
		calenderFrom.setEditable(false);

		Label lblTo = new Label("Dato til:");
		pane.add(lblTo, 0, 5);
		lblTo.setTranslateY(40);

		calenderTo = new DatePicker();
		pane.add(calenderTo, 0, 5);
		calenderTo.setTranslateY(60);
		calenderTo.setEditable(false);
		calenderTo.setDisable(true);

		final Callback<DatePicker, DateCell> dayCellFactory = new Callback<DatePicker, DateCell>() {
			@Override
			public DateCell call(final DatePicker datePicker) {
				return new DateCell() {
					@Override
					public void updateItem(LocalDate item, boolean empty) {
						super.updateItem(item, empty);

						if (item.isBefore(calenderFrom.getValue().plusDays(1))) {
							setDisable(true);
							setStyle("-fx-background-color: #ffc0cb;");
						}
					}
				};
			}
		};
		calenderTo.setDayCellFactory(dayCellFactory);
		calenderFrom.setOnAction(event);

		ChangeListener<LocalDate> listener5 = (ov, oldProduct, newProduct) -> this.selectedDate();
		calenderTo.valueProperty().addListener(listener5);
		calenderFrom.valueProperty().addListener(listener5);

		Label lblDiscount = new Label("Indtast rabat %");
		pane.add(lblDiscount, 0, 8);

		txfDiscount = new TextField();
		pane.add(txfDiscount, 0, 9);

		btnDiscount = new Button("Tilføj Rabat %");
		pane.add(btnDiscount, 0, 10);
		btnDiscount.setOnAction(e -> addDiscountAction());

		btnCancel = new Button("Anuller Ordre");
		pane.add(btnCancel, 0, 11);
		btnCancel.setStyle("-fx-border-color: #ee2c2c; -fx-border-width: 2px;");
		btnCancel.setOnAction(e -> cancelOrderAction());

		btnFinish = new Button("Færdiggør Ordre");
		pane.add(btnFinish, 0, 11);
		GridPane.setHalignment(btnFinish, HPos.RIGHT);
		btnFinish.setStyle("-fx-border-color: #9aff9a; -fx-border-width: 2px;");
		btnFinish.setTranslateX(40);
		btnFinish.setOnAction(e -> endOrderAction());

		lblCalcPrice = new Label("Samlet pris incl pant: 0.00");
		pane.add(lblCalcPrice, 4, 7, 2, 1);

		lblAmountLeft = new Label("Andel resterende: 0.00");
		pane.add(lblAmountLeft, 4, 8, 2, 1);

		lblReturn = new Label("Kunden skal have: 0.00");
		pane.add(lblReturn, 4, 9, 2, 1);

		lblError = new Label();
		pane.add(lblError, 4, 11, 2, 1);
		lblError.setStyle("-fx-text-fill: #ee2c2c");

	}

	// ----------------------------------------------------------------

	private void createOrderAction() {
		if (pane.isDisable()) {
			pane.setDisable(false);
			image1.setImage(imagePay1);
			btnCreate.setDisable(true);
			btnFinish.setDisable(true);
			order = Controller.getController().createOrder();
			comboPL.getItems().setAll(Controller.getController().getPriceListsExceptFriday());
			if (comboPL.getItems().size() != 0)
				comboPL.getSelectionModel().select(0);
		}
	}

	private void cancelOrderAction() {
		if (!pane.isDisable()) {
			pane.setDisable(true);
			btnCreate.setDisable(false);
			Controller.getController().removeOrder(order);
			clear();
		}
	}

	private void addToOrderAction() {
		product = lvwProducts.getSelectionModel().getSelectedItem();
		if (product instanceof Rentable) {
			addRentableToOrderAction();
		} else if (product instanceof Tour)
			addTour();
		else
			addProductToOrderAction();
	}

	private void addRentableToOrderAction() {
		rentable = (Rentable) lvwProducts.getSelectionModel().getSelectedItem();
		int amount = Integer.parseInt(txfAmount.getText().trim());
		if (product != null) {
			if (txfAmount.getText().trim().isEmpty()) {
				txfAmount.setText("" + 1);
			}
			if (dateFrom == null && dateTo == null) {
				lblError.setText("Tilføj dato");
				return;
			} else if (customer == null) {
				lblError.setText("Tilføj Kunde");
				return;
			} else {

				reservation = Controller.getController().createReservation(dateFrom, dateTo, customer);
				reservation.addRentable(rentable);
				lblError.setText("");
				btnFinish.setDisable(false);
			}
			OrderLine orderLine = Controller.getController().createOrderLine(order, amount, calcRent, rentable,
					priceList);
			if (rentable.getKegSize() != 0 && !txfKegSize.getText().isEmpty()) {
				orderLine.setTempKegSize(Integer.parseInt(txfKegSize.getText().trim()));
			}
			if (!txfAgreedPrice.getText().isEmpty()) {
				orderLine.setPrice(Integer.parseInt(txfAgreedPrice.getText().trim()));
			}

			StringBuilder sb = new StringBuilder();
			for (OrderLine ol : order.getOrderLines()) {
				sb.append(ol.toString() + "\n");
			}
			txaOrderList.setText(sb.toString());
			lblCalcPrice.setText("Samlet pris incl pant: " + order.calcPrice(priceList));
			rentable.setCondition(Condition.RENTED);
			calenderFrom.getEditor().clear();
			calenderTo.getEditor().clear();
		} else {
			lblError.setText("Husk at vælge et produkt.");
			return;
		}
	}

	public void addTour() {
		tour = (Tour) lvwProducts.getSelectionModel().getSelectedItem();
		int amount = Integer.parseInt(txfAmount.getText().trim());
		if (product != null) {
			if (txfAmount.getText().trim().isEmpty()) {
				txfAmount.setText("" + 1);
			}
			if (dateFrom == null) {
				lblError.setText("Tilføj dato");
				return;
			} else if (customer == null) {
				lblError.setText("Tilføj Kunde");
				return;
			} else {
				lblError.setText("");
				btnFinish.setDisable(false);
			}
			OrderLine orderLine = Controller.getController().createOrderLine(order, amount, calcTour, tour, priceList);
			Controller.getController().addDateToTour(dateFrom, tour);
			if (!txfAgreedPrice.getText().isEmpty()) {
				orderLine.setPrice(Integer.parseInt(txfAgreedPrice.getText().trim()));
			}
			StringBuilder sb = new StringBuilder();
			for (OrderLine ol : order.getOrderLines()) {
				sb.append(ol.toString() + "\n");
			}
			txaOrderList.setText(sb.toString());
			lblCalcPrice.setText("Samlet pris incl pant: " + order.calcPrice(priceList));
		} else {
			lblError.setText("Husk at vælge et produkt.");
			return;
		}

	}

	private void addProductToOrderAction() {
		product = lvwProducts.getSelectionModel().getSelectedItem();
		if (product != null) {

			if (txfAmount.getText().trim().isEmpty()) {
				txfAmount.setText("" + 1);
			}
			int amount = Integer.parseInt(txfAmount.getText().trim());
			OrderLine orderLine = Controller.getController().createOrderLine(order, amount, calcProduct, product,
					priceList);
			if (!txfAgreedPrice.getText().isEmpty()) {
				orderLine.setPrice(Integer.parseInt(txfAgreedPrice.getText().trim()));
			}

			btnFinish.setDisable(false);
			btnDiscount.setDisable(false);
			StringBuilder sb = new StringBuilder();
			for (OrderLine ol : order.getOrderLines()) {
				sb.append(ol.toString() + "\n");
			}
			txaOrderList.setText(sb.toString());
			lblCalcPrice.setText("Samlet pris: " + order.calcPrice(priceList));
		} else {
			lblError.setText("Husk at vælge et produkt.");
			return;
		}
	}

	private void endOrderAction() {
		order.calcPrice(priceList);
		boolean check = false;
		for (OrderLine ol : order.getOrderLines()) {
			if (!(ol.getProduct() instanceof Tour)) {
				if (Controller.getController().calcOrderProducts(order, priceList) <= Controller.getController()
						.amountPayed(order)) {
					check = true;
				}
			}
		}
		if (check) {
			if (!pane.isDisable()) {
				clear();
				// can use an Alert, Dialog, or PopupWindow as needed...
				Stage popup = new Stage();
				// configure UI for popup etc...
				StackPane root = new StackPane();
				Label lblTransactionEnded = new Label("Ordre aflsuttet");
				root.getChildren().add(lblTransactionEnded);
				popup.setScene(new Scene(root, 200, 100));
				popup.setY(300);
				popup.setX(500);
				// hide popup after 1 second:
				PauseTransition delay = new PauseTransition(Duration.seconds(1));
				delay.setOnFinished(e -> popup.hide());

				popup.show();
				delay.play();
			}
		} else {
			lblError.setText("Betalingen opfylder ikke samlet beløb!!");
		}
	}

	private void increaseAmount() {
		product = lvwProducts.getSelectionModel().getSelectedItem();
		if (product != null) {
			if (txfAmount.getText().trim().isEmpty())
				txfAmount.setText("" + 1);
			else {
				int amount = Integer.parseInt(txfAmount.getText().trim());
				amount++;
				txfAmount.setText("" + amount);
			}
		}
	}

	private void addDiscountAction() {
		if (txfDiscount.getText().isEmpty()) {
			lblError.setText("Indtast rabat");
		} else {
			Discount discount = new Discount(Double.parseDouble(txfDiscount.getText().trim()));
			order.setDiscount(discount);
			lblCalcPrice.setText("Samlet pris incl pant: " + order.calcPrice(priceList));
		}
	}

	private void ammountPayAction() {
		PaymentType pt = (PaymentType) group.getSelectedToggle().getUserData();
		double payed = 0.0;
		if (!txfPayment.getText().isEmpty()) {
			payed = Double.parseDouble("" + txfPayment.getText().trim());
			order.calcPrice(priceList);
			Controller.getController().createPayment(order, payed, pt);
			lblAmountLeft.setText("Andel resterende: " + Controller.getController().amountLeft(priceList, order));
			if (order.checkPayment()) {
				btnAdd.setDisable(true);
				btnDiscount.setDisable(true);
				lblReturn.setText("Kunden skal have: " + order.getMoneyBack());
			}
		} else {
			lblError.setText("Husk at tilføje et beløb");
			return;
		}
	}

	private void PayAllAction() {
		PaymentType pt = (PaymentType) group.getSelectedToggle().getUserData();
		Controller.getController().createPayment(order, Controller.getController().amountLeft(priceList, order), pt);
		lblAmountLeft.setText("Andel resterende: 0.00");
		if (order.checkPayment()) {
			btnAdd.setDisable(true);
			btnDiscount.setDisable(true);
			lblReturn.setText("Kunden skal have: " + order.getMoneyBack());
		}
	}

	private void payDeposit() {

		order.calcPrice(priceList);

		PaymentType pt = (PaymentType) group.getSelectedToggle().getUserData();
		Controller.getController().createPayment(order,
				Controller.getController().totalOrderDeposit(order.getOrderLines()), pt);
		lblAmountLeft.setText("Andel resterende: " + Controller.getController().amountLeft(priceList, order));

		btnFinish.setDisable(false);
	}

	private void decreaseAmount() {
		int amount = Integer.parseInt(txfAmount.getText().trim());
		if (txfAmount.getText().trim().isEmpty() || amount <= 1)
			return;
		else {
			amount--;
			txfAmount.setText("" + amount);
		}
	}

	private void selectCustomerAction() {
		SelectCustomerWindow dia = new SelectCustomerWindow("Vælg Kunde");
		dia.showAndWait();

		// Wait for the modal dialog to close

		customerSelected();
	}

	private void createCustomerAction() {
		CreateCustomerWindow dia = new CreateCustomerWindow("Opret Kunde");
		dia.showAndWait();

		// Wait for the modal dialog to close
	}

	// ----------------------------------------------------------------

	private void selectedTour() {
		Object object2 = lvwProducts.getSelectionModel().getSelectedItem();

		if (object2 instanceof Tour) {
			calenderTo.setDisable(true);
			calenderTo.getEditor().clear();
		} else if (object2 instanceof Rentable) {
			dateFrom = calenderFrom.getValue();
			calenderTo.setDisable(false);
		}

	}

	private void selectedGroupChanged() {
		ProductGroup pg = lvwProductGroup.getSelectionModel().getSelectedItem();
		if (pg != null) {
			lvwProducts.getItems().setAll(pg.getProducts());
		} else
			lvwProducts.getItems().setAll();

	}

	private void selectedProductChanged() {
		Product p = lvwProducts.getSelectionModel().getSelectedItem();
		if (p != null) {
			txfAmount.setText("" + 1);
		}

	}

	private void selectSize() {
		Product p = lvwProducts.getSelectionModel().getSelectedItem();
		if (p instanceof Rentable) {
			if (((Rentable) p).getKegSize() != 0) {
				txfKegSize.setDisable(false);
			}
		} else {
			txfKegSize.clear();
			txfKegSize.setDisable(true);
		}
	}

	private void selectedDate() {
		if (calenderFrom.getValue() != null && calenderTo.getValue() != null) {
			dateFrom = calenderFrom.getValue();
			dateTo = calenderTo.getValue();
		}
	}

	private void customerSelected() {
		if (customer != null) {
			txfCustomer.setText(customer.getName());
			order.setCustomer(customer);
		}
	}

	private void priceListChanged() {
		priceList = comboPL.getSelectionModel().getSelectedItem();
		lvwProductGroup.getItems().setAll(Controller.getController().getSpecificProductGroups(priceList));
	}

	// ----------------------------------------------------------------

	public static void setCustomer(Customer c) {
		customer = c;
	}

	public void toggleRadioButton() {
		type = (PaymentType) group.getSelectedToggle().getUserData();
	}

	EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
		@Override
		public void handle(ActionEvent e) {
//			Object object = lvwProductGroup.getSelectionModel().getSelectedItem();
			Object object2 = lvwProducts.getSelectionModel().getSelectedItem();

			if (object2 instanceof Tour) {
				calenderTo.setDisable(true);
				dateFrom = calenderFrom.getValue();
			} else if (object2 instanceof Rentable) {
				dateFrom = calenderFrom.getValue();
				calenderTo.setDisable(false);
			}
		}
	};

	private void clear() {
		pane.setDisable(true);
		txfDiscount.clear();
		txfAmount.clear();
		txfAgreedPrice.clear();
		txfPayment.clear();
		txfKegSize.clear();
		txfKegSize.setDisable(true);
		txaOrderList.clear();
		btnAdd.setDisable(false);
		btnDiscount.setDisable(false);
		lblCalcPrice.setText("Samlet pris incl pant: 0.00");
		lblAmountLeft.setText("Andel resterende: 0.00 ");
		lblReturn.setText("Kunden skal have: 0.00");
		dateFrom = null;
		dateTo = null;
		calenderFrom.getEditor().clear();
		calenderTo.getEditor().clear();
		customer = null;
		txfCustomer.clear();
		btnCreate.setDisable(false);
		image1.setImage(imagePay2);
	}

}
