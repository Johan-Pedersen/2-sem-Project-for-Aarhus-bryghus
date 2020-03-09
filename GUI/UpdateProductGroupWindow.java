package gui;

import java.text.DecimalFormat;
import java.text.ParsePosition;

import controller.Controller;
import javafx.beans.value.ChangeListener;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Discount;
import model.Observer;
import model.PriceList;
import model.Product;
import model.ProductGroup;
import model.Rentable;
import storage.Storage;

public class UpdateProductGroupWindow extends Stage {

	public UpdateProductGroupWindow(String title, ProductGroup productGroup) {

		this.productGroup = productGroup;
		this.initStyle(StageStyle.UTILITY);
		this.initModality(Modality.APPLICATION_MODAL);
		this.setResizable(false);

		this.setTitle(title);
		GridPane pane = new GridPane();
		this.initContent(pane);

		Scene scene = new Scene(pane);
		this.setScene(scene);
		initControls();
	}
	// -------------------------------------------------------------------------

	private TextField txfName, txfPrice, txfDiscount, txfName2, txfVoucher, txfDeposit;
	private ComboBox<ProductGroup> comBox;
	private ProductGroup productGroup;
	private Product product;
	private Observer observer;
	private ListView<Product> lvwProduct;
	private ListView<Observer> lvwObserver;
	private ComboBox<PriceList> comboPL;
	private PriceList priceList;
	private DecimalFormat format = new DecimalFormat("#,###.00");
	private DecimalFormat formatInt = new DecimalFormat("#");
	private Label lblError;
	private TextField txfKeg;

