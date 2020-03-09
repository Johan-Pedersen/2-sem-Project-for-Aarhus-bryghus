package gui;

import controller.Controller;
import javafx.animation.PauseTransition;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import model.Order;
import model.OrderLine;
import model.PaymentType;
import model.PriceList;
import model.Tour;

public class OrderWindow extends Stage {

	private Button btnFinish, btnAdd, btnPayAll;
	private TextField txfPayment;
	private TextArea txaOrderList;
	private VBox box;
	private ToggleGroup group;
	private PaymentType type;
	private GridPane pane = new GridPane();
	private Order order;
	private PriceList priceList;
	private Label lblError, lblAmountLeft, lblReturn, lblCalcPrice, lblDeposit;
	private ImageView image1;
	private Image imagePay1;

	public OrderWindow(String title, Order order) {

		this.order = order;
		this.initStyle(StageStyle.UTILITY);
		this.initModality(Modality.APPLICATION_MODAL);
		this.setResizable(false);

		this.setTitle(title);
		GridPane pane = new GridPane();
		this.initContent(pane);

		Scene scene = new Scene(pane);
		this.setScene(scene);

		pane.setPadding(new Insets(10));
		pane.setHgap(10);
		pane.setVgap(10);
		pane.setGridLinesVisible(false);
	}

	private void initContent(GridPane pane) {

		this.priceList = order.getOrderLines().get(0).getPriceList();

		Label lblOrder = new Label("Kvittering");
		pane.add(lblOrder, 4, 1);

		txaOrderList = new TextArea();
		pane.add(txaOrderList, 4, 2, 2, 1);
		txaOrderList.setPrefWidth(350);
		txaOrderList.setEditable(false);

		updateList();

		Label lblTypes = new Label("Betalings typer");
		pane.add(lblTypes, 4, 3);

		box = new VBox();
		group = new ToggleGroup();
		String[] typer = { "Dankort", "Kontant", "MobilePay", "Regning" };
		PaymentType[] type = { PaymentType.DANKORT, PaymentType.CASH, PaymentType.MOBILEPAY, PaymentType.BILL };
		RadioButton rb = null;
		for (int i = 0; i < typer.length; i++) {

			rb = new RadioButton();
			rb.setToggleGroup(group);
			rb.setText(typer[i]);
			rb.setUserData(type[i]);
			box.getChildren().add(rb);
		}
		pane.add(box, 4, 4, 1, 1);
		group.selectedToggleProperty().addListener(e -> toggleRadioButton());
		rb.setSelected(true);

		imagePay1 = new Image("PaymentStore.png");
		image1 = new ImageView(imagePay1);
		image1.setFitHeight(50);
		image1.setFitWidth(20);
		pane.add(image1, 5, 4);
		image1.setTranslateY(-20);

		Label lblAdd = new Label("Beløb betalt");
		lblAdd.setTranslateX(-70);
		pane.add(lblAdd, 4, 5);

		txfPayment = new TextField();
		pane.add(txfPayment, 4, 6);
		txfPayment.setTranslateY(-35);
		txfPayment.setPrefWidth(80);

		btnAdd = new Button("Tilføj betaling");
		pane.add(btnAdd, 4, 6);
		btnAdd.setOnAction(e -> ammountPayAction());

		btnPayAll = new Button("Betal alt");
		pane.add(btnPayAll, 4, 6);
		btnPayAll.setTranslateX(200);
		btnPayAll.setOnAction(e -> PayAllAction());

		btnFinish = new Button("Færdiggør Ordre");
		pane.add(btnFinish, 0, 11);
		GridPane.setHalignment(btnFinish, HPos.RIGHT);
		btnFinish.setStyle("-fx-border-color: #9aff9a; -fx-border-width: 2px;");
		btnFinish.setOnAction(e -> endOrderAction());

		lblCalcPrice = new Label("Samlet pris incl pant: 0.00");
		pane.add(lblCalcPrice, 4, 7, 2, 1);
		lblCalcPrice.setText("Samlet pris incl pant: " + order.calcPrice(priceList));

		lblAmountLeft = new Label("Andel resterende: 0.00");
		pane.add(lblAmountLeft, 4, 8, 2, 1);
		lblAmountLeft.setText("Andel resterende: " + Controller.getController().amountLeft(priceList, order));

		lblReturn = new Label();
		pane.add(lblReturn, 4, 9, 2, 1);
		lblReturn.setText("Kunden skal have: " + order.getMoneyBack());

		lblDeposit = new Label("Pant tilbage: "
				+ (order.calcPrice(priceList) - Controller.getController().amountLeft(priceList, order)));
		pane.add(lblDeposit, 4, 10, 2, 1);
		lblError = new Label();
		pane.add(lblError, 4, 11, 2, 1);
		lblError.setStyle("-fx-text-fill: #ee2c2c");
		lblError.setTranslateX(80);

	}

