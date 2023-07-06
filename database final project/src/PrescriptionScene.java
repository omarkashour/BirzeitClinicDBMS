
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

public class PrescriptionScene extends BorderPane {
	private ArrayList<Patient> data;
	String labelValue;
	   TextArea textArea ;
    private ObservableList<Patient> dataList;
	 ScrollPane sideBox;
	
	 TabPane tabPane; Tab viewAppointmentsTab;
	   private VBox createViewPatientPane() {
		   Label addPre=new Label("Add Prescription");
	        addPre.setStyle("-fx-font-family: 'Consolas'; -fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #333333;");
	    	VBox vb=new VBox(10);
	    	 sideBox=new ScrollPane();
	    	Label label1=new Label("Search Patient");
	        TextField searchField = new TextField();
	        searchField.setPromptText("ENTER PATIENT NAME OR ID");
	        searchField.setPrefWidth(300);

	        // Apply transparent background color to the text field
	        searchField.setStyle("-fx-background-color: rgba(0, 0, 0, 0.1); -fx-text-fill: white;");

	       

	        Button searchButton = new Button("Search");
	       // searchButton.setStyle("-fx-font-size: 12px; -fx-pref-width: 80px;");

	        // Apply a wider width to the search button
	        searchButton.setPrefWidth(170);

	        HBox searchBox = new HBox(10);
	        searchBox.getChildren().addAll(searchField, searchButton);

	        ImageView iv = new ImageView(new Image("search.png"));
	        iv.setFitWidth(16);
	        iv.setFitHeight(16);

	        searchButton.setGraphic(iv);
	        searchButton.setOnAction( e ->{
	        	if (!searchField.getText().isEmpty()) {
	        		String searchText = searchField.getText();
	                
	                // Check if the text contains only digits
	                if (searchText.matches("\\d+")) {
	                    System.out.println("Search text contains only digits");
	                    displayNotification(  createNotificationBox( searchPatient(Integer.valueOf(searchField.getText()))));
	                }
	                
	                // Check if the text contains only letters
	                if (searchText.matches("[a-zA-Z]+")) {
	                    System.out.println("Search text contains only letters");
	                    displayNotification( createNotificationBox(searchAppointmentsByName(searchField.getText())));
	                   
	                }
	        	}
	        });

	        searchBox.setPrefWidth(200);
	        searchBox.setPrefHeight(30);
	        searchBox.setPadding(new Insets(10));
	       
	        // Apply background color to the searchBox
	        searchBox.setStyle("-fx-background-color: lightgray;");
	        sideBox=new ScrollPane();
	        HBox gather = new HBox(5);
	        gather.getChildren().addAll(sideBox);
	        label1.setStyle("-fx-font-family: 'Consolas'; -fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #333333;");
	        VBox vbox = new VBox(20);
	        gather.setPrefWidth(600);
	        vbox.setPrefWidth(500);
	        vbox.setPrefHeight(700);

	        // Set the background color to white
	        BackgroundFill backgroundFill = new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, null);
	        Background background = new Background(backgroundFill);
	        vbox.setBackground(background);

	        // Set the border with blue stroke
	        vbox.setStyle("-fx-border-color: #0174DF; -fx-border-width: 2px; -fx-border-style: solid;");
	        HBox incrementBox=new HBox(20);
	        incrementBox.setAlignment(Pos.CENTER);
	        Button plus=new Button("+");
	        Button minus=new Button("-");
	        Label dosage=new Label("1");
	        dosage.setStyle("-fx-font-family: 'Consolas'; -fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #333333;");
	        incrementBox.getChildren().addAll(plus,dosage,minus);
	        HBox timeBox=new HBox(10);
	        Button plus1=new Button("+");
	        Button minus1=new Button("-");
	        Label duration=new Label("1");
	        duration.setStyle("-fx-font-family: 'Consolas'; -fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #333333;");
	        plus.setStyle("-fx-background-color: #ADD8E6;");
	        minus.setStyle("-fx-background-color: #ADD8E6;");