	private void initContent(GridPane pane) {

		pane.setPadding(new Insets(10));
		pane.setHgap(10);
		pane.setVgap(10);
		pane.setGridLinesVisible(false);

		Label lblName = new Label("Produktgruppe navn");
		pane.add(lblName, 0, 0);

		txfName = new TextField();
		pane.add(txfName, 0, 1);

		Label lblVoucher = new Label("Klippekort værdi");
		pane.add(lblVoucher, 0, 2);

		txfVoucher = new TextField();
		pane.add(txfVoucher, 0, 3);
		txfVoucher.setTextFormatter(new TextFormatter<>(c -> {
			if (c.getControlNewText().isEmpty()) {
				return c;
			}

			ParsePosition parsePosition = new ParsePosition(0);
			Object object = formatInt.parse(c.getControlNewText(), parsePosition);

			if (object == null || parsePosition.getIndex() < c.getControlNewText().length()) {
				return null;
			} else {
				return c;
			}
		}));

		Label lblPG = new Label("Produkt gruppe");
		pane.add(lblPG, 2, 0);

		comBox = new ComboBox<>();
		pane.add(comBox, 2, 1);
		comBox.getItems().addAll(Controller.getController().getProductGroup());
		comBox.getSelectionModel().select(productGroup);
		ChangeListener<ProductGroup> listener = (ov, oldValue, newValue) -> comBoxChanged();
		comBox.getSelectionModel().selectedItemProperty().addListener(listener);

		Label lblDeposit = new Label("Pant");
		pane.add(lblDeposit, 0, 4);

		txfDeposit = new TextField();
		pane.add(txfDeposit, 0, 5);
		txfDeposit.setTextFormatter(new TextFormatter<>(c -> {
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

		Label lblPL = new Label("PrisLister");
		pane.add(lblPL, 2, 2);

		comboPL = new ComboBox<>();
		pane.add(comboPL, 2, 3);
		comboPL.getItems().addAll(Controller.getController().getGroupPriceList(productGroup));
		comboPL.getSelectionModel().select(0);
		ChangeListener<PriceList> listener4 = (ov, oldValue, newValue) -> priceListChanged();
		comboPL.getSelectionModel().selectedItemProperty().addListener(listener4);
		priceList = comboPL.getSelectionModel().getSelectedItem();

		Label lblProduct = new Label("Produkter");
		pane.add(lblProduct, 0, 6);

		lvwProduct = new ListView<>();
		pane.add(lvwProduct, 0, 7);
		lvwProduct.setPrefHeight(150);
		lvwProduct.setPrefWidth(100);
		lvwProduct.getItems().addAll(Controller.getController().getProductNotObserved(productGroup));
		ChangeListener<Product> listener2 = (ov, oldValue, newValue) -> productChanged();
		lvwProduct.getSelectionModel().selectedItemProperty().addListener(listener2);

		Label lblObsever = new Label("Produkter valgt til ændring");
		pane.add(lblObsever, 2, 6);

		lvwObserver = new ListView<>();
		pane.add(lvwObserver, 2, 7);
		lvwObserver.setPrefHeight(150);
		lvwObserver.setPrefWidth(100);
		lvwObserver.getItems().addAll(productGroup.getObservers());
		ChangeListener<Observer> listener3 = (ov, oldValue, newValue) -> ObseverChanged();
		lvwObserver.getSelectionModel().selectedItemProperty().addListener(listener3);

		Label lblPrice = new Label("Ændre Prisen");
		pane.add(lblPrice, 1, 8);
		GridPane.setHalignment(lblPrice, HPos.RIGHT);

		txfPrice = new TextField();
		pane.add(txfPrice, 2, 8);
		txfPrice.setTextFormatter(new TextFormatter<>(c -> {
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

		Label lblDiscount = new Label("Ændre Rabat %");
		pane.add(lblDiscount, 1, 9);
		GridPane.setHalignment(lblDiscount, HPos.RIGHT);

		txfDiscount = new TextField();
		pane.add(txfDiscount, 2, 9);
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

		Label lblName2 = new Label("Produkt navn");
		pane.add(lblName2, 0, 8);

		txfName2 = new TextField();
		pane.add(txfName2, 0, 9);

		txfKeg = new TextField();
		pane.add(txfKeg, 0, 10);
		txfKeg.setTextFormatter(new TextFormatter<>(c -> {
			if (c.getControlNewText().isEmpty()) {
				return c;
			}

			ParsePosition parsePosition = new ParsePosition(0);
			Object object = formatInt.parse(c.getControlNewText(), parsePosition);

			if (object == null || parsePosition.getIndex() < c.getControlNewText().length()) {
				return null;
			} else {
				return c;
			}
		}));

		Button btnProGrp = new Button("Opdater Gruppe");
		pane.add(btnProGrp, 1, 3);
		btnProGrp.setOnAction(e -> updateNameAction());

		Button btnAddAll = new Button("-->");
		pane.add(btnAddAll, 1, 7);
		btnAddAll.setOnAction(e -> quickSubscribe());
		GridPane.setValignment(btnAddAll, VPos.TOP);
		GridPane.setHalignment(btnAddAll, HPos.CENTER);

		Button btnAdd = new Button("->");
		pane.add(btnAdd, 1, 7);
		btnAdd.setOnAction(e -> subscribe());
		GridPane.setValignment(btnAdd, VPos.TOP);
		GridPane.setHalignment(btnAdd, HPos.CENTER);
		btnAdd.setTranslateY(30);

		Button btnRemove = new Button("<-");
		pane.add(btnRemove, 1, 7);
		btnRemove.setOnAction(e -> unSubscribe());
		GridPane.setHalignment(btnRemove, HPos.CENTER);

		Button btnRemoveAll = new Button("<--");
		pane.add(btnRemoveAll, 1, 7);
		btnRemoveAll.setOnAction(e -> quickUnSubscribe());
		GridPane.setHalignment(btnRemoveAll, HPos.CENTER);
		btnRemoveAll.setTranslateY(30);

		Button btnChange = new Button("Registrer Ændringer");
		pane.add(btnChange, 2, 10);
		btnChange.setOnAction(e -> changeAction());

		Button btnCancel = new Button("Afslut");
		pane.add(btnCancel, 0, 11);
		btnCancel.setOnAction(e -> hide());

		Button btnProduct = new Button("Opdater Produkt");
		pane.add(btnProduct, 1, 10);
		btnProduct.setOnAction(e -> updateProductAction());

		lblError = new Label("");
		pane.add(lblError, 2, 11, 2, 1);
		lblError.setStyle("-fx-text-fill: green");

		comBoxChanged();

	}

	private void initControls() {
		txfName.setText(productGroup.getName());
		txfVoucher.setText("" + productGroup.getVoucherValue());
		txfDeposit.setText("" + productGroup.getContainerDeposit());
	}

	private void comBoxChanged() {
		productGroup = comBox.getSelectionModel().getSelectedItem();
		if (productGroup != null) {
			lvwProduct.getItems().setAll(Controller.getController().getProductNotObserved(productGroup));
			lvwProduct.getSelectionModel().select(0);
			lvwObserver.getItems().setAll(productGroup.getObservers());
			comboPL.getItems().setAll(Controller.getController().getGroupPriceList(productGroup));
			comboPL.getSelectionModel().select(0);
			lblError.setText("");
			initControls();
		}
	}

	private void productChanged() {
		product = lvwProduct.getSelectionModel().getSelectedItem();
		if (product != null) {
			txfName2.setText(product.getName());
			if (product instanceof Rentable) {
				txfKeg.setDisable(false);
				Rentable r = (Rentable) product;
				txfKeg.setText("" + r.getKegSize());
			} else {
				txfKeg.setDisable(true);
			}
		}
	}

	private void ObseverChanged() {
		observer = lvwObserver.getSelectionModel().getSelectedItem();
	}

	private void priceListChanged() {
		priceList = comboPL.getSelectionModel().getSelectedItem();
	}

	private void subscribe() {
		if (product != null) {
			productGroup.addObserver(product);
			lvwObserver.getItems().setAll(productGroup.getObservers());
			lvwProduct.getItems().setAll(Controller.getController().getProductNotObserved(productGroup));
			lblError.setText("");
		}
	}

	private void unSubscribe() {
		if (observer != null) {
			productGroup.removeObserver(observer);
			lvwObserver.getItems().setAll(productGroup.getObservers());
			lvwProduct.getItems().setAll(Controller.getController().getProductNotObserved(productGroup));
			lblError.setText("");
		}
	}

	private void quickSubscribe() {
		if (!lvwProduct.getItems().isEmpty()) {
			Controller.getController().addAllObservers(lvwProduct.getItems(), productGroup);
			lvwObserver.getItems().setAll(productGroup.getObservers());
			lvwProduct.getItems().setAll(Controller.getController().getProductNotObserved(productGroup));
			lblError.setText("");
		}
	}

	private void quickUnSubscribe() {
		if (!lvwObserver.getItems().isEmpty()) {
			Controller.getController().removeAllObservers(lvwObserver.getItems(), productGroup);
			lvwObserver.getItems().setAll(productGroup.getObservers());
			lvwProduct.getItems().setAll(Controller.getController().getProductNotObserved(productGroup));
			lblError.setText("");
		}
	}

	private void updateNameAction() {
		if (!txfName.getText().isEmpty()) {
			String name = txfName.getText();
			int voucher = 0;
			if (!txfVoucher.getText().isEmpty()) {
				if (txfVoucher.getText().length() > 5) {
					txfVoucher.setText(txfVoucher.getText().substring(0, 5));
				}
				voucher = Integer.parseInt(txfVoucher.getText());
			}
			double deposit = 0.0;
			if (!txfDeposit.getText().isEmpty()) {
				deposit = doubleConvert(txfDeposit);
			}

			Controller.getController().updateProductGroup(productGroup, name, voucher, deposit);
			comBox.getItems().setAll(Controller.getController().getProductGroup());

		}
	}

	private void updateProductAction() {
		if (!txfName2.getText().isEmpty() && product != null) {
			String name = txfName2.getText();
			if (product instanceof Rentable) {
				Controller.getController().updateProduct(product, name);
				if (!txfKeg.getText().isEmpty()) {
					if (txfKeg.getText().length() > 5) {
						txfKeg.setText(txfKeg.getText().substring(0, 5));
					}
					Rentable r = (Rentable) product;
					r.setKegSize(Integer.parseInt(txfKeg.getText()));
				}
			} else {
				Controller.getController().updateProduct(product, name);
			}
			txfName2.clear();
			lvwProduct.getItems().setAll(Controller.getController().getProductNotObserved(productGroup));
		}
	}

	private void changeAction() {
		if (!lvwObserver.getItems().isEmpty()) {
			if (!txfPrice.getText().isEmpty()) {
				double price = doubleConvert(txfPrice);
				Controller.getController().notifyObserversPrice(productGroup, price, priceList);
				lblError.setText("Ændring registreret");
			}
			if (!txfDiscount.getText().isEmpty()) {
				double discount = doubleConvert(txfDiscount);
				Controller.getController().notifyObserversDiscount(productGroup, new Discount(discount), priceList);
				lblError.setText("Ændring registreret");
			}
		}
	}

	private double doubleConvert(TextField txf) {
		double value = 0;
		String s = txf.getText();
		if (s.length() > 5) {
			s = s.substring(0, 5);
		}
		if (s.contains(",")) {
			value = Double.parseDouble(s.replace(',', '.'));
		} else
			value = Double.parseDouble(s);

		return value;
	}
}