	// ----------------------------------------------------------------

	private void endOrderAction() {

		if (check()) {
			clear();
			// can use an Alert, Dialog, or PopupWindow as needed...
			Stage popup = new Stage();
			// configure UI for popup etc...
			StackPane root = new StackPane();
			Label lblTransactionEnded = new Label("Ordre aflsuttet");
			root.getChildren().add(lblTransactionEnded);
			popup.setScene(new Scene(root, 200, 100));
			popup.setY(300);
			popup.setX(500);
			// hide popup after 1 second:
			PauseTransition delay = new PauseTransition(Duration.seconds(1));
			delay.setOnFinished(e -> popup.hide());

			popup.show();
			delay.play();

			if (check()) {
				this.hide();
			} else {
				lblError.setText("Betalingen opfylder ikke samlet beløb!!");
			}
		} else {
			lblError.setText("Betalingen opfylder ikke samlet beløb!!");
		}
	}

	public boolean check() {
		order.calcPrice(priceList);

		boolean check = false;
		for (OrderLine ol : order.getOrderLines()) {
			if (!(ol.getProduct() instanceof Tour)) {
				if (Controller.getController().calcOrderProducts(order, priceList) <= Controller.getController()
						.amountPayed(order)) {
					check = true;
				}

			}
		}
		return check;
	}

	private void ammountPayAction() {
		PaymentType pt = (PaymentType) group.getSelectedToggle().getUserData();
		double payed = 0.0;
		if (!txfPayment.getText().isEmpty()) {
			payed = Double.parseDouble("" + txfPayment.getText().trim());
			order.calcPrice(priceList);
			Controller.getController().createPayment(order, payed, pt);
			lblAmountLeft.setText("Andel resterende: " + Controller.getController().amountLeft(priceList, order));
			if (order.checkPayment()) {
				btnAdd.setDisable(true);
				lblReturn.setText("Kunden skal have: " + order.getMoneyBack());
			}
		} else {
			lblError.setText("Husk at tilføje et beløb");
			return;
		}
	}

	private void PayAllAction() {
		PaymentType pt = (PaymentType) group.getSelectedToggle().getUserData();
		Controller.getController().createPayment(order, Controller.getController().amountLeft(priceList, order), pt);
		lblAmountLeft.setText("Andel resterende: 0.00");
		if (order.checkPayment()) {
			btnAdd.setDisable(true);
			lblReturn.setText("Kunden skal have: " + order.getMoneyBack());

			updateList();

		}
	}

	// ----------------------------------------------------------------

	public void toggleRadioButton() {
		type = (PaymentType) group.getSelectedToggle().getUserData();
	}

	private void clear() {
		txfPayment.clear();
		txaOrderList.clear();
		lblCalcPrice.setText("Samlet pris incl pant: 0.00");
		lblAmountLeft.setText("Andel resterende: 0.00 ");
		lblReturn.setText("Kunden skal have: 0.00");
	}

	private void updateList() {

		order.calcPrice(priceList);

		StringBuilder sb = new StringBuilder();
		for (OrderLine ol : order.getOrderLines()) {
			sb.append(ol.toString() + "\n");
		}
		txaOrderList.setText(sb.toString());
	}
}