	        // Add hover effect on buttons
	        plus.setOnMouseEntered(e -> plus.setStyle("-fx-background-color: #87CEFA;"));
	        plus.setOnMouseExited(e -> plus.setStyle("-fx-background-color: #ADD8E6;"));
	        minus.setOnMouseEntered(e -> minus.setStyle("-fx-background-color: #87CEFA;"));
	        minus.setOnMouseExited(e -> minus.setStyle("-fx-background-color: #ADD8E6;"));
	        plus1.setStyle("-fx-background-color: #ADD8E6;");
	        minus1.setStyle("-fx-background-color: #ADD8E6;");
	        plus.setOnAction(e -> {
	            int value = Integer.parseInt(dosage.getText());
	            value++;
	            dosage.setText(Integer.toString(value));
	        });

	        // Event handler for minus button
	        minus.setOnAction(e -> {
	            int value = Integer.parseInt(dosage.getText());
	            if (value > 1) {
	                value--;
	                dosage.setText(Integer.toString(value));
	            }
	        });

	        // Add hover effect on buttons
	        plus1.setOnMouseEntered(e -> plus1.setStyle("-fx-background-color: #87CEFA;"));
	        plus1.setOnMouseExited(e -> plus1.setStyle("-fx-background-color: #ADD8E6;"));
	        minus1.setOnMouseEntered(e -> minus1.setStyle("-fx-background-color: #87CEFA;"));
	        minus1.setOnMouseExited(e -> minus1.setStyle("-fx-background-color: #ADD8E6;"));
	       timeBox.getChildren().addAll(plus1,duration,minus1);
	       timeBox.setAlignment(Pos.CENTER);
	       VBox vbox1=new VBox(10);
	       Label dosageLbl=new Label("Dosage : (tablets)");
	       dosageLbl.setStyle("-fx-font-family: 'Consolas'; -fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #333333;");
	       vbox1.getChildren().addAll(dosageLbl,incrementBox);
	       VBox vbox2=new VBox(10);
	       plus1.setOnAction(e -> {
	    	    int value = Integer.parseInt(duration.getText());
	    	    value++;
	    	    duration.setText(Integer.toString(value));
	    	});

	    	// Event handler for minus button
	    	minus1.setOnAction(e -> {
	    	    int value = Integer.parseInt(duration.getText());
	    	    if (value > 1) {
	    	        value--;
	    	        duration.setText(Integer.toString(value));
	    	    }
	    	});
	       Label durationLbl=new Label("Duration : (week)");
	       durationLbl.setStyle("-fx-font-family: 'Consolas'; -fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #333333;");
	       vbox2.getChildren().addAll(durationLbl,timeBox);
	       HBox hbox1=new HBox(40);
	       hbox1.getChildren().addAll(vbox1,vbox2);

	        // Create a TextArea
	       String strokeStyle = "-fx-border-color: black; -fx-border-width: 1px;";
	        
	        String roundedStyle = "-fx-border-radius: 5px; -fx-background-radius: 10px; -fx-background-color: rgba(128, 128, 128, 0.2);";
	        
	       textArea = new TextArea();
	        HBox header=new HBox(20);
	        TextField name=new TextField();
	        name.setPromptText("Medication Name");
	        name.setStyle(strokeStyle + roundedStyle);
	        header.getChildren().addAll(addPre,name);
	        vbox.getChildren().addAll(header,textArea,hbox1);
	        hbox1.setAlignment(Pos.CENTER);
	        //////////////////////////////////////////
	        
	        
	       
	        
	   
	    
	      
	        VBox freq1=new VBox(10);
	   
	        Label toBeTaken=new Label("To Be Taken");
	        toBeTaken.setStyle("-fx-font-family: 'Consolas'; -fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #333333;");
	        CheckBox afterCheckbox = new CheckBox("After Food");
	        CheckBox beforeCheckbox = new CheckBox("Before Food");

	        // Set the style for the checkboxes when checked and unchecked
	        String checkedStyle = "-fx-background-color: #87CEFA;-fx-text-fill: black; -fx-font-size: 14px;";
	        String uncheckedStyle = "-fx-background-color: #ADD8E6;-fx-text-fill: black; -fx-font-size: 14px;";
	        afterCheckbox.setStyle("-fx-text-fill: black; -fx-font-size: 14px;");
	        beforeCheckbox.setStyle("-fx-text-fill: black; -fx-font-size: 14px;");

	    
	        // Handle checkbox actions
	        afterCheckbox.setOnAction(e -> {
	            boolean checked = afterCheckbox.isSelected();
	            afterCheckbox.setStyle(checked ? checkedStyle : uncheckedStyle);
	            afterCheckbox.setDisable(checked);
	       //     
	        });
	        beforeCheckbox.setOnAction(e -> {
	            boolean checked = beforeCheckbox.isSelected();
	            beforeCheckbox.setStyle(checked ? checkedStyle : uncheckedStyle);
	          //  beforeCheckbox.setDisable(checked);
	           
	        });

