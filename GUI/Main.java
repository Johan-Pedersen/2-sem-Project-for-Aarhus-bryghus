package gui;

import controller.Controller;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {

	public static void main(String[] args) {
		
		
		Application.launch(args);
	}

	@Override
	public void init() {
//		Controller.getController().initStorage();
		Controller.getController().loadStorage();

	}
	
	@Override
	public void start(Stage stage) {
		stage.setTitle("Aarhus Bryghus System");
		BorderPane pane = new BorderPane();

//		Controller.getController().saveStorage();
		this.initContent(pane);

		Scene scene = new Scene(pane);
		stage.setScene(scene);
		stage.show();
	}

	private static final Tab tabFriday = new Tab("Fredags bar");
	private static final Tab tabStore = new Tab("Butik");
	private static final Tab tabAdmin = new Tab("Adminstrator");

	private void initContent(BorderPane pane) {
		TabPane tabPane = new TabPane();
		this.initTabPane(tabPane);
		pane.setCenter(tabPane);
	}
	@Override
	public void stop() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setHeaderText("Do you want to save?");
		
		alert.showAndWait();
		if (alert.getResult().equals(ButtonType.OK)) {
			Controller.getController().saveStorage();
		}
	}

	private void initTabPane(TabPane tabPane) {
		tabPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);

		FridayPane fridayPane = new FridayPane();
		tabPane.getTabs().add(tabFriday);
		tabFriday.setContent(fridayPane);

		StorePane storePane = new StorePane();
		tabPane.getTabs().add(tabStore);
		tabStore.setContent(storePane);

		AdminPane adminPane = new AdminPane();
		tabPane.getTabs().add(tabAdmin);
		tabAdmin.setContent(adminPane);

	}

}
