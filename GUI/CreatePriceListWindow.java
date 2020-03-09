package gui;

import controller.Controller;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.PriceList;

public class CreatePriceListWindow extends Stage {

	public CreatePriceListWindow(String title, PriceList priceList) {

		this.priceList = priceList;
		this.initStyle(StageStyle.UTILITY);
		this.initModality(Modality.APPLICATION_MODAL);
		this.setResizable(false);

		this.setTitle(title);
		GridPane pane = new GridPane();
		this.initContent(pane);

		Scene scene = new Scene(pane);
		this.setScene(scene);

		if (this.priceList != null) {
			this.initControls();
		}

	}

	public CreatePriceListWindow(String title) {
		this(title, null);
	}

	// -------------------------------------------------------------------------

	private TextField txfName;
	private PriceList priceList;
	private Label lblError;

	private void initContent(GridPane pane) {

		pane.setPadding(new Insets(10));
		pane.setHgap(10);
		pane.setVgap(10);
		pane.setGridLinesVisible(false);

		Label lblName = new Label("Indtast navn på prisliste");
		pane.add(lblName, 0, 0);

		txfName = new TextField();
		pane.add(txfName, 0, 1);

		Button btnAdd = new Button("Ok");
		pane.add(btnAdd, 0, 2);
		GridPane.setHalignment(btnAdd, HPos.RIGHT);
		btnAdd.setOnAction(e -> OkAction());

		Button btnCancel = new Button("Anuller");
		pane.add(btnCancel, 0, 2);
		btnCancel.setOnAction(e -> hide());

		lblError = new Label("");
		pane.add(lblError, 0, 3);
		lblError.setStyle("-fx-text-fill: #ee2c2c");

	}

	// ------------------------------------------------------------------------------

	private void OkAction() {
		String name = txfName.getText().trim();
		if (priceList == null) {
			priceList = Controller.getController().createPriceList(name);
			if (name.isEmpty()) {
				lblError.setText("Tilføj navn!!");
				return;
			}
		} else {
			Controller.getController().updatePriceList(priceList, name);
		}

		hide();
	}

	private void initControls() {

		txfName.setText(priceList.getName());
	}

}