	        // Create the container for the checkboxes
	        HBox checkboxContainer = new HBox(10);
	        checkboxContainer.getChildren().addAll(afterCheckbox, beforeCheckbox);
	        checkboxContainer.setPadding(new Insets(10));

	        // Create the label and add it to the container
	        Label toBeTakenLabel = new Label("To Be Taken");
	        toBeTakenLabel.setStyle("-fx-font-family: 'Consolas'; -fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #333333;");
	        checkboxContainer.getChildren().add(0, toBeTakenLabel);

	        // Create the HBox to hold the container
	        HBox whenBox = new HBox(10);
	        whenBox.getChildren().add(checkboxContainer);

	        // Create the VBox to hold the HBox
	        VBox whenContainer = new VBox(10);
	        whenContainer.getChildren().add(whenBox);
	        TextField details=new TextField();
	        details.setPromptText("Extra Details ");
	        
	        TextField phys_id=new TextField();
	        phys_id.setPromptText("physician id :");
	        phys_id.setStyle(strokeStyle + roundedStyle);
	        
	        
	        Button addBtn=new Button("prescribe");
	        addBtn.setStyle("-fx-background-color: #ADD8E6;");

	        // Add hover effect on buttons
	        addBtn.setOnMouseEntered(e ->  addBtn.setStyle("-fx-background-color: #87CEFA;"));
	        addBtn.setOnMouseExited(e ->  addBtn.setStyle("-fx-background-color: #ADD8E6;"));
	        addBtn.setOnAction(e -> {
	        	if((beforeCheckbox.isSelected()==false&&(afterCheckbox.isSelected()==false))||labelValue==null||phys_id.getText().isEmpty()||name.getText().isEmpty()||dosage.getText().isEmpty()||duration.getText().isEmpty()||duration.getText().isEmpty())
	            	
	        	{
	        		showEmptyInputWarning();
	        	}
	        	//String patientId, String physicianId, String reason, String date, String hour, String minute, String cost, String duration
	        	else if (!(validateInputs(labelValue,phys_id.getText(),name.getText(),
	            		dosage.getText(),duration.getText()))) {
	        		showInvalidInput();
	        	}
	        	else {
		           
		        addBtn.setStyle("-fx-background-color: #87CEFA;");
	               
		       // addBtn.setDisable(true);
		        Prescription newP=new Prescription(Integer.parseInt(labelValue),Integer.parseInt(phys_id.getText())
		        		,name.getText(),dosage.getText(),duration.getText()+"times daily");
		        
		        insertData(newP);
		        insertData2(newP);
		        textArea.setText("patient : "+labelValue+"  ,  "+newP.getMedication_name()+". "+" physician id: "+newP.getPhys_id()+" dosage : "+dosage.getText()+"daily "+" for "+duration.getText()+" week/s"+"\n"+
		        toBeTaken.getText()+((beforeCheckbox.isSelected() ? " before" : " after")));
		        phys_id.clear();
		        name.clear();
		        dosage.setText(null);
		        duration.setText(null);}
		        
	               
	            
	        });
	       
	        details.setStyle(strokeStyle + roundedStyle);
	        vbox.setMaxWidth(500);
	        vbox.setMaxHeight(570);
	        HBox when1=new HBox(10);
	        
	        vbox.getChildren().addAll(freq1,checkboxContainer,phys_id,details,addBtn);
	        vbox.setPadding(new Insets(10,10,10,5));
	        
	      

			 
			 
			 ///////////////////////////////////////////////
			 HBox finalBox=new HBox(10);
			 
