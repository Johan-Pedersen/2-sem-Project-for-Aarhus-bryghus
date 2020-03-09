package gui;

import java.text.DecimalFormat;
import java.text.ParsePosition;

import controller.Controller;
import javafx.animation.PauseTransition;
import javafx.beans.value.ChangeListener;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
import javafx.util.Duration;
import model.CalcProduct;
import model.Discount;
import model.Order;
import model.OrderLine;
import model.PaymentType;
import model.PriceList;
import model.Product;
import model.ProductGroup;
import storage.Storage;

public class FridayPane extends GridPane {

	private Button btnCreate, btnAdd, btnPlus, btnMinus, btnAddToOrder, btnDiscount, btnFinish, btnPayAll;
	private TextField txfPayment, txfAmount, txfAgreedPrice, txfDiscount;
	private static ListView<ProductGroup> lvwProductGroup;
	private static ListView<Product> lvwProducts;
	private TextArea txaOrderList;
	private VBox box;
	private ToggleGroup group;
	private PaymentType type;
	private GridPane pane1 = new GridPane();
	private Order order;
	private CalcProduct calcProduct = new CalcProduct();
	private Product product;
	private Label lblError, lblAmountLeft, lblReturn, lblCalcPrice, lblCalcVoucher;
	private DecimalFormat format = new DecimalFormat("#");
	private PriceList priceList = Controller.getController().getPriceList().get(0);
	private ImageView image1;
	private Image imagePay1, imagePay2;

