package gui;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Order;
import model.OrderLine;
import model.Rentable;

public class RentableBackWindow extends Stage {

	public RentableBackWindow(String title, Order order) {

		this.order = order;
		this.initStyle(StageStyle.UTILITY);
		this.initModality(Modality.APPLICATION_MODAL);
		this.setResizable(false);

		this.setTitle(title);
		GridPane pane = new GridPane();
		this.initContent(pane);

		Scene scene = new Scene(pane);
		this.setScene(scene);

	}

	private ListView<OrderLine> lwOrderLines;
	private TextField txfAmount;
	private Label lblOrderLines, lblAmount, lblError, lblSuccess;
	private Button btnAmount;
	private Button btnAfbryd;

	private Order order;

	private void initContent(GridPane pane) {
		pane.setPadding(new Insets(10));
		pane.setHgap(10);
		pane.setVgap(10);
		pane.setGridLinesVisible(false);

		lwOrderLines = new ListView<OrderLine>();
		pane.add(lwOrderLines, 0, 2);
		lwOrderLines.getItems().addAll(order.getOrderLines());

		lblOrderLines = new Label("Ordre linjer");
		pane.add(lblOrderLines, 0, 0);

		lblAmount = new Label("antal");
		pane.add(lblAmount, 3, 3);

		txfAmount = new TextField();
		pane.add(txfAmount, 3, 4);

		btnAmount = new Button("Ændre mængde");
		pane.add(btnAmount, 3, 4);
		btnAmount.setTranslateY(-60);
		btnAmount.setOnAction(e -> updateAmount());

		btnAfbryd = new Button("Afslut");
		pane.add(btnAfbryd, 0, 5);
		btnAfbryd.setOnAction(e -> hide());

		lblError = new Label();
		pane.add(lblError, 0, 5);
		lblError.setTranslateX(60);
		lblError.setStyle("-fx-text-fill: #ee2c2c");

		lblSuccess = new Label();
		pane.add(lblSuccess, 0, 5);
		lblSuccess.setTranslateX(60);
		lblSuccess.setStyle("-fx-text-fill: green");

	}

	private void updateAmount() {

		if (lwOrderLines.getSelectionModel().getSelectedItem() != null && !txfAmount.getText().trim().isEmpty()) {
			OrderLine ol = lwOrderLines.getSelectionModel().getSelectedItem();
			int amount = Integer.parseInt(txfAmount.getText().trim());
			lblSuccess.setText("");
			if (ol.getProduct() instanceof Rentable) {
				if (amount < ol.getAmount()) {
					lblError.setText("");
					ol.setAmount(amount);
					order.calcPrice(order.getOrderLines().get(0).getPriceList());
					lblSuccess.setText("Ændrede succesfuld ");
				} else {
					lblError.setText("Den indtastede værdi er for høj");
				}

			} else {
				lblError.setText("Dette er ikke et udlejningsprodukt");
			}
		} else {
			lblError.setText("Husk at vælge et produkt og skrive i feltet");
		}
	}
}
