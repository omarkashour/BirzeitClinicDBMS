import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;

public class EditTables extends ViewTables {
	Font font = Font.loadFont(getClass().getResourceAsStream("Satoshi-Variable.ttf"), 30);
	Connection connection;
	Statement statement;
	GridPane gp = new GridPane();
	
	Button addBtn = new Button("Add");
	Button deleteBtn = new Button("Delete");
	ToggleGroup tg = new ToggleGroup();
	RadioButton addRadio = new RadioButton("Add Record");
	RadioButton deleteRadio = new RadioButton("Delete Record");
	HBox radiosHb = new HBox(addRadio,deleteRadio);
	VBox mainVb = new VBox(radiosHb,gp);

	public EditTables(Stage primaryStage, Scene scene) throws SQLException {
		super(primaryStage, scene);
		setCenter(tv);
		chooseTableL.setText("Choose a Table to edit:");
		connection = DriverManager.getConnection(Main.url, Main.username, Main.password);
		statement = connection.createStatement();
		tg.getToggles().addAll(addRadio,deleteRadio);
		radiosHb.setSpacing(15);
		mainVb.setSpacing(15);
		addBtn.setAlignment(Pos.CENTER);
		gp.setAlignment(Pos.CENTER);
		gp.setVgap(15);
		gp.setHgap(15);
		addBtn.setMinWidth(100);
		addBtn.setMinHeight(30);
		
		deleteBtn.setMinWidth(100);
		deleteBtn.setMinHeight(30);
		radiosHb.setAlignment(Pos.CENTER);		
	}

