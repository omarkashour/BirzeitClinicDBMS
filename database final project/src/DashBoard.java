import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.YearMonth;
import java.util.Calendar;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
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
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class DashBoard extends BorderPane {
	HBox tablesHB = new HBox();

	public DashBoard(Stage primaryStage, Scene scene) {
		
		GridPane gp = new GridPane();
		String totalPatients = "";
		try {
			totalPatients = getTotalPatients() + "";
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String averageCost = "";
		try {
			averageCost = "\u20AA" + getAverageCost();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String currentMonthProfit = "";
		try {
			currentMonthProfit = "\u20AA" + getCurrentMonthProfit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String currentYearProfit = "";
		try {
			currentYearProfit = "\u20AA" + getCurrentYearProfit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		StackPane totalPatientsCard = createCard("Total Patients", totalPatients, "heart-red.png");
		StackPane averageCostCard = createCard("Overall Average Cost", averageCost, "health-checkup.png");
		StackPane currentMonthProfitCard = createCard("Current Month Profit", currentMonthProfit, "money-month.png");
		StackPane currentYearProfitCard = createCard("Current Year Profit", currentYearProfit, "money-year.png");

		gp.add(totalPatientsCard, 0, 0);
		gp.add(averageCostCard, 1, 0);
		gp.add(currentMonthProfitCard, 2, 0);
		gp.add(currentYearProfitCard, 3, 0);
		gp.setAlignment(Pos.CENTER);
		gp.setHgap(15);
		setPadding(new Insets(20));
		LineChart<String, Number> chart;
		try {
			chart = createMaleFemaleChart();
			setBottom(chart);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		setTop(gp);
		
	}

	private LineChart<String,Number> createMaleFemaleChart() throws SQLException { // Create the x-axis (category axis)
		Connection connection = DriverManager.getConnection(Main.url, Main.username, Main.password);
		Statement statement = connection.createStatement();
		CategoryAxis xAxis = new CategoryAxis();
		NumberAxis yAxis = new NumberAxis();
		String[] months = { "January", "February", "March", "April", "May", "June", "July", "August", "September",
				"October", "November", "December" };
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(System.currentTimeMillis());
		int year = c.get(c.YEAR);

		// Create the y-axis (number axis)

		// Create the line chart
		LineChart<String, Number> chart = new LineChart<>(xAxis, yAxis);

		// Set chart title and axis labels
		chart.setTitle("Patient Visits by Gender");
		xAxis.setLabel("Month");
		yAxis.setLabel("Number of Visits");
		yAxis.setTickUnit(1);
		
        chart.setStyle("-fx-font-size: 20px;"); 
        yAxis.setStyle("-fx-font-size: 20px;"); 
        xAxis.setStyle("-fx-font-size: 20px;"); 
        
		// Create data series for female visits
		XYChart.Series<String, Number> femaleSeries = new XYChart.Series<>();
		femaleSeries.setName("Female");
		

		XYChart.Series<String, Number> maleSeries = new XYChart.Series<>();
		maleSeries.setName("Male");
		
		XYChart.Series<String, Number> totalSeries = new XYChart.Series<>();
		totalSeries.setName("Total Visits");

		// Populate the female data series with sample data (replace with your own data)
		int femaleCount = 0;
		int maleCount = 0;
		for (int i = 0; i < 12; i++) {
			int month = i + 1;
			String sqlMonth = "";
			if(month%10 == 0) {
				sqlMonth = "0" + month;
			}else {
				sqlMonth = month +"";
			}
			YearMonth yearMonthObject = YearMonth.of(year, month);
			int daysInMonth = yearMonthObject.lengthOfMonth();
			String sqlDaysInMonth = "";
			if(daysInMonth%10 == 0) {
				sqlDaysInMonth = "0" + daysInMonth;
			}else {
				sqlDaysInMonth = daysInMonth +"";
			}
			ResultSet resultSet = statement.executeQuery("Select count(*) from appointment a , patient p where p.gender = 'F' and p.patient_id = a.patient_id and a.ap_date >= " + "'" + year +"-" + month + "-01'" + "and a.ap_date <= " + "'" + year + "-" + sqlMonth + "-" + sqlDaysInMonth + "';");
			if (resultSet.next()) {
				femaleCount = resultSet.getInt(1);
			}
			femaleSeries.getData().add(new XYChart.Data<>(months[i], femaleCount));

			ResultSet resultSet2 = statement.executeQuery("Select count(*) from appointment a , patient p where p.gender = 'M' and p.patient_id = a.patient_id and a.ap_date >= '" + year +"-" + month + "-01'" + " and a.ap_date <= '" + year + "-" + sqlMonth + "-" + sqlDaysInMonth + "';");
			if (resultSet2.next()) {
				maleCount = resultSet2.getInt(1);
			}
			maleSeries.getData().add(new XYChart.Data<>(months[i], maleCount));
			totalSeries.getData().add(new XYChart.Data<>(months[i], maleCount + femaleCount));
		}

		// Add the data series to the chart
		chart.getData().add(femaleSeries);
		chart.getData().add(maleSeries);
		chart.getData().add(totalSeries);
        chart.lookup(".chart-title").setStyle("-fx-font-size: 18px; "); // Set title color
        xAxis.lookup(".axis-label").setStyle("-fx-font-size: 18px;"); // Set X-axis title font size
        yAxis.lookup(".axis-label").setStyle("-fx-font-size: 18px;"); // Set X-axis title font size
        
        chart.setLegendSide(Side.BOTTOM);
		return chart;
	}

	private double getCurrentYearProfit() throws SQLException {
		Connection connection = DriverManager.getConnection(Main.url, Main.username, Main.password);
		Statement statement = connection.createStatement();
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(System.currentTimeMillis());
		int year = c.get(c.YEAR);
		ResultSet resultSet = statement.executeQuery("Select SUM(cost) from appointment where ap_date >= '" + year
				+ "-1-1' and ap_date <= " + "'" + year + "-12-31'");
		double res = 0;
		if (resultSet.next()) {
			res = resultSet.getDouble(1);
		}
		return res;
	}

	private double getCurrentMonthProfit() throws SQLException {
		Connection connection = DriverManager.getConnection(Main.url, Main.username, Main.password);
		Statement statement = connection.createStatement();
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(System.currentTimeMillis());
		int year = c.get(c.YEAR);
		int month = c.get(c.MONTH) + 1;
		YearMonth yearMonthObject = YearMonth.of(year, month);
		int daysInMonth = yearMonthObject.lengthOfMonth();
		ResultSet resultSet = statement.executeQuery("Select SUM(cost) from appointment where ap_date >= '" + year + "-"
				+ month + "-1'" + "and ap_date <= " + "'" + year + "-" + month + "-" + daysInMonth + "'");
		double res = 0;
		if (resultSet.next()) {
			res = resultSet.getDouble(1);
		}
		return res;
	}

	private double getAverageCost() throws SQLException {
		Connection connection = DriverManager.getConnection(Main.url, Main.username, Main.password);
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery("Select AVG(cost) from appointment;");
		double res = 0;
		if (resultSet.next()) {
			res = resultSet.getDouble(1);
		}
		return res;
	}

	private long getTotalPatients() throws SQLException {
		Connection connection = DriverManager.getConnection(Main.url, Main.username, Main.password);
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery("Select count(*) from patient;");
		long res = 0;
		if (resultSet.next()) {
			res = resultSet.getLong(1);
		}
		return res;
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
	   HBox iconAndPriceHB = new HBox(imageView,label2);
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

}