			 finalBox.getChildren().addAll(gather,vbox);
			 vb.getChildren().addAll(label1,searchBox,finalBox);

	       
	        return vb;
	        
	    }
	   private void insertData(Prescription rc) {
	    	
	    	try {
	    			System.out.println("INSERT INTO prescription (patient_id, phys_id, medication_name, dosage, frequency)VALUES("+
	    					rc.getPatient_id()+","
	    					+rc.getPhys_id()+",'"
	    					+ rc.getMedication_name() +"',"
	    					+ rc.getDosage()+", '"
	    					+ rc.getFrequency()+"')");
	    			
	  

			ExecuteStatement("INSERT INTO prescription (patient_id, phys_id, medication_name, dosage, frequency) VALUES (" +
					    rc.getPatient_id() + ", " +
					    rc.getPhys_id() + ", '" +
					    rc.getMedication_name() + "', " +
					    rc.getDosage() + ", '" +
					    rc.getFrequency() + "')"
);
			
			
			//con.close();
			System.out.println("Connection closed" + data.size());
	    	} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	}
	/*   private void insertData2(Prescription rc) {
		    try {
		        System.out.println(rc.toString());
		        connectDB();
		        StringBuilder sb = new StringBuilder(getMedicationHistory(rc.getPatient_id()));
		        sb.append(", ").append(rc.getMedication_name());

		        ExecuteStatement("UPDATE medical_record SET medication_history = '" + sb.toString() + "' WHERE patient_id = " + rc.getPatient_id());
		        
		        System.out.println("Connection closed" + data.size());
		    } catch (SQLException e) {
		        e.printStackTrace();
		    } finally {
		        try {
		            if (con != null && !con.isClosed()) {
		                con.close();
		            }
		        } catch (SQLException e) {
		            e.printStackTrace();
		        }
		    }
		}*/


	   
		public String getName(int patient_id) {
		    String firstName = null;
		    try {
		        System.out.println("SELECT first_name FROM patient WHERE patient_id = '" + patient_id + "';");
		        Statement statement = Main.connection.createStatement();
		        ResultSet resultSet = statement.executeQuery("SELECT first_name FROM patient WHERE patient_id = '" + patient_id + "';");
		        if (resultSet.next()) {
		            firstName = resultSet.getString("first_name");
		        }
		      //  con.close();
		        System.out.println("Connection closed");
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }
		    return firstName;
		}
	   private VBox createNotificationBox(Patient x) {
		    ImageView iv = new ImageView(new Image("user.png"));
		    

		    Label pName = new Label(getName(x.getPatient_id()));
		    Label date = new Label(x.getDate_of_birth());
		    Label id=new Label(String.valueOf(x.getPatient_id()));
		   date.setStyle("-fx-font-family: 'Product Sans'; -fx-text-fill: white;-fx-font-size:20.0px; -fx-font-weight: bold;");
		  id.setStyle("-fx-font-family: 'Product Sans'; -fx-text-fill: white;-fx-font-size:20.0px; -fx-font-weight: bold;");
		  
	

		   

		    
		  Button View = new Button("View");
		  View.setPrefWidth(170); // Set the preferred width for the "View" button

		  Button prescript = new Button("prescribe");
		  prescript.setPrefWidth(170); // 

		    // Apply styles
		   VBox notificationBox = new VBox(10);
		    notificationBox.setStyle("-fx-background-color: rgba(0, 0, 0, 0.2); -fx-background-radius: 5;");

		    HBox buttonBox = new HBox(10);
		    buttonBox.setStyle("-fx-background-color: rgba(255, 0, 0, 0.2); -fx-background-radius: 5;");
		    View.setStyle("-fx-background-color: rgba(0, 0, 255, 0.2); -fx-background-radius: 5;");
		    prescript.setStyle("-fx-background-color: rgba(255, 0, 0, 0.2); -fx-background-radius: 5;");

		    // Hover effect on buttons
		    View .setOnMouseEntered(e -> View .setStyle("-fx-background-color: rgba(0, 0, 255, 0.5); -fx-background-radius: 5;"));
		    View .setOnMouseExited(e -> View .setStyle("-fx-background-color: rgba(0, 0, 255, 0.2); -fx-background-radius: 5;"));

		    prescript.setOnMouseEntered(e -> prescript.setStyle("-fx-background-color: rgba(255, 0, 0, 0.5); -fx-background-radius: 5;"));
		    prescript.setOnMouseExited(e -> prescript.setStyle("-fx-background-color: rgba(255, 0, 0, 0.2); -fx-background-radius: 5;"));
		    
		    View.setOnAction(event -> {
		        Button clickedButton = (Button) event.getSource(); // Get the clicked button (View button)
		        VBox parentVBox = (VBox) clickedButton.getParent().getParent(); // Get the grandparent VBox of the clicked button
		        Label targetLabel = getLabelFromVBox(parentVBox); // Retrieve the target label from the parent VBox
		         labelValue = targetLabel.getText(); // Retrieve the value of the label
		        //System.out.println("Label value: " + labelValue);
		        viewPatient(Integer.parseInt(labelValue));
		    });
		    

		    prescript.setOnAction(event -> {
		    	
		    	textArea.setText("patient :"+labelValue);
		    	 Button clickedButton = (Button) event.getSource(); // Get the clicked button (View button)
			        VBox parentVBox = (VBox) clickedButton.getParent().getParent(); // Get the grandparent VBox of the clicked button
			        Label targetLabel = getLabelFromVBox(parentVBox); // Retrieve the target label from the parent VBox
			        labelValue = targetLabel.getText();
			        textArea.setText("patient :"+labelValue);
		    		
		    });
		    

		    buttonBox.getChildren().addAll( View,  prescript);

		    pName.setStyle("-fx-font-family: 'Product Sans'; -fx-text-fill: white;-fx-font-size:20.0px; -fx-font-weight: bold;");
		  
		 

		    notificationBox.getChildren().addAll(iv, id,pName, date, buttonBox);

		    return notificationBox;
		}
	   private Label getLabelFromVBox(VBox vbox) {
		    for (Node node : vbox.getChildren()) {
		        if (node instanceof Label) {
		            return (Label) node; // Return the first found label within the VBox
		        }
		    }
		    return null; // Return null if no label is found
		}
		private VBox createNotificationBox(List<Patient> appointments) {
		    VBox notificationBox = new VBox(10);

		    for (Patient appointment : appointments) {
		       VBox hbox = createNotificationBox(appointment);
		        notificationBox.getChildren().add(hbox);
		    }
		    notificationBox.setPrefWidth(600); // Set the preferred width
		    notificationBox.setPrefHeight(700);
		    return notificationBox;
		}
	   public List<Patient> searchPatient(int patient_id) {
		    List<Patient> patients = new ArrayList<>();
		    try {
		     
		        Statement statement = Main.connection.createStatement();
		        ResultSet resultSet = statement.executeQuery("SELECT * FROM patient WHERE patient_id = '" + patient_id + "';");
		        System.out.println(resultSet);
		        while (resultSet.next()) {
		            Patient  Patient = new  Patient();
		            Patient.setPatient_id(resultSet.getInt("patient_id"));
		            Patient.setFirst_name(resultSet.getString("first_name"));
		            Patient.setLast_name(resultSet.getString("last_name"));
		            Patient.setAddress(resultSet.getString("address"));
		            Patient.setDate_of_birth(resultSet.getDate("dob").toString());
		            Patient.setEmail_address(resultSet.getString("email_address").toString());
		            Patient.setPhone_number(resultSet.getString("phone_number"));
		            Patient.setGender(resultSet.getString("gender").toString());
		            Patient.setWeight(resultSet.getDouble("weight"));
		            Patient.setHeight(resultSet.getDouble("height"));
		            System.out.println(Patient);
		            patients.add(Patient);
		        }
		        
		       // con.close();
		        //System.out.println("Connection closed");
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }
		 //   System.out.println(appointments);
		    return patients;
		}

	   public List<Patient> searchAppointmentsByName(String name) {
		    List<Patient> patients = new ArrayList<>();
		    try {
		        
		        Statement statement = Main.connection.createStatement();
		        ResultSet resultSet = statement.executeQuery("SELECT * FROM patient WHERE patient_id IN (SELECT patient_id FROM patient WHERE first_name LIKE '%" + name + "%' OR last_name LIKE '%" + name + "%');");
		        System.out.println(resultSet);
		        while (resultSet.next()) {
		            Patient patient = new Patient();
		            patient.setPatient_id(resultSet.getInt("patient_id"));
		            patient.setFirst_name(resultSet.getString("first_name"));
		            patient.setLast_name(resultSet.getString("last_name"));
		            patient.setAddress(resultSet.getString("address"));
		            patient.setDate_of_birth(resultSet.getDate("dob").toString());
		            patient.setEmail_address(resultSet.getString("email_address").toString());
		            patient.setPhone_number(resultSet.getString("phone_number"));
		            patient.setGender(resultSet.getString("gender").toString());
		            patient.setWeight(resultSet.getDouble("weight"));
		            patient.setHeight(resultSet.getDouble("height"));
		            System.out.println(patient);
		            patients.add(patient);
		        }
		     //   con.close();
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }
		    return patients;
		}

		public void ExecuteStatement(String SQL) throws SQLException {

			try {
				Statement stmt = Main.connection.createStatement();
				stmt.executeUpdate(SQL);
				stmt.close();
			
				 
			}
			catch(SQLException s) {
				s.printStackTrace();
				System.out.println("SQL statement is not executed!");
				  
			}
			
			
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

private void getData() throws SQLException, ClassNotFoundException {
	// TODO Auto-generated method stub
	
	String SQL;
			
	
	System.out.println("Connection established");

	SQL = " select * from patient;";
	Statement stmt = Main.connection.createStatement();
	ResultSet rs = stmt.executeQuery(SQL);


	while ( rs.next() ) 
		data.add(new Patient(
				Integer.parseInt(rs.getString(1)),
				rs.getString(2),rs.getString(3),
				rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),
				
				Double.parseDouble(rs.getString(9)),Double.parseDouble(rs.getString(10)))
				);
	
	rs.close();
	stmt.close();

	//con.close();
	System.out.println("Connection closed" + data.size());
	
	
}

