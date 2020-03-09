package gui;

import java.time.LocalDate;

import controller.Controller;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;
import model.Customer;
import model.Order;
import model.OrderLine;
import model.PriceList;
import model.ProductGroup;
import model.Rentable;
import model.Tour;
import storage.Storage;

public class AdminPane extends GridPane {

	private ListView<Object> lvwList;
	private ComboBox<String> cbbList;
	private String cbb;
	private DatePicker calenderTour;
	private Button btnCreate, btnCalcReturned;
	private Label lblError, lblCalender;

	public AdminPane() {

		this.setPadding(new Insets(20));
		this.setHgap(20);
		this.setVgap(10);
		this.setGridLinesVisible(false);

		Label lblGroup = new Label("Vælg gruppe");
		this.add(lblGroup, 0, 0);

		cbbList = new ComboBox<>();
		this.add(cbbList, 0, 1);
		cbbList.getItems().add("Kunder");
		cbbList.getItems().add("Produkt grupper");
		cbbList.getItems().add("Ordre");
		cbbList.getItems().add("Prislister");
		ChangeListener<String> listener = (ov, oldValue, newValue) -> cbbListChanged();
		cbbList.getSelectionModel().selectedItemProperty().addListener(listener);

		lvwList = new ListView<>();
		this.add(lvwList, 0, 2);
		lvwList.setPrefHeight(600);
		lvwList.setPrefWidth(400);

		btnCreate = new Button("Tilføj");
		this.add(btnCreate, 1, 1);
		btnCreate.setPrefWidth(150);
		btnCreate.setTranslateY(40);
		btnCreate.setOnAction(e -> createAction());

		Button btnUpdate = new Button("Opdatere");
		this.add(btnUpdate, 1, 1);
		btnUpdate.setPrefWidth(150);
		btnUpdate.setTranslateY(80);
		btnUpdate.setOnAction(e -> updateAction());

		Button btnDelete = new Button("Slet");
		this.add(btnDelete, 1, 1);
		btnDelete.setPrefWidth(150);
		btnDelete.setTranslateY(120);
		btnDelete.setOnAction(e -> deleteAction());

		Button btnStatistics = new Button("Statistik");
		this.add(btnStatistics, 1, 1);
		btnStatistics.setPrefWidth(150);
		btnStatistics.setTranslateY(180);
		btnStatistics.setOnAction(e -> statisticsAction());

		Button btnRented = new Button("Udlejede produkter");
		this.add(btnRented, 1, 1);
		btnRented.setPrefWidth(150);
		btnRented.setTranslateY(220);
		btnRented.setOnAction(e -> rentedAction());

		btnCalcReturned = new Button("Udregn brugte produkter");
		this.add(btnCalcReturned, 1, 1);
		btnCalcReturned.setTranslateY(270);
		btnCalcReturned.setOnAction(e -> createRentableBackWindow());

		lblError = new Label();
		this.add(lblError, 1, 1);
		lblError.setStyle("-fx-text-fill: #ee2c2c");
		lblError.setTranslateY(300);

		lblCalender = new Label("Dage med rundvisninger");
		this.add(lblCalender, 1, 1);
		lblCalender.setTranslateY(320);

		calenderTour = new DatePicker();
		this.add(calenderTour, 1, 1);
		calenderTour.setTranslateY(350);

		final Callback<DatePicker, DateCell> dayCellFactory = new Callback<DatePicker, DateCell>() {
			@Override
			public DateCell call(final DatePicker datePicker) {
				return new DateCell() {
					@Override
					public void updateItem(LocalDate item, boolean empty) {
						super.updateItem(item, empty);

						for (Order uo : Controller.getController(

						).getAllUnfinishedOrders()) {
							for (OrderLine ol : uo.getOrderLines()) {
								if (ol.getProduct() instanceof Tour) {
									Tour tour = (Tour) ol.getProduct();
									if (item.isEqual(tour.getDate()))
										this.setStyle("-fx-background-color:#7FFFD4;");
								}
							}
						}
					}
				};
			}
		};
		calenderTour.setDayCellFactory(dayCellFactory);

	}

	// --------------------------------------------------------------------------------------

