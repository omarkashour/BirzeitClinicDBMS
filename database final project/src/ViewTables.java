import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class ViewTables extends BorderPane {
	Font font = Font.loadFont(getClass().getResourceAsStream("Satoshi-Variable.ttf"), 30);
	ComboBox<String> cb = new ComboBox<String>();
	Label chooseTableL = new Label("Choose a Table to view:");
	TableView tv;
	Button refreshBtn = new Button("Refresh");

	public ViewTables(Stage primaryStage, Scene scene) {
		cb.setOnAction(e -> {
			String table = cb.getValue();
			if (table.equals("patient")) {
				patientHandler();
			} else if (table.equals("physician")) {
				physicianHandler();
			} else if (table.equals("prescription")) {
				prescriptionHandler();
			} else if (table.equals("billing_record")) {
				billingRecordHandler();
			} else if (table.equals("medical_record")) {
				medicalRecordHandler();
			} else if (table.equals("appointment")) {
				appointmentHandler();
			}
		});
		cb.getItems().addAll("patient","billing_record", "medical_record", "physician",
				"prescription", "appointment");
		HBox hb = new HBox(chooseTableL, cb, refreshBtn);
		hb.setAlignment(Pos.CENTER);
		hb.setSpacing(15);
		setTop(hb);
		setPadding(new Insets(15));
	}

	public void billingRecordHandler() {
		tv = new TableView<BillingRecord>();
		refreshBtn.setOnAction(e -> {
			try {
				tv.getItems().clear();
				addBillingRecords(tv);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		TableColumn<BillingRecord, Integer> recordIdColumn = new TableColumn<>("record_id");
		recordIdColumn.setCellValueFactory(new PropertyValueFactory<>("record_id"));

		TableColumn<BillingRecord, Integer> patientIdColumn = new TableColumn<>("patient_id");
		patientIdColumn.setCellValueFactory(new PropertyValueFactory<>("patient_id"));

		TableColumn<BillingRecord, String> billingMethodColumn = new TableColumn<>("billing_method");
		billingMethodColumn.setCellValueFactory(new PropertyValueFactory<>("billing_method"));

		TableColumn<BillingRecord, Double> totalAmountDueColumn = new TableColumn<>("total_amount");
		totalAmountDueColumn.setCellValueFactory(new PropertyValueFactory<>("total_amount"));

		TableColumn<BillingRecord, Double> amountPaidColumn = new TableColumn<>("amount_payed");
		amountPaidColumn.setCellValueFactory(new PropertyValueFactory<>("amount_paid"));

		TableColumn<BillingRecord, Double> amountLeftColumn = new TableColumn<>("amount_left");
		amountLeftColumn.setCellValueFactory(new PropertyValueFactory<>("amount_left"));

		TableColumn<BillingRecord, LocalDate> dateOfBillingColumn = new TableColumn<>("date_of_billing");
		dateOfBillingColumn.setCellValueFactory(new PropertyValueFactory<>("date_of_billing"));

		TableColumn<BillingRecord, String> detailsColumn = new TableColumn<>("details");
		detailsColumn.setCellValueFactory(new PropertyValueFactory<>("details"));

		TableColumn<BillingRecord, String> paymentStatusColumn = new TableColumn<>("payment_status");
		paymentStatusColumn.setCellValueFactory(new PropertyValueFactory<>("payment_status"));

		tv.getColumns().addAll(recordIdColumn, patientIdColumn, billingMethodColumn, totalAmountDueColumn,
				amountPaidColumn, amountLeftColumn, dateOfBillingColumn, detailsColumn, paymentStatusColumn);
		// Call the method to add data to the table view
		try {
			addBillingRecords(tv);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		setCenter(tv);
		tv.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		setMargin(tv, new Insets(15));
	}

	public void appointmentHandler() {
		tv = new TableView<>();

		refreshBtn.setOnAction(e -> {
			try {
				tv.getItems().clear();
				addAppointments(tv);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		TableColumn<Appointment, Integer> appointmentIdColumn = new TableColumn<>("ap_id");
		appointmentIdColumn.setCellValueFactory(new PropertyValueFactory<>("ap_id"));

		TableColumn<Appointment, String> appointmentReasonColumn = new TableColumn<>("ap_reason");
		appointmentReasonColumn.setCellValueFactory(new PropertyValueFactory<>("ap_reason"));

		TableColumn<Appointment, Integer> physicianIdColumn = new TableColumn<>("phys_id");
		physicianIdColumn.setCellValueFactory(new PropertyValueFactory<>("phys_id"));

		TableColumn<Appointment, String> appointmentDateColumn = new TableColumn<>("ap_date");
		appointmentDateColumn.setCellValueFactory(new PropertyValueFactory<>("ap_date"));

		TableColumn<Appointment, String> appointmentTimeColumn = new TableColumn<>("ap_time");
		appointmentTimeColumn.setCellValueFactory(new PropertyValueFactory<>("ap_time"));

		TableColumn<Appointment, Integer> patientIdColumn = new TableColumn<>("patient_id");
		patientIdColumn.setCellValueFactory(new PropertyValueFactory<>("patient_id"));

		// Add the columns to the TableView
		tv.getColumns().addAll(appointmentIdColumn, appointmentReasonColumn, physicianIdColumn, appointmentDateColumn,
				appointmentTimeColumn, patientIdColumn);
		try {
			addAppointments(tv);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		setCenter(tv);
		tv.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		setMargin(tv, new Insets(15));
	}


	public void medicalRecordHandler() {
		tv = new TableView<>();

		refreshBtn.setOnAction(e -> {
			try {
				tv.getItems().clear();
				addMedicalRecords(tv);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		TableColumn<MedicalRecord, Integer> idColumn = new TableColumn<>("record_id");
		idColumn.setCellValueFactory(new PropertyValueFactory<>("record_id"));

		TableColumn<MedicalRecord, Integer> patientIdColumn = new TableColumn<>("patient_id");
		patientIdColumn.setCellValueFactory(new PropertyValueFactory<>("patient_id"));

		TableColumn<MedicalRecord, String> historyColumn = new TableColumn<>("illness_history");
		historyColumn.setCellValueFactory(new PropertyValueFactory<>("illness_history"));

		TableColumn<MedicalRecord, String> allergiesColumn = new TableColumn<>("allergies");
		allergiesColumn.setCellValueFactory(new PropertyValueFactory<>("allergies"));

		TableColumn<MedicalRecord, String> surgeriesColumn = new TableColumn<>("surgeries");
		surgeriesColumn.setCellValueFactory(new PropertyValueFactory<>("surgeries"));

		TableColumn<MedicalRecord, String> medicationColumn = new TableColumn<>("medication_history");
		medicationColumn.setCellValueFactory(new PropertyValueFactory<>("medication_history"));

		// Add the columns to the TableView
		tv.getColumns().addAll(idColumn, patientIdColumn, historyColumn, allergiesColumn, surgeriesColumn,
				medicationColumn);
		try {
			addMedicalRecords(tv);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		setCenter(tv);
		tv.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		setMargin(tv, new Insets(15));
	}

	public void physicianHandler() {
		tv = new TableView<>();

		refreshBtn.setOnAction(e -> {
			try {
				tv.getItems().clear();
				addPhysicians(tv);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		TableColumn<Physician, Integer> idColumn = new TableColumn<>("phys_id");
		idColumn.setCellValueFactory(new PropertyValueFactory<>("phys_id"));

		TableColumn<Physician, String> firstNameColumn = new TableColumn<>("first_name");
		firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("first_name"));

		TableColumn<Physician, String> lastNameColumn = new TableColumn<>("last_name");
		lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("last_name"));

		TableColumn<Physician, String> specialtyColumn = new TableColumn<>("speciality");
		specialtyColumn.setCellValueFactory(new PropertyValueFactory<>("speciality"));

		TableColumn<Physician, String> emailColumn = new TableColumn<>("email_address");
		emailColumn.setCellValueFactory(new PropertyValueFactory<>("email_address"));

		TableColumn<Physician, String> addressColumn = new TableColumn<>("address");
		addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));

		TableColumn<Physician, String> genderColumn = new TableColumn<>("gender");
		genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));

		// Add the columns to the TableView
		tv.getColumns().addAll(idColumn, firstNameColumn,lastNameColumn, specialtyColumn, emailColumn, addressColumn, genderColumn);
		try {
			addPhysicians(tv);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		setCenter(tv);
		tv.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		setMargin(tv, new Insets(15));
	}

	public void patientHandler() {
		tv = new TableView<Patient>();
		refreshBtn.setOnAction(e -> {
			try {
				tv.getItems().clear();
				addPatients(tv);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});

		TableColumn<Patient, Integer> patientIdColumn = new TableColumn<>("patient_id");
		patientIdColumn.setCellValueFactory(new PropertyValueFactory<>("patient_id"));

		TableColumn<Patient, String> firstNameColumn = new TableColumn<>("first_name");
		firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("first_name"));

		TableColumn<Patient, String> lastNameColumn = new TableColumn<>("last_name");
		lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("last_name"));
		
		TableColumn<Patient, Date> dateColumn = new TableColumn<>("date_of_birth");
		dateColumn.setCellValueFactory(new PropertyValueFactory<>("date_of_birth"));

		TableColumn<Patient, String> genderColumn = new TableColumn<>("gender");
		genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
		
		TableColumn<Patient, String> heightColumn = new TableColumn<>("height (cm)");
		heightColumn.setCellValueFactory(new PropertyValueFactory<>("height"));
		
		TableColumn<Patient, String> weightColumn = new TableColumn<>("weight (kg)");
		weightColumn.setCellValueFactory(new PropertyValueFactory<>("weight"));
		
		
		TableColumn<Patient, String> emailColumn = new TableColumn<>("email_address");
		emailColumn.setCellValueFactory(new PropertyValueFactory<>("email_address"));

		TableColumn<Patient, String> phoneColumn = new TableColumn<>("phone_number");
		phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone_number"));


		TableColumn<Patient, String> addressColumn = new TableColumn<>("address");
		addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));


		// Add the columns to the TableView
		tv.getColumns().addAll(patientIdColumn,firstNameColumn,lastNameColumn, dateColumn, emailColumn, phoneColumn, genderColumn,heightColumn,weightColumn
				,addressColumn);
		try {
			addPatients(tv);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		setCenter(tv);
		tv.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		setMargin(tv, new Insets(15));
	}

	public void prescriptionHandler() {
		tv = new TableView<>();

		refreshBtn.setOnAction(e -> {
			try {
				tv.getItems().clear();
				addPrescriptions(tv);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});

		TableColumn<Prescription, Integer> prIdColumn = new TableColumn<>("pr_id");
		prIdColumn.setCellValueFactory(new PropertyValueFactory<>("pr_id"));

		TableColumn<Prescription, String> medicationNameColumn = new TableColumn<>("medication_name");
		medicationNameColumn.setCellValueFactory(new PropertyValueFactory<>("medication_name"));

		TableColumn<Prescription, String> dosageColumn = new TableColumn<>("dosage");
		dosageColumn.setCellValueFactory(new PropertyValueFactory<>("dosage"));

		TableColumn<Prescription, String> frequencyColumn = new TableColumn<>("frequency");
		frequencyColumn.setCellValueFactory(new PropertyValueFactory<>("frequency"));

		TableColumn<Prescription, Integer> patientIdColumn = new TableColumn<>("patient_id");
		patientIdColumn.setCellValueFactory(new PropertyValueFactory<>("patient_id"));

		TableColumn<Prescription, Integer> physIdColumn = new TableColumn<>("phys_id");
		physIdColumn.setCellValueFactory(new PropertyValueFactory<>("phys_id"));

		// Add the columns to the TableView
		tv.getColumns().addAll(prIdColumn, medicationNameColumn, dosageColumn, frequencyColumn,
				patientIdColumn, physIdColumn);

		try {
			addPrescriptions(tv);
		} catch (SQLException e) {
			e.printStackTrace();
			// Handle the exception as per your requirements
		}

		setCenter(tv);
		tv.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		setMargin(tv, new Insets(15));
	}

	public void addPatients(TableView<Patient> tv) throws SQLException {
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

	}

	public void addBillingRecords(TableView<BillingRecord> tv) throws SQLException {
		Connection connection = DriverManager.getConnection(Main.url, Main.username, Main.password);
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery("SELECT * FROM billing_record");

		ArrayList<BillingRecord> billingRecords = new ArrayList<>();
		while (resultSet.next()) {
			int record_id = resultSet.getInt("record_id");
			int patient_id = resultSet.getInt("patient_id");
			String billing_method = resultSet.getString("billing_method");
			double total_amount = resultSet.getDouble("total_amount");
			double amount_paid = resultSet.getDouble("amount_paid");
			double amount_left = resultSet.getDouble("amount_left");
			String date_of_billing = resultSet.getString("date_of_billing");
			String details = resultSet.getString("details");
			String payment_status = resultSet.getString("payment_status");

			BillingRecord billingRecord = new BillingRecord(record_id, patient_id, billing_method, total_amount,
					amount_paid, amount_left, date_of_billing, details, payment_status);
			billingRecords.add(billingRecord);
		}

		tv.getItems().addAll(billingRecords);
	}

	public void addMedicalRecords(TableView<MedicalRecord> tv) throws SQLException {
		Connection connection = DriverManager.getConnection(Main.url, Main.username, Main.password);
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery("SELECT * FROM medical_record");

		ArrayList<MedicalRecord> medicalRecords = new ArrayList<>();
		while (resultSet.next()) {
			int record_id = resultSet.getInt("record_id");
			int patient_id = resultSet.getInt("patient_id");
			String illness_history = resultSet.getString("illness_history");
			String allergies = resultSet.getString("allergies");
			String surgeries = resultSet.getString("surgeries");
			String medication_history = resultSet.getString("medication_history");

			MedicalRecord medicalRecord = new MedicalRecord(record_id, patient_id, illness_history, allergies,
					surgeries, medication_history);
			medicalRecords.add(medicalRecord);
		}

		tv.getItems().addAll(medicalRecords);
	}

	public void addPhysicians(TableView<Physician> tv) throws SQLException {
		Connection connection = DriverManager.getConnection(Main.url, Main.username, Main.password);
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery("SELECT * FROM physician");

		ArrayList<Physician> physicians = new ArrayList<>();
		while (resultSet.next()) {
			int phys_id = resultSet.getInt("phys_id");
			String first_name = resultSet.getString("first_name");
			String last_name = resultSet.getString("last_name");
			String address = resultSet.getString("address");
			String phone_number = resultSet.getString("phone_number");
			String speciality = resultSet.getString("speciality");
			String email_address = resultSet.getString("email_address");
			String gender = resultSet.getString("gender");

			Physician physician = new Physician(phys_id, first_name, last_name, address,phone_number, speciality, email_address,
					gender);
			physicians.add(physician);
		}

		tv.getItems().addAll(physicians);
	}

	public void addPrescriptions(TableView<Prescription> tv) throws SQLException {
		Connection connection = DriverManager.getConnection(Main.url, Main.username, Main.password);
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery("SELECT * FROM prescription");

		ArrayList<Prescription> prescriptions = new ArrayList<>();
		while (resultSet.next()) {
			int pr_id = resultSet.getInt("pr_id");
			int patient_id = resultSet.getInt("patient_id");
			int phys_id = resultSet.getInt("phys_id");
			String medication_name = resultSet.getString("medication_name");
			String dosage = resultSet.getString("dosage");
			String frequency = resultSet.getString("frequency");

			Prescription prescription = new Prescription(pr_id, patient_id, phys_id, medication_name, dosage,
					frequency);
			prescriptions.add(prescription);
		}

		tv.getItems().addAll(prescriptions);
	}

	public void addAppointments(TableView<Appointment> tv) throws SQLException {
		Connection connection = DriverManager.getConnection(Main.url, Main.username, Main.password);
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery("SELECT * FROM appointment");

		ArrayList<Appointment> appointments = new ArrayList<>();
		while (resultSet.next()) {
			int ap_id = resultSet.getInt("ap_id");
			String ap_reason = resultSet.getString("ap_reason");
			int phys_id = resultSet.getInt("phys_id");
			String ap_date = resultSet.getString("ap_date");
			String ap_time = resultSet.getString("ap_time");
			int patient_id = resultSet.getInt("patient_id");
			String status = resultSet.getString("status");
			double cost = resultSet.getDouble("cost");
			String duration = resultSet.getString("duration");

			Appointment appointment = new Appointment( ap_id,  patient_id,  phys_id,  ap_reason,  ap_date,  ap_time,
					 status,  cost,  duration);
			appointments.add(appointment);
		}

		tv.getItems().addAll(appointments);
	}

}