public PrescriptionScene() {
	data = new ArrayList<>();
	try {
        getData();  // Fetch data from the database and populate the 'data' list
        dataList = FXCollections.observableArrayList(data);
    } catch (ClassNotFoundException | SQLException e1) {
        e1.printStackTrace();
    }
	setCenter(createViewPatientPane());
}
public void viewPatient(int patient_id) {
	
	

    Stage stage = new Stage();
    stage.setTitle("View Medical Summary ");
	stage.initModality(Modality.APPLICATION_MODAL);
	stage.initOwner(this.getScene().getWindow());
    
   
     

    GridPane gridPane = new GridPane();
    gridPane.setHgap(10);
    gridPane.setVgap(10);

    Pane cellPane1 = createCellPane("Record Id:", getRecordId(patient_id), new ImageView(new Image("recordId.png")));
    Pane cellPane2 = createCellPane("illness History : ", getHistory(patient_id), new ImageView(new Image("history.png")));
    Pane cellPane3 =createCellPane("allergies : ", getAllergies(patient_id), new ImageView(new Image("allergies.png")));
    Pane cellPane4 = createCellPane("surgeries : ", getSurgeries(patient_id), new ImageView(new Image("surgeries.png")));
    Pane cellPane5 = createCellPane("medications :", getMedicationHistory(patient_id), new ImageView(new Image("medications.png")));
    Pane cellPane6 = createCellPane("", "", new ImageView());

    // Set cell properties
    GridPane.setRowIndex(cellPane1, 0);
    GridPane.setColumnIndex(cellPane1, 0);

    GridPane.setRowIndex(cellPane2, 0);
    GridPane.setColumnIndex(cellPane2, 1);

    GridPane.setRowIndex(cellPane3, 0);
    GridPane.setColumnIndex(cellPane3, 2);

    GridPane.setRowIndex(cellPane4, 1);
    GridPane.setColumnIndex(cellPane4, 0);

    GridPane.setRowIndex(cellPane5, 1);
    GridPane.setColumnIndex(cellPane5, 1);

    GridPane.setRowIndex(cellPane6, 1);
    GridPane.setColumnIndex(cellPane6, 2);

 // Add the cell panes to the grid pane
    gridPane.getChildren().addAll(cellPane1, cellPane2, cellPane3, cellPane4, cellPane5, cellPane6);
    

    Scene scene = new Scene(gridPane,900,500);
    stage.setScene(scene);
    stage.show();
}

