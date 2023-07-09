import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class PhysicianPage extends BorderPane {
	private TextField fnameTf, lnameTf, addressTf, specialityTf, emailTf, phoneTf, id;
	private ComboBox<String> genderCbx;
	private ArrayList<Physician> data;
	private ObservableList<Physician> dataList;
	private Button add, delete, search;
	private TableView<Physician> tb;

	public PhysicianPage() {
		search = new Button("search");
		id = new TextField();
		Label searchL = new Label("Search by id: ");
		searchL.setStyle("-fx-font-family: 'Product Sans'; -fx-text-fill: white; -fx-font-size: 22px; -fx-font-weight: bold;");
		HBox hbx = new HBox(20, searchL, id, search);
		hbx.setPadding(new Insets(20));
		hbx.setAlignment(Pos.CENTER);
		delete = new Button("Delete physician");
		add = new Button("Add new physician");
		data = new ArrayList<>();
		dataList = FXCollections.observableArrayList(data);

		fnameTf = new TextField();
		fnameTf.setPromptText("e.g: Mahmood");
		lnameTf = new TextField();
		lnameTf.setPromptText("e.g: Abbas");
		addressTf = new TextField();
		addressTf.setPromptText("e.g: Ramallah, Al Irsal");
		specialityTf = new TextField();
		specialityTf.setPromptText("e.g: Cardiolology");
		emailTf = new TextField();
		emailTf.setPromptText("Mabbas@example.com");
		phoneTf = new TextField();
		phoneTf.setPromptText("0512345678");
		initializeTf(id, fnameTf, lnameTf, addressTf, specialityTf, emailTf, phoneTf);
		genderCbx = new ComboBox<>();
		genderCbx.getItems().addAll("F", "M");

		// gridpane for the textfields when adding a new physician
		GridPane gpane = new GridPane();
		gpane.setHgap(20);
		gpane.setVgap(20);
		Label firstNameLabel = new Label("First name: ");
		firstNameLabel.setStyle("-fx-font-family: 'Product Sans'; -fx-text-fill: white; -fx-font-size: 22px; -fx-font-weight: bold;");

		Label lastNameLabel = new Label("Last name: ");
		lastNameLabel.setStyle("-fx-font-family: 'Product Sans'; -fx-text-fill: white; -fx-font-size: 22px; -fx-font-weight: bold;");

		Label specialtyLabel = new Label("Specialty: ");
		specialtyLabel.setStyle("-fx-font-family: 'Product Sans'; -fx-text-fill: white; -fx-font-size: 22px; -fx-font-weight: bold;");

		Label addressLabel = new Label("Address: ");
		addressLabel.setStyle("-fx-font-family: 'Product Sans'; -fx-text-fill: white; -fx-font-size: 22px; -fx-font-weight: bold;");

		Label emailAddressLabel = new Label("Email address: ");
		emailAddressLabel.setStyle("-fx-font-family: 'Product Sans'; -fx-text-fill: white; -fx-font-size: 22px; -fx-font-weight: bold;");

		Label phoneNumberLabel = new Label("Phone Number: ");
		phoneNumberLabel.setStyle("-fx-font-family: 'Product Sans'; -fx-text-fill: white; -fx-font-size: 22px; -fx-font-weight: bold;");

		Label genderLabel = new Label("Gender: ");
		genderLabel.setStyle("-fx-font-family: 'Product Sans'; -fx-text-fill: white; -fx-font-size: 22px; -fx-font-weight: bold;");

		gpane.addRow(0, firstNameLabel, fnameTf, lastNameLabel, lnameTf, specialtyLabel, specialityTf);
		gpane.addRow(1, addressLabel, addressTf, emailAddressLabel, emailTf, phoneNumberLabel, phoneTf);
		gpane.addRow(2, genderLabel, genderCbx, add, delete);

		gpane.setPadding(new Insets(20));
		gpane.setAlignment(Pos.CENTER);
		tb = getTableView();
		tb.getItems().setAll(dataList);
		setCenter(tb);
		setBottom(gpane);
		setTop(hbx);
		add.setOnAction(e -> {
			addPhysician();
		});

		delete.setOnAction(e -> {
			deletePhysician();
		});
		search.setOnAction(e -> {
			searchPhysician();
		});

	}

	private void searchPhysician() {
		try {
			int idNum = Integer.parseInt(id.getText());
			System.out.println(idNum);
			String query = "SELECT * FROM physician where(phys_id=" + idNum + ");";
			try (Connection connection = DriverManager.getConnection(Main.url, Main.username, Main.password)) {
				try (Statement statement = connection.createStatement();) {
					ResultSet rs = statement.executeQuery(query);
					if (rs.next()) {
						int id = rs.getInt("phys_id");
						String fname = rs.getString("first_name");
						String lname = rs.getString("last_name");
						String address = rs.getString("address");
						String specialty = rs.getString("speciality");
						String emailAddress = rs.getString("email_address");
						String phoneNumber = rs.getString("phone_number");
						String gender = rs.getString("gender");
						Physician ph = new Physician(id, fname, lname, address, phoneNumber, specialty, emailAddress,
								gender);
						showPhys(ph);
						rs.close();
					}

				} catch (NumberFormatException e) {
					System.out.println("EE");
				}
			}
		} catch (Exception d) {
			
		}
	}

	private void showPhys(Physician ph) {
		Stage st = new Stage();
		Label id = new Label("Id: " + ph.getPhys_id());
		Label name = new Label("Name: " + ph.getFirst_name() + " " + ph.getLast_name());
		Label speciality = new Label("Speciality: " + ph.getSpeciality());
		Label phone = new Label("Phone Number: " + ph.getPhone_number());
		Label address = new Label("Address: " + ph.getAddress());
		Label email = new Label("Email: " + ph.getEmail_address());
		Label NumOfpatient = new Label("Patients treated: " + getNumOfPatients());
		id.setStyle("-fx-font-family: 'Product Sans'; -fx-text-fill: white; -fx-font-size: 22px; -fx-font-weight: bold;");
		name.setStyle("-fx-font-family: 'Product Sans'; -fx-text-fill: white; -fx-font-size: 22px; -fx-font-weight: bold;");
		speciality.setStyle("-fx-font-family: 'Product Sans'; -fx-text-fill: white; -fx-font-size: 22px; -fx-font-weight: bold;");
		phone.setStyle("-fx-font-family: 'Product Sans'; -fx-text-fill: white; -fx-font-size: 22px; -fx-font-weight: bold;");
		address.setStyle("-fx-font-family: 'Product Sans'; -fx-text-fill: white; -fx-font-size: 22px; -fx-font-weight: bold;");
		email.setStyle("-fx-font-family: 'Product Sans'; -fx-text-fill: white; -fx-font-size: 22px; -fx-font-weight: bold;");
		NumOfpatient.setStyle("-fx-font-family: 'Product Sans'; -fx-text-fill: white; -fx-font-size: 22px; -fx-font-weight: bold;");
		GridPane gpane = new GridPane();
		Label genderL = new Label("Gender: " + ph.getGender());
		genderL.setStyle("-fx-font-family: 'Product Sans'; -fx-text-fill: white; -fx-font-size: 22px; -fx-font-weight: bold;");
		gpane.addRow(0, id, name);
		gpane.addRow(1, speciality, phone);
		gpane.addRow(2, address, email);
		gpane.addRow(3, NumOfpatient, genderL);
		gpane.setHgap(20);
		gpane.setVgap(20);
		gpane.setStyle(
				"-fx-font-family: 'Product Sans'; -fx-text-fill: white; -fx-font-size: 22px; -fx-font-weight: bold;");
		gpane.setAlignment(Pos.CENTER);

		Scene s = new Scene(gpane, 900, 300);
		gpane.setStyle("-fx-background-color: #FCAEAE;");
		s.getStylesheets().add("style.css");
		st.setScene(s);
		st.show();
	}

	private String getNumOfPatients() {
		int count = 0;
		String query = "select Count(p.patient_id ) as count from prescription a, patient p, physician ph where a.patient_id=p.patient_id and a.phys_id=ph.phys_id and ph.phys_id=7;";

		try (Connection connection = DriverManager.getConnection(Main.url, Main.username, Main.password)) {
			try (Statement statement = connection.createStatement();) {
				ResultSet rs = statement.executeQuery(query);

				if (rs.next()) {
					count = rs.getInt("count");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count + "";

	}

	private void addPhysician() {
		String fname = fnameTf.getText();
		String lname = lnameTf.getText();
		String address = addressTf.getText();
		String specialty = specialityTf.getText();
		String email = emailTf.getText();
		String phone = phoneTf.getText();
		String gender = genderCbx.getSelectionModel().getSelectedItem();
		if (isEmpty(fnameTf, lnameTf, addressTf, specialityTf, emailTf, phoneTf)) {
			ExecuteStatement(
					"INSERT INTO physician (phys_id, first_name,last_name, address, speciality, email_address, phone_number, gender) VALUES ("
							+ 900 + ", '" + fname + "', '" + lname + "', '" + address + "', '" + specialty + "', '"
							+ email + "', '" + phone + "', '" + gender + "');");
			tb.getItems().add(new Physician(900, fname, lname, address, phone, specialty, email, gender));
			tb.refresh();

		}

	}

	private void deletePhysician() {
		if (!tb.getSelectionModel().getSelectedItem().equals(null)) {
			Physician ph = tb.getSelectionModel().getSelectedItem();
			ExecuteStatement("delete from  physician where phys_id=" + ph.getPhys_id() + ";");
			tb.getItems().remove(ph);
			tb.refresh();
		}

	}

	private boolean isEmpty(TextField... i) {
		for (TextField textField : i) {
			if (textField.getText().isEmpty())
				return false;
		}
		return true;
	}

	// we just need to add the items here when connected
	private TableView<Physician> getTableView() {

		// Create columns and define their properties
		TableView<Physician> tableView = new TableView<>();
		tableView.setEditable(true);
		// Create columns and define their properties
		TableColumn<Physician, Integer> idColumn = new TableColumn<>("id");
		idColumn.setCellValueFactory(new PropertyValueFactory<>("phys_id"));
		idColumn.setEditable(false);
		TableColumn<Physician, String> firstNameColumn = new TableColumn<>("First Name");
		firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("first_name"));
		firstNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());

		firstNameColumn.setEditable(true);
		firstNameColumn.setOnEditCommit(e -> {
			String fname = e.getNewValue();
			int id = e.getRowValue().getPhys_id();
			ExecuteStatement("update physician set first_name = " + "'" + fname + "'" + " where phys_id = " + id + ";");
		});
		TableColumn<Physician, String> lastNameColumn = new TableColumn<>("Last Name");
		lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("last_name"));
		lastNameColumn.setEditable(true);
		lastNameColumn.setOnEditCommit(e -> {
			String fname = e.getNewValue();
			int id = e.getRowValue().getPhys_id();
			ExecuteStatement("update physician set last_name = " + "'" + fname + "'" + " where phys_id = " + id + ";");
		});
		TableColumn<Physician, String> addressColumn = new TableColumn<>("Address");
		addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
		addressColumn.setEditable(true);
		addressColumn.setOnEditCommit(e -> {
			String address = e.getNewValue();
			int id = e.getRowValue().getPhys_id();
			ExecuteStatement("update physician set address = " + "'" + address + "'" + " where phys_id = " + id + ";");
		});
		TableColumn<Physician, String> specialityColumn = new TableColumn<>("Speciality");
		specialityColumn.setCellValueFactory(new PropertyValueFactory<>("speciality"));
		specialityColumn.setEditable(true);
		specialityColumn.setOnEditCommit(e -> {
			String spec = e.getNewValue();
			int id = e.getRowValue().getPhys_id();
			ExecuteStatement("update physician set speciality = " + "'" + spec + "'" + " where phys_id = " + id + ";");
		});
		TableColumn<Physician, String> emailColumn = new TableColumn<>("Email");
		emailColumn.setCellValueFactory(new PropertyValueFactory<>("email_address")); // Corrected property name
		emailColumn.setEditable(true);
		emailColumn.setOnEditCommit(e -> {
			String email = e.getNewValue();
			int id = e.getRowValue().getPhys_id();
			ExecuteStatement(
					"update physician set email_address = " + "'" + email + "'" + " where phys_id = " + id + ";");
		});
		TableColumn<Physician, String> phoneColumn = new TableColumn<>("Phone Number");
		phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone_number"));
		phoneColumn.setEditable(true);
		phoneColumn.setOnEditCommit(e -> {
			String phone = e.getNewValue();
			int id = e.getRowValue().getPhys_id();
			ExecuteStatement(
					"update physician set phone_number = " + "'" + phone + "'" + " where phys_id = " + id + ";");
		});

		TableColumn<Physician, String> genderColumn = new TableColumn<>("Gender");
		genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
		genderColumn.setEditable(true);
		genderColumn.setOnEditCommit(e -> {
			String ge = e.getNewValue();
			int id = e.getRowValue().getPhys_id();
			ExecuteStatement("update physician set gender = " + "'" + ge + "'" + " where phys_id = " + id + ";");
		});
		// Add columns to the TableView
		tableView.getColumns().addAll(idColumn, firstNameColumn, lastNameColumn, addressColumn, specialityColumn,
				emailColumn, phoneColumn, genderColumn);

		tableView.setMaxWidth(1000);
		tableView.setMaxHeight(500);

		tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		getPhysicians(tableView);

		return tableView;
	}

	private void initializeTf(TextField... Tfs) {
		for (TextField tf : Tfs) {
			tf.setMaxSize(200, 30);
		}
	}

	private void getPhysicians(TableView<Physician> tb) {
		String query = "SELECT * FROM physician order by phys_id";
		try (Connection connection = DriverManager.getConnection(Main.url, Main.username, Main.password)) {
			try (Statement statement = connection.createStatement();) {
				ResultSet rs = statement.executeQuery(query);

				while (rs.next()) {
					int id = rs.getInt("phys_id");
					String fname = rs.getString("first_name");
					String lname = rs.getString("last_name");
					String address = rs.getString("address");
					String specialty = rs.getString("speciality");
					String emailAddress = rs.getString("email_address");
					String phoneNumber = rs.getString("phone_number");
					String gender = rs.getString("gender");
					dataList.add(
							new Physician(id, fname, lname, address, phoneNumber, specialty, emailAddress, gender));

				}

				rs.close();
				tb.getItems().addAll(dataList);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void ExecuteStatement(String SQL) {

		try {
			Connection connection = DriverManager.getConnection(Main.url, Main.username, Main.password);
			Statement statement = connection.createStatement();
			statement.executeUpdate(SQL);
			statement.close();

		} catch (SQLException s) {
			s.printStackTrace();
			System.out.println("SQL statement is not executed!");

		}

	}
}