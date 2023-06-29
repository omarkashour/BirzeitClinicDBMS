

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Main extends Application {

	
	public static String username = "root";
	public static String password = "50342ADMIN54ZZZ8DATABASESSA5OLJ~$3-=LHJ;MYSQL;;-=-=SSH32`4===TABLE-SIZE:m=2R456BNMKLJSGFJ40a2444444423-..+8r---_+TUICNM911!2243586234-=XZ";
	public static String ip = "localhost";
	public static String port = "3306";
	public static String dbName = "bzuClinic";
	public static String url = "";
	private static void decodePass() {
		
		StringBuilder sb = new StringBuilder("");
		for (int i = 0; i < password.length(); i++) {
			char c = password.charAt(i);
			if ((c < 'a' || c > 'z') && (c != '_' && c != '9' && c != '1' && c != '!' && c != 'O'))
				continue;
			sb.append(c);
		}
		password = sb.toString();
	}
	
    @Override
    public void start(Stage primaryStage) {
    	decodePass();
    	url = "jdbc:mysql://" + ip + ":" + port + "/" + dbName;
  
        // Create sidebar
        VBox sidebar = new VBox();
        sidebar.setPadding(new Insets(10));
        sidebar.setStyle("-fx-background-color: #FF8989;");
        sidebar.setAlignment(Pos.CENTER); // Center the buttons vertically
        sidebar.setSpacing(10);
        Image patientIcon = new Image("patient-white.png");
        ImageView patientIconView = new ImageView(patientIcon);
        patientIconView.setFitWidth(24); 
        patientIconView.setFitHeight(24); 
        
        Image dashboardIcon = new Image("heart-white.png");
        ImageView dashboardIconView = new ImageView(dashboardIcon);
        dashboardIconView.setFitWidth(24); 
        dashboardIconView.setFitHeight(24); 
        
        Image appointmentIcon = new Image("appointment-white.png");
        ImageView appointmentIconView = new ImageView(appointmentIcon);
        appointmentIconView.setFitWidth(24); 
        appointmentIconView.setFitHeight(24); 
        
        Image clinicIcon = new Image("clinic-white.png");
        ImageView clinicIconView = new ImageView(clinicIcon);
        clinicIconView.setFitHeight(41);
        clinicIconView.setFitWidth(41);
        
        Image prescriptionIcon = new Image("pill-white.png");
        ImageView prescriptionIconView = new ImageView(prescriptionIcon);
        prescriptionIconView.setFitHeight(24);
        prescriptionIconView.setFitWidth(24);
        
        Image physicianIcon = new Image("physician-white.png");
        ImageView physicianIconView = new ImageView(physicianIcon);
        physicianIconView.setFitHeight(24);
        physicianIconView.setFitWidth(24);
        
        Button dashBoardBtn = new Button("Dashboard");
        Button patientsBtn = new Button("Patients");
        Button appointmentsBtn = new Button("Appointments");
        Button prescriptionsBtn = new Button("Prescriptions");
        Button physiciansBtn = new Button("Physicians");

        dashBoardBtn.getStyleClass().add("navigation-button");
        patientsBtn.getStyleClass().add("navigation-button");
        appointmentsBtn.getStyleClass().add("navigation-button");
        prescriptionsBtn.getStyleClass().add("navigation-button");
        physiciansBtn.getStyleClass().add("navigation-button");
        
        dashBoardBtn.setGraphic(dashboardIconView);
        patientsBtn.setGraphic(patientIconView);
        appointmentsBtn.setGraphic(appointmentIconView);
        prescriptionsBtn.setGraphic(prescriptionIconView);
        physiciansBtn.setGraphic(physicianIconView);
        
        sidebar.getChildren().addAll(dashBoardBtn, patientsBtn, appointmentsBtn,prescriptionsBtn,physiciansBtn);

        
        Button logoBtn = new Button("Birzeit Clinic");
        logoBtn.setGraphic(clinicIconView);
        logoBtn.setMaxWidth(170);
        logoBtn.setStyle("-fx-background-color: #FF8989; -fx-text-fill: white; -fx-font-size: 17px; -fx-background-radius: 0px; -fx-font-weight: bold;");
        
        BorderPane root = new BorderPane();
        root.setLeft(sidebar);
        root.setStyle("-fx-background-color: #FCAEAE;");

        root.setTop(logoBtn);

        Scene scene = new Scene(root,1300,750);

        DashBoard dashboard = new DashBoard(primaryStage,scene);
  
        dashBoardBtn.setOnAction(e -> {
        	root.setCenter(null);
        	root.setCenter(dashboard);
        });

        PatientsTab patients = new PatientsTab();
        patientsBtn.setOnAction(e -> {
        	root.setCenter(null);
        	root.setCenter(patients);
        });

        appointmentsBtn.setOnAction(e -> {
        	root.setCenter(null);

        });
        
        prescriptionsBtn.setOnAction(e->{
        	root.setCenter(null);

        });
        physiciansBtn.setOnAction(e->{
        	root.setCenter(null);

        });

//		Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());
        scene.getStylesheets().add("style.css");
//        primaryStage.setFullScreen(true);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Birzeit University Clinic DBMS");
        primaryStage.show();
        dashBoardBtn.fire();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