	public FridayPane() {
		
		this.setPadding(new Insets(20));
		this.setHgap(20);
		this.setVgap(10);
		this.setGridLinesVisible(false);
		this.setPrefHeight(600);
		this.add(pane1, 0, 1);

		pane1.setDisable(true);
		pane1.setPadding(new Insets(10));
		pane1.setHgap(10);
		pane1.setVgap(10);
		pane1.setGridLinesVisible(false);

		btnCreate = new Button("Opret Ordre");
		this.add(btnCreate, 0, 0);
		btnCreate.setOnAction(e -> createOrderAction());

		Label lblGroup = new Label("Produkt Gruppe");
		pane1.add(lblGroup, 0, 1);

		lvwProductGroup = new ListView<>();
		pane1.add(lvwProductGroup, 0, 2);
		lvwProductGroup.setPrefHeight(100);
		lvwProductGroup.setPrefWidth(200);
		lvwProductGroup.getItems().setAll(Controller.getController().getSpecificProductGroups(priceList));
		ChangeListener<ProductGroup> listener = (ov, oldGroup, newGroup) -> this.selectedGroupChanged();
		lvwProductGroup.getSelectionModel().selectedItemProperty().addListener(listener);

		Label lblProducts = new Label("Produkter");
		pane1.add(lblProducts, 0, 3);

		lvwProducts = new ListView<>();
		pane1.add(lvwProducts, 0, 4);
		lvwProducts.setPrefHeight(100);
		lvwProducts.setPrefWidth(200);
		ChangeListener<Product> listener2 = (ov, oldProduct, newProduct) -> this.selectedProductChanged();
		lvwProducts.getSelectionModel().selectedItemProperty().addListener(listener2);

		btnMinus = new Button("-");
		pane1.add(btnMinus, 3, 4);
		btnMinus.setTranslateY(-10);
		btnMinus.setOnAction(e -> decreaseAmount());

		btnPlus = new Button("+");
		pane1.add(btnPlus, 1, 4);
		btnPlus.setTranslateY(-10);
		btnPlus.setOnAction(e -> increaseAmount());

		txfAmount = new TextField();
		pane1.add(txfAmount, 2, 4);
		txfAmount.setTranslateY(-10);
		txfAmount.setPrefWidth(30);
		ChangeListener<String> listener3 = (ov, oldValue, newValue) -> this.selectedAmountChanged();
		txfAmount.textProperty().addListener(listener3);
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

		Label lblPrice = new Label("Indtast aftalt pris:");
		pane1.add(lblPrice, 2, 4);
		lblPrice.setTranslateY(35);

		txfAgreedPrice = new TextField();
		pane1.add(txfAgreedPrice, 2, 4);
		txfAgreedPrice.setTranslateY(60);
		txfAgreedPrice.setTextFormatter(new TextFormatter<>(c -> {
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

		btnAddToOrder = new Button("Tilføj til kurv");
		pane1.add(btnAddToOrder, 2, 4);
		btnAddToOrder.setTranslateY(95);
		btnAddToOrder.setOnAction(e -> addToOrderAction());

		Label lblOrder = new Label("Kvittering");
		pane1.add(lblOrder, 4, 1);

		txaOrderList = new TextArea();
		pane1.add(txaOrderList, 4, 2, 2, 1);
		txaOrderList.setPrefWidth(350);
		txaOrderList.setEditable(false);

		Label lblTypes = new Label("Betalings typer");
		pane1.add(lblTypes, 4, 3);

		box = new VBox();
		group = new ToggleGroup();

		String[] typer = { "Dankort", "Kontant", "Klippe kort", "MobilePay", "Regning" };
		PaymentType[] type = { PaymentType.DANKORT, PaymentType.CASH, PaymentType.VOUCHER, PaymentType.MOBILEPAY,
				PaymentType.BILL };
		RadioButton rb = null;
		for (int i = 0; i < typer.length; i++) {
			rb = new RadioButton();
			rb.setToggleGroup(group);
			rb.setText(typer[i]);
			rb.setUserData(type[i]);
			box.getChildren().add(rb);
		}
		pane1.add(box, 4, 4, 1, 1);
		box.setStyle("-fx-padding: 0 0 20 0;");
		group.selectedToggleProperty().addListener(e -> toggleRadioButton());
		rb.setSelected(true);

		imagePay1 = new Image("Payments.png");
		imagePay2 = new Image("Payments2.png");
		image1 = new ImageView(imagePay2);
		image1.setFitHeight(70);
		image1.setFitWidth(20);
		pane1.add(image1, 5, 4);
		image1.setTranslateY(-15);

		Label lblAdd = new Label("Beløb betalt");
		lblAdd.setTranslateY(-20);
		pane1.add(lblAdd, 4, 5);

		txfPayment = new TextField();
		pane1.add(txfPayment, 4, 6);
		txfPayment.setTranslateY(-30);
		txfPayment.setPrefWidth(80);
		txfPayment.setTextFormatter(new TextFormatter<>(c -> {
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

		btnAdd = new Button("Tilføj betaling");
		pane1.add(btnAdd, 4, 6);
		btnAdd.setOnAction(e -> ammountPayAction());

		btnPayAll = new Button("Betal alt");
		pane1.add(btnPayAll, 4, 6);
		btnPayAll.setTranslateX(120);
		btnPayAll.setOnAction(e -> PayAllAction());

		Label lblDiscount = new Label("Indtast rabat %");
		pane1.add(lblDiscount, 0, 5);

		txfDiscount = new TextField();
		pane1.add(txfDiscount, 0, 6);
		txfDiscount.setTextFormatter(new TextFormatter<>(c -> {
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

		btnDiscount = new Button("Tilføj Rabat %");
		pane1.add(btnDiscount, 0, 7);
		btnDiscount.setOnAction(e -> addDiscountAction());

		Button btnCancel = new Button("Anuller Ordre");
		pane1.add(btnCancel, 0, 8);
		btnCancel.setStyle("-fx-border-color: #ee2c2c; -fx-border-width: 2px;");
		btnCancel.setOnAction(e -> cancelOrderAction());

		btnFinish = new Button("Færdiggør Ordre");
		pane1.add(btnFinish, 0, 8);
		GridPane.setHalignment(btnFinish, HPos.RIGHT);
		btnFinish.setStyle("-fx-border-color: #9aff9a; -fx-border-width: 2px;");
		btnFinish.setTranslateX(40);
		btnFinish.setOnAction(e -> endOrderAction());

		lblCalcPrice = new Label("Samlet pris: 0.00");
		pane1.add(lblCalcPrice, 4, 7, 2, 1);

		lblAmountLeft = new Label("Andel resterende: 0.00");
		pane1.add(lblAmountLeft, 4, 8, 2, 1);

		lblReturn = new Label("Kunden skal have: 0.00");
		pane1.add(lblReturn, 4, 9, 2, 1);

		lblCalcVoucher = new Label("Samlet klippekort: 0");
		pane1.add(lblCalcVoucher, 4, 10, 2, 1);

		lblError = new Label();
		pane1.add(lblError, 4, 11, 2, 1);
		lblError.setStyle("-fx-text-fill: #ee2c2c");

	}
	// ----------------------------------------------------------------

	private void createOrderAction() {
		if (pane1.isDisable()) {
			pane1.setDisable(false);
			image1.setImage(imagePay1);
			btnCreate.setDisable(true);
			btnFinish.setDisable(true);
			order = Controller.getController().createOrder();
		}
	}

	private void cancelOrderAction() {
		if (!pane1.isDisable()) {
			pane1.setDisable(true);
			Controller.getController().removeOrder(order);
			clear();
		}
	}

	private void addToOrderAction() {
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
			StringBuilder sb = new StringBuilder();
			for (OrderLine ol : order.getOrderLines()) {

				if (ol.getProduct().getProductGroup().getVoucherValue() > 0)
					sb.append(ol.toString() + ", Samlet klippekort: "
							+ ol.getProduct().getProductGroup().getVoucherValue() * ol.getAmount() + "\n");
				else
					sb.append(ol.toString() + "\n");
			}
			txaOrderList.setText(sb.toString());
			lblCalcPrice.setText("Samlet pris: " + order.calcPrice(priceList));
			lblCalcVoucher.setText("Samlet klippekort: " + order.calcVoucher(priceList));

			btnFinish.setDisable(false);

			if (btnAdd.isDisabled()) {
				btnAdd.setDisable(false);
				btnDiscount.setDisable(false);
			}
		} else {
			lblError.setText("Husk at vælge et produkt.");
			return;
		}
	}

	private void endOrderAction() {
		order.calcPrice(priceList);
		if (order.checkPayment()) {
			if (!pane1.isDisable()) {
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
				// hide popup after 1.5 seconds:
				PauseTransition delay = new PauseTransition(Duration.seconds(1.5));
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
			lblCalcPrice.setText("Samlet pris: " + order.calcPrice(priceList));
		}
	}

	private void ammountPayAction() {
		PaymentType pt = (PaymentType) group.getSelectedToggle().getUserData();
		double payed = 0.0;
		if (!checkVoucher(pt)) {
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
	}

	private void PayAllAction() {
		PaymentType pt = (PaymentType) group.getSelectedToggle().getUserData();
		if (pt == PaymentType.VOUCHER && checkVoucher(pt) || pt != PaymentType.VOUCHER) {
			Controller.getController().createPayment(order, Controller.getController().amountLeft(priceList, order),
					pt);
			lblAmountLeft.setText("Andel resterende: 0.00");
			if (order.checkPayment()) {
				btnAdd.setDisable(true);
				btnDiscount.setDisable(true);
				lblReturn.setText("Kunden skal have: " + order.getMoneyBack());
			}
		}
	}

	public boolean checkVoucher(PaymentType pt) {
		boolean check = true;

		for (OrderLine ol : order.getOrderLines()) {
			if (ol.getProduct().getProductGroup().getVoucherValue() == 0) {
				check = false;
			}
		}
		if (pt == PaymentType.VOUCHER && !check) {
			lblError.setText("Kan ikke betale denne ordre med klippekort");
		}
		return check;
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

	// -----------------------------------------------------------------

	private void selectedGroupChanged() {
		ProductGroup pg = lvwProductGroup.getSelectionModel().getSelectedItem();
		if (pg != null) {
			lvwProducts.getItems().setAll(pg.getProducts());
		}

	}

	private void selectedProductChanged() {
		Product p = lvwProducts.getSelectionModel().getSelectedItem();
		if (p != null) {
			txfAmount.setText("" + 1);
		}

	}

	private void selectedAmountChanged() {
		if (txfAmount.getText().contains(",")) {
			int i = txfAmount.getText().indexOf(",");
			txfAmount.setText(txfAmount.getText().substring(0, i));
		}

		if (txfAmount.getText().length() > 5)
			txfAmount.setText(txfAmount.getText().substring(0, 5));

		int amount = -1;
		try {
			amount = Integer.parseInt(txfAmount.getText().trim());
		} catch (NumberFormatException ex) {

		}
		if (amount <= 0)
			txfAmount.setText("");
	}

	public void toggleRadioButton() {
		type = (PaymentType) group.getSelectedToggle().getUserData();
	}

	private void clear() {
		pane1.setDisable(true);
		txfDiscount.clear();
		txfAmount.clear();
		txfAgreedPrice.clear();
		txfPayment.clear();
		txaOrderList.clear();
		btnAdd.setDisable(false);
		btnDiscount.setDisable(false);
		lblCalcPrice.setText("Samlet pris: 0.00");
		lblAmountLeft.setText("Andel resterende: 0.00");
		lblReturn.setText("Kunden skal have: 0.00");
		lblError.setText("");
		btnCreate.setDisable(false);
		lblCalcVoucher.setText("Samelt klippekort: 0");
		image1.setImage(imagePay2);

	}
}
