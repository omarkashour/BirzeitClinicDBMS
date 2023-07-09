import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Label;
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

public class ReportTab extends BorderPane {
  private static StackPane physicians;
private static StackPane AcceptedAppointments;
private static StackPane PatientsIndept;
private static StackPane PatientsWhoPaid;
private static StackPane RejectedAppoinments;
private static StackPane NumOfPrescriptions;
  private BarChart<String, Number> bchart;
  static GridPane gpane = new GridPane();

  public ReportTab() {
    bchart = getChart();
    bchart.lookup(".chart-title").setStyle("-fx-font-size: 22px;    -fx-text-fill: white; "); // Set title color
    bchart.lookup(".axis-label").setStyle("-fx-font-size: 22px;    -fx-text-fill: white;"); // Set X-axis title font size
	
    refreshCards();
   
    gpane.setAlignment(Pos.CENTER);
    gpane.setHgap(20);
    gpane.setVgap(20);
    gpane.setPadding(new Insets(20));
    VBox vb = new VBox(gpane,bchart);
    vb.setSpacing(10);
    setCenter(vb);

  }

public static void refreshCards() {
	gpane.getChildren().clear();
	physicians = createCard("Total physicians", getPhysiciansNum(), "doctors.png");
    AcceptedAppointments = createCard("Confirmed appointments", getAcceptedApointments(), "appointment.png");
    PatientsIndept = createCard("Patients with debt", getPatientsDebt(), "warning.png");
    PatientsWhoPaid = createCard("Patients with no debt", getPatientsNoDebt(), "money2.png");
    RejectedAppoinments = createCard("Canceled Appointments", getCanceledApp(), "canceled-appointment.png");
    NumOfPrescriptions = createCard("Total prescriptions given", getPrescriptions(), "prescription.png");
    
    gpane.addRow(0, physicians, PatientsIndept, PatientsWhoPaid);
    gpane.addRow(1, AcceptedAppointments, RejectedAppoinments,
        NumOfPrescriptions);
}

  private static String getPrescriptions() {
    int count = 0;
    String query = "SELECT COUNT(*) AS count FROM prescription;";

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

  private static String getCanceledApp() {
    int count = 0;
    String query = "SELECT COUNT(*) AS count FROM appointment where(status=\"Canceled\");";

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

  private String getConfirmedApp() {
    return null;
  }

  private static String getPatientsDebt() {
    int count = 0;
    String query = "SELECT COUNT(*) AS count FROM patient p, billing_Record b where p.patient_id = b.patient_id and b.amount_left<>0;";

    try (Connection connection = DriverManager.getConnection(Main.url, Main.username, Main.password)) {
      try (Statement statement = connection.createStatement();) {
        ResultSet rs = statement.executeQuery(query);

        if (rs.next()) {
          count = rs.getInt(1);
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }
    return count + "";
  }

  private static String getPatientsNoDebt() {
    int count = 0;
    String query = "SELECT COUNT(*) AS count FROM patient p, billing_Record b where p.patient_id = b.patient_id and b.amount_left=0;";

    try (Connection connection = DriverManager.getConnection(Main.url, Main.username, Main.password)) {
      try (Statement statement = connection.createStatement();) {
        ResultSet rs = statement.executeQuery(query);

        if (rs.next()) {
          count = rs.getInt(1);
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }
    return count + "";
  }

  private static String getAcceptedApointments() {
    int count = 0;
    String query = "SELECT COUNT(*) AS count FROM appointment where(status=\"Confirmed\");";

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

  private static String getPhysiciansNum() {
    int count = 0;
    String query = "SELECT COUNT(*) AS count FROM physician";

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

  private BarChart<String, Number> getChart() {
    CategoryAxis xAxis = new CategoryAxis();
    xAxis.setLabel("Age Group");

    // Create a number axis for the number of patients
    NumberAxis yAxis = new NumberAxis();
    yAxis.setLabel("Number of Patients");
    
    xAxis.lookup(".axis-label").setStyle("-fx-font-size: 22px;    -fx-text-fill: white;"); // Set X-axis title font size
    yAxis.lookup(".axis-label").setStyle("-fx-font-size: 22px;    -fx-text-fill: white;"); // Set X-axis title font size

    // Create a bar chart
    BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
    barChart.setTitle("Age Distribution of Patients");

    ObservableList<XYChart.Data<String, Number>> data = getList();

    // Create a data series for age distribution
    XYChart.Series<String, Number> series = new XYChart.Series<>();
    series.setName("Patients");
    series.setData(data);

    // Add the data series to the chart
    ObservableList<XYChart.Series<String, Number>> chartData = FXCollections.observableArrayList();
    chartData.add(series);
    barChart.setData(chartData);
    return barChart;
  }

  private ObservableList<XYChart.Data<String, Number>> getList() {
    ObservableList<XYChart.Data<String, Number>> data = FXCollections.observableArrayList();
    String query = "SELECT FLOOR(DATEDIFF(CURDATE(), dob) / 365) AS age, COUNT(*) AS patient_count FROM patient GROUP BY age;";
    try (Connection connection = DriverManager.getConnection(Main.url, Main.username, Main.password)) {
      try (Statement statement = connection.createStatement();) {
        // Execute the query and retrieve data
        ResultSet rs = statement.executeQuery(query);
        while (rs.next()) {
          String ageGroup = rs.getString("age");
          int patientCount = rs.getInt("patient_count");

          // Add the data to the list
          data.add(new XYChart.Data<>(ageGroup, patientCount));
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }

    } catch (SQLException d) {
    }
    return data;
  }

  public static StackPane createCard(String label1Text, String label2Text, String imagePath) {

    HBox card = new HBox(20);
    card.setPadding(new Insets(15)); // Increase padding for a larger card
    card.setAlignment(Pos.CENTER);
    card.setPrefSize(250, 200); // Increase width and height for a larger card
    card.setStyle("-fx-border-color: #FF6666; -fx-border-width: 2px; -fx-border-radius: 15px;");

    VBox contentBox = new VBox(30);
    contentBox.setAlignment(Pos.CENTER_LEFT);

    Label label1 = new Label(label1Text);
    label1.setStyle("-fx-font-size: 16px; -fx-text-fill: #000000;");
    label1.setWrapText(true);
    Label label2 = new Label(label2Text);
    label2.setStyle("-fx-font-size: 20px; -fx-text-fill: #000000; -fx-font-weight: bold;");

    ImageView imageView = new ImageView(new Image(imagePath));
    imageView.setFitWidth(50); // Increase width for a larger image
    imageView.setPreserveRatio(true);
    imageView.setFitHeight(50);

    HBox iconAndPriceHB = new HBox(imageView, label2);
    iconAndPriceHB.setAlignment(Pos.CENTER);
    iconAndPriceHB.setSpacing(20);
    contentBox.getChildren().addAll(iconAndPriceHB, label1);
    contentBox.setAlignment(Pos.CENTER);
    // card.getChildren().addAll(contentBox, label2);

    Rectangle rec = new Rectangle();
    rec.setFill(Color.WHITE);
    rec.setWidth(200); // Increase width for a larger card
    rec.setHeight(150); // Increase height for a larger card
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

}
