package gui;

import java.text.DecimalFormat;
import java.text.ParsePosition;

import controller.Controller;
import javafx.beans.value.ChangeListener;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.GiftPackage;
import model.PriceList;
import model.PriceListLine;
import model.Product;
import model.ProductGroup;
import model.Rentable;
import model.Tour;
import storage.Storage;

public class CreateProductGroupWindow extends Stage {

	public CreateProductGroupWindow(String title) {
		this.initStyle(StageStyle.UTILITY);
		this.initModality(Modality.APPLICATION_MODAL);
		this.setResizable(false);

		this.setTitle(title);
		GridPane pane = new GridPane();
		this.initContent(pane);

		Scene scene = new Scene(pane);
		this.setScene(scene);
	}

	// -------------------------------------------------------------------------

	private TextField txfName, txfPrice, txfName2, txfVoucher, txfDeposit, txfBeerAmount;
	private ComboBox<ProductGroup> comBox;
	private ProductGroup productGroup;
	private Product product;
	private ListView<Product> lvwProduct;
	private ListView<PriceList> lvwPriceListGrp;
	private ListView<PriceListLine> lvwPriceListP;
	private PriceListLine priceListLine;
	private PriceList priceList;
	private DecimalFormat format = new DecimalFormat("#,###.00");
	private DecimalFormat formatInt = new DecimalFormat("#");
	private Label lblError, lblBeerAmount;
	private ComboBox<PriceList> comboPL;
	private int productStance;
	private Button btnChange, btnRemoveProdukt, btnAdd, btnCancel, btnCreate, btnRentable, btnTour, btnRemoveLinje;

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
		comBox.getSelectionModel().select(0);
		ChangeListener<ProductGroup> listener = (ov, oldValue, newValue) -> comBoxChanged();
		comBox.getSelectionModel().selectedItemProperty().addListener(listener);
		productGroup = comBox.getValue();

		Label lblDeposit = new Label("Pant");
		pane.add(lblDeposit, 0, 4);

		txfDeposit = new TextField();
		pane.add(txfDeposit, 0, 5);
		txfDeposit.setTranslateY(-30);
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

		Label lblProduct = new Label("Produkter");
		pane.add(lblProduct, 0, 6);

		lvwProduct = new ListView<>();
		pane.add(lvwProduct, 0, 7, 1, 2);
		lvwProduct.setPrefHeight(150);
		lvwProduct.setPrefWidth(100);
		ChangeListener<Product> listener2 = (ov, oldValue, newValue) -> productChanged();
		lvwProduct.getSelectionModel().selectedItemProperty().addListener(listener2);

		lvwPriceListGrp = new ListView<>();
		pane.add(lvwPriceListGrp, 2, 3, 1, 3);
		lvwPriceListGrp.setPrefHeight(150);
		lvwPriceListGrp.setPrefWidth(100);

		Label lblPll = new Label("Produktets linjer");
		pane.add(lblPll, 2, 6);

		lvwPriceListP = new ListView<>();
		pane.add(lvwPriceListP, 2, 7, 1, 2);
		lvwPriceListP.setPrefHeight(150);
		lvwPriceListP.setPrefWidth(100);
		ChangeListener<PriceListLine> listener1 = (ov, oldValue, newValue) -> priceListLineChanged();
		lvwPriceListP.getSelectionModel().selectedItemProperty().addListener(listener1);

		Label lblOpretP = new Label("Opret et nyt produkt");
		pane.add(lblOpretP, 1, 5);
		GridPane.setValignment(lblOpretP, VPos.BASELINE);
		GridPane.setHalignment(lblOpretP, HPos.CENTER);
		lblOpretP.setTranslateY(35);

		Label lblPName = new Label("Navn:");
		pane.add(lblPName, 0, 6);
		GridPane.setValignment(lblPName, VPos.TOP);
		GridPane.setHalignment(lblPName, HPos.RIGHT);
		lblPName.setTranslateY(-27);

		txfName2 = new TextField();
		pane.add(txfName2, 1, 6);
		GridPane.setValignment(txfName2, VPos.TOP);
		GridPane.setHalignment(txfName2, HPos.CENTER);
		txfName2.setTranslateY(-30);

		lblBeerAmount = new Label("Øl antal");
		pane.add(lblBeerAmount, 1, 7);
		GridPane.setHalignment(lblBeerAmount, HPos.CENTER);
		lblBeerAmount.setTranslateY(25);

