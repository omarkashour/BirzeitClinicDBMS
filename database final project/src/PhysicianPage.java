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
import javafx.stage.Stage;

public class PhysicianPage extends BorderPane {
  private TextField fnameTf, lnameTf, addressTf, specialityTf, emailTf, phoneTf;
  private ComboBox<String> genderCbx;
  private ArrayList<Physician> data;
  private ObservableList<Physician> dataList;
  private Button add, delete;
  private TableView<Physician> tb;
  public PhysicianPage() {
	  
	  try {
    delete = new Button("Delete physician");
    add = new Button("Add new physician");
    data = new ArrayList<>();
    dataList = FXCollections.observableArrayList(data);

    getStylesheets().addAll("style.css");
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
    initializeTf(fnameTf, lnameTf, addressTf, specialityTf, emailTf, phoneTf);
//    System.out.println(fnameTf);
    genderCbx = new ComboBox<>();
    genderCbx.getItems().addAll("F", "M");

    // gridpane for the textfields when adding a new physician
    GridPane gpane = new GridPane();
    gpane.setHgap(20);
    gpane.setVgap(20);
    gpane.addRow(0, new Label("First name: "), fnameTf, new Label("Last name: "), lnameTf, new Label("Specialty: "),
        specialityTf);
    gpane.addRow(1, new Label("Address"), addressTf, new Label("Email address: "), emailTf, new Label("Phone Number: "),
        phoneTf);
    gpane.addRow(2, new Label("Gender: "), genderCbx, add, delete);
    gpane.setPadding(new Insets(20));
    gpane.setAlignment(Pos.CENTER);

    tb = getTableView();
    tb.getItems().setAll(dataList);
    setCenter(tb);
    setBottom(gpane);

    add.setOnAction(e -> {
      addPhysician();
    });

    delete.setOnAction(e -> {
      deletePhysician();
    });
    
	  }catch(Exception e) {
		  
	  }

  }
  

  private void addPhysician() {
    String fname = fnameTf.getText();
    String lname = lnameTf.getText();
    String address = addressTf.getText();
    String specialty = specialityTf.getText();
    String email = emailTf.getText();
    String phone = phoneTf.getText();
    String gender = genderCbx.getSelectionModel().getSelectedItem();
    if(isEmpty(fnameTf, lnameTf, addressTf, specialityTf, emailTf, phoneTf)){
       ExecuteStatement("INSERT INTO physician (phys_id, first_name,last_name, address, speciality, email_address, phone_number, gender) VALUES ("+ 900+ ", '" + fname + "', '"+ lname +"', '" + address + "', '" + specialty+"', '"+ email+"', '"+ phone + "', '"+ gender +"');");
        tb.getItems().add(new Physician(900, fname, lname, address, phone, specialty, email, gender));
       tb.refresh();

    }

  }
  private void deletePhysician() { 
    if(!tb.getSelectionModel().getSelectedItem().equals(null)){
      Physician ph = tb.getSelectionModel().getSelectedItem();
      ExecuteStatement( "delete from  physician where phys_id=" + ph.getPhys_id() + ";");
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
  private TableView<Physician> getTableView() throws SQLException {

    // Create columns and define their properties
    TableView<Physician> tableView = new TableView<>();

    // Create columns and define their properties
    TableColumn<Physician, Integer> idColumn = new TableColumn<>("id");
    idColumn.setCellValueFactory(new PropertyValueFactory<>("phys_id"));
    idColumn.setEditable(false);
    TableColumn<Physician, String> firstNameColumn = new TableColumn<>("First Name");
    firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("first_name"));
    	firstNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());

    firstNameColumn.setEditable(true);
    firstNameColumn.setOnEditCommit(e->{
      String fname = e.getNewValue();
      int id = e.getRowValue().getPhys_id();
      ExecuteStatement("update physician set first_name = " + "'" + fname + "'" + " where phys_id = " + id + ";");
    });
    TableColumn<Physician, String> lastNameColumn = new TableColumn<>("Last Name");
    lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("last_name"));

    TableColumn<Physician, String> addressColumn = new TableColumn<>("Address");
    addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));

    TableColumn<Physician, String> specialityColumn = new TableColumn<>("Speciality");
    specialityColumn.setCellValueFactory(new PropertyValueFactory<>("speciality"));

    TableColumn<Physician, String> emailColumn = new TableColumn<>("Email");
    emailColumn.setCellValueFactory(new PropertyValueFactory<>("email_address")); // Corrected property name

    TableColumn<Physician, String> phoneColumn = new TableColumn<>("Phone Number");
    phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone_number"));
    TableColumn<Physician, String> genderColumn = new TableColumn<>("Gender");
    genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));

    // Add columns to the TableView
    tableView.getColumns().addAll(idColumn, firstNameColumn, lastNameColumn, addressColumn,
        specialityColumn, emailColumn, phoneColumn, genderColumn);
    
    tableView.setMaxWidth(1000);
    tableView.setMaxHeight(500);

    tableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
    getPhysicians(tableView);

    return tableView;
  }

  private void initializeTf(TextField... Tfs) {
    for (TextField tf : Tfs) {
      tf.setMaxSize(200, 30);
    }
  }

  private void getPhysicians(TableView<Physician> tb) throws SQLException {
    String query = "SELECT * FROM physician order by phys_id";
	Connection connection = DriverManager.getConnection(Main.url, Main.username, Main.password);
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
        dataList.add(new Physician(id, fname, lname,address , phoneNumber, specialty, emailAddress, gender));
        
      }

      rs.close();
      tb.getItems().addAll(dataList);
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