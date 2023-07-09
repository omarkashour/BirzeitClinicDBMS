
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AppointmentScene extends BorderPane {

	private ArrayList<Appointment> data;
	private ObservableList<Appointment> dataList;
	TableView<Appointment> myTable;
	ScrollPane sideBox;
	FXMLLoader fxmlLoader;
	private String dbURL;
	private String dbUsername = "root";
	private String dbPassword = "Omar_911!";
	private String URL = "127.0.0.1";
	private String port = "3306";
	private String dbName = "bzuClinic";
	TabPane tabPane;
	Tab viewAppointmentsTab;
	private Connection con;
	CalendarController cn = new CalendarController();

	private HBox createNotificationBox(Appointment x) {
		ImageView iv = new ImageView(new Image("user.png"));
		VBox vbox1 = new VBox(10);

		Label pName = new Label(getName(x.getPatient_id()));
		// pName.setStyle("-fx-font-family: 'Consolas'; -fx-font-size: 14px;
		// -fx-font-weight: bold; -fx-text-fill: black;");
		Label date = new Label(x.getAp_date());
		Label duration = new Label(x.getAp_time() + ">" + x.getDuration());
		duration.setStyle(
				"-fx-font-family: 'Product Sans'; -fx-text-fill: white;-fx-font-size:14.0px; -fx-font-weight: bold;");

		vbox1.getChildren().addAll(pName, date, duration);
		VBox vbox2 = new VBox(10);

		Label phme = new Label(getPhName(x.getPhys_id()));

		pName.setStyle(
				"-fx-font-family: 'Product Sans'; -fx-text-fill: white;-fx-font-size:14.0px; -fx-font-weight: bold;");

		date.setStyle(
				"-fx-font-family: 'Product Sans'; -fx-text-fill: white;-fx-font-size:14.0px; -fx-font-weight: bold;");

		phme.setStyle(
				"-fx-font-family: 'Product Sans'; -fx-text-fill: white;-fx-font-size:14.0px; -fx-font-weight: bold;");

		Label idLabel = new Label("id: " + Integer.toString(x.getPhys_id()));
		idLabel.setStyle(
				"-fx-font-family: 'Product Sans'; -fx-text-fill: white;-fx-font-size:14.0px; -fx-font-weight: bold;");

		Label reasonLabel = new Label(x.getAp_reason());
		reasonLabel.setStyle(
				"-fx-font-family: 'Product Sans'; -fx-text-fill: white;-fx-font-size:14.0px; -fx-font-weight: bold;");
		Label phyId = new Label("id : " + Integer.toString(x.getPhys_id()));
		phyId.setStyle(
				"-fx-font-family: 'Product Sans'; -fx-text-fill: white;-fx-font-size:14.0px; -fx-font-weight: bold;");
		Label reson = new Label(x.getAp_reason());
		reson.setStyle(
				"-fx-font-family: 'Product Sans'; -fx-text-fill: white;-fx-font-size:14.0px; -fx-font-weight: bold;");

		vbox2.getChildren().addAll(phme, phyId, reson);
		Button confirm = new Button("Confirm");
		Button decline = new Button("Decline");
		confirm.setMinWidth(50);
		confirm.setMinHeight(20);
		decline.setMinWidth(50);
		decline.setMinHeight(20);

		// Apply styles
		HBox notificationBox = new HBox(20);
		notificationBox.setStyle("-fx-background-color: rgba(0, 0, 0, 0.2); -fx-background-radius: 5;");
		notificationBox.setMaxWidth(600);

		VBox buttonBox = new VBox(10);
		buttonBox.setStyle("-fx-background-color: rgba(255, 0, 0, 0.2); -fx-background-radius: 5;");
		confirm.setStyle("-fx-background-color: rgba(0, 255, 0, 0.2); -fx-background-radius: 5;");
		decline.setStyle("-fx-background-color: rgba(255, 0, 0, 0.2); -fx-background-radius: 5;");

		// Hover effect on buttons
		confirm.setOnMouseEntered(
				e -> confirm.setStyle("-fx-background-color: rgba(0, 255, 0, 0.5); -fx-background-radius: 5;"));
		confirm.setOnMouseExited(
				e -> confirm.setStyle("-fx-background-color: rgba(0, 255, 0, 0.2); -fx-background-radius: 5;"));

		decline.setOnMouseEntered(
				e -> decline.setStyle("-fx-background-color: rgba(255, 0, 0, 0.5); -fx-background-radius: 5;"));
		decline.setOnMouseExited(
				e -> decline.setStyle("-fx-background-color: rgba(255, 0, 0, 0.2); -fx-background-radius: 5;"));

		confirm.setOnAction(event -> {
			updateStatus(x.getAp_id(), "confirmed");
			decline.setVisible(false); // Hide the decline button
			confirm.setText("Confirmed"); // Update the text of the confirm button
			confirm.setDisable(true);
			try {
				getData(); // Fetch data from the database and populate the 'data' list
				dataList = FXCollections.observableArrayList(data);
			} catch (ClassNotFoundException | SQLException e1) {
				e1.printStackTrace();
			}
			myTable.getItems().clear();// Disable the confirm button
			myTable.setItems(dataList);
			ReportTab.refreshCards();
			DashBoard.refreshDashBoard();
		});

		decline.setOnAction(event -> {
			updateStatus(x.getAp_id(), "Canceled");
			confirm.setVisible(false); // Hide the decline button
			decline.setText("Canceled"); // Update the text of the confirm button
			decline.setDisable(true); // Disable the confirm button
			try {
				getData(); // Fetch data from the database and populate the 'data' list
				dataList = FXCollections.observableArrayList(data);
			} catch (ClassNotFoundException | SQLException e1) {
				e1.printStackTrace();
			}
			myTable.getItems().clear();// Disable the confirm button
			myTable.setItems(dataList);
			ReportTab.refreshCards();
			DashBoard.refreshDashBoard();
		});

		buttonBox.getChildren().addAll(confirm, decline);

		// Font style for labels
//	    pName.setStyle("-fx-font-family: Consolas;");
//	    date.setStyle("-fx-font-family: Consolas;");
//	    phme.setStyle("-fx-font-family: Consolas;");

		notificationBox.getChildren().addAll(iv, vbox1, vbox2, buttonBox);

		return notificationBox;
	}

	private VBox createNotificationBox(List<Appointment> appointments) {
		VBox notificationBox = new VBox(10);
		notificationBox.setMaxWidth(600);

		for (Appointment appointment : appointments) {
			HBox hbox = createNotificationBox(appointment);
			notificationBox.getChildren().add(hbox);
		}

		return notificationBox;
	}

	private VBox createViewAppointmentsPane() {
		VBox vb = new VBox(10);
		cn = new CalendarController();
		fxmlLoader = new FXMLLoader(getClass().getResource("calendar.fxml"));
		TextField searchField = new TextField();
		searchField.setPromptText("ENTER PATIENT NAME OR ID");
		searchField.setPrefWidth(300);

		// Apply transparent background color to the text field
		searchField.setStyle("-fx-background-color: rgba(0, 0, 0, 0.1); -fx-text-fill: white;");

		Button searchButton = new Button("Search");
		searchButton.setStyle("-fx-font-size: 12px; -fx-pref-width: 100px;");

		// Apply a wider width to the search button
		searchButton.setPrefWidth(60);

		HBox searchBox = new HBox(10);
		searchBox.getChildren().addAll(searchField, searchButton);

		ImageView iv = new ImageView(new Image("search.png"));
		iv.setFitWidth(16);
		iv.setFitHeight(16);

		searchButton.setGraphic(iv);
		searchButton.setOnAction(e -> {
			if (!searchField.getText().isEmpty()) {
				String searchText = searchField.getText();

				// Check if the text contains only digits
				if (searchText.matches("\\d+")) {
					System.out.println("Search text contains only digits");
					displayNotification(
							createNotificationBox(searchAppointment(Integer.valueOf(searchField.getText()))));
				}

				// Check if the text contains only letters
				if (searchText.matches("[a-zA-Z]+")) {
					System.out.println("Search text contains only letters");
					displayNotification(createNotificationBox(searchAppointmentsByName(searchField.getText())));

				}
			}
		});

		searchBox.setPrefWidth(200);
		searchBox.setPrefHeight(30);
		searchBox.setPadding(new Insets(10));

		// Apply background color to the searchBox
		// searchBox.setStyle("-fx-background-color: lightgray;");
		try {
			sideBox = new ScrollPane();
			HBox gather = new HBox(5);
			// gather.FFEADD
			gather.setMargin(gather, new Insets(10, 10, 10, 10));

			gather.getChildren().addAll(fxmlLoader.load(), sideBox);
			vb.setMargin(gather, new Insets(10, 10, 10, 10));
			vb.getChildren().addAll(searchBox, gather);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		vb.setStyle("-fx-background-color: #FCAEAE;");
		return vb;

	}

	private HBox createCreateNewPane() {

		try {
			getData(); // Fetch data from the database and populate the 'data' list
			dataList = FXCollections.observableArrayList(data);
		} catch (ClassNotFoundException | SQLException e1) {
			e1.printStackTrace();
		}
		myTable.getItems().clear();// Disable the confirm button
		myTable.setItems(dataList);
		Label createNewLabel = new Label("Make An Appointment");
		createNewLabel.setStyle(
				"-fx-font-family: 'Consolas'; -fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #333333;");
		TextField patient_id = new TextField();
		TextField phys_id = new TextField();
		TextField ap_reason = new TextField();

		Label time = new Label("hour : minute");
		time.setStyle(
				"-fx-font-family: 'Consolas'; -fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: #333333;");

		ComboBox<Integer> hourComboBox = new ComboBox<>();
		hourComboBox.getItems().addAll(8, 9, 10, 11, 12, 13, 14, 15, 16);
		hourComboBox.getStyleClass().add("my-combo-box");
		ComboBox<Integer> minuteComboBox = new ComboBox<>();
		minuteComboBox.getStyleClass().add("my-combo-box");
		minuteComboBox.getItems().addAll(0, 15, 30, 45);
		HBox timeBox = new HBox(10);

		TextField cost = new TextField();
		TextField duration = new TextField();

		// Set prompt text for text fields
		patient_id.setPromptText("Patient ID");
		phys_id.setPromptText("Physician ID");
		ap_reason.setPromptText("Appointment Reason");

		cost.setPromptText("Cost");
		duration.setPromptText("Duration");

		// Set stroke effect for text fields
		String strokeStyle = "-fx-border-color: black; -fx-border-width: 1px;";

		String roundedStyle = "-fx-border-radius: 5px; -fx-background-radius: 10px; -fx-background-color: rgba(128, 128, 128, 0.2);";
		patient_id.setStyle(strokeStyle + roundedStyle);
		phys_id.setStyle(strokeStyle + roundedStyle);
		ap_reason.setStyle(strokeStyle + roundedStyle);
		cost.setStyle(strokeStyle + roundedStyle);
		duration.setStyle(strokeStyle + roundedStyle);
		CalendarApp app = new CalendarApp();
		VBox vbox1 = new VBox(5);
		vbox1.getChildren().addAll(patient_id, phys_id);
		VBox vbox2 = new VBox(5);
		vbox2.getChildren().addAll(ap_reason);
		VBox vbox3 = new VBox(5);
		vbox3.getChildren().addAll(cost, duration);
		HBox all = new HBox(10);
		Button submit = new Button("Add");
		submit.setOnAction(e -> {

			if (app.getSelectedDate() == null || patient_id.getText().isEmpty() || phys_id.getText().isEmpty()
					|| ap_reason.getText().isEmpty() || hourComboBox.getValue() == null
					|| minuteComboBox.getValue() == null || cost.getText().isEmpty() || duration.getText().isEmpty())

			{
				showEmptyInputWarning();
			}
			// String patientId, String physicianId, String reason, String date, String
			// hour, String minute, String cost, String duration
			else if (validateInputs(patient_id.getText(), phys_id.getText(), ap_reason.getText(),
					hourComboBox.getValue().toString(), minuteComboBox.getValue().toString(), cost.getText(),
					duration.getText()) == false) {
				showInvalidInput();
			} else {

				LocalDate selectedDate = app.getSelectedDate();
				System.out.println("Selected Date: " + selectedDate);
				Appointment appointment = new Appointment(Integer.parseInt(patient_id.getText()),
						Integer.parseInt(phys_id.getText()), ap_reason.getText(), selectedDate.toString(),
						hourComboBox.getValue() + ":" + minuteComboBox.getValue() + ":00", "Confirmed",
						Double.parseDouble(cost.getText()), duration.getText());
				showPaymentStage(appointment);
				insertData(appointment);
				data.add(appointment);
				dataList.add(appointment);
				myTable.refresh();
				patient_id.clear();
				phys_id.clear();
				ap_reason.clear();
				hourComboBox.setValue(null);
				minuteComboBox.setValue(null);
				cost.clear();
				duration.clear();
				// cn=new CalendarController();

				viewAppointmentsTab.setContent(createViewAppointmentsPane());
			}

		});
		timeBox.getChildren().addAll(hourComboBox, time, minuteComboBox, submit);
		all.getChildren().addAll(vbox1, vbox2, vbox3);

		Button deleteBtn = new Button("delete Appointment");
		deleteBtn.setPrefWidth(300);
		deleteBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {

				int selectedIndex = myTable.getSelectionModel().getSelectedIndex();

				// Retrieve the selected row data
				Appointment selectedAppointment = myTable.getItems().get(selectedIndex);

				if (deleteWarning()) {

					deleteRow(selectedAppointment);
					dataList.remove(selectedAppointment);
					data.remove(selectedAppointment);
					myTable.getItems().remove(selectedAppointment);
					myTable.refresh();
					viewAppointmentsTab.setContent(createViewAppointmentsPane());
				}
			}
		});
		VBox createNewPane = new VBox(20);
		createNewPane.getChildren().addAll(createNewLabel, all, timeBox, app, deleteBtn);
		createNewPane.setSpacing(10);
		createNewPane.setStyle("-fx-padding: 20px;");
		HBox createLast = new HBox(10);
		// createLast.setMaxHeight(600);
		// createLast.setMaxWidth(800);

		createLast.getChildren().addAll(createNewPane, createVerticalLine(), myTable);

		return createLast;
	}

	private Separator createVerticalLine() {
		Separator separator = new Separator();
		separator.setOrientation(Orientation.VERTICAL);
		separator.setStyle("-fx-background-color: rgba(128, 128, 128, 0.3);");
		separator.setPrefWidth(2); // Set the preferred width of the separator
		separator.setMaxWidth(2); // Set the maximum width of the separator
		separator.setMinWidth(2);
		return separator;
	}

	private void insertData(Appointment rc) {

		try {
			System.out.println(
					"INSERT INTO appointment ( patient_id, phys_id, ap_reason, ap_date, ap_time, status, cost, duration)VALUES("
							+ rc.getPatient_id() + "," + rc.getPhys_id() + ",'" + rc.getAp_reason() + "','"
							+ rc.getAp_date() + "', '" + rc.getAp_time() + "','" + rc.getStatus() + "'," + rc.getCost()
							+ ",'" + rc.getDuration() + "')");

			try {
				connectDB();

				ExecuteStatement(
						"INSERT INTO appointment ( patient_id, phys_id, ap_reason, ap_date, ap_time, status, cost, duration)VALUES("
								+ rc.getPatient_id() + "," + rc.getPhys_id() + ",'" + rc.getAp_reason() + "','"
								+ rc.getAp_date() + "', '" + rc.getAp_time() + "','" + rc.getStatus() + "',"
								+ rc.getCost() + ",'" + rc.getDuration() + "')");

				con.close();
				System.out.println("Connection closed" + data.size());

			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public AppointmentScene() {
		tabPane = new TabPane();
		data = new ArrayList<>();
		myTable = new TableView<Appointment>();
		try {
			getData(); // Fetch data from the database and populate the 'data' list
			dataList = FXCollections.observableArrayList(data);
		} catch (ClassNotFoundException | SQLException e1) {
			e1.printStackTrace();
		}

		myTable = new TableView<>();

		myTable.setEditable(true);
		myTable.setMaxHeight(500);
		myTable.setMaxWidth(500);
		//////////////////////////////////////
		TableColumn<Appointment, Integer> ap_id = new TableColumn<Appointment, Integer>("ap_id");
		ap_id.setMinWidth(50);
		ap_id.setStyle("-fx-font-size: 14px; -fx-font-family: Arial;");
		ap_id.setCellValueFactory(new PropertyValueFactory<Appointment, Integer>("ap_id"));
		TableColumn<Appointment, Integer> patient_id = new TableColumn<Appointment, Integer>("patient_id");
		patient_id.setMinWidth(50);
		patient_id.setCellValueFactory(new PropertyValueFactory<Appointment, Integer>("patient_id"));
		patient_id.setStyle("-fx-font-size: 14px; -fx-font-family: Arial;");

		TableColumn<Appointment, Integer> phys_id = new TableColumn<Appointment, Integer>("phys_id");
		phys_id.setMinWidth(50);
		phys_id.setCellValueFactory(new PropertyValueFactory<Appointment, Integer>("phys_id"));
		phys_id.setStyle("-fx-font-size: 14px; -fx-font-family: Arial;");
		TableColumn<Appointment, String> ap_reason = new TableColumn<>("ap_reason");
		ap_reason.setMinWidth(50);
		ap_reason.setCellValueFactory(new PropertyValueFactory<>("ap_reason"));
		ap_reason.setOnEditCommit((CellEditEvent<Appointment, String> t) -> {
			((Appointment) t.getTableView().getItems().get(t.getTablePosition().getRow()))
					.setAp_reason(t.getNewValue()); // display only
			updateAp_reason(t.getRowValue().getAp_id(), t.getNewValue());
		});
		ap_reason.setStyle("-fx-font-size: 14px; -fx-font-family: Arial;");

		TableColumn<Appointment, String> ap_date = new TableColumn<Appointment, String>("ap_date");
		ap_date.setMinWidth(50);
		ap_date.setCellValueFactory(new PropertyValueFactory<Appointment, String>("ap_date"));
		ap_date.setOnEditCommit((CellEditEvent<Appointment, String> t) -> {
			((Appointment) t.getTableView().getItems().get(t.getTablePosition().getRow())).setAp_date(t.getNewValue()); // display
																														// only
			updateAp_date(t.getRowValue().getAp_id(), t.getNewValue());
		});
		ap_date.setStyle("-fx-font-size: 14px; -fx-font-family: Arial;");
		////////////////////////

		TableColumn<Appointment, String> ap_time = new TableColumn<Appointment, String>("ap_time");
		ap_time.setMinWidth(50);
		ap_time.setCellValueFactory(new PropertyValueFactory<Appointment, String>("ap_time"));
		ap_time.setOnEditCommit((CellEditEvent<Appointment, String> t) -> {
			((Appointment) t.getTableView().getItems().get(t.getTablePosition().getRow())).setAp_time(t.getNewValue()); // display
																														// only
			updateAp_time(t.getRowValue().getAp_id(), t.getNewValue());
		});
		ap_time.setStyle("-fx-font-size: 14px; -fx-font-family: Arial;");

		TableColumn<Appointment, String> status = new TableColumn<Appointment, String>("status");
		status.setMinWidth(50);
		status.setCellValueFactory(new PropertyValueFactory<Appointment, String>("status"));
		status.setOnEditCommit((CellEditEvent<Appointment, String> t) -> {
			((Appointment) t.getTableView().getItems().get(t.getTablePosition().getRow())).setStatus(t.getNewValue()); // display
																														// only
			updateStatus(t.getRowValue().getAp_id(), t.getNewValue());
		});
		status.setStyle("-fx-font-size: 14px; -fx-font-family: Arial;");
		TableColumn<Appointment, Double> cost = new TableColumn<Appointment, Double>("cost");
		cost.setMinWidth(50);
		cost.setStyle("-fx-font-size: 14px; -fx-font-family: Arial;");
		cost.setCellValueFactory(new PropertyValueFactory<Appointment, Double>("cost"));
		cost.setOnEditCommit((CellEditEvent<Appointment, Double> t) -> {
			((Appointment) t.getTableView().getItems().get(t.getTablePosition().getRow())).setCost(t.getNewValue()); // display
																														// only
			updateCost(t.getRowValue().getAp_id(), t.getNewValue());
		});
		TableColumn<Appointment, String> duration = new TableColumn<Appointment, String>("duration");
		duration.setMinWidth(50);
		duration.setStyle("-fx-font-size: 14px; -fx-font-family: Arial;");
		duration.setCellValueFactory(new PropertyValueFactory<Appointment, String>("duration"));
		duration.setOnEditCommit((CellEditEvent<Appointment, String> t) -> {
			((Appointment) t.getTableView().getItems().get(t.getTablePosition().getRow())).setDuration(t.getNewValue()); // display
																															// only
			updateDuration(t.getRowValue().getAp_id(), t.getNewValue());
		});

		myTable.setItems(dataList);

		myTable.getColumns().addAll(ap_id, patient_id, phys_id, ap_reason, ap_date, ap_time, status, cost, duration);
		////////////////////////////////////////////////////////

		// View Appointments Tab
		viewAppointmentsTab = new Tab("View Appointments");
		viewAppointmentsTab.setClosable(false);
		viewAppointmentsTab.setContent(createViewAppointmentsPane());
		tabPane.getTabs().add(viewAppointmentsTab);

		// Create New Tab
		Tab createNewTab = new Tab("Create New");
		createNewTab.setContent(createCreateNewPane());
		tabPane.getTabs().add(createNewTab);
		createNewTab.setClosable(false);

		setCenter(tabPane);

		//////////////////////////
		Label nameLabel = new Label("Patient Name:");
		TextField nameTextField = new TextField();

		Label numberLabel = new Label("Patient Number:");
		TextField numberTextField = new TextField();

		Label hourLabel = new Label("Hour:");
		TextField hourTextField = new TextField();

		Label minuteLabel = new Label("Minute:");
		TextField minuteTextField = new TextField();

		Button submitButton = new Button("Submit");
		FlowPane fp = new FlowPane();
		fp.setOrientation(Orientation.HORIZONTAL);
		fp.getChildren().addAll(nameLabel, nameTextField, numberLabel, numberTextField, hourLabel, hourTextField,
				minuteLabel, minuteTextField, submitButton);

		// HBox hbox = new HBox(10);

		// myTable.setColumnResizePolicy(myTable.CONSTRAINED_RESIZE_POLICY);

	}

	public void displayNotification(HBox h) {
		sideBox.setContent(h);
		sideBox.setFitToWidth(true);
		sideBox.setFitToHeight(true);
	}

	public void displayNotification(VBox h) {

		sideBox.setContent(h);
		sideBox.setFitToWidth(true);
		sideBox.setFitToHeight(true);

	}

	public void updateAp_date(int ap_id, String date) {

		try {
			System.out.println("update  Appointment set ap_date = '" + date + "' where ap_id = " + ap_id + ";");
			connectDB();
			ExecuteStatement("update  Appointment set ap_date = '" + date + "' where ap_id = " + ap_id + ";");
			con.close();
			System.out.println("Connection closed");

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void updateAp_time(int ap_id, String date) {

		try {
			System.out.println("update  Appointment set ap_time= '" + date + "' where ap_id = " + ap_id + ";");
			connectDB();
			ExecuteStatement("update  Appointment set ap_time = '" + date + "' where ap_id = " + ap_id + ";");
			con.close();
			System.out.println("Connection closed");

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void updateStatus(int ap_id, String date) {

		try {
			System.out.println("update  Appointment set status = '" + date + "' where ap_id = " + ap_id + ";");
			connectDB();
			ExecuteStatement("update  Appointment set status = '" + date + "' where ap_id = " + ap_id + ";");
			con.close();
			System.out.println("Connection closed");

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void updateCost(int ap_id, double cost) {

		try {
			System.out.println("update  Appointment set cost = '" + cost + "' where ap_id = " + ap_id + ";");
			connectDB();
			ExecuteStatement("update  Appointment set cost = '" + cost + "' where ap_id = " + ap_id + ";");
			con.close();
			System.out.println("Connection closed");

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void updateDuration(int ap_id, String duration) {

		try {
			System.out.println("update  Appointment set duration = '" + duration + "' where ap_id = " + ap_id + ";");
			connectDB();
			ExecuteStatement("update  Appointment set duration = '" + duration + "' where ap_id = " + ap_id + ";");
			con.close();
			System.out.println("Connection closed");

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void updateAp_reason(int ap_id, String reason) {

		try {
			System.out.println("update  Appointment set ap_reason = '" + reason + "' where ap_id = " + ap_id + ";");
			connectDB();
			ExecuteStatement("update  Appointment set ap_reason = '" + reason + "' where ap_id = " + ap_id + ";");
			con.close();
			System.out.println("Connection closed");

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public String getName(int patient_id) {
		String firstName = null;
		try {
			System.out.println("SELECT first_name FROM patient WHERE patient_id = '" + patient_id + "';");
			connectDB();
			Statement statement = con.createStatement();
			ResultSet resultSet = statement
					.executeQuery("SELECT first_name FROM patient WHERE patient_id = '" + patient_id + "';");
			if (resultSet.next()) {
				firstName = resultSet.getString("first_name");
			}
			con.close();
			System.out.println("Connection closed");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return firstName;
	}

	public String getPhName(int phys_id) {
		String firstName = null;
		try {
			System.out.println("SELECT first_name FROM physician WHERE phys_id = '" + phys_id + "';");
			connectDB();
			Statement statement = con.createStatement();
			ResultSet resultSet = statement
					.executeQuery("SELECT first_name FROM physician WHERE phys_id = '" + phys_id + "';");
			if (resultSet.next()) {
				firstName = resultSet.getString("first_name");
			}
			con.close();
			System.out.println("Connection closed");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return firstName;
	}

	private void getData() throws SQLException, ClassNotFoundException {
		// TODO Auto-generated method stub
		data.clear();
		String SQL;

		connectDB();
		System.out.println("Connection established");

		SQL = " select * from appointment;";
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(SQL);

		while (rs.next())
			data.add(new Appointment(Integer.parseInt(rs.getString(1)), Integer.parseInt(rs.getString(2)),
					Integer.parseInt(rs.getString(3)), rs.getString(4), rs.getString(5), rs.getString(6),

					rs.getString(7), Double.parseDouble(rs.getString(8)), rs.getString(9)));

		rs.close();
		stmt.close();

		con.close();
		System.out.println("Connection closed" + data.size());

	}

	private void deleteRow(Appointment row) {
		// TODO Auto-generated method stub

		try {
			System.out.println("delete from  appointment where ap_id=" + row.getAp_id() + ";");
			connectDB();
			ExecuteStatement("delete from  appointment where ap_id=" + row.getAp_id() + ";");
			con.close();
			System.out.println("Connection closed");

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

	private void connectDB() throws ClassNotFoundException, SQLException {

		dbURL = "jdbc:mysql://" + URL + ":" + port + "/" + dbName + "?verifyServerCertificate=false";
		Properties p = new Properties();
		p.setProperty("user", dbUsername);
		p.setProperty("password", dbPassword);
		p.setProperty("useSSL", "false");
		p.setProperty("autoReconnect", "true");
		Class.forName("com.mysql.cj.jdbc.Driver");

		con = DriverManager.getConnection(dbURL, p);

	}

	public List<Appointment> searchAppointment(int patient_id) {
		List<Appointment> appointments = new ArrayList<>();
		try {
			connectDB();
			Statement statement = con.createStatement();
			ResultSet resultSet = statement
					.executeQuery("SELECT * FROM appointment WHERE patient_id = '" + patient_id + "';");
			System.out.println(resultSet);
			while (resultSet.next()) {
				Appointment appointment = new Appointment();
				appointment.setAp_id(resultSet.getInt("ap_id"));
				appointment.setPatient_id(resultSet.getInt("patient_id"));
				appointment.setPhys_id(resultSet.getInt("phys_id"));
				appointment.setAp_reason(resultSet.getString("ap_reason"));
				appointment.setAp_date(resultSet.getDate("ap_date").toString());
				appointment.setAp_time(resultSet.getTime("ap_time").toString());
				appointment.setStatus(resultSet.getString("status"));
				appointment.setCost(resultSet.getDouble("cost"));
				appointment.setDuration(resultSet.getTime("duration").toString());
				System.out.println(appointment);
				appointments.add(appointment);
			}

			con.close();
			// System.out.println("Connection closed");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println(appointments);
		return appointments;
	}

	public List<Appointment> searchAppointmentsByName(String name) {
		List<Appointment> appointments = new ArrayList<>();
		try {
			connectDB();
			Statement statement = con.createStatement();
			ResultSet resultSet = statement.executeQuery(
					"SELECT * FROM appointment WHERE patient_id IN (SELECT patient_id FROM patient WHERE first_name LIKE '%"
							+ name + "%' OR last_name LIKE '%" + name + "%');");
			while (resultSet.next()) {
				Appointment appointment = new Appointment();
				appointment.setAp_id(resultSet.getInt("ap_id"));
				appointment.setPatient_id(resultSet.getInt("patient_id"));
				appointment.setPhys_id(resultSet.getInt("phys_id"));
				appointment.setAp_reason(resultSet.getString("ap_reason"));
				appointment.setAp_date(resultSet.getDate("ap_date").toString());
				appointment.setAp_time(resultSet.getTime("ap_time").toString());
				appointment.setStatus(resultSet.getString("status"));
				appointment.setCost(resultSet.getDouble("cost"));
				appointment.setDuration(resultSet.getTime("duration").toString());
				appointments.add(appointment);
			}
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return appointments;
	}

	public boolean deleteWarning() {
		Stage stage = new Stage();
		stage.setTitle("Warning");
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.initOwner(this.getScene().getWindow());

		Label warning = new Label("Are you sure you want to delete this tuple?");
		Button yes = new Button("Yes");
		Button no = new Button("No");

		VBox gather = new VBox(10);
		gather.setAlignment(Pos.CENTER);
		gather.getChildren().addAll(warning, yes, no);

		Scene scene = new Scene(gather, 400, 150);
		stage.setScene(scene);

		final boolean[] yesFlag = { false };

		yes.setOnAction(event -> {
			yesFlag[0] = true;
			stage.close();
		});

		no.setOnAction(event -> {
			stage.close();
		});

		stage.showAndWait();

		return yesFlag[0];
	}

	public boolean validateInputs(String patientId, String physicianId, String reason, String hour, String minute,
			String cost, String duration) {
		try {
			// Validate patientId and physicianId as integers
			int parsedPatientId = Integer.parseInt(patientId);
			int parsedPhysicianId = Integer.parseInt(physicianId);

			// Validate cost as a double
			double parsedCost = Double.parseDouble(cost);

			// Check if reason and duration contain only letters
			if (!reason.matches("[a-zA-Z\\s]+")) {
				return false; // Invalid syntax, reason contains non-letter or non-space characters
			}

			// Other input validations if necessary

			return true; // All inputs are valid
		} catch (NumberFormatException e) {
			e.getMessage();
			// Invalid input syntax, one or more inputs are not numbers
			return false;
		}
	}

	public void showEmptyInputWarning() {
		Stage stage = new Stage();
		stage.setTitle("Warning");
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.initOwner(this.getScene().getWindow());

		Label warning = new Label("make sure you don't have empty inputs ");

		VBox gather = new VBox(10);
		gather.setAlignment(Pos.CENTER);
		gather.getChildren().addAll(warning);

		Scene scene = new Scene(gather, 400, 150);
		stage.setScene(scene);

		final boolean[] yesFlag = { false };

		stage.showAndWait();

	}

	public void showInvalidInput() {
		Stage stage = new Stage();
		stage.setTitle("Warning");
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.initOwner(this.getScene().getWindow());

		Label warning = new Label("invalid set of values . make sure you submit with right format ");

		VBox gather = new VBox(10);
		gather.setAlignment(Pos.CENTER);
		gather.getChildren().addAll(warning);

		Scene scene = new Scene(gather, 400, 150);
		stage.setScene(scene);

		stage.showAndWait();

	}

	public void ExecuteStatement(String SQL) throws SQLException {

		try {
			Statement stmt = con.createStatement();
			stmt.executeUpdate(SQL);
			stmt.close();

		} catch (SQLException s) {
			s.printStackTrace();
			System.out.println("SQL statement is not executed!");

		}

	}

	public void showPaymentStage(Appointment c) {
		Stage stage = new Stage();
		stage.setTitle("Payment status");
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.initOwner(this.getScene().getWindow());
		VBox containerVBox = new VBox(20);
		containerVBox.setAlignment(Pos.CENTER);
		containerVBox.setPadding(new Insets(10));
		containerVBox.setBackground(new Background(new BackgroundFill(Color.BEIGE, CornerRadii.EMPTY, Insets.EMPTY)));
		ImageView paymentImageView = new ImageView(new Image("payment.png"));
		paymentImageView.setFitWidth(300);
		paymentImageView.setFitHeight(300);

		// Create an HBox for the payment process
		VBox paymentProcessBox = new VBox();
		paymentProcessBox.setAlignment(Pos.CENTER);
		paymentProcessBox.setSpacing(10);
		paymentProcessBox.setStyle("-fx-background-color: rgba(181, 43, 64, 0.5); -fx-background-radius: 10px;");

		// Create a Label for the payment process
		Label paymentProcessLabel = new Label("Payment Process");

		// Create a ComboBox for payment options
		ComboBox<String> paymentOptionsComboBox = new ComboBox<>();
		paymentOptionsComboBox.getItems().addAll("Credit Card", "Insurance", "Cash");
		paymentOptionsComboBox.setStyle("-fx-font-size: 14px;");
		paymentOptionsComboBox.setPrefWidth(200);

		// Create an ImageView for the payment options
		ImageView paymentOptionsImage = new ImageView();
		paymentOptionsImage.setFitWidth(50);
		paymentOptionsImage.setFitHeight(50);

		// Set the image based on the selected payment option
		paymentOptionsComboBox.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> {
					if (newValue.equals("Credit Card")) {
						paymentOptionsImage.setImage(new Image("credit.png"));
					} else if (newValue.equals("Insurance")) {
						paymentOptionsImage.setImage(new Image("insurance.png"));
					} else if (newValue.equals("Cash")) {
						paymentOptionsImage.setImage(new Image("cash.png"));
					}
				});

		paymentProcessBox.getChildren().addAll(paymentProcessLabel, paymentOptionsComboBox, paymentOptionsImage);

		// Create TextFields and Label for amount paid and amount left
		TextField amountPaidTextField = new TextField();
		amountPaidTextField.setPromptText("Amount Paid");
		amountPaidTextField.setStyle(
				"-fx-font-size: 14px; -fx-border-radius: 5px; -fx-background-radius: 5px; -fx-background-color: rgba(181, 43, 64, 0.2);");

		TextField amountLeftTextField = new TextField();
		amountLeftTextField.setPromptText("Amount Left");
		amountLeftTextField.setEditable(false);
		amountLeftTextField.setStyle(
				"-fx-font-size: 14px; -fx-border-radius: 5px; -fx-background-radius: 5px; -fx-background-color: rgba(181, 43, 64, 0.2);");

		paymentProcessLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
		Label lastLabel = new Label("");

		// Add the TextFields and Label to a VBox
		VBox textFieldVBox = new VBox(10);
		textFieldVBox.getChildren().addAll(amountPaidTextField, amountLeftTextField, lastLabel);

		// Create a Button
		Button okBtn = new Button("Ok");
		okBtn.setOnAction(e -> {
			amountLeftTextField
					.setText(String.valueOf(c.getCost() - Double.parseDouble(amountPaidTextField.getText())));
			if (Double.parseDouble(amountLeftTextField.getText()) == 0) {
				insertMedicalRecord(c.getPatient_id(), paymentOptionsComboBox.getValue(), c.getCost(),
						Double.parseDouble(amountPaidTextField.getText()),
						Double.parseDouble(amountLeftTextField.getText()), c.getAp_date(), c.getAp_reason(), "paid");
				lastLabel.setText("patient id : " + c.getPatient_id() + "  " + paymentOptionsComboBox.getValue()
						+ " cost of treatment : " + c.getCost() + " paid : "
						+ Double.parseDouble(amountPaidTextField.getText()) + " left : "
						+ Double.parseDouble(amountLeftTextField.getText()) + " in date " + c.getAp_date() + " "
						+ c.getAp_reason() + " status " + "paid");
			} else {
				lastLabel.setText("patient id : " + c.getPatient_id() + "  " + paymentOptionsComboBox.getValue()
						+ " cost of treatment : " + c.getCost() + " paid : "
						+ Double.parseDouble(amountPaidTextField.getText()) + " left : "
						+ Double.parseDouble(amountLeftTextField.getText()) + " in date " + c.getAp_date() + " "
						+ c.getAp_reason() + " status " + "unpaid");
				insertMedicalRecord(c.getPatient_id(), paymentOptionsComboBox.getValue(), c.getCost(),
						Double.parseDouble(amountPaidTextField.getText()),
						Double.parseDouble(amountLeftTextField.getText()), c.getAp_date(), c.getAp_reason(), "unpaid");
			}

		});
		// Add the HBox, VBox, and Button to the container VBox
		containerVBox.getChildren().addAll(paymentImageView, paymentProcessBox, textFieldVBox, okBtn);

		// Set the size of the VBox
		containerVBox.setPrefSize(400, 700);
		Scene scene = new Scene(containerVBox, 400, 700);

		// Set the scene to the stage
		stage.setScene(scene);
		stage.setTitle("Payment Stage");
		stage.show();

	}

	private void insertMedicalRecord(int patient_id, String billing_method, double total_amount, double amount_paid,
			double amount_left, String date_of_billing, String details, String payment_status) {
		// TODO Auto-generated method stub

		try {
			connectDB();
			ExecuteStatement(
					"INSERT INTO billing_Record (patient_id, billing_method, total_amount, amount_paid, amount_left, date_of_billing, details, payment_status) VALUES ("
							+ patient_id + ",'" + billing_method + "'," + total_amount + "," + amount_paid + ","
							+ amount_left + ",'" + date_of_billing + "','" + details + "','" + payment_status + "')");
			con.close();
			System.out.println("Connection closed");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

}
