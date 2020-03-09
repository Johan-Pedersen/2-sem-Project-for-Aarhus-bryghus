package gui;

import controller.Controller;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Customer;
import storage.Storage;

public class SelectCustomerWindow extends Stage {

	public SelectCustomerWindow(String title) {
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

	private Label lblError;
	private ListView<Customer> lvwCustomers;

	private void initContent(GridPane pane) {

		pane.setPadding(new Insets(10));
		pane.setHgap(10);
		pane.setVgap(10);
		pane.setGridLinesVisible(false);

		Label lblNames = new Label("Kunder");
		pane.add(lblNames, 0, 0);

		lvwCustomers = new ListView<>();
		pane.add(lvwCustomers, 0, 1);
		lvwCustomers.setPrefHeight(200);
		lvwCustomers.setPrefWidth(200);
		lvwCustomers.getItems().setAll(Controller.getController().getCustomers());

		Button btnCancel = new Button("Anuller");
		pane.add(btnCancel, 0, 2);
		btnCancel.setOnAction(e -> hide());

		Button btnOk = new Button("Ok");
		pane.add(btnOk, 0, 2);
		btnOk.setTranslateX(100);
		btnOk.setOnAction(e -> OkAction());

	}

	// -------------------------------------------------------------------------

	public void OkAction() {
		Customer customer = lvwCustomers.getSelectionModel().getSelectedItem();
		StorePane.setCustomer(customer);
		hide();
	}

}
