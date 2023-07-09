import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.Period;
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
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
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
	Label weightL = new Label("Weight (kg):");
	Label heightL = new Label("Height (cm):");
	Label phoneL = new Label("Phone Number:");
	Label statusL = new Label("");

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

	Button viewMedicalRecordBtn = new Button("View Medical Record");
	Button viewBillingRecordBtn = new Button("Edit Or View Billing Record");

	static int current_id = -1;
	static TableView<Patient> patientsTV = new TableView<Patient>();

	public PatientsTab(Stage primaryStage, Scene scene) throws SQLException {
		genderCB.getItems().addAll("Male", "Female");
		dateOfBirthPicker.setOnAction(e -> {
			LocalDate selectedDate = dateOfBirthPicker.getValue();

		});

		patientIDTF.setPromptText("leave empty for auto id");
		firstNameTF.setPromptText("eg. Omar");
		lastNameTF.setPromptText("eg. Kashour");
		emailAddressTF.setPromptText("eg. example@gmail.com");
		addressTF.setPromptText("eg. Jaffa Street 25");
		phone_numberTF.setPromptText("eg. 0523456789");
		weightTF.setPromptText("eg. 65.4");
		heightTF.setPromptText("eg. 185.3");

		phoneL.setStyle(
				"-fx-font-family: 'Product Sans'; -fx-text-fill: white;-fx-font-size: 22.0px; -fx-font-weight: bold;");
		statusL.setStyle("-fx-text-fill: white; -fx-font-size: 22px; -fx-font-weight: bold;");
		patientsTV = createPatientsTable();
		editOrViewPatientsTable.setPrefHeight(50);
		editOrViewPatientsTable.setOnAction(e -> {
			addAllPatientsToTable(patientsTV);
			patientsTV.setMinHeight(600);
			Label titleL = new Label("Edit or View Patients Table (Double click to edit)");
			titleL.setStyle(
					"-fx-font-family: 'Product Sans'; -fx-text-fill: white;-fx-font-size: 27.0px; -fx-font-weight: bold;");
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

		});

		searchByIDBtn.setOnAction(e -> {
			firstNameTF.setText("");
			lastNameTF.setText("");
			dateOfBirthPicker.setValue(null);
			emailAddressTF.setText("");
			addressTF.setText("");
			phone_numberTF.setText("");
			weightTF.setText("");
			heightTF.setText("");
			genderCB.setValue(null);
			try {
				int patient_id = Integer.parseInt(patientIDTF.getText().trim());
				Patient p = searchForPatient(patient_id);
				if (p == null) {
					statusL.setText("Patient not found");
					current_id = -1;
					return;
				}
				current_id = patient_id;
				firstNameTF.setText(p.getFirst_name());
				lastNameTF.setText(p.getLast_name());
				if (p.getGender().equals("M")) {
					genderCB.setValue("Male");
				} else if (p.getGender().equals("F")) {
					genderCB.setValue("Female");
				}
				String dateString = p.getDate_of_birth();
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
				LocalDate date = LocalDate.parse(dateString, formatter);
				dateOfBirthPicker.setValue(date);
				emailAddressTF.setText(p.getEmail_address());
				addressTF.setText(p.getAddress());
				phone_numberTF.setText(p.getPhone_number());
				weightTF.setText(p.getWeight() + "");
				heightTF.setText(p.getHeight() + "");
				statusL.setText("Patient found");
				double bmi = 10000 * (p.getWeight() / (p.getHeight() * p.getHeight()));
//				16 -> 18.4 under weight,   18.5 -> 24 normal , 25 -> 29 overweight , bmi > 29 obesity
				String status = "";
				if (bmi >= 16 && bmi < 18.5) {
					status = "Under Weight";
				} else if (bmi >= 18.5 && bmi < 25) {
					status = "Normal";
				} else if (bmi >= 25 && bmi < 30) {
					status = "Over Weight";
				} else if (bmi >= 30) {
					status = "Obesity";
				}
				StackPane bmiCard = createCard("Body Mass Index\nStatus: " + status, String.format("%.1f", bmi),
						"bmi.png");
				StackPane totalPaidCard = createCard("Total Amount Paid", "\u20AA" + getTotalPaid(current_id),
						"credit-card.png");
				GridPane rightGP = new GridPane();
				rightGP.add(bmiCard, 0, 0);
				String birthdateString = p.getDate_of_birth();

				// Parse the birthdate string into a LocalDate object
				LocalDate birthdate = LocalDate.parse(birthdateString);

				// Get the current date
				LocalDate currentDate = LocalDate.now();
				// Calculate the period between the birthdate and current date
				Period period = Period.between(birthdate, currentDate);
				StackPane ageCard = createCard("Patient's Age", period.getYears() + " years old", "age.png");
				StackPane totalLeftCard = createCard("Total Amount Left",
						"\u20AA" + (getTotalAmount(patient_id) - getTotalPaid(patient_id)), "money.png");

				viewMedicalRecordBtn.setPrefHeight(50);
				rightGP.add(ageCard, 1, 0);
				rightGP.add(totalPaidCard, 0, 1);
				rightGP.add(totalLeftCard, 1, 1);
				rightGP.add(viewMedicalRecordBtn, 0, 2);
				rightGP.add(viewBillingRecordBtn, 1, 2);
				rightGP.setHgap(15);
				rightGP.setVgap(15);
				setRight(rightGP);
			} catch (NumberFormatException e2) {
				statusL.setText("Invalid Patient ID");
			} catch (Exception e1) {
				statusL.setText("Patient not found");
			}

		});
		viewBillingRecordBtn.setPrefHeight(50);
		addPatientBtn.setOnAction(e -> {
			try {
				String id = patientIDTF.getText().trim();
				int patient_id = -1;
				boolean auto = true;
				if (!id.equals("")) {
					patient_id = Integer.parseInt(patientIDTF.getText().trim());
					auto = false;
				}
				String first_name = firstNameTF.getText().trim();
				String last_name = lastNameTF.getText().trim();
				String address = addressTF.getText().trim();
				String date_of_birth = dateOfBirthPicker.getValue().toString().replaceAll("/", "-");
				String email_address = emailAddressTF.getText().trim();
				String phone_number = phone_numberTF.getText().trim();
				String gender = "";
				if (genderCB.getValue().toLowerCase().equals("male"))
					gender = "M";
				else
					gender = "F";
				double height = Double.parseDouble(heightTF.getText().trim());
				double weight = Double.parseDouble(weightTF.getText().trim());
				if (!auto) {
					addPatientToDB(patient_id, first_name, last_name, address, date_of_birth, email_address,
							phone_number, gender, weight, height);
				} else {
					addPatientAutoIncrementToDB(first_name, last_name, address, date_of_birth, email_address,
							phone_number, gender, height, weight);
				}
				addAllPatientsToTable(patientsTV);
				statusL.setText("Patient has been added successfully");
			} catch (Exception e1) {
				statusL.setText("An error occured while adding patient");
			} finally {
				DashBoard.refreshDashBoard();
			}
		});

		deletePatientBtn.setOnAction(e -> {
			try {
				int patient_id = Integer.parseInt(patientIDTF.getText().trim());
				Statement statement = Main.connection.createStatement();
				statement.executeUpdate("Delete from patient where patient_id = " + patient_id + ";");
				addAllPatientsToTable(patientsTV);
				statusL.setText("Patient deleted successfully");
			} catch (SQLException e1) {
				statusL.setText("Patient has scheduled appointments and cannot be deleted");
			} catch (NumberFormatException e2) {
				statusL.setText("Invalid Patient ID");
			} finally {
				DashBoard.refreshDashBoard();
			}
		});

		viewMedicalRecordBtn.setOnAction(e -> { // use rightGP
			if (current_id == -1)
				return;
			Label titleL = new Label("Medical Record Of Patient ID: " + current_id);
			titleL.setStyle(
					"-fx-font-family: 'Product Sans'; -fx-text-fill: white;-fx-font-size: 27.0px; -fx-font-weight: bold;");
			TableView<MedicalRecord> medicationHistoryTV = new TableView<MedicalRecord>();
			TableColumn<MedicalRecord, String> medicationHistoryColumn = new TableColumn<MedicalRecord, String>(
					"medication_history");
			medicationHistoryColumn
					.setCellValueFactory(new PropertyValueFactory<MedicalRecord, String>("medication_history"));
			medicationHistoryTV.getColumns().add(medicationHistoryColumn);
			medicationHistoryTV.setColumnResizePolicy(medicationHistoryTV.CONSTRAINED_RESIZE_POLICY);
			medicationHistoryTV.setStyle("-fx-background-color: #FFEADD;");

			TableView<MedicalRecord> surgeriesTV = new TableView<MedicalRecord>();
			TableColumn<MedicalRecord, String> surgeriesColumn = new TableColumn<MedicalRecord, String>("surgeries");
			surgeriesColumn.setCellValueFactory(new PropertyValueFactory<MedicalRecord, String>("surgeries"));
			surgeriesTV.getColumns().add(surgeriesColumn);
			surgeriesTV.setColumnResizePolicy(surgeriesTV.CONSTRAINED_RESIZE_POLICY);
			surgeriesTV.setStyle("-fx-background-color: #FFEADD;");

			TableView<MedicalRecord> illnessHistoryTV = new TableView<MedicalRecord>();
			TableColumn<MedicalRecord, String> illnessHistoryColumn = new TableColumn<MedicalRecord, String>(
					"illness_history");
			illnessHistoryColumn
					.setCellValueFactory(new PropertyValueFactory<MedicalRecord, String>("illness_history"));
			illnessHistoryTV.getColumns().add(illnessHistoryColumn);
			illnessHistoryTV.setColumnResizePolicy(surgeriesTV.CONSTRAINED_RESIZE_POLICY);
			illnessHistoryTV.setStyle("-fx-background-color: #FFEADD;");

			TableView<MedicalRecord> allergiesTV = new TableView<MedicalRecord>();
			TableColumn<MedicalRecord, String> allergiesColumn = new TableColumn<MedicalRecord, String>("allergies");
			allergiesColumn.setCellValueFactory(new PropertyValueFactory<MedicalRecord, String>("allergies"));
			allergiesTV.getColumns().add(allergiesColumn);
			allergiesTV.setColumnResizePolicy(allergiesTV.CONSTRAINED_RESIZE_POLICY);
			allergiesTV.setStyle("-fx-background-color: #FFEADD;");
			try {
				Statement statement = Main.connection.createStatement();
				ResultSet res = statement
						.executeQuery("select * from medical_record where patient_id = " + current_id + ";");
				if (res.next()) {
					String[] illness_history = res.getString("illness_history").split(",");
					String[] allergies = res.getString("allergies").split(",");
					String[] surgeries = res.getString("surgeries").split(",");
					String[] medication_history = res.getString("medication_history").split(",");
					for (int i = 0; i < medication_history.length; i++)
						medicationHistoryTV.getItems()
								.add(new MedicalRecord(-1, -1, "", "", "", medication_history[i]));
					for (int i = 0; i < surgeries.length; i++)
						surgeriesTV.getItems().add(new MedicalRecord(-1, -1, "", "", surgeries[i], ""));
					for (int i = 0; i < illness_history.length; i++)
						illnessHistoryTV.getItems().add(new MedicalRecord(-1, -1, illness_history[i], "", "", ""));
					for (int i = 0; i < allergies.length; i++)
						allergiesTV.getItems().add(new MedicalRecord(-1, -1, "", allergies[i], "", ""));
				}
			} catch (SQLException e1) {
				;
			}

			Label medicationlHistoryL = new Label("Medication History");
			Label surgeriesL = new Label("Previous Surgeries");
			Label illnessHistoryL = new Label("Illness History");
			Label allergiesL = new Label("Allergies");
			medicationlHistoryL.setStyle(
					"-fx-font-family: 'Product Sans'; -fx-text-fill: white;-fx-font-size: 24.0px; -fx-font-weight: bold;");
			surgeriesL.setStyle(
					"-fx-font-family: 'Product Sans'; -fx-text-fill: white;-fx-font-size: 24.0px; -fx-font-weight: bold;");
			illnessHistoryL.setStyle(
					"-fx-font-family: 'Product Sans'; -fx-text-fill: white;-fx-font-size: 24.0px; -fx-font-weight: bold;");
			allergiesL.setStyle(
					"-fx-font-family: 'Product Sans'; -fx-text-fill: white;-fx-font-size: 24.0px; -fx-font-weight: bold;");
			VBox vb1 = new VBox(medicationlHistoryL, medicationHistoryTV);
			VBox vb2 = new VBox(surgeriesL, surgeriesTV);
			VBox vb3 = new VBox(illnessHistoryL, illnessHistoryTV);
			VBox vb4 = new VBox(allergiesL, allergiesTV);

			HBox tablesHB = new HBox(vb1, vb2, vb3, vb4);
			tablesHB.setAlignment(Pos.CENTER);
			tablesHB.setSpacing(15);
			VBox tableVB = new VBox(titleL, tablesHB);
			tableVB.setSpacing(15);
			titleL.setAlignment(Pos.CENTER);
			tableVB.setAlignment(Pos.CENTER);
			Stage popupStage = new Stage();
			popupStage.setTitle("Patient's Medication History");
			BorderPane contentPane = new BorderPane();
			contentPane.setStyle("-fx-background-color: #FCAEAE;");
			contentPane.setCenter(tableVB);
			contentPane.setPadding(new Insets(20));
			Scene popupScene = new Scene(contentPane, 1100, 700);
//				popupScene.getStylesheets().add("style.css");
			popupScene.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());
			popupStage.setScene(popupScene);
			popupStage.show();

		});

		viewBillingRecordBtn.setOnAction(e -> {
			if (current_id == -1)
				return;

			Label titleL = new Label("Billing Record Of Patient ID: " + current_id);
			titleL.setStyle(
					"-fx-font-family: 'Product Sans'; -fx-text-fill: white;-fx-font-size: 27.0px; -fx-font-weight: bold;");

			TableView<BillingRecord> tv = new TableView<BillingRecord>();

			TableColumn<BillingRecord, Integer> recordIdColumn = new TableColumn<>("record_id");
			recordIdColumn.setCellValueFactory(new PropertyValueFactory<>("record_id"));

			TableColumn<BillingRecord, Integer> patientIdColumn = new TableColumn<>("patient_id");
			patientIdColumn.setCellValueFactory(new PropertyValueFactory<>("patient_id"));

			TableColumn<BillingRecord, String> billingMethodColumn = new TableColumn<>("billing_method");
			billingMethodColumn.setCellValueFactory(new PropertyValueFactory<>("billing_method"));

			TableColumn<BillingRecord, Double> totalAmountDueColumn = new TableColumn<>("total_amount");
			totalAmountDueColumn.setCellValueFactory(new PropertyValueFactory<>("total_amount"));

			totalAmountDueColumn.setCellFactory(
					TextFieldTableCell.<BillingRecord, Double>forTableColumn(new DoubleStringConverter()));
			totalAmountDueColumn.setEditable(true);

			totalAmountDueColumn.setOnEditCommit(e1 -> {
				double total_amount_due = e1.getNewValue();
				int record_id = e1.getRowValue().getRecord_id();
				try {
					Statement statement = Main.connection.createStatement();
					statement.execute("update billing_record set total_amount = " + total_amount_due
							+ " where record_id = " + record_id + ";");
					double amount_left = total_amount_due - e1.getRowValue().getAmount_paid();
					statement.execute("update billing_record set amount_left = " + amount_left + " where record_id = "
							+ record_id + ";");
					if (amount_left > 0) {
						statement.execute("update billing_record set payment_status = 'Unpaid' where record_id = "
								+ record_id + ";");
					} else if (amount_left == 0) {
						statement.execute("update billing_record set payment_status = 'Paid' where record_id = "
								+ record_id + ";");
					}
				} catch (SQLException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}

				updateBillingRecordTable(tv);
				searchByIDBtn.fire();
			});

			TableColumn<BillingRecord, Double> amountPaidColumn = new TableColumn<>("amount_payed");
			amountPaidColumn.setCellValueFactory(new PropertyValueFactory<>("amount_paid"));

			amountPaidColumn.setCellFactory(
					TextFieldTableCell.<BillingRecord, Double>forTableColumn(new DoubleStringConverter()));

			amountPaidColumn.setEditable(true);
			amountPaidColumn.setOnEditCommit(e1 -> {
				double amount_payed = e1.getNewValue();
				int record_id = e1.getRowValue().getRecord_id();
				try {
					Statement statement = Main.connection.createStatement();
					statement.execute("update billing_record set amount_paid = " + amount_payed + " where record_id = "
							+ record_id + ";");
					double amount_left = e1.getRowValue().getTotal_amount() - amount_payed;
					statement.execute("update billing_record set amount_left = " + amount_left + " where record_id = "
							+ record_id + ";");
					if (amount_left > 0) {
						statement.execute("update billing_record set payment_status = 'Unpaid' where record_id = "
								+ record_id + ";");
					} else if (amount_left == 0) {
						statement.execute("update billing_record set payment_status = 'Paid' where record_id = "
								+ record_id + ";");
					}
				} catch (SQLException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}

				updateBillingRecordTable(tv);
				searchByIDBtn.fire();

			});

			TableColumn<BillingRecord, Double> amountLeftColumn = new TableColumn<>("amount_left");
			amountLeftColumn.setCellValueFactory(new PropertyValueFactory<>("amount_left"));

			amountLeftColumn.setCellFactory(
					TextFieldTableCell.<BillingRecord, Double>forTableColumn(new DoubleStringConverter()));
			amountLeftColumn.setEditable(true);

			amountLeftColumn.setOnEditCommit(e1 -> {
				double amount_left = e1.getNewValue();
				int record_id = e1.getRowValue().getRecord_id();
				try {
					Statement statement = Main.connection.createStatement();
					statement.execute("update billing_record set amount_left = " + amount_left + " where record_id = "
							+ record_id + ";");
				} catch (SQLException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				updateBillingRecordTable(tv);
				searchByIDBtn.fire();

			});

			TableColumn<BillingRecord, LocalDate> dateOfBillingColumn = new TableColumn<>("date_of_billing");
			dateOfBillingColumn.setCellValueFactory(new PropertyValueFactory<>("date_of_billing"));

			TableColumn<BillingRecord, String> detailsColumn = new TableColumn<>("details");
			detailsColumn.setCellValueFactory(new PropertyValueFactory<>("details"));

			TableColumn<BillingRecord, String> paymentStatusColumn = new TableColumn<>("payment_status");
			paymentStatusColumn.setCellValueFactory(new PropertyValueFactory<>("payment_status"));

			tv.getColumns().addAll(recordIdColumn, patientIdColumn, billingMethodColumn, totalAmountDueColumn,
					amountPaidColumn, amountLeftColumn, dateOfBillingColumn, detailsColumn, paymentStatusColumn);
			// Call the method to add data to the table view
			updateBillingRecordTable(tv);
			tv.setEditable(true);
			titleL.setAlignment(Pos.CENTER);

			tv.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
			setMargin(tv, new Insets(15));
			Stage popupStage = new Stage();
			popupStage.setTitle("Patient's Billing Record");
			BorderPane contentPane = new BorderPane();
			contentPane.setStyle("-fx-background-color: #FCAEAE;");
			contentPane.setTop(titleL);
			contentPane.setCenter(tv);
			contentPane.setAlignment(titleL, Pos.CENTER);
			contentPane.setPadding(new Insets(20));
			Scene popupScene = new Scene(contentPane, 1100, 700);
//				popupScene.getStylesheets().add("style.css");
			popupScene.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());
			popupStage.setScene(popupScene);
			popupStage.show();

		});

		firstNameL.setStyle("-fx-text-fill: white; -fx-font-size: 22px; -fx-font-weight: bold;");
		lastNameL.setStyle("-fx-text-fill: white; -fx-font-size: 22px; -fx-font-weight: bold;");
		genderL.setStyle("-fx-text-fill: white; -fx-font-size: 22px; -fx-font-weight: bold;");
		dateOfBirthL.setStyle("-fx-text-fill: white; -fx-font-size: 22px; -fx-font-weight: bold;");
		addressL.setStyle("-fx-text-fill: white; -fx-font-size: 22px; -fx-font-weight: bold;");
		emailAddressL.setStyle("-fx-text-fill: white; -fx-font-size: 22px; -fx-font-weight: bold;");
		patientIDL.setStyle("-fx-text-fill: white; -fx-font-size: 22px; -fx-font-weight: bold;");
		heightL.setStyle("-fx-text-fill: white; -fx-font-size: 22px; -fx-font-weight: bold;");
		weightL.setStyle("-fx-text-fill: white; -fx-font-size: 22px; -fx-font-weight: bold;");
		GridPane gp = new GridPane();
		HBox statusHB = new HBox(statusL);
		statusHB.setAlignment(Pos.CENTER_LEFT);
		setTop(statusHB);
		gp.add(patientIDL, 0, 1);
		gp.add(firstNameL, 0, 2);
		gp.add(lastNameL, 0, 3);
		gp.add(genderL, 0, 4);
		gp.add(dateOfBirthL, 0, 5);
		gp.add(heightL, 0, 6);
		gp.add(weightL, 0, 7);
		gp.add(addressL, 0, 8);
		gp.add(emailAddressL, 0, 9);
		gp.add(phoneL, 0, 10);

		gp.add(patientIDTF, 1, 1);
		gp.add(firstNameTF, 1, 2);
		gp.add(lastNameTF, 1, 3);
		gp.add(genderCB, 1, 4);
		gp.add(dateOfBirthPicker, 1, 5);
		gp.add(heightTF, 1, 6);
		gp.add(weightTF, 1, 7);
		gp.add(addressTF, 1, 8);
		gp.add(emailAddressTF, 1, 9);
		gp.add(phone_numberTF, 1, 10);

		gp.add(searchByIDBtn, 2, 1);

		// BIM
		HBox optionsHB = new HBox(addPatientBtn, deletePatientBtn);
		optionsHB.setSpacing(15);
		gp.add(optionsHB, 1, 11);
		gp.add(editOrViewPatientsTable, 1, 12);
		gp.setHgap(15);
		gp.setVgap(15);
		gp.setAlignment(Pos.CENTER);
		setMargin(gp, new Insets(25));
		setAlignment(statusL, Pos.CENTER);
		setLeft(gp);
		setPadding(new Insets(15));

	}

	private void updateBillingRecordTable(TableView<BillingRecord> tv) {
		try {
			tv.getItems().clear();
			Statement statement = Main.connection.createStatement();
			ResultSet res = statement
					.executeQuery("select * from billing_record where patient_id = " + current_id + ";");
			while (res.next()) {
				int record_id = res.getInt("record_id");
				int patient_id = res.getInt("patient_id");
				String billing_method = res.getString("billing_method");
				double total_amount = res.getDouble("total_amount");
				double amount_paid = res.getDouble("amount_paid");
				double amount_left = res.getDouble("amount_left");
				String date_of_billing = res.getString("date_of_billing");
				String details = res.getString("details");
				String payment_status = res.getString("payment_status");

				BillingRecord billingRecord = new BillingRecord(record_id, patient_id, billing_method, total_amount,
						amount_paid, amount_left, date_of_billing, details, payment_status);
				tv.getItems().add(billingRecord);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	private void addPatientAutoIncrementToDB(String first_name, String last_name, String address, String date_of_birth,
			String email_address, String phone_number, String gender, double height, double weight) {
		try {
			Statement statement = Main.connection.createStatement();
			statement.executeUpdate(
					"insert into patient (first_name,last_name,address,dob,email_address,phone_number,gender,weight,height) values ("
							+ "\"" + first_name + "\",'" + last_name + "','" + address + "','" + date_of_birth + "','"
							+ email_address + "','" + phone_number + "','" + gender + "'," + height + "," + weight
							+ ");");
		} catch (SQLException e) {

		}
	}

	private void addPatientToDB(int patient_id, String first_name, String last_name, String address,
			String date_of_birth, String email_address, String phone_number, String gender, double height,
			double weight) {
		try {
			Statement statement = Main.connection.createStatement();
			statement.executeUpdate(
					"insert into patient (patient_id,first_name,last_name,address,dob,email_address,phone_number,gender,weight,height) values ("
							+ patient_id + ",\"" + first_name + "\",'" + last_name + "','" + address + "','"
							+ date_of_birth + "','" + email_address + "','" + phone_number + "','" + gender + "',"
							+ height + "," + weight + ");");
		} catch (SQLException e) {

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
			Statement statement = Main.connection.createStatement();
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
			Statement statement = Main.connection.createStatement();
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
						phone_number, gender, weight, height);
			}

		} catch (Exception e) {

		}
		return null;
	}

	public StackPane createCard(String label1Text, String label2Text, String imagePath) {
		HBox card = new HBox(20);
		card.setPadding(new Insets(15)); // Increase padding for a larger card
		card.setAlignment(Pos.CENTER);
		card.setPrefSize(250, 200); // Increase width and height for a larger card
		card.setStyle("-fx-border-color: #FF6666; -fx-border-width: 2px; -fx-border-radius: 15px;");

		VBox contentBox = new VBox(30);
		contentBox.setAlignment(Pos.CENTER_LEFT);

		Label label1 = new Label(label1Text);
		label1.setStyle("-fx-font-size: 18px; -fx-text-fill: #000000;");

		Label label2 = new Label(label2Text);
		label2.setStyle("-fx-font-size: 24px; -fx-text-fill: #000000; -fx-font-weight: bold;");

		ImageView imageView = new ImageView(new Image(imagePath));
		imageView.setFitWidth(80); // Increase width for a larger image
		imageView.setPreserveRatio(true);
		HBox iconAndPriceHB = new HBox(imageView, label2);
		iconAndPriceHB.setAlignment(Pos.CENTER);
		iconAndPriceHB.setSpacing(20);
		contentBox.getChildren().addAll(iconAndPriceHB, label1);
		contentBox.setAlignment(Pos.CENTER);
//	    card.getChildren().addAll(contentBox, label2);

		Rectangle rec = new Rectangle();
		rec.setFill(Color.WHITE);
		rec.setWidth(250); // Increase width for a larger card
		rec.setHeight(200); // Increase height for a larger card
		rec.setArcWidth(32);
		rec.setArcHeight(32);
		rec.setStroke(Color.web("#FF8989")); // Set the stroke color
		rec.setStrokeWidth(4); // Set the stroke width

		StackPane overall = new StackPane();
		overall.getChildren().addAll(rec, contentBox);
		// Add drop shadow effect to the StackPane
		DropShadow dropShadow = new DropShadow();
		dropShadow.setColor(Color.web("#454545"));
		dropShadow.setRadius(5);
		dropShadow.setOffsetX(0);
		dropShadow.setOffsetY(2);
		overall.setEffect(dropShadow);

		return overall;
	}

	public double getTotalPaid(int patient_id) {
		try {
			Statement st = Main.connection.createStatement();
			ResultSet res = st
					.executeQuery("select sum(b.amount_paid) from patient p, billing_record b where p.patient_id = "
							+ patient_id + " and p.patient_id = b.patient_id;");
			if (res.next()) {
				return res.getDouble(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		return 0;
	}

	public double getTotalAmount(int patient_id) {
		try {
			Statement st = Main.connection.createStatement();
			ResultSet res = st
					.executeQuery("select sum(b.total_amount) from patient p, billing_record b where p.patient_id = "
							+ patient_id + " and p.patient_id = b.patient_id;");
			if (res.next()) {
				return res.getDouble(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		return 0;
	}

}
