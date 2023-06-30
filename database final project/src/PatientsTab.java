import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import atlantafx.base.theme.PrimerLight;
import atlantafx.base.util.DoubleStringConverter;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;

public class PatientsTab extends BorderPane {

	Label mainLabel = new Label("Search - Add - Delete Patient");

	Label firstNameL = new Label("First Name:");
	Label lastNameL = new Label("Last Name:");
	Label genderL = new Label("Gender:");
	Label dateOfBirthL = new Label("Date of birth:");
	Label addressL = new Label("Address:");
	Label emailAddressL = new Label("Email Address:");
	Label patientIDL = new Label("Patient ID:");
	Label weightL = new Label("Weight:");
	Label heightL = new Label("Height:");
	Label phoneL = new Label("Phone Number:");

	TextField firstNameTF = new TextField();
	TextField lastNameTF = new TextField();
	ComboBox<String> genderCB = new ComboBox<String>();
	DatePicker dateOfBirthPicker = new DatePicker();
	TextField emailAddressTF = new TextField();
	TextField addressTF = new TextField();
	TextField patientIDTF = new TextField();
	TextField phone_numberTF = new TextField();
	TextField weightTF = new TextField();
	TextField heightTF = new TextField();

	Button editOrViewPatientsTable = new Button("Edit Or View Patients Table");
	Button searchByIDBtn = new Button("Search by ID");
	Button addPatientBtn = new Button("Add Patient");
	Button deletePatientBtn = new Button("Delete Patient");
	static TableView<Patient> patientsTV = new TableView<Patient>();