		txfBeerAmount = new TextField();
		pane.add(txfBeerAmount, 1, 7);
		txfBeerAmount.setTranslateY(50);

		comboPL = new ComboBox<>();
		pane.add(comboPL, 2, 9);
		GridPane.setHalignment(comboPL, HPos.CENTER);
		ChangeListener<PriceList> listener4 = (ov, oldValue, newValue) -> priceListChanged();
		comboPL.getSelectionModel().selectedItemProperty().addListener(listener4);

		Label lblPrice = new Label("Linje pris");
		pane.add(lblPrice, 1, 8);
		GridPane.setValignment(lblPrice, VPos.BOTTOM);
		GridPane.setHalignment(lblPrice, HPos.CENTER);
		lblPrice.setTranslateY(45);

		txfPrice = new TextField();
		pane.add(txfPrice, 1, 9);
		GridPane.setHalignment(txfPrice, HPos.CENTER);
		txfPrice.setTranslateY(50);
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

		btnCreate = new Button("Opret Gruppe");
		pane.add(btnCreate, 1, 1);
		GridPane.setHalignment(btnCreate, HPos.CENTER);
		btnCreate.setOnAction(e -> createProGrpAction());

		Button btnAddList = new Button("Tilføj PrisListe");
		pane.add(btnAddList, 1, 3);
		GridPane.setHalignment(btnAddList, HPos.CENTER);
		btnAddList.setOnAction(e -> addPriceListToGrp());

		btnChange = new Button("Tilføj Produkt");
		pane.add(btnChange, 1, 7);
		btnChange.setOnAction(e -> createProductAction());
		GridPane.setHalignment(btnChange, HPos.CENTER);
		btnChange.setTranslateY(-30);

		btnRemoveProdukt = new Button("Fjern valgte Produkt");
		pane.add(btnRemoveProdukt, 1, 8);
		btnRemoveProdukt.setTranslateY(65);
		btnRemoveProdukt.setOnAction(e -> removeProduct());
		GridPane.setHalignment(btnRemoveProdukt, HPos.CENTER);

		btnAdd = new Button("Tilføj Prisliste linje");
		pane.add(btnAdd, 2, 10);
		btnAdd.setOnAction(e -> createPriseLLAction());
		GridPane.setHalignment(btnAdd, HPos.CENTER);

		btnRemoveLinje = new Button("Fjern Prisliste linje");
		pane.add(btnRemoveLinje, 2, 11);
		btnRemoveLinje.setOnAction(e -> removePriceListLine());
		GridPane.setHalignment(btnRemoveLinje, HPos.CENTER);

		btnCancel = new Button("Afslut");
		pane.add(btnCancel, 0, 11);
		btnCancel.setOnAction(e -> hide());

		lblError = new Label("");
		pane.add(lblError, 0, 11, 2, 1);
		lblError.setStyle("-fx-text-fill: green");
		lblError.setTranslateX(80);

