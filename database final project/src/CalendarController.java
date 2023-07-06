
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.net.URL;
import java.time.ZonedDateTime;
import java.util.*;

public class CalendarController implements Initializable {
	private ArrayList<Appointment> data;
    private ObservableList<Appointment> dataList;
	private String dbURL;
	private String dbUsername = "root";
	private String dbPassword = Main.password;
	private String URL = "localhost";
	private String port = "3306";
	private String dbName = "bzuClinic";
	private Connection con;

    ZonedDateTime dateFocus;
    static List<CalendarActivity> calendarActivities;
    ZonedDateTime today;
    @FXML
    private TextField textFieldInteger;

    @FXML
    private TextField textFieldString;

    @FXML
    private Button addEventButton;

    @FXML
    private Text year;

    @FXML
    private Text month;

    @FXML
    private FlowPane calendar;
    String hour;
    String minute;
    

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dateFocus = ZonedDateTime.now();
        data = new ArrayList<>();
        calendarActivities = new ArrayList<>();
        try {
	        getData();  // Fetch data from the database and populate the 'data' list
	        dataList = FXCollections.observableArrayList(data);
	    } catch (ClassNotFoundException | SQLException e1) {
	        e1.printStackTrace();
	    }
	    
        today = ZonedDateTime.now();
                drawCalendar();
    }

    @FXML
    void backOneMonth(ActionEvent event) {
        dateFocus = dateFocus.minusMonths(1);
        redrawCalendar();
    }

    @FXML
    void forwardOneMonth(ActionEvent event) {
        dateFocus = dateFocus.plusMonths(1);
        redrawCalendar();
    }

   
    public void drawCalendar(){
        year.setText(String.valueOf(dateFocus.getYear()));
        month.setText(String.valueOf(dateFocus.getMonth()));

        double calendarWidth = calendar.getPrefWidth();
        double calendarHeight = calendar.getPrefHeight();
        double strokeWidth = 1;
        double spacingH = calendar.getHgap();
        double spacingV = calendar.getVgap();

        //List of activities for a given month
        Map<Integer, List<CalendarActivity>> calendarActivityMap = getCalendarActivitiesMonth(dateFocus);

        int monthMaxDate = dateFocus.getMonth().maxLength();
        //Check for leap year
        if(dateFocus.getYear() % 4 != 0 && monthMaxDate == 29){
            monthMaxDate = 28;
        }
        int dateOffset = ZonedDateTime.of(dateFocus.getYear(), dateFocus.getMonthValue(), 1,0,0,0,0,dateFocus.getZone()).getDayOfWeek().getValue();

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                StackPane stackPane = new StackPane();

                Rectangle rectangle = new Rectangle();
                rectangle.setFill(Color.TRANSPARENT);
                rectangle.setStroke(Color.BLACK);
                rectangle.setStrokeWidth(strokeWidth);
                double rectangleWidth =(calendarWidth/7) - strokeWidth - spacingH;
                rectangle.setWidth(rectangleWidth);
                double rectangleHeight = (calendarHeight/6) - strokeWidth - spacingV;
                rectangle.setHeight(rectangleHeight);
                stackPane.getChildren().add(rectangle);

                int calculatedDate = (j+1)+(7*i);
                if(calculatedDate > dateOffset){
                    int currentDate = calculatedDate - dateOffset;
                    if(currentDate <= monthMaxDate){
                        Text date = new Text(String.valueOf(currentDate));
                        double textTranslationY = - (rectangleHeight / 2) * 0.75;
                        date.setTranslateY(textTranslationY);
                        stackPane.getChildren().add(date);

                        List<CalendarActivity> calendarActivities = calendarActivityMap.get(currentDate);
                        if(calendarActivities != null){
                            createCalendarActivity(calendarActivities, rectangleHeight, rectangleWidth, stackPane);
                        }
                    }
                    if(today.getYear() == dateFocus.getYear() && today.getMonth() == dateFocus.getMonth() && today.getDayOfMonth() == currentDate){
                        rectangle.setStroke(Color.BLUE);
                    }
                }
                calendar.getChildren().add(stackPane);
            }
        }
    }
       
    
    private List<CalendarActivity> getActivitiesOnDate(ZonedDateTime date) {
        List<CalendarActivity> activities = new ArrayList<>();
        for (CalendarActivity activity : calendarActivities) {
            if (activity.getDate().toLocalDate().isEqual(date.toLocalDate())) {
                activities.add(activity);
            }
        }
        
        return activities;
    }
    private void createCalendarActivity(List<CalendarActivity> calendarActivities, double rectangleHeight, double rectangleWidth, StackPane stackPane) {
        VBox calendarActivityBox = new VBox();
        for (int k = 0; k < calendarActivities.size(); k++) {
            if(k >= 2) {
                Text moreActivities = new Text("...");
                calendarActivityBox.getChildren().add(moreActivities);
                moreActivities.setOnMouseClicked(mouseEvent -> {
                    //On ... click print all activities for given date
                  //  System.out.println(calendarActivities);
                });
                break;
            }
            Text text = new Text(calendarActivities.get(k).getClientName() + ",\n " + calendarActivities.get(k).getDate().toLocalTime());
            calendarActivityBox.getChildren().add(text);
            text.setOnMouseClicked(mouseEvent -> {
                //On Text clicked
                System.out.println(text.getText());
            });
        }
        calendarActivityBox.setTranslateY((rectangleHeight / 2) * 0.20);
        calendarActivityBox.setMaxWidth(rectangleWidth * 0.8);
        calendarActivityBox.setMaxHeight(rectangleHeight * 0.65);
        calendarActivityBox.setStyle("-fx-background-color:GRAY");
        stackPane.getChildren().add(calendarActivityBox);
    }

    private Map<Integer, List<CalendarActivity>> createCalendarMap(List<CalendarActivity> calendarActivities) {
        Map<Integer, List<CalendarActivity>> calendarActivityMap = new HashMap<>();

        for (CalendarActivity activity : calendarActivities) {
            int activityDate = activity.getDate().getDayOfMonth();
            if (!calendarActivityMap.containsKey(activityDate)) {
                calendarActivityMap.put(activityDate, new ArrayList<>(Collections.singletonList(activity)));
            } else {
                List<CalendarActivity> oldListByDate = calendarActivityMap.get(activityDate);
                List<CalendarActivity> newList = new ArrayList<>(oldListByDate);
                newList.add(activity);
                calendarActivityMap.put(activityDate, newList);
            }
        }
        return calendarActivityMap;
    }
    private Map<Integer, List<CalendarActivity>> getCalendarActivitiesMonth(ZonedDateTime dateFocus) {
        // No need to generate random events, use the existing calendarActivities list
        List<CalendarActivity> calendarActivities = CalendarController.calendarActivities;

        ZonedDateTime startDate = dateFocus.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        ZonedDateTime endDate = dateFocus.withDayOfMonth(dateFocus.getMonth().maxLength()).withHour(23).withMinute(59).withSecond(59);

        Map<Integer, List<CalendarActivity>> calendarActivityMap = new HashMap<>();

        for (CalendarActivity activity : calendarActivities) {
            ZonedDateTime activityDate = activity.getDate();
            if (!activityDate.isBefore(startDate) && !activityDate.isAfter(endDate)) {
                int dayOfMonth = activityDate.getDayOfMonth();
                calendarActivityMap.computeIfAbsent(dayOfMonth, key -> new ArrayList<>()).add(activity);
            }
        }

        // Remove duplicates from the lists
        for (List<CalendarActivity> activities : calendarActivityMap.values()) {
            Set<CalendarActivity> uniqueActivities = new LinkedHashSet<>(activities);
            activities.clear();
            activities.addAll(uniqueActivities);
        }

        return calendarActivityMap;
    }



       




    

    private void redrawCalendar() {
        calendar.getChildren().clear();
        drawCalendar();
    }

  
    


    public class CalendarActivity {
        private ZonedDateTime date;
        private String clientName;
        private Integer serviceNo;

        public CalendarActivity(ZonedDateTime date, String clientName) {
            this.date = date;
            this.clientName = clientName;
            this.serviceNo = serviceNo;
        }

        public ZonedDateTime getDate() {
            return date;
        }

        public void setDate(ZonedDateTime date) {
            this.date = date;
        }

        public String getClientName() {
            return clientName;
        }

        public void setClientName(String clientName) {
            this.clientName = clientName;
        }

        public Integer getServiceNo() {
            return serviceNo;
        }

        public void setServiceNo(Integer serviceNo) {
            this.serviceNo = serviceNo;
        }

        @Override
        public String toString() {
            return "CalendarActivity{" +
                    "date=" + date +
                    ", clientName='" + clientName + '\'' +
                    ", serviceNo=" + serviceNo +
                    '}';
        }
    }
    private void getData() throws SQLException, ClassNotFoundException {
        String SQL;
        connectDB();
        System.out.println("Connection established");

        SQL = "SELECT * FROM appointment;";
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(SQL);

        while (rs.next()) {
            int appointmentId = rs.getInt("ap_id");
            int patientId = rs.getInt("patient_id");
            int physicianId = rs.getInt("phys_id");
            String reason = rs.getString("ap_reason");
            LocalDate date = rs.getDate("ap_date").toLocalDate();
            LocalTime time = rs.getTime("ap_time").toLocalTime();
            String status = rs.getString("status");
            double cost = rs.getDouble("cost");
            LocalTime duration = rs.getTime("duration").toLocalTime();

            ZonedDateTime appointmentDateTime = ZonedDateTime.of(date, time, ZoneId.systemDefault());
            String clientName ="patient:"+patientId; // Implement getClientName() to retrieve the client name based on the patientId

            CalendarActivity calendarActivity = new CalendarActivity(appointmentDateTime, clientName);
            calendarActivity.setServiceNo(appointmentId);
            calendarActivities.add(calendarActivity);
            //System.out.println(calendarActivity);
            
        }

        rs.close();
        stmt.close();
        con.close();
        System.out.println("Connection closed: " + calendarActivities.size());
    }
private void connectDB() throws ClassNotFoundException, SQLException {
		
		dbURL = "jdbc:mysql://" + URL + ":" + port + "/" + dbName + "?verifyServerCertificate=false";
		Properties p = new Properties();
		p.setProperty("user", dbUsername);
		p.setProperty("password", dbPassword);
		p.setProperty("useSSL", "false");
		p.setProperty("autoReconnect", "true");
		Class.forName("com.mysql.cj.jdbc.Driver");
	
		con = DriverManager.getConnection (dbURL, p);

	}

    
}