	private void updateBillingRecordTable() {
		try {
			tv.getItems().clear();
			addBillingRecords(tv);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	private void updateAppointmentTable() {
		try {
			tv.getItems().clear();
			addAppointments(tv);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	private void updateMedicalRecordTable() {
		try {
			tv.getItems().clear();
			addMedicalRecords(tv);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	private void updatePhysicianTable() {
		try {
			tv.getItems().clear();
			addPhysicians(tv);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}	
	private void updatePatientTable() {
		try {
			tv.getItems().clear();
			addPatients(tv);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}	
	private void updatePrescriptionTable() {
		try {
			tv.getItems().clear();
			addPrescriptions(tv);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}	
	
	// needs add and delete
	public void appointmentHandler() {
		tv = new TableView<>();
		tv.setEditable(true);
		gp.getChildren().clear();
		refreshBtn.setOnAction(e->{
			updateAppointmentTable();
		});
		TableColumn<Appointment, Integer> appointmentIdColumn = new TableColumn<>("ap_id");
		appointmentIdColumn.setCellValueFactory(new PropertyValueFactory<>("ap_id"));

		appointmentIdColumn
				.setCellFactory(TextFieldTableCell.<Appointment, Integer>forTableColumn(new IntegerStringConverter()));
		appointmentIdColumn.setEditable(true);

		appointmentIdColumn.setOnEditCommit(e -> {
			int ap_id = e.getNewValue();
			int oldId = e.getOldValue();
			try {
				statement.execute("update appointment set ap_id = " + ap_id + " where ap_id = " + oldId + ";");
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			updateAppointmentTable();
		});

		TableColumn<Appointment, String> appointmentReasonColumn = new TableColumn<>("ap_reason");
		appointmentReasonColumn.setCellValueFactory(new PropertyValueFactory<>("ap_reason"));

		appointmentReasonColumn.setCellFactory(TextFieldTableCell.<Appointment>forTableColumn());
		appointmentReasonColumn.setEditable(true);

		appointmentReasonColumn.setOnEditCommit(e -> {
			String ap_reason = e.getNewValue();
			int ap_id = e.getRowValue().getAp_id();
			try {
				statement.execute("update appointment set ap_reason = " + "'" + ap_reason + "'" + " where ap_id = " + ap_id + ";");
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			updateAppointmentTable();
		});

		TableColumn<Appointment, Integer> physicianIdColumn = new TableColumn<>("phys_id");
		physicianIdColumn.setCellValueFactory(new PropertyValueFactory<>("phys_id"));
		

		physicianIdColumn.setCellFactory(TextFieldTableCell.<Appointment, Integer>forTableColumn(new IntegerStringConverter()));
		physicianIdColumn.setEditable(true);
		
		physicianIdColumn.setOnEditCommit(e->{
			int phys_id = e.getNewValue();
			int ap_id = e.getRowValue().getAp_id();
			try {
				statement.execute("update appointment set phys_id = " + phys_id + " where ap_id = " + ap_id + ";");
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			updateAppointmentTable();
		});
		
		
		TableColumn<Appointment, String> appointmentDateColumn = new TableColumn<>("ap_date");
		appointmentDateColumn.setCellValueFactory(new PropertyValueFactory<>("ap_date"));

		appointmentDateColumn.setCellFactory(TextFieldTableCell.<Appointment>forTableColumn());
		appointmentDateColumn.setEditable(true);
		
		appointmentDateColumn.setOnEditCommit(e->{
			String ap_date = e.getNewValue();
			int ap_id = e.getRowValue().getAp_id();
			
			try {
				statement.execute("update appointment set ap_Date = " + "'" + ap_date +"'" + " where ap_id = " + ap_id +";");
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			updateAppointmentTable();
		});
		
		TableColumn<Appointment, String> appointmentTimeColumn = new TableColumn<>("ap_time");
		appointmentTimeColumn.setCellValueFactory(new PropertyValueFactory<>("ap_time"));

		appointmentTimeColumn.setCellFactory(TextFieldTableCell.<Appointment>forTableColumn());
		appointmentTimeColumn.setEditable(true);
		
		appointmentTimeColumn.setOnEditCommit(e->{
			String ap_time = e.getNewValue();
			int ap_id = e.getRowValue().getAp_id();
			
			try {
				statement.execute("update appointment set ap_time = " + "'" + ap_time + "'" + " where ap_id = "+ ap_id + ";");
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			updateAppointmentTable();
		});
		
		TableColumn<Appointment, Integer> patientIdColumn = new TableColumn<>("patient_id");
		patientIdColumn.setCellValueFactory(new PropertyValueFactory<>("patient_id"));
		
		patientIdColumn.setCellFactory(TextFieldTableCell.<Appointment, Integer>forTableColumn(new IntegerStringConverter()));
		patientIdColumn.setEditable(true);
		
		patientIdColumn.setOnEditCommit(e->{
			int patient_id = e.getNewValue();
			int ap_id = e.getRowValue().getAp_id();
			
			try {
				statement.execute("update appointment set patient_id = " + patient_id + " where ap_id = " + ap_id + ";");
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		
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

	// add and delete are done
	public void billingRecordHandler() {
		tv = new TableView<BillingRecord>();
		tv.setEditable(true);
		setBottom(mainVb);
		gp.getChildren().clear();
		refreshBtn.setOnAction(e->{
			updateBillingRecordTable();
		});
		TableColumn<BillingRecord, Integer> recordIdColumn = new TableColumn<BillingRecord, Integer>("record_id");
		recordIdColumn.setCellValueFactory(new PropertyValueFactory<BillingRecord, Integer>("record_id"));
		recordIdColumn.setCellFactory(
				TextFieldTableCell.<BillingRecord, Integer>forTableColumn(new IntegerStringConverter()));

		recordIdColumn.setEditable(true);
		recordIdColumn.setOnEditCommit(e -> {
			int record_id = e.getNewValue();
			try {
				statement.execute("update billing_record set record_id = " + record_id + " where record_id = "
						+ e.getOldValue() + ";");
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			updateBillingRecordTable();

		});

		TableColumn<BillingRecord, Integer> prIdColumn = new TableColumn<>("pr_id");
		prIdColumn.setCellValueFactory(new PropertyValueFactory<>("pr_id"));

		prIdColumn.setCellFactory(
				TextFieldTableCell.<BillingRecord, Integer>forTableColumn(new IntegerStringConverter()));

		prIdColumn.setEditable(true);
		prIdColumn.setOnEditCommit(e -> {
			int pr_id = e.getNewValue();
			int record_id = e.getRowValue().getRecord_id();
			try {
				statement.execute(
						"update billing_record set pr_id = " + pr_id + " where record_id = " + record_id + ";");
				ResultSet set = statement.executeQuery("select cost from prescription where pr_id = " + pr_id + ";");
				if (set.next()) {
					double cost = set.getDouble("cost");
					statement.execute("update billing_record set total_amount_due = " + cost + " where record_id = "
							+ record_id + ";");
					double amount_left = cost - e.getRowValue().getAmount_paid();
					statement.execute("update billing_record set amount_left = " + amount_left + " where record_id = "
							+ record_id + ";");
					if (amount_left > 0) {
						statement.execute("update billing_record set payment_status = 'Unpaid' where record_id = "
								+ record_id + ";");
					} else if (amount_left == 0) {
						statement.execute("update billing_record set payment_status = 'Paid' where record_id = "
								+ record_id + ";");
					}
				} else {
					System.out.println("No cost found for pr_id: " + pr_id);
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			updateBillingRecordTable();

		});

		TableColumn<BillingRecord, Double> amountPaidColumn = new TableColumn<>("amount_payed");
		amountPaidColumn.setCellValueFactory(new PropertyValueFactory<>("amount_payed"));

		amountPaidColumn
				.setCellFactory(TextFieldTableCell.<BillingRecord, Double>forTableColumn(new DoubleStringConverter()));

		amountPaidColumn.setEditable(true);
		amountPaidColumn.setOnEditCommit(e -> {
			double amount_payed = e.getNewValue();
			int record_id = e.getRowValue().getRecord_id();
			try {
				statement.execute("update billing_record set amount_payed = " + amount_payed + " where record_id = "
						+ record_id + ";");
				double amount_left = e.getRowValue().getTotal_amount() - amount_payed;
				statement.execute("update billing_record set amount_left = " + amount_left + " where record_id = "
						+ record_id + ";");
				if (amount_left > 0) {
					statement.execute(
							"update billing_record set payment_status = 'Unpaid' where record_id = " + record_id + ";");
				} else if (amount_left == 0) {
					statement.execute(
							"update billing_record set payment_status = 'Paid' where record_id = " + record_id + ";");
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			updateBillingRecordTable();
		});

		TableColumn<BillingRecord, Double> amountLeftColumn = new TableColumn<>("amount_left");
		amountLeftColumn.setCellValueFactory(new PropertyValueFactory<>("amount_left"));

		amountLeftColumn
				.setCellFactory(TextFieldTableCell.<BillingRecord, Double>forTableColumn(new DoubleStringConverter()));
		amountLeftColumn.setEditable(true);

		amountLeftColumn.setOnEditCommit(e -> {
			double amount_left = e.getNewValue();
			int record_id = e.getRowValue().getRecord_id();
			try {
				statement.execute("update billing_record set amount_left = " + amount_left + " where record_id = "
						+ record_id + ";");
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			updateBillingRecordTable();
		});

		TableColumn<BillingRecord, Double> totalAmountDueColumn = new TableColumn<>("total_amount_due");
		totalAmountDueColumn.setCellValueFactory(new PropertyValueFactory<>("total_amount_due"));

		totalAmountDueColumn
				.setCellFactory(TextFieldTableCell.<BillingRecord, Double>forTableColumn(new DoubleStringConverter()));
		totalAmountDueColumn.setEditable(true);

		totalAmountDueColumn.setOnEditCommit(e -> {
			double total_amount_due = e.getNewValue();
			int record_id = e.getRowValue().getRecord_id();
			try {
				statement.execute("update billing_record set total_amount_due = " + total_amount_due
						+ " where record_id = " + record_id + ";");
				double amount_left = total_amount_due - e.getRowValue().getAmount_paid();
				statement.execute("update billing_record set amount_left = " + amount_left + " where record_id = "
						+ record_id + ";");
				if (amount_left > 0) {
					statement.execute(
							"update billing_record set payment_status = 'Unpaid' where record_id = " + record_id + ";");
				} else if (amount_left == 0) {
					statement.execute(
							"update billing_record set payment_status = 'Paid' where record_id = " + record_id + ";");
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			updateBillingRecordTable();

		});

		TableColumn<BillingRecord, String> dateOfBillingColumn = new TableColumn<>("date_of_billing");
		dateOfBillingColumn.setCellValueFactory(new PropertyValueFactory<>("date_of_billing"));

		dateOfBillingColumn.setCellFactory(TextFieldTableCell.<BillingRecord>forTableColumn());
		dateOfBillingColumn.setEditable(true);

		dateOfBillingColumn.setOnEditCommit(e -> {
			String date_of_billing = e.getNewValue();
			int record_id = e.getRowValue().getPatient_id();
			try {
				statement.execute("update billing_record set date_of_billing = " + "'" + date_of_billing + "'"
						+ " where record_id = " + record_id + ";");
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			updateBillingRecordTable();
		});

		TableColumn<BillingRecord, String> paymentStatusColumn = new TableColumn<>("payment_status");
		paymentStatusColumn.setCellValueFactory(new PropertyValueFactory<>("payment_status")); // no need to edit

		TableColumn<BillingRecord, String> detailsColumn = new TableColumn<>("details");
		detailsColumn.setCellValueFactory(new PropertyValueFactory<>("details"));

		detailsColumn.setCellFactory(TextFieldTableCell.<BillingRecord>forTableColumn());
		detailsColumn.setEditable(true);

		detailsColumn.setOnEditCommit(e -> {
			String details = e.getNewValue();
			int record_id = e.getRowValue().getRecord_id();

			try {
				statement.execute("update billing_record set details = " + "'" + details + "'" + " where record_id = "
						+ record_id + ";");
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			updateBillingRecordTable();
		});

		TableColumn<BillingRecord, Integer> patientIdColumn = new TableColumn<>("patient_id");
		patientIdColumn.setCellValueFactory(new PropertyValueFactory<>("patient_id"));

		patientIdColumn.setCellFactory(
				TextFieldTableCell.<BillingRecord, Integer>forTableColumn(new IntegerStringConverter()));
		patientIdColumn.setEditable(true);
		patientIdColumn.setOnEditCommit(e -> {
			int patient_id = e.getNewValue();
			int record_id = e.getRowValue().getRecord_id();
			try {
				statement.execute("update billing_record set patient_id = " + patient_id + " where record_id = "
						+ record_id + ";");
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			updateBillingRecordTable();
		});

		TableColumn<BillingRecord, Integer> bIdColumn = new TableColumn<>("b_id");
		bIdColumn.setCellValueFactory(new PropertyValueFactory<>("b_id"));
		bIdColumn.setCellFactory(
				TextFieldTableCell.<BillingRecord, Integer>forTableColumn(new IntegerStringConverter()));
		bIdColumn.setEditable(true);

		tv.getColumns().addAll(recordIdColumn, prIdColumn, amountPaidColumn, amountLeftColumn, totalAmountDueColumn,
				dateOfBillingColumn, paymentStatusColumn, detailsColumn, patientIdColumn, bIdColumn);
		// Call the method to add data to the table view
		try {
			addBillingRecords(tv);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		setCenter(tv);
		tv.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		setMargin(tv, new Insets(15));
		Label record_idL = new Label("Record ID:");
		Label pr_idL = new Label("Prescription ID:");
		Label amount_payedL = new Label("Amount Payed:");
		Label amount_leftL = new Label("Amount Left:");
		Label amount_dueL = new Label("Amount Due:");
		Label date_of_billingL = new Label("Billing Date:");
		Label payment_statusL = new Label("Payment Status:");
		Label detailsL = new Label("details:");
		Label patient_idL = new Label("Patient ID:");
		Label b_idL = new Label("Billing ID:");
		TextField record_idTF = new TextField();
		TextField pr_idTF = new TextField();
		TextField amount_payedTF = new TextField();
		TextField amount_leftTF = new TextField();
		TextField amount_dueTF = new TextField();
		TextField date_of_billingTF = new TextField();
		TextField payment_statusTF = new TextField();
		TextField detailsTF = new TextField();
		TextField patient_idTF = new TextField();
		TextField b_idTF = new TextField();


		gp.setHgap(10);
		gp.setVgap(10);
		
		
//  billing_record (total_amount_due,amount_payed,amount_left,pr_id, date_of_billing, payment_status,details , patient_id, b_id)
		radiosHb.setSpacing(15);
		addRadio.setOnAction(e->{
			gp.getChildren().clear();
			gp.add(addBtn, 3, 4);
			gp.add(pr_idL, 0, 0);
			gp.add(pr_idTF, 1, 0);
			gp.add(amount_dueL, 2, 0);
			gp.add(amount_dueTF, 3, 0);
			gp.add(amount_payedL, 0, 1);
			gp.add(amount_payedTF, 1, 1);
			gp.add(amount_leftL, 2, 1);
			gp.add(amount_leftTF, 3, 1);
			gp.add(date_of_billingL, 0, 2);
			gp.add(date_of_billingTF, 1, 2);
			gp.add(payment_statusL, 2, 2);
			gp.add(payment_statusTF, 3, 2);
			gp.add(detailsL, 0, 3);
			gp.add(detailsTF, 1, 3);
			gp.add(patient_idL, 2, 3);
			gp.add(patient_idTF, 3, 3);
			gp.add(b_idL, 0, 4);
			gp.add(b_idTF, 1, 4);
		});
		deleteRadio.setOnAction(e->{
			gp.getChildren().clear();
			gp.add(record_idL, 0, 0);
			gp.add(record_idTF, 1, 0);
			gp.add(deleteBtn, 1, 1);
		});
		addBtn.setOnAction(e->{
			if (amount_dueTF.getText() == null || amount_dueTF.getText().trim().isEmpty() ||
				    amount_payedTF.getText() == null || amount_payedTF.getText().trim().isEmpty() ||
				    amount_leftTF.getText() == null || amount_leftTF.getText().trim().isEmpty() ||
				    pr_idTF.getText() == null || pr_idTF.getText().trim().isEmpty() ||
				    date_of_billingTF.getText() == null || date_of_billingTF.getText().trim().isEmpty() ||
				    payment_statusTF.getText() == null || payment_statusTF.getText().trim().isEmpty() ||
				    detailsTF.getText() == null || detailsTF.getText().trim().isEmpty() ||
				    patient_idTF.getText() == null || patient_idTF.getText().trim().isEmpty() ||
				    b_idTF.getText() == null || b_idTF.getText().trim().isEmpty()) return;
			
			double total_amount_due = Double.parseDouble(amount_dueTF.getText().trim());
			double amount_payed = Double.parseDouble(amount_payedTF.getText().trim());
			double amount_left = Double.parseDouble(amount_leftTF.getText().trim());
			int pr_id = Integer.parseInt(pr_idTF.getText().trim());
			String date_of_billing = date_of_billingTF.getText().trim();
			String payment_status = payment_statusTF.getText().trim();
			String details = detailsTF.getText().trim();
			int patient_id = Integer.parseInt(patient_idTF.getText().trim());
			int b_id = Integer.parseInt(b_idTF.getText().trim());
			try {
				statement.execute("INSERT INTO"
						+ " billing_record (total_amount_due,amount_payed,amount_left,pr_id, date_of_billing, payment_status,details , patient_id, b_id) "
						+ "VALUES (" + total_amount_due + "," + amount_payed + "," + amount_left + "," + pr_id + "," + "'" + date_of_billing + "'" + "," + "'" + payment_status + "'" +"," + "'" + details + "'" +"," + patient_id + "," + b_id +");" );
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			updateBillingRecordTable();
			
		});
		
		deleteBtn.setOnAction(e->{
			if(record_idTF.getText().trim().equals("")) return;
			int record_id  = Integer.parseInt(   record_idTF.getText().trim());
			try {
				statement.execute("delete from billing_record where record_id = " + record_id);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			updateBillingRecordTable();
		});
		
		addBtn.setAlignment(Pos.CENTER);
		gp.setAlignment(Pos.CENTER);
		radiosHb.setAlignment(Pos.CENTER);
		setBottom(mainVb);
	}

	// add and delete are done
	public void medicalRecordHandler() {
		tv = new TableView<>();
		tv.setEditable(true);
		gp.getChildren().clear();

		refreshBtn.setOnAction(e->{
			updateMedicalRecordTable();
		});
		TableColumn<MedicalRecord, Integer> idColumn = new TableColumn<>("record_id");
		idColumn.setCellValueFactory(new PropertyValueFactory<>("record_id"));
		
		idColumn.setCellFactory(TextFieldTableCell.<MedicalRecord, Integer>forTableColumn(new IntegerStringConverter()));
		idColumn.setEditable(true);
		
		idColumn.setOnEditCommit(e->{
			int record_id = e.getNewValue();
			int oldId = e.getOldValue();
		
			try {
				statement.execute("update medical_record set record_id = " + record_id + " where record_id = " + oldId + ";" );
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			updateMedicalRecordTable();
		});
		
		TableColumn<MedicalRecord, Integer> patientIdColumn = new TableColumn<>("patient_id");
		patientIdColumn.setCellValueFactory(new PropertyValueFactory<>("patient_id"));

		patientIdColumn.setCellFactory(TextFieldTableCell.<MedicalRecord, Integer>forTableColumn(new IntegerStringConverter()));
		patientIdColumn.setEditable(true);
		
		patientIdColumn.setOnEditCommit(e->{
			int patient_id = e.getNewValue();
			int record_id = e.getRowValue().getRecord_id();
			
			try {
				statement.execute("update medical_record set patient_id = " + patient_id + " where record_id = " + record_id + ";");
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			updateMedicalRecordTable();
		});
		
		TableColumn<MedicalRecord, String> historyColumn = new TableColumn<>("illness_history");
		historyColumn.setCellValueFactory(new PropertyValueFactory<>("illness_history"));

		historyColumn.setCellFactory(TextFieldTableCell.<MedicalRecord>forTableColumn());
		historyColumn.setEditable(true);
		
		historyColumn.setOnEditCommit(e->{
			String illness_history = e.getNewValue();
			int record_id = e.getRowValue().getRecord_id();
			try {
				statement.execute("update medical_record set illness_history = " +  "'" + illness_history + "'" + " where record_id = " + record_id + ";" );
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			updateMedicalRecordTable();
		});
		
		TableColumn<MedicalRecord, String> allergiesColumn = new TableColumn<>("allergies");
		allergiesColumn.setCellValueFactory(new PropertyValueFactory<>("allergies"));

		allergiesColumn.setCellFactory(TextFieldTableCell.<MedicalRecord>forTableColumn());
		allergiesColumn.setEditable(true);
		
		allergiesColumn.setOnEditCommit(e->{
			String allergies = e.getNewValue();
			int record_id = e.getRowValue().getPatient_id();
			
			try {
				statement.execute("update medical_record set allergies = " + "'" + allergies + "'" + " where record_id = " + record_id +";");
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			updateMedicalRecordTable();
		});
		
		TableColumn<MedicalRecord, String> surgeriesColumn = new TableColumn<>("surgeries");
		surgeriesColumn.setCellValueFactory(new PropertyValueFactory<>("surgeries"));

		surgeriesColumn.setCellFactory(TextFieldTableCell.<MedicalRecord>forTableColumn());
		surgeriesColumn.setEditable(true);
		
		surgeriesColumn.setOnEditCommit(e->{
			String surgeries = e.getNewValue();
			int patient_id = e.getRowValue().getPatient_id();
			
			try {
				statement.execute("update medical_record set surgeries = " + "'" + surgeries  + "'" + " where patient_id = " + patient_id  +";");
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			updateMedicalRecordTable();
		});
		
		TableColumn<MedicalRecord, String> medicationColumn = new TableColumn<>("prescribed_medication");
		medicationColumn.setCellValueFactory(new PropertyValueFactory<>("prescribed_medication"));

		medicationColumn.setCellFactory(TextFieldTableCell.<MedicalRecord>forTableColumn());
		medicationColumn.setEditable(true);
		
		medicationColumn.setOnEditCommit(e->{
			String medication = e.getNewValue();
			int record_id = e.getRowValue().getPatient_id();
			
			try {
				statement.execute("update medication_record set medication = " + "'" + medication + "'" + " where record_id = " + record_id + ";");
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			updateMedicalRecordTable();
		});
		
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
		
//		
//		record_id int auto_increment,
//	    patient_id int,
//	    illness_history varchar(1000),
//	    allergies varchar(1000),
//	    surgeries varchar(1000),
//	    prescribed_medication varchar(1000),
		
		Label patient_idL = new Label("patient_id:");
		Label illness_historyL = new Label("illness_history:");
		Label allergiesL = new Label("allergies:");
		Label surgeriesL = new Label("surgeries:");
		Label prescribed_medicationL = new Label("prescribed_medication:");
		
		TextField patient_idTF = new TextField();
		TextField illness_historyTF = new TextField();
		TextField allergiesTF = new TextField();
		TextField surgeriesTF = new TextField();
		TextField prescribed_medicationTF = new TextField();
		
		addRadio.setOnAction(e->{
			gp.getChildren().clear();
			
			gp.add(patient_idL, 0, 0);
			gp.add(patient_idTF, 1, 0);
			gp.add(illness_historyL, 2, 0);
			gp.add(illness_historyTF, 3, 0);
			gp.add(allergiesL, 0, 1);
			gp.add(allergiesTF, 1, 1);
			gp.add(surgeriesL, 2, 1);
			gp.add(surgeriesTF, 3, 1);
			gp.add(prescribed_medicationL, 0, 2);
			gp.add(prescribed_medicationTF, 1, 2);
			gp.add(addBtn, 3, 2);
		});
		
		addBtn.setOnAction(e->{
			int patient_id = Integer.parseInt(patient_idTF.getText().trim());
			String allergies = allergiesTF.getText().trim();
			String illness_history = illness_historyTF.getText().trim();
			String surgeries = surgeriesTF.getText().trim();
			String prescribed_medication = prescribed_medicationTF.getText().trim();
			try {
				statement.execute("insert into medical_record (patient_id, illness_history, allergies, surgeries, prescribed_medication) values(" + patient_id +",'" + illness_history + "','" + allergies + "','" + surgeries + "','" + prescribed_medication + "');");
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			updateMedicalRecordTable();
		});
		
		Label record_idL = new Label("record_id");
		TextField record_idTF = new TextField("record_id");

		deleteRadio.setOnAction(e->{
			gp.getChildren().clear();
			gp.add(record_idL, 0, 0);
			gp.add(record_idTF, 1, 0);
			gp.add(deleteBtn, 1, 1);
			
		});
		
		deleteBtn.setOnAction(e->{
			int record_id = Integer.parseInt(record_idTF.getText().trim());
			
			try {
				statement.execute("delete from medical_record where record_id = " + record_id + ";");
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			updateMedicalRecordTable();
		});
		setBottom(mainVb);
	}

	// needs add and delete
	public void physicianHandler() {
		tv = new TableView<>();
		tv.setEditable(true);
		gp.getChildren().clear();
		refreshBtn.setOnAction(e->{
			updatePhysicianTable();
		});
		TableColumn<Physician, Integer> idColumn = new TableColumn<>("phys_id");
		idColumn.setCellValueFactory(new PropertyValueFactory<>("phys_id"));

		idColumn.setCellFactory(TextFieldTableCell.<Physician, Integer>forTableColumn(new IntegerStringConverter()));
		idColumn.setEditable(true);
		
		idColumn.setOnEditCommit(e->{
			int phys_id = e.getNewValue();
			int oldId = e.getRowValue().getPhys_id();
			
			try {
				statement.execute("update physician set phys_id = " + phys_id + " where phys_id = " + oldId + ";");
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			updatePhysicianTable();
		});
		
		TableColumn<Physician, String> nameColumn = new TableColumn<>("name");
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

		nameColumn.setCellFactory(TextFieldTableCell.<Physician>forTableColumn());
		nameColumn.setEditable(true);
		
		nameColumn.setOnEditCommit(e->{
			String name = e.getNewValue();
			int phys_id = e.getRowValue().getPhys_id();
			
			try {
				statement.execute("update physician set name = " + "'" + name + "'" + " where phys_id = " + phys_id + ";");
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			updatePhysicianTable();
		});
		
		TableColumn<Physician, String> specialtyColumn = new TableColumn<>("speciality");
		specialtyColumn.setCellValueFactory(new PropertyValueFactory<>("speciality"));

		specialtyColumn.setCellFactory(TextFieldTableCell.<Physician>forTableColumn());
		specialtyColumn.setEditable(true);
		
		specialtyColumn.setOnEditCommit(e->{
			String speciality = e.getNewValue();
			int phys_id = e.getRowValue().getPhys_id();
			
			try {
				statement.execute("update physician set speciality = " + "'" + speciality + "'" + " where phys_id = " + phys_id + ";");
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			updatePhysicianTable();

		});
		
		TableColumn<Physician, String> emailColumn = new TableColumn<>("email_address");
		emailColumn.setCellValueFactory(new PropertyValueFactory<>("email_address"));
		
		emailColumn.setCellFactory(TextFieldTableCell.<Physician>forTableColumn());
		emailColumn.setEditable(true);
		
		emailColumn.setOnEditCommit(e->{
			String email_address = e.getNewValue();
			int phys_id = e.getRowValue().getPhys_id();
			
			try {
				statement.execute("update physician set email_address = " + "'" + email_address + "'" + " where phys_id = " + phys_id +";");
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			updatePhysicianTable();
			
		});

		TableColumn<Physician, String> addressColumn = new TableColumn<>("address");
		addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));

		addressColumn.setCellFactory(TextFieldTableCell.<Physician>forTableColumn());
		addressColumn.setEditable(true);
		
		addressColumn.setOnEditCommit(e->{
			String address = e.getNewValue();
			int phys_id = e.getRowValue().getPhys_id();
			
			try {
				statement.execute("update physician set address = " + "'" + address + "'" + " where phys_id = "+ phys_id + ";");
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			updatePhysicianTable();
		});
		
		TableColumn<Physician, String> genderColumn = new TableColumn<>("gender");
		genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));

		genderColumn.setCellFactory(TextFieldTableCell.<Physician>forTableColumn());
		genderColumn.setEditable(true);
		
		genderColumn.setOnEditCommit(e->{
			String gender = e.getNewValue();
			int phys_id = e.getRowValue().getPhys_id();
			
			try {
				statement.execute("update physician set gender = " + "'" + gender + "'" + " where phys_id = " + phys_id + ";");
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			updatePhysicianTable();
		});
		
		// Add the columns to the TableView
		tv.getColumns().addAll(idColumn, nameColumn, specialtyColumn, emailColumn, addressColumn, genderColumn);
		try {
			addPhysicians(tv);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		setCenter(tv);
		tv.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		setMargin(tv, new Insets(15));
	}

	// needs add and delete

	public void patientHandler() {
		tv = new TableView<Patient>();
		tv.setEditable(true);
		gp.getChildren().clear();

		refreshBtn.setOnAction(e->{
			updatePatientTable();
		});
		TableColumn<Patient, Integer> patientIdColumn = new TableColumn<>("patient_id");
		patientIdColumn.setCellValueFactory(new PropertyValueFactory<>("patient_id"));

		patientIdColumn.setCellFactory(TextFieldTableCell.<Patient, Integer>forTableColumn(new IntegerStringConverter()));
		patientIdColumn.setEditable(true);
		
		patientIdColumn.setOnEditCommit(e->{
			int patient_id = e.getNewValue();
			int oldId = e.getOldValue();
			
			try {
				statement.execute("update patient set patient_id = " + patient_id + " where patient_id = " + oldId + ";");
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			updatePatientTable(); 
		});
		
		TableColumn<Patient, String> nameColumn = new TableColumn<>("name");
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

		nameColumn.setCellFactory(TextFieldTableCell.<Patient>forTableColumn());
		nameColumn.setEditable(true);
		
		nameColumn.setOnEditCommit(e->{
			String name = e.getNewValue();
			int patient_id = e.getRowValue().getPatient_id();
			
			try {
				statement.execute("update patient set name = " + "'" + name + "'" + " where patient_id = " + patient_id + ";");
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			updatePatientTable(); 
		});
		
		TableColumn<Patient, String> dateColumn = new TableColumn<>("date");
		dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

		dateColumn.setCellFactory(TextFieldTableCell.<Patient>forTableColumn());
		dateColumn.setEditable(true);
		
		dateColumn.setOnEditCommit(e->{
			String date = e.getNewValue();
			int patient_id = e.getRowValue().getPatient_id();
			
			try {
				statement.execute("update patient set date = " + "'" +  date + "'" + " where patient_id = " + patient_id + ";");
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			updatePatientTable();
		});
		
		TableColumn<Patient, String> emailColumn = new TableColumn<>("email_address");
		emailColumn.setCellValueFactory(new PropertyValueFactory<>("email_address"));

		emailColumn.setCellFactory(TextFieldTableCell.<Patient>forTableColumn());
		emailColumn.setEditable(true);		
		
		emailColumn.setOnEditCommit(e->{
			String email = e.getNewValue();
			int patient_id = e.getRowValue().getPatient_id();
			
			try {
				statement.execute("update patient set email = " + "'" + email + "'" + " where patient_id = " + patient_id +";");
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			updatePatientTable();
		});
		
		TableColumn<Patient, String> phoneColumn = new TableColumn<>("phone_number");
		phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone_number"));

		phoneColumn.setCellFactory(TextFieldTableCell.<Patient>forTableColumn());
		phoneColumn.setEditable(true);		
		
		phoneColumn.setOnEditCommit(e->{
			String phone = e.getNewValue();
			int patient_id = e.getRowValue().getPatient_id();
			try {
				statement.execute("update patient set phone = " + "'" + phone + "'" + " where patient_id  = " + patient_id + ";");
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			updatePatientTable();
		});
		
		TableColumn<Patient, String> genderColumn = new TableColumn<>("gender");
		genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));


		genderColumn.setCellFactory(TextFieldTableCell.<Patient>forTableColumn());
		genderColumn.setEditable(true);	
		
		genderColumn.setOnEditCommit(e->{
			String gender = e.getNewValue();
			int patient_id = e.getRowValue().getPatient_id();
			
			try {
				statement.execute("update patient set gender = " + "'" + gender + "'" + " where patient_id = " + patient_id + ";");
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			updatePatientTable();
		});
		
		TableColumn<Patient, String> addressColumn = new TableColumn<>("address");
		addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));

		addressColumn.setCellFactory(TextFieldTableCell.<Patient>forTableColumn());
		addressColumn.setEditable(true);	
		
		addressColumn.setOnEditCommit(e->{
			String address = e.getNewValue();
			int patient_id = e.getRowValue().getPatient_id();
			
			try {
				statement.execute("update patient set address = " + "'" + address + "'" + " where patient_id = " + patient_id + ";");
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			updatePatientTable();
		});
		
		TableColumn<Patient, Integer> bIdColumn = new TableColumn<>("b_id");
		bIdColumn.setCellValueFactory(new PropertyValueFactory<>("b_id"));

		bIdColumn.setCellFactory(TextFieldTableCell.<Patient, Integer>forTableColumn(new IntegerStringConverter()));
		bIdColumn.setEditable(true);
		bIdColumn.setOnEditCommit(e->{
			int b_id = e.getNewValue();
			int patient_id = e.getRowValue().getPatient_id();
			try {
				statement.execute("update patient set b_id = " + b_id + " where patient_id = " + patient_id + ";");
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			updatePatientTable();
		});
		
		// Add the columns to the TableView
		tv.getColumns().addAll(patientIdColumn, nameColumn, dateColumn, emailColumn, phoneColumn, genderColumn,
				addressColumn, bIdColumn);
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

	// needs add and delete

	public void prescriptionHandler() {
		tv = new TableView<>();
		tv.setEditable(true);
		gp.getChildren().clear();

		refreshBtn.setOnAction(e->{
			updatePrescriptionTable();
		});
		TableColumn<Prescription, Integer> prIdColumn = new TableColumn<>("pr_id");
		prIdColumn.setCellValueFactory(new PropertyValueFactory<>("pr_id"));

		prIdColumn.setCellFactory(TextFieldTableCell.<Prescription, Integer>forTableColumn(new IntegerStringConverter()));
		prIdColumn.setEditable(true);
		
		prIdColumn.setOnEditCommit(e->{
			int pr_id = e.getNewValue();
			int oldId = e.getOldValue();
			
			try {
				statement.execute("update prescription set pr_id = " + pr_id + " where pr_id = " + oldId +";");
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			updatePrescriptionTable();
		});
		
		TableColumn<Prescription, Double> costColumn = new TableColumn<>("cost");
		costColumn.setCellValueFactory(new PropertyValueFactory<>("cost"));

		costColumn.setCellFactory(TextFieldTableCell.<Prescription, Double>forTableColumn(new DoubleStringConverter()));
		costColumn.setEditable(true);
		
		costColumn.setOnEditCommit(e->{
			double cost = e.getNewValue();
			int pr_id = e.getRowValue().getPr_id();
			
			try {
				statement.execute("update prescription set cost = " + cost + " where pr_id = " + pr_id + ";");
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			updatePrescriptionTable();
		});
		
		TableColumn<Prescription, String> medicationNameColumn = new TableColumn<>("medication_name");
		medicationNameColumn.setCellValueFactory(new PropertyValueFactory<>("medication_name"));

		medicationNameColumn.setCellFactory(TextFieldTableCell.<Prescription>forTableColumn());
		medicationNameColumn.setEditable(true);
		
		medicationNameColumn.setOnEditCommit(e->{
			String medication_name = e.getNewValue();
			int pr_id = e.getRowValue().getPr_id();
			
			try {
				statement.execute("update prescription set medication_name = " + "'" +  medication_name + "'" + " where pr_id = " + pr_id + ";");
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			updatePrescriptionTable();
		});
		
		TableColumn<Prescription, String> dosageColumn = new TableColumn<>("dosage");
		dosageColumn.setCellValueFactory(new PropertyValueFactory<>("dosage"));

		dosageColumn.setCellFactory(TextFieldTableCell.<Prescription>forTableColumn());
		dosageColumn.setEditable(true);
		
		dosageColumn.setOnEditCommit(e->{
			String dosage = e.getNewValue();
			int pr_id = e.getRowValue().getPr_id();
			
			try {
				statement.execute("update prescription set dosage = " + "'" + dosage + "'" + " where pr_id = " + pr_id + ";");
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			updatePrescriptionTable();
		});
		
		TableColumn<Prescription, String> frequencyColumn = new TableColumn<>("frequency");
		frequencyColumn.setCellValueFactory(new PropertyValueFactory<>("frequency"));

		frequencyColumn.setCellFactory(TextFieldTableCell.<Prescription>forTableColumn());
		frequencyColumn.setEditable(true);
		
		frequencyColumn.setOnEditCommit(e->{
			String frequency = e.getNewValue();
			int pr_id = e.getRowValue().getPr_id();
			try {
				statement.execute("update prescription set frequency = " + "'" + frequency + "'" + " where pr_id = " + pr_id + ";");
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			updatePrescriptionTable();
		});
		
		TableColumn<Prescription, Integer> patientIdColumn = new TableColumn<>("patient_id");
		patientIdColumn.setCellValueFactory(new PropertyValueFactory<>("patient_id"));

		patientIdColumn.setCellFactory(TextFieldTableCell.<Prescription, Integer>forTableColumn(new IntegerStringConverter()));
		patientIdColumn.setEditable(true);
		
		patientIdColumn.setOnEditCommit(e->{
			int patient_id = e.getNewValue();
			int pr_id = e.getRowValue().getPr_id();
			
			try {
				statement.execute("update prescription set patient_id = " + patient_id + " where pr_id = " + pr_id +";");
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			updatePrescriptionTable();
		});
		
		
		TableColumn<Prescription, Integer> physIdColumn = new TableColumn<>("phys_id");
		physIdColumn.setCellValueFactory(new PropertyValueFactory<>("phys_id"));

		physIdColumn.setCellFactory(TextFieldTableCell.<Prescription, Integer>forTableColumn(new IntegerStringConverter()));
		physIdColumn.setEditable(true);
		
		physIdColumn.setOnEditCommit(e->{
			int phys_id = e.getNewValue();
			int pr_id = e.getRowValue().getPr_id();
			
			try {
				statement.execute("update prescription set phys_id = " + phys_id + " where pr_id = " + pr_id +";");
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			updatePrescriptionTable();
		});
		
		
		// Add the columns to the TableView
		tv.getColumns().addAll(prIdColumn, costColumn, medicationNameColumn, dosageColumn, frequencyColumn,
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
}