private VBox createCellPane(String labelText1, String labelText2, ImageView imageView) {
	
    VBox cellPane = new VBox(20);
    cellPane.setPadding(new Insets(10,10,10,10));
    cellPane.setPrefSize(300, 250); // Set the size of the cell
    cellPane.setStyle("-fx-background-color: #ECECEC;"); // Set color to the VBox

    Label label1 = new Label(labelText1);
    label1.setStyle("-fx-font-family: Consolas;"); // Set font to Consolas for label1

    Label label2 = new Label(labelText2);
    label2.setStyle("-fx-font-family: Consolas;"); // Set font to Consolas for label2

    imageView.setFitWidth(50); // Set the width of the ImageView
    imageView.setFitHeight(50); // Set the height of the ImageView

    // Add the labels and image view to the cell pane
    cellPane.getChildren().addAll( imageView,label1, label2);

    return cellPane;
}









public String getRecordId(int patient_id) {
    StringBuilder sb=new StringBuilder();
    try {
        System.out.println("SELECT medical_id FROM medical_record WHERE patient_id = '" + patient_id + "';");
  
        Statement statement = Main.connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM medical_record WHERE patient_id = '" + patient_id + "';");
        if (resultSet.next()) {
            sb.append( resultSet.getString("record_id"));
  
        }
      //  con.close();
        System.out.println("Connection closed");
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return sb.toString();
}
public String getHistory(int patient_id) {
    StringBuilder sb=new StringBuilder();
    try {
        System.out.println("SELECT illness_history FROM medical_record WHERE patient_id = '" + patient_id + "';");
  
        Statement statement = Main.connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM medical_record WHERE patient_id = '" + patient_id + "';");
        if (resultSet.next()) {
            sb.append( resultSet.getString("illness_history"));
  
        }
       // con.close();
        System.out.println("Connection closed");
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return sb.toString();
}
public String getAllergies(int patient_id) {
    StringBuilder sb=new StringBuilder();
    try {
        System.out.println("SELECT allergies FROM medical_record WHERE patient_id = '" + patient_id + "';");

        Statement statement = Main.connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT allergies FROM medical_record WHERE patient_id = '" + patient_id + "';");
        if (resultSet.next()) {
            sb.append( resultSet.getString("allergies"));
  
        }
      //  con.close();
        System.out.println("Connection closed");
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return sb.toString();
}
public String getSurgeries(int patient_id) {
    StringBuilder sb=new StringBuilder();
    try {
        System.out.println("SELECT surgeries FROM medical_record WHERE patient_id = '" + patient_id + "';");
  
        Statement statement = Main.connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT surgeries FROM medical_record WHERE patient_id = '" + patient_id + "';");
        if (resultSet.next()) {
            sb.append( resultSet.getString("surgeries"));
  
        }
       // con.close();
        System.out.println("Connection closed");
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return sb.toString();
}
public String getMedicationHistory(int patient_id) {
    StringBuilder sb=new StringBuilder();
    try {
        System.out.println("SELECT medication_history FROM medical_record WHERE patient_id = '" + patient_id + "';");
       
        Statement statement = Main.connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT medication_history FROM medical_record WHERE patient_id = '" + patient_id + "';");
        if (resultSet.next()) {
            sb.append( resultSet.getString("medication_history"));
  
        }
       // con.close();
        System.out.println("Connection closed");
    } catch (SQLException e) {
        e.printStackTrace();
    } 
    return sb.toString();
}
private void insertData2(Prescription rc) {
    try {
        System.out.println(rc.toString());
      
        StringBuilder sb = new StringBuilder(getMedicationHistory(rc.getPatient_id()));
        sb.append(", ").append(rc.getMedication_name());

        ExecuteStatement("UPDATE medical_record SET medication_history = '" + sb.toString() + "' WHERE patient_id = " + rc.getPatient_id());
        
        System.out.println("Connection closed" + data.size());
    } catch (SQLException e) {
        e.printStackTrace();
    } 
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
public boolean validateInputs(String patientId, String physicianId,String name, String dosage,  String duration) {
    try {
    	System.out.print(patientId);
        // Validate patientId and physicianId as integers
        int parsedPatientId = Integer.parseInt(patientId.trim());
        int parsedPhysicianId = Integer.parseInt(physicianId.trim());
        int parsedDosage = Integer.parseInt(dosage.trim());
        int parsedDuration = Integer.parseInt(duration.trim());

        // Validate cost as a double
       

        // Check if reason and duration contain only letters
        if (!name.matches("[a-zA-Z\\s]+")) {
            return false; // Invalid syntax, reason contains non-letter or non-space characters
        }

        // Other input validations if necessary

        return true; // All inputs are valid
    } catch (NumberFormatException e) {
    	e.printStackTrace();
        // Invalid input syntax, one or more inputs are not numbers
        return false;
    }
}




}
