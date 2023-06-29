import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.YearMonth;
import java.util.Calendar;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
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
			averageCost = "$" + getAverageCost();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String currentMonthProfit = "";
		try {
			currentMonthProfit = "$" + getCurrentMonthProfit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String currentYearProfit = "";
		try {
			currentYearProfit = "$" + getCurrentYearProfit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		StackPane totalPatientsCard = createCard("Total Patients", totalPatients, "heart-red.png");
		StackPane averageCostCard = createCard("Average Cost", averageCost, "health-checkup.png");
		StackPane currentMonthProfitCard = createCard("Month Profit", currentMonthProfit, "money-month.png");
		StackPane currentYearProfitCard = createCard("Year Profit", currentYearProfit, "money-year.png");

		gp.add(totalPatientsCard, 0, 0);
		gp.add(averageCostCard, 1, 0);
		gp.add(currentMonthProfitCard, 2, 0);
		gp.add(currentYearProfitCard, 3, 0);

		gp.setHgap(15);
		setPadding(new Insets(20));
		setTop(gp);
	}

	private double getCurrentYearProfit() throws SQLException {
		Connection connection = DriverManager.getConnection(Main.url, Main.username, Main.password);
		Statement statement = connection.createStatement();
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(System.currentTimeMillis());
		int year = c.get(c.YEAR);
		ResultSet resultSet = statement.executeQuery("Select SUM(cost) from appointment where ap_date >= '" + year + "-1-1' and ap_date <= " + "'" + year + "-12-31'" );
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
		card.setPadding(new Insets(10));
		card.setAlignment(Pos.CENTER);
		card.setPrefSize(200, 150);
		card.setStyle(" -fx-border-color: #FF6666; -fx-border-width: 2px; -fx-border-radius: 15px;");

		VBox contentBox = new VBox(30);
		contentBox.setAlignment(Pos.CENTER_LEFT);

		Label label1 = new Label(label1Text);
		label1.setFont(new Font("Roboto", 15));
		label1.setTextFill(Color.web("#000000"));

		Label label2 = new Label(label2Text);
		label2.setFont(new Font("Roboto", 22));
		label2.setTextFill(Color.web("#000000"));
		label2.setStyle("-fx-font-weight: bold;");

		ImageView imageView = new ImageView(new Image(imagePath));
		imageView.setFitWidth(60);
		imageView.setPreserveRatio(true);

		contentBox.getChildren().addAll(imageView, label1);

		card.getChildren().addAll(contentBox, label2);

		Rectangle rec = new Rectangle();
		rec.setFill(Color.WHITE);
		rec.setWidth(200);
		rec.setHeight(200);
		rec.setArcWidth(32);
		rec.setArcHeight(32);

		StackPane overall = new StackPane();
		overall.getChildren().addAll(rec, card);

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