	private void cbbListChanged() {
		cbb = cbbList.getSelectionModel().getSelectedItem();
		if (cbb == "Kunder") {
			lvwList.getItems().setAll(Controller.getController().getCustomers());
			btnCreate.setDisable(false);
		} else if (cbb == "Produkt grupper") {
			lvwList.getItems().setAll(Controller.getController().getProductGroup());
			btnCreate.setDisable(false);
		} else if (cbb == "Prislister") {
			lvwList.getItems().setAll(Controller.getController().getPriceList());
			btnCreate.setDisable(false);
		} else if (cbb == "Ordre") {
			lvwList.getItems().setAll(Controller.getController().getAllUnfinishedOrders());
			btnCreate.setDisable(true);
		}
	}

	private void createAction() {
		if (cbb == "Kunder") {
			CreateCustomerWindow dia = new CreateCustomerWindow("Opret Kunde");
			dia.showAndWait();

		} else if (cbb == "Produkt grupper") {
			CreateProductGroupWindow dia = new CreateProductGroupWindow("Produktgruppe");
			dia.showAndWait();
		} else if (cbb == "Prislister") {
			CreatePriceListWindow dia = new CreatePriceListWindow("Opret Prisliste");
			dia.showAndWait();
		}
		updateControls();

	}

	private void updateAction() {
		if (lvwList.getSelectionModel().getSelectedItem() != null) {
			Object selected = lvwList.getSelectionModel().getSelectedItem();
			if (cbb == "Kunder") {
				Customer c = (Customer) selected;
				CreateCustomerWindow dia = new CreateCustomerWindow("Opdater Kunde", c);
				dia.showAndWait();
			} else if (cbb == "Ordre") {
				Order o = (Order) selected;
				OrderWindow dia = new OrderWindow("Opdater Ordre", o);
				dia.showAndWait();
			} else if (cbb == "Produkt grupper") {
				ProductGroup pg = (ProductGroup) selected;
				UpdateProductGroupWindow dia = new UpdateProductGroupWindow("Opdater Produkt", pg);
				dia.showAndWait();
			} else if (cbb == "Prislister") {
				PriceList pl = (PriceList) selected;
				CreatePriceListWindow dia = new CreatePriceListWindow("Opdater Prisliste", pl);
				dia.showAndWait();
			}
		}
		updateControls();
	}

	private void deleteAction() {
		Object o = lvwList.getSelectionModel().getSelectedItem();
		if (o != null) {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setHeaderText("Er du sikker?");
			alert.showAndWait();
			if (alert.getResult().equals(ButtonType.OK)) {
				if (o instanceof Customer) {
					Controller.getController().removeCustomer((Customer) o);
				}
				if (o instanceof ProductGroup) {
					Controller.getController().removeProductGroup((ProductGroup) o);
				}
				if (o instanceof Order) {
					Controller.getController().removeOrder((Order) o);
				}
				if (o instanceof PriceList) {
					Controller.getController().removePriceList((PriceList) o);
				}

				updateControls();
			}
		}
	}

	private void statisticsAction() {

		StatisticsWindow dia = new StatisticsWindow("Statistik");
		dia.showAndWait();
	}

	private void rentedAction() {

		RentedWindow dia = new RentedWindow("Udlejede Produkter");
		dia.showAndWait();
	}

	private void updateControls() {
		if (cbb == "Kunder") {
			lvwList.getItems().setAll(Controller.getController().getCustomers());
		} else if (cbb == "Produkt grupper") {
			lvwList.getItems().setAll(Controller.getController().getProductGroup());
		} else if (cbb == "Prislister") {
			lvwList.getItems().setAll(Controller.getController().getPriceList());
		} else if (cbb == "Ordre") {
			lvwList.getItems().setAll(Controller.getController().getAllUnfinishedOrders());
		}
	}

	private void createRentableBackWindow() {
		boolean check = false;
		Order o = null;
		if (cbb == "Ordre") {
			o = (Order) lvwList.getSelectionModel().getSelectedItem();
		}

		for (OrderLine ol : o.getOrderLines()) {
			if (ol.getProduct() instanceof Rentable)
				check = true;
		}
		if (check) {
			RentableBackWindow rbw = new RentableBackWindow("Produkter tilbage leveret", o);
			rbw.showAndWait();

			lvwList.refresh();

		} else {
			lblError.setText("Ordren indeholder ikke udlejningsprodukter");
		}
	}

}
