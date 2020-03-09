package gui;

import java.time.LocalDate;
import java.util.function.Predicate;

import controller.Controller;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Order;

public class StatisticsWindow extends Stage {

	public StatisticsWindow(String title) {
		this.initStyle(StageStyle.UTILITY);
		this.initModality(Modality.APPLICATION_MODAL);
		this.setResizable(false);

		this.setTitle(title);
		GridPane pane = new GridPane();
		this.initContent(pane);

		Scene scene = new Scene(pane);
		this.setScene(scene);
	}

	private TextArea txaOrders, txaStatistics;
	private LocalDate date;
	private ComboBox<String> comBox;
	private String sBox;
	private TextField txf;
	private Label lblDate, lblError, lblOverview;
	private DatePicker dPicker;
	private Controller controller = Controller.getController();

	private void initContent(GridPane pane) {

		pane.setPadding(new Insets(10));
		pane.setHgap(10);
		pane.setVgap(10);
		pane.setGridLinesVisible(false);

		lblOverview = new Label("Overview");
		pane.add(lblOverview, 0, 0);

		txaOrders = new TextArea();
		pane.add(txaOrders, 0, 1, 1, 6);
		txaOrders.setPrefWidth(500);
		txaOrders.setEditable(false);
		txaOrders.setPrefHeight(400);

		Label lblStat = new Label("Statistik:");
		pane.add(lblStat, 0, 7);

		txaStatistics = new TextArea();
		pane.add(txaStatistics, 0, 8, 1, 4);
		txaStatistics.setPrefWidth(500);
		txaStatistics.setEditable(false);
		txaStatistics.setPrefHeight(150);

		Label lblBox = new Label("Vælg Statistik");
		pane.add(lblBox, 1, 0);

		comBox = new ComboBox<>();
		pane.add(comBox, 1, 1);
		comBox.getItems().add("Daglig");
		comBox.getItems().add("Månedlig");
		comBox.getItems().add("Årlig");
		comBox.getSelectionModel().select(0);
		ChangeListener<String> listener = (ov, oldValue, newValue) -> comBoxChanged();
		comBox.getSelectionModel().selectedItemProperty().addListener(listener);
		sBox = comBox.getSelectionModel().getSelectedItem();

		lblDate = new Label("Vælg en Dato");
		pane.add(lblDate, 1, 2);

		dPicker = new DatePicker();
		pane.add(dPicker, 1, 3);

		Button btn = new Button("Vis Statistik");
		pane.add(btn, 1, 4);
		btn.setOnAction(e -> createStatistic());

		lblError = new Label("");
		pane.add(lblError, 1, 5);
		lblError.setStyle("-fx-text-fill: #ee2c2c");
	}

	private void comBoxChanged() {
		sBox = comBox.getSelectionModel().getSelectedItem();
	}

	private void createStatistic() {
		if (dPicker.getValue() == null) {
			lblError.setText("Vælg en Dato");
		}

		if (sBox == "Daglig") {
			date = dPicker.getValue();
			Predicate<Order> filter = o -> o.getDate().equals(date);
			StringBuilder sb = new StringBuilder();
			lblOverview.setText("Oversigt: " + date);
			print(filter, sb);
		} else if (sBox == "Månedlig") {
			date = dPicker.getValue();
			Predicate<Order> filter = o -> o.getDate().getMonth().equals(date.getMonth());
			StringBuilder sb = new StringBuilder();
			lblOverview.setText("Oversigt: " + date.getMonth() + " / " + date.getYear());
			print(filter, sb);
		} else if (sBox == "Årlig") {
			date = dPicker.getValue();
			Predicate<Order> filter = o -> o.getDate().getYear() == date.getYear();
			StringBuilder sb = new StringBuilder();
			lblOverview.setText("Oversigt: " + date.getYear());
			print(filter, sb);
		} else
			return;
	}

	private void print(Predicate<Order> filter, StringBuilder sb) {

		txaOrders.setText("Order:\n" + controller.getPeriodicSale(filter));

		sb.append("\nTotal indtjeneste: " + controller.calcTotalPayment(filter) + "kr.");
		sb.append("\nAntal af produkter solgt: " + controller.calcTotalProductsSold(filter));
		sb.append("\nAntal af Fadøl solgt: " + controller.calcTotalDraftBeerSold(filter));
		sb.append("\nAntal af Flasker solgt: " + controller.calcTotalBeerBottlesSold(filter));
		sb.append("\nAntal af Klippekort solgt: " + controller.calcTotalVouchersSold(filter));
		sb.append("\nAntal af Klip brugt på at købe vare med: " + controller.calcTotalVoucherUsed(filter));
		sb.append("\nAntal af øl solgt via gaveæsker: " + controller.beerSoldInPackages(filter));

		txaStatistics.setText(sb.toString());
	}

}
