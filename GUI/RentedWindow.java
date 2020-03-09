package gui;

import controller.Controller;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class RentedWindow extends Stage {

	public RentedWindow(String title) {
		this.initStyle(StageStyle.UTILITY);
		this.initModality(Modality.APPLICATION_MODAL);
		this.setResizable(false);

		this.setTitle(title);
		GridPane pane = new GridPane();
		this.initContent(pane);

		Scene scene = new Scene(pane);
		this.setScene(scene);
	}
	
	private TextArea txaRented;
	
	private void initContent(GridPane pane) {

		pane.setPadding(new Insets(10));
		pane.setHgap(10);
		pane.setVgap(10);
		pane.setGridLinesVisible(false);
		
		Label lblOverview = new Label("Udlejede Produkter:");
		pane.add(lblOverview, 0, 0);

		txaRented = new TextArea();
		pane.add(txaRented, 0, 1, 1, 5);
		txaRented.setPrefWidth(500);
		txaRented.setEditable(false);
		txaRented.setText("" +Controller.getController().getAllRentedProducts());
		
		Button btn = new Button("OK");
		pane.add(btn, 0, 6);
		btn.setOnAction(e -> okAction());
		GridPane.setHalignment(btn, HPos.CENTER);
	}

	private void okAction() {
		this.close();
	}
}
