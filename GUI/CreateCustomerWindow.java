package gui;

import java.text.DecimalFormat;
import java.text.ParsePosition;

import controller.Controller;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Customer;
import model.Discount;

public class CreateCustomerWindow extends Stage {

	public CreateCustomerWindow(String title, Customer customer) {
		this.initStyle(StageStyle.UTILITY);
		this.initModality(Modality.APPLICATION_MODAL);
		this.setResizable(false);

		this.setTitle(title);
		GridPane pane = new GridPane();
		this.initContent(pane);

		Scene scene = new Scene(pane);
		this.setScene(scene);

		this.customer = customer;
		if (this.customer != null) {
			this.initControls();
		}

	}

	public CreateCustomerWindow(String title) {
		this(title, null);
	}

	// -------------------------------------------------------------------------

	private TextField txfName, txfTel, txfAdress, txfDiscount;
	private Label lblError;
	private CheckBox chbDiscount;
	private Customer customer;
	private DecimalFormat format = new DecimalFormat("#.0");

	private void initContent(GridPane pane) {

		pane.setPadding(new Insets(10));
		pane.setHgap(10);
		pane.setVgap(10);
		pane.setGridLinesVisible(false);

		Label lblName = new Label("Indtast kundens navn");
		pane.add(lblName, 0, 0);

		txfName = new TextField();
		pane.add(txfName, 0, 1);

		Label lblTel = new Label("Indtast telefon nr");
		pane.add(lblTel, 0, 2);

		txfTel = new TextField("+45");

		pane.add(txfTel, 0, 3);

		Label lblAdress = new Label("Indtast adresse");
		pane.add(lblAdress, 0, 4);

		txfAdress = new TextField();
		txfAdress.setPromptText("vejnavn HusNr postNr by");
		pane.add(txfAdress, 0, 5);

		Label lblDiscount = new Label("Tilføj discount");
		pane.add(lblDiscount, 0, 6);

		chbDiscount = new CheckBox();
		pane.add(chbDiscount, 0, 7);
		ChangeListener<Boolean> listener = (ov, oldValue, newValue) -> selectedDiscountChanged(newValue);
		chbDiscount.selectedProperty().addListener(listener);

		txfDiscount = new TextField();
		pane.add(txfDiscount, 0, 8);
		txfDiscount.setDisable(true);
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

		Button btnCancel = new Button("Anuller");
		pane.add(btnCancel, 0, 10);
		btnCancel.setOnAction(e -> hide());

		Button btnAdd = new Button("Tilføj kunde");
		pane.add(btnAdd, 0, 10);
		btnAdd.setTranslateX(75);
		btnAdd.setOnAction(e -> AddAction());

		lblError = new Label("");
		pane.add(lblError, 0, 9);
		lblError.setStyle("-fx-text-fill: #ee2c2c");

	}

	private void initControls() {
		txfName.setText(customer.getName());
		txfTel.setText(customer.getTelNum());
		txfAdress.setText(customer.getAdresse());
	}

	// -------------------------------------------------------------------------

	private void AddAction() {
		String telPattern = ".\\d\\d\\d\\d\\d\\d\\d\\d\\d\\d";
		String adressePattern = "\\w*\\s\\d*\\s\\d\\d\\d\\d\\s\\w*";

		String name = null;
		if (!txfName.getText().isEmpty()) {
			name = txfName.getText();
		} else {
			lblError.setText("Det indtastede matcher ikke");
			return;
		}

		String telNum = null;
		if (!txfTel.getText().isEmpty() && txfTel.getText().trim().matches(telPattern)) {
			telNum = txfTel.getText();
		} else {
			lblError.setText("Det indtastede matcher ikke");
			return;
		}

		String adresse = null;
		if (!txfAdress.getText().isEmpty() && txfAdress.getText().trim().matches(adressePattern)) {
			adresse = txfAdress.getText();
		} else {
			lblError.setText("Det indtastede matcher ikke");
			return;
		}

		double discount = 0;
		if (!txfDiscount.isDisabled() && !txfDiscount.getText().isEmpty()) {
			String s = txfDiscount.getText();
			if (s.contains(",")) {
				discount = Double.parseDouble(s.replace(',', '.'));
			} else
				discount = Double.parseDouble(s);
		}

		if (customer == null) {
			customer = Controller.getController().createCustomer(name, telNum, adresse);
			if (!txfDiscount.isDisabled() && discount > 0) {
				customer.setDiscount(new Discount(discount));
			}
		} else {
			Controller.getController().updateCustomer(customer, name, telNum, adresse);
			if (!txfDiscount.isDisabled() && discount > 0) {
				customer.setDiscount(new Discount(discount));
			}
		}
		this.hide();
	}

	private void selectedDiscountChanged(boolean checked) {
		txfDiscount.setDisable(!checked);

	}

}