		comBoxChanged();

	}

	private void comBoxChanged() {
		productGroup = comBox.getSelectionModel().getSelectedItem();
		txfName.clear();
		txfName2.clear();
		txfDeposit.clear();
		txfVoucher.clear();
		txfPrice.clear();
		if (productGroup != null) {
			lvwPriceListGrp.getItems().setAll(Controller.getController().getGroupPriceList(productGroup));
			if (productGroup.getName().equals("Sampakninger")) {
				txfBeerAmount.setDisable(false);
				lblBeerAmount.setDisable(false);
			} else {
				txfBeerAmount.setDisable(true);
				lblBeerAmount.setDisable(true);
			}
			if (Controller.getController().getGroupPriceList(productGroup).size() > 0) {
				comboPL.getItems().setAll(Controller.getController().getGroupPriceList(productGroup));
			} else {
				comboPL.getItems().setAll(Controller.getController().getPriceList());
			}
			comboPL.getSelectionModel().select(0);
			lvwProduct.getItems().setAll(productGroup.getProducts());
			productStance = 0;
			if (productGroup.getProducts().size() != 0) {
				lvwProduct.getSelectionModel().select(0);

				if (btnRemoveProdukt.isDisabled())
					btnRemoveProdukt.setDisable(false);

				if (lvwProduct.getItems().get(0) instanceof Tour) {
					productStance = 2;

				} else if (lvwProduct.getItems().get(0) instanceof Rentable) {
					productStance = 3;

				} else if (lvwProduct.getItems().get(0) instanceof GiftPackage) {
					productStance = 4;

				} else {
					productStance = 1;
				}
			}
		} else {
			btnRemoveProdukt.setDisable(true);
		}
		if (btnChange.isDisabled()) {
			btnChange.setDisable(false);
		}
		lblError.setText("");
	}

	private void productChanged() {
		product = lvwProduct.getSelectionModel().getSelectedItem();
		if (product != null) {
			lvwPriceListP.getItems().setAll(product.getPriceListLines());
			if (product.getPriceListLines().size() == 0) {
				btnRemoveLinje.setDisable(true);
			} else {
				if (btnRemoveLinje.isDisabled())
					btnRemoveLinje.setDisable(false);
			}
		} else {
			lvwPriceListP.getItems().setAll();
		}

	}

	private void priceListChanged() {
		priceList = comboPL.getSelectionModel().getSelectedItem();
	}

	private void priceListLineChanged() {
		priceListLine = lvwPriceListP.getSelectionModel().getSelectedItem();
	}

	private void createProGrpAction() {
		if (!txfName.getText().isEmpty()) {
			String name = txfName.getText();
			ProductGroup newPg = Controller.getController().createProductGroup(name);
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
			Controller.getController().updateProductGroup(newPg, name, voucher, deposit);
			comBox.getItems().setAll(Controller.getController().getProductGroup());
		}
	}

	private void addPriceListToGrp() {
		if (!lvwProduct.getItems().isEmpty()) {
			Alert confirmation = new Alert(AlertType.CONFIRMATION);
			confirmation.setHeaderText("Vælg en PrisListe for at tilføje den til produkt gruppen");
			GridPane grid = new GridPane();
			ComboBox<PriceList> tempBox = new ComboBox<>();
			grid.add(tempBox, 0, 0);
			tempBox.getItems().setAll(Controller.getController().getPriceListNotInGrp(productGroup));
			ChangeListener<PriceList> listener = (ov, oldValue, newValue) -> {
				priceList = tempBox.getValue();
			};
			tempBox.getSelectionModel().selectedItemProperty().addListener(listener);

			confirmation.getDialogPane().setContent(grid);
			confirmation.showAndWait();

			if (!lvwProduct.getItems().isEmpty() && confirmation.getResult().equals(ButtonType.OK)
					&& tempBox.getValue() != null) {
				Alert info = new Alert(AlertType.INFORMATION);
				info.setHeaderText("Du har nu tilføjet en priseliste til gruppen."
						+ "\n Alle produkter i gruppen får en midlertidigt linje sat til den."
						+ "\n Sæt venligst produkterne til den rigtige pris i opdatere vinduet.");
				Controller.getController().setAllPriceListLinesToList(lvwProduct.getItems(), priceList);
				info.showAndWait();
				comBoxChanged();
			}
		}
	}

	private void removeProduct() {
		if (product != null) {
			Controller.getController().removeProduct(productGroup, product);
			if (productGroup.getProducts().isEmpty()) {
				btnChange.setDisable(false);
			}
			lvwProduct.getItems().setAll(productGroup.getProducts());
		}
	}

	private void removePriceListLine() {
		if (priceListLine != null) {
			if (lvwPriceListP.getItems().size() > 1) {
				Controller.getController().removePriceListLine(product, priceListLine);
				lvwPriceListP.getItems().setAll(product.getPriceListLines());
			} else {
				lblError.setText("Et Produkt skal have en linje");
			}
		}
	}

	private void createProductAction() {
		if (!txfName2.getText().isEmpty() && !txfPrice.getText().isEmpty()) {
			String name = txfName2.getText();
			if (lvwProduct.getItems().isEmpty()) {
				firstProduct();
				btnChange.setDisable(true);
			}
			if (productStance == 1) {
				product = Controller.getController().createProduct(name, productGroup);
			} else if (productStance == 2) {
				product = Controller.getController().createTour(productGroup, name);
			} else if (productStance == 3) {
				createRentableAction(name);
			} else if (productStance == 4) {
				Controller.getController().createGiftPackage(name, Integer.parseInt(txfBeerAmount.getText().trim()),
						productGroup);
			}
			lvwProduct.getItems().setAll(productGroup.getProducts());
			lvwProduct.getSelectionModel().select(product);
			createPriseLLAction();
//			setDisable(true);
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("Produkt Navn og Linje pris skal være udfyldt, når du opretter et nyt produkt");
			alert.showAndWait();
		}
	}

	private void createRentableAction(String name) {
		Alert kegSize = new Alert(AlertType.INFORMATION);
		kegSize.setHeaderText("Set en liter størrelse, hvis den skal have en");
		GridPane grid = new GridPane();
		TextField txfKeg = new TextField();
		grid.add(txfKeg, 0, 0);
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
		kegSize.getDialogPane().setContent(grid);
		kegSize.showAndWait();
		product = Controller.getController().createRentable(name, productGroup);
		if (!txfKeg.getText().isEmpty()) {
			if (txfKeg.getText().length() > 5) {
				txfKeg.setText(txfKeg.getText().substring(0, 5));
			}
			Rentable r = (Rentable) product;
			r.setKegSize(Integer.parseInt(txfKeg.getText()));
		}
	}

	private void firstProduct() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setHeaderText("Dette er dit første produkt i en tom gruppe.\nHvilken produkttype skal den indeholde?");
		GridPane grid = new GridPane();
		CheckBox cBoxPro = new CheckBox("Normalt Produkt");
		grid.add(cBoxPro, 0, 0);
		CheckBox cBoxTour = new CheckBox("Rundvisnings Produkt");
		grid.add(cBoxTour, 2, 0);
		CheckBox cBoxRent = new CheckBox("Udlejnings Produkt");
		grid.add(cBoxRent, 4, 0);
		CheckBox cBoxGift = new CheckBox("Sampaknings Produkt");
		grid.add(cBoxGift, 6, 0);

		cBoxPro.setOnAction(e -> {
			if (cBoxPro.isSelected()) {
				cBoxRent.setSelected(false);
				cBoxGift.setSelected(false);
				cBoxTour.setSelected(false);
				productStance = 1;
			}
		});

		cBoxTour.setOnAction(e -> {
			if (cBoxTour.isSelected()) {
				cBoxRent.setSelected(false);
				cBoxGift.setSelected(false);
				cBoxPro.setSelected(false);
				productStance = 2;
			}
		});

		cBoxRent.setOnAction(e -> {
			if (cBoxRent.isSelected()) {
				cBoxPro.setSelected(false);
				cBoxGift.setSelected(false);
				cBoxTour.setSelected(false);
				productStance = 3;
			}
		});

		cBoxGift.setOnAction(e -> {
			if (cBoxGift.isSelected()) {
				cBoxRent.setSelected(false);
				cBoxPro.setSelected(false);
				cBoxTour.setSelected(false);
				productStance = 3;
			}
		});

		cBoxPro.setSelected(true);
		productStance = 1;
		alert.getDialogPane().setContent(grid);
		alert.showAndWait();
	}

	private void createPriseLLAction() {
		if (product != null) {
			double price = 0.0;
			if (!findPriceListLine(priceList)) {
				if (!txfPrice.getText().isEmpty()) {
					price = doubleConvert(txfPrice);
					Controller.getController().createPriceListLine(price, priceList, product);
					lvwPriceListP.getItems().setAll(product.getPriceListLines());
					if (lvwPriceListGrp.getItems().size() == lvwPriceListP.getItems().size()) {
//						setDisable(false);
						lvwProduct.getItems().setAll(productGroup.getProducts());
					}
					lblError.setText("Linje tilføjet");
				}
			} else
				lblError.setText("Linjen til " + priceList + " findes allerede");
		}
	}

	private double doubleConvert(TextField txf) {
		double value = 0;
		String s = txf.getText();
		if (s.contains(",")) {
			value = Double.parseDouble(s.replace(',', '.'));
		} else
			value = Double.parseDouble(s);

		return value;
	}

	/**
	 * Returns true, if the product has a pricelistline with the inserted pricelist.
	 */
	private boolean findPriceListLine(PriceList priceList) {
		boolean found = false;
		for (PriceListLine pll : product.getPriceListLines()) {
			if (pll.getPriceList().equals(priceList)) {
				found = true;
			}
		}
		return found;
	}
}
