package gui;

import controller.Controller;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.PriceList;
import model.ProductGroup;
import storage.Storage;

public class CreateProductWindow extends Stage {

	public CreateProductWindow(String title) {
		this.initStyle(StageStyle.UTILITY);
		this.initModality(Modality.APPLICATION_MODAL);
		this.setResizable(false);

		this.setTitle(title);
		GridPane pane = new GridPane();
		this.initContent(pane);

		Scene scene = new Scene(pane);
		this.setScene(scene);
	}

	// ----------------------------------------------------

	private TextField txfName, txfPrice;
	private ComboBox<ProductGroup> cbbProductGroup;
	private ComboBox<PriceList> cbbPriceList;
	private TextArea txaProducts;

	private void initContent(GridPane pane) {

		pane.setPadding(new Insets(10));
		pane.setHgap(10);
		pane.setVgap(10);
		pane.setGridLinesVisible(false);

		Label lblName = new Label("Indtast produkt navn");
		pane.add(lblName, 0, 0);

		txfName = new TextField();
		pane.add(txfName, 0, 1);

		Label lblProductGroup = new Label("Vælg produkt grupppe");
		pane.add(lblProductGroup, 0, 2);

		cbbProductGroup = new ComboBox<>();
		pane.add(cbbProductGroup, 0, 3);
		cbbProductGroup.getItems().setAll(Controller.getController().getProductGroup());

		Label lblPriceList = new Label("Vælg pris liste");
		pane.add(lblPriceList, 0, 4);

		cbbPriceList = new ComboBox<>();
		pane.add(cbbPriceList, 0, 5);
		cbbPriceList.getItems().setAll(Controller.getController().getPriceList());

		Button btnAddPrice = new Button("Tilføj pris");
		pane.add(btnAddPrice, 0, 6);

		txfPrice = new TextField();
		pane.add(txfPrice, 0, 7);

		Button btnAdd = new Button("Tilføj");
		pane.add(btnAdd, 0, 8);
		GridPane.setHalignment(btnAdd, HPos.RIGHT);

		Button btnCancel = new Button("Anuller");
		pane.add(btnCancel, 0, 8);
		btnCancel.setOnAction(e -> hide());

	}

}