	public PatientsTab(Stage primaryStage, Scene scene) throws SQLException {
		genderCB.getItems().addAll("Male", "Female");
		dateOfBirthPicker.setOnAction(e -> {
			LocalDate selectedDate = dateOfBirthPicker.getValue();

		});

		editOrViewPatientsTable.setOnAction(e -> {
			try {
				patientsTV = createPatientsTable();
				patientsTV.setMinHeight(600);
				Label titleL = new Label("Edit or View Patients Table (Double click to edit)");
				titleL.setStyle("-fx-text-fill: white; -fx-font-size: 26px; -fx-font-weight: bold;");
				VBox tableVB = new VBox(titleL, patientsTV);
				tableVB.setSpacing(10);
				titleL.setAlignment(Pos.CENTER);
				tableVB.setAlignment(Pos.CENTER);
				Stage popupStage = new Stage();
				popupStage.setTitle("Patients Table");
				BorderPane contentPane = new BorderPane();
				contentPane.setStyle("-fx-background-color: #FCAEAE;");
				contentPane.setCenter(tableVB);
				contentPane.setPadding(new Insets(20));
				Scene popupScene = new Scene(contentPane, 1100, 700);
				popupScene.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());
//			 popupScene.getStylesheets().add("style.css");
				popupStage.setScene(popupScene);
				popupStage.show();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		});

		searchByIDBtn.setOnAction(e -> {
			try {
				int patient_id = Integer.parseInt(patientIDTF.getText().trim());
				Patient p = searchForPatient(patient_id);
				if (p == null)
					return;
				firstNameTF.setText(p.getFirst_name());
				lastNameTF.setText(p.getLast_name());
				genderCB.setValue((p.getGender() == "M") ? "Male" : "Female");
				String dateString = "2023-06-30";
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
				LocalDate date = LocalDate.parse(dateString, formatter);
				dateOfBirthPicker.setValue(date);
				emailAddressTF.setText(p.getEmail_address());
				addressTF.setText(p.getAddress());
				phone_numberTF.setText(p.getPhone_number());
				weightTF.setText(p.getWeight() + "");
				heightTF.setText(p.getHeight() + "");

			} catch (Exception e1) {

			}
		});

		addPatientBtn.setOnAction(e -> {
			int patient_id = Integer.parseInt(patientIDTF.getText().trim());
			String first_name = firstNameTF.getText().trim();
			String last_name = lastNameTF.getText().trim();
			String address = addressTF.getText().trim();
			String date_of_birth = dateOfBirthPicker.getValue().toString().replaceAll("/", "-");
			String email_address = emailAddressTF.getText().trim();
			String phone_number = phone_numberTF.getText().trim();
			String gender = (genderCB.getValue().equals("Male")) ? "M" : "F";
			double height = Double.parseDouble(heightTF.getText().trim());
			double weight = Double.parseDouble(weightTF.getText().trim());
			addPatientToDB(patient_id, first_name, last_name, address, date_of_birth, email_address, phone_number,
					gender, height, weight);
			try {
				patientsTV = createPatientsTable();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		});

		deletePatientBtn.setOnAction(e -> {
			try {
				int patient_id = Integer.parseInt(patientIDTF.getText().trim());
				Connection connection = DriverManager.getConnection(Main.url, Main.username, Main.password);
				Statement statement = connection.createStatement();
				statement.executeUpdate("Delete from patient where patient_id = " + patient_id + ";");
				patientsTV = createPatientsTable();
			} catch (Exception e1) {

			}

		});
		GridPane gp = new GridPane();
		firstNameL.setStyle("-fx-text-fill: white; -fx-font-size: 22px; -fx-font-weight: bold;");
		lastNameL.setStyle("-fx-text-fill: white; -fx-font-size: 22px; -fx-font-weight: bold;");
		genderL.setStyle("-fx-text-fill: white; -fx-font-size: 22px; -fx-font-weight: bold;");
		dateOfBirthL.setStyle("-fx-text-fill: white; -fx-font-size: 22px; -fx-font-weight: bold;");
		addressL.setStyle("-fx-text-fill: white; -fx-font-size: 22px; -fx-font-weight: bold;");
		emailAddressL.setStyle("-fx-text-fill: white; -fx-font-size: 22px; -fx-font-weight: bold;");
		patientIDL.setStyle("-fx-text-fill: white; -fx-font-size: 22px; -fx-font-weight: bold;");
		heightL.setStyle("-fx-text-fill: white; -fx-font-size: 22px; -fx-font-weight: bold;");
		weightL.setStyle("-fx-text-fill: white; -fx-font-size: 22px; -fx-font-weight: bold;");

		gp.add(patientIDL, 0, 0);
		gp.add(firstNameL, 0, 1);
		gp.add(lastNameL, 0, 2);
		gp.add(genderL, 0, 3);
		gp.add(dateOfBirthL, 0, 4);
		gp.add(heightL, 0, 5);
		gp.add(weightL, 0, 6);
		gp.add(addressL, 0, 7);
		gp.add(emailAddressL, 0, 8);
		gp.add(phoneL, 0, 9);

		gp.add(patientIDTF, 1, 0);
		gp.add(firstNameTF, 1, 1);
		gp.add(lastNameTF, 1, 2);
		gp.add(genderCB, 1, 3);
		gp.add(dateOfBirthPicker, 1, 4);
		gp.add(heightTF, 1, 5);
		gp.add(weightTF, 1, 6);
		gp.add(addressTF, 1, 7);
		gp.add(emailAddressTF, 1, 8);
		gp.add(phone_numberTF, 1, 9);

		gp.add(searchByIDBtn, 2, 0);

		HBox optionsHB = new HBox(addPatientBtn, deletePatientBtn);
		optionsHB.setSpacing(15);
		gp.add(optionsHB, 1, 10);
		gp.add(editOrViewPatientsTable, 0, 14);
		gp.setHgap(15);
		gp.setVgap(15);
		gp.setAlignment(Pos.CENTER);
		setMargin(gp, new Insets(25));
		setLeft(gp);
		setPadding(new Insets(15));

	}

	private void addPatientToDB(int patient_id, String first_name, String last_name, String address,
			String date_of_birth, String email_address, String phone_number, String gender, double height,
			double weight) {
		try {
			Connection connection = DriverManager.getConnection(Main.url, Main.username, Main.password);
			Statement statement = connection.createStatement();
			statement.executeUpdate(
					"insert into patient (patient_id,first_name,last_name,address,dob,email_address,phone_number,gender,weight,height) values ("
							+ patient_id + ",'" + first_name + "','" + last_name + "','" + address + "','"
							+ date_of_birth + "','" + email_address + "','" + phone_number + "','" + gender + "',"
							+ height + "," + weight + ");");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public TableView<Patient> createPatientsTable() throws SQLException {
		Connection connection = DriverManager.getConnection(Main.url, Main.username, Main.password);
		Statement statement = connection.createStatement();

		TableView<Patient> tv = new TableView<Patient>();
		tv.setEditable(true);
		TableColumn<Patient, Integer> patientIdColumn = new TableColumn<>("patient_id");
		patientIdColumn.setCellValueFactory(new PropertyValueFactory<>("patient_id"));

		patientIdColumn
				.setCellFactory(TextFieldTableCell.<Patient, Integer>forTableColumn(new IntegerStringConverter()));
		patientIdColumn.setEditable(true);

		patientIdColumn.setOnEditCommit(e -> {
			int patient_id = e.getNewValue();
			int oldId = e.getOldValue();

			try {
				statement.execute(
						"update patient set patient_id = " + patient_id + " where patient_id = " + oldId + ";");
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			addAllPatientsToTable(tv);
		});

		TableColumn<Patient, String> first_nameColumn = new TableColumn<>("first_name");
		first_nameColumn.setCellValueFactory(new PropertyValueFactory<>("first_name"));

		first_nameColumn.setCellFactory(TextFieldTableCell.<Patient>forTableColumn());
		first_nameColumn.setEditable(true);

		first_nameColumn.setOnEditCommit(e -> {
			String first_name = e.getNewValue();
			int patient_id = e.getRowValue().getPatient_id();

			try {
				statement.execute("update patient set first_name = " + "'" + first_name + "'" + " where patient_id = "
						+ patient_id + ";");
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			addAllPatientsToTable(tv);
		});

		TableColumn<Patient, String> last_nameColumn = new TableColumn<>("last_name");
		last_nameColumn.setCellValueFactory(new PropertyValueFactory<>("last_name"));

		last_nameColumn.setCellFactory(TextFieldTableCell.<Patient>forTableColumn());
		last_nameColumn.setEditable(true);

		last_nameColumn.setOnEditCommit(e -> {
			String last_name = e.getNewValue();
			int patient_id = e.getRowValue().getPatient_id();

			try {
				statement.execute("update patient set last_name = " + "'" + last_name + "'" + " where patient_id = "
						+ patient_id + ";");
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			addAllPatientsToTable(tv);
		});

		TableColumn<Patient, String> dateOfBirthColumn = new TableColumn<>("Date of birth");
		dateOfBirthColumn.setCellValueFactory(new PropertyValueFactory<>("date_of_birth"));

		dateOfBirthColumn.setCellFactory(TextFieldTableCell.<Patient>forTableColumn());
		dateOfBirthColumn.setEditable(true);

		dateOfBirthColumn.setOnEditCommit(e -> {
			String date = e.getNewValue();
			int patient_id = e.getRowValue().getPatient_id();

			try {
				statement.execute(
						"update patient set dob = " + "'" + date + "'" + " where patient_id = " + patient_id + ";");
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			addAllPatientsToTable(tv);
		});

		TableColumn<Patient, String> emailColumn = new TableColumn<>("email_address");
		emailColumn.setCellValueFactory(new PropertyValueFactory<>("email_address"));

		emailColumn.setCellFactory(TextFieldTableCell.<Patient>forTableColumn());
		emailColumn.setEditable(true);

		emailColumn.setOnEditCommit(e -> {
			String email = e.getNewValue();
			int patient_id = e.getRowValue().getPatient_id();

			try {
				statement.execute(
						"update patient set email = " + "'" + email + "'" + " where patient_id = " + patient_id + ";");
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			addAllPatientsToTable(tv);
		});

		TableColumn<Patient, String> phoneColumn = new TableColumn<>("phone_number");
		phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone_number"));

		phoneColumn.setCellFactory(TextFieldTableCell.<Patient>forTableColumn());
		phoneColumn.setEditable(true);

		phoneColumn.setOnEditCommit(e -> {
			String phone = e.getNewValue();
			int patient_id = e.getRowValue().getPatient_id();
			try {
				statement.execute("update patient set phone_number = " + "'" + phone + "'" + " where patient_id  = "
						+ patient_id + ";");
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			addAllPatientsToTable(tv);
		});

		TableColumn<Patient, String> genderColumn = new TableColumn<>("gender");
		genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));

		genderColumn.setCellFactory(TextFieldTableCell.<Patient>forTableColumn());
		genderColumn.setEditable(true);

		genderColumn.setOnEditCommit(e -> {
			String gender = e.getNewValue();
			int patient_id = e.getRowValue().getPatient_id();

			try {
				statement.execute("update patient set gender = " + "'" + gender + "'" + " where patient_id = "
						+ patient_id + ";");
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			addAllPatientsToTable(tv);
		});

		TableColumn<Patient, String> addressColumn = new TableColumn<>("address");
		addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));

		addressColumn.setCellFactory(TextFieldTableCell.<Patient>forTableColumn());
		addressColumn.setEditable(true);

		addressColumn.setOnEditCommit(e -> {
			String address = e.getNewValue();
			int patient_id = e.getRowValue().getPatient_id();

			try {
				statement.execute("update patient set address = " + "'" + address + "'" + " where patient_id = "
						+ patient_id + ";");
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			addAllPatientsToTable(tv);
		});

		TableColumn<Patient, Double> heightColumn = new TableColumn<>("Height");
		heightColumn.setCellValueFactory(new PropertyValueFactory<>("height"));

		heightColumn.setCellFactory(TextFieldTableCell.<Patient, Double>forTableColumn(new DoubleStringConverter()));
		heightColumn.setEditable(true);

		heightColumn.setOnEditCommit(e -> {
			double height = e.getNewValue();
			int patient_id = e.getRowValue().getPatient_id();

			try {
				statement.execute("update patient set height = " + "'" + height + "'" + " where patient_id = "
						+ patient_id + ";");
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			addAllPatientsToTable(tv);
		});

		TableColumn<Patient, Double> weightColumn = new TableColumn<>("Weight");
		weightColumn.setCellValueFactory(new PropertyValueFactory<>("weight"));
		weightColumn.setCellFactory(TextFieldTableCell.<Patient, Double>forTableColumn(new DoubleStringConverter()));
		weightColumn.setEditable(true);

		weightColumn.setOnEditCommit(e -> {
			double weight = e.getNewValue();
			int patient_id = e.getRowValue().getPatient_id();

			try {
				statement.execute("update patient set weight = " + "'" + weight + "'" + " where patient_id = "
						+ patient_id + ";");
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			addAllPatientsToTable(tv);
		});

		// Add the columns to the TableView
		tv.getColumns().addAll(patientIdColumn, first_nameColumn, last_nameColumn, dateOfBirthColumn, genderColumn,
				heightColumn, weightColumn, emailColumn, phoneColumn, addressColumn);

		addAllPatientsToTable(tv);

		tv.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		return tv;
	}

	public void addAllPatientsToTable(TableView<Patient> tv) {
		tv.getItems().clear();
		try {
			Connection connection = DriverManager.getConnection(Main.url, Main.username, Main.password);
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("Select * from patient;");
			ArrayList<Patient> patients = new ArrayList<Patient>();
			while (resultSet.next()) {
				int patient_id = resultSet.getInt("patient_id");
				String first_name = resultSet.getString("first_name");
				String last_name = resultSet.getString("last_name");
				String address = resultSet.getString("address");
				String date_of_birth = resultSet.getString("dob");
				String email_address = resultSet.getString("email_address");
				String phone_number = resultSet.getString("phone_number");
				String gender = resultSet.getString("gender");
				double height = resultSet.getDouble("height");
				double weight = resultSet.getDouble("weight");
				patients.add(new Patient(patient_id, first_name, last_name, address, date_of_birth, email_address,
						phone_number, gender, weight, height));
			}
			tv.getItems().addAll(patients);

		} catch (Exception e) {

		}

	}

	public Patient searchForPatient(int patient_id) {
		try {
			Connection connection = DriverManager.getConnection(Main.url, Main.username, Main.password);
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement
					.executeQuery("Select * from patient where patient_id = " + patient_id + ";");
			if (resultSet.next()) {
				String first_name = resultSet.getString("first_name");
				String last_name = resultSet.getString("last_name");
				String address = resultSet.getString("address");
				String date_of_birth = resultSet.getString("dob");
				String email_address = resultSet.getString("email_address");
				String phone_number = resultSet.getString("phone_number");
				String gender = resultSet.getString("gender");
				double height = resultSet.getDouble("height");
				double weight = resultSet.getDouble("weight");
				return new Patient(patient_id, first_name, last_name, address, date_of_birth, email_address,
						phone_number, gender, height, weight);
			}

		} catch (Exception e) {

		}
		return null;
	}
}
