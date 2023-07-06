

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.time.LocalDate;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;

public class CalendarApp extends BorderPane {

    private TableView<ObservableList<SimpleStringProperty>> tableView;
    private LocalDate dateFocus; 

    

  public CalendarApp(){
     

	  dateFocus = LocalDate.now(); // Set initial focused date to the current date

      tableView = new TableView<>();
      tableView.setEditable(true);

      populateTableView();

      Button previousButton = new Button("Previous");
      previousButton.setOnAction(event -> {
          dateFocus = dateFocus.minusMonths(1); // Move to the previous month
          populateTableView();
      });

      Button nextButton = new Button("Next");
      nextButton.setOnAction(event -> {
          dateFocus = dateFocus.plusMonths(1); // Move to the next month
          populateTableView();
      });

      HBox buttonBox = new HBox(10);
      buttonBox.getChildren().addAll(previousButton, nextButton);
buttonBox.setAlignment(Pos.CENTER);
      GridPane gridPane = new GridPane();
      gridPane.add(tableView, 0, 1);
      gridPane.add(buttonBox, 0, 0);
      GridPane.setMargin(tableView, new Insets(10));

       
        setTop(gridPane);
       

       
    }

  private void populateTableView() {
	    ObservableList<ObservableList<SimpleStringProperty>> data = generateCalendarData();

	    tableView.getColumns().clear(); // Clear existing columns

	    String[] dayNames = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};

	    for (int columnIndex = 0; columnIndex < 7; columnIndex++) {
	        TableColumn<ObservableList<SimpleStringProperty>, String> column = new TableColumn<>(dayNames[columnIndex]);
	        final int columnIndexFinal = columnIndex;
	        column.setCellValueFactory(cellData -> cellData.getValue().get(columnIndexFinal));

	        tableView.getColumns().add(column);
	    }
	    tableView.getSelectionModel().setCellSelectionEnabled(true);
	    tableView.setPrefHeight(170);
	    tableView.setItems(data);
	}

  private ObservableList<ObservableList<SimpleStringProperty>> generateCalendarData() {
      ObservableList<ObservableList<SimpleStringProperty>> data = FXCollections.observableArrayList();

      LocalDate startDate = dateFocus.withDayOfMonth(1);
      int monthMaxDate = startDate.getMonth().length(startDate.isLeapYear());
      int startDayOfWeek = startDate.getDayOfWeek().getValue(); // Day of the week for the first day of the month

      int rowIndex = 0;
      int dayOfMonth = 1;

      while (dayOfMonth <= monthMaxDate) {
          ObservableList<SimpleStringProperty> weekData = FXCollections.observableArrayList();

          // Add cells for the days in the month
          for (int j = 1; j <= 7; j++) {
              if (rowIndex == 0 && j < startDayOfWeek) {
                  weekData.add(new SimpleStringProperty(""));
              } else if (dayOfMonth <= monthMaxDate) {
                  SimpleStringProperty day = new SimpleStringProperty(startDate.withDayOfMonth(dayOfMonth).toString());
                  weekData.add(day);
                  dayOfMonth++;
              } else {
                  weekData.add(new SimpleStringProperty(""));
              }
          }

          data.add(weekData);
          rowIndex++;
      }

      return data;
  }
  LocalDate getSelectedDate() {
      ObservableList<TablePosition> selectedCells = tableView.getSelectionModel().getSelectedCells();
      if (!selectedCells.isEmpty()) {
          TablePosition selectedCell = selectedCells.get(0);
          int rowIndex = selectedCell.getRow();
          int columnIndex = selectedCell.getColumn();
          ObservableList<SimpleStringProperty> row = tableView.getItems().get(rowIndex);
          SimpleStringProperty cellValue = row.get(columnIndex);
          return LocalDate.parse(cellValue.get());
      }
      return null;
  }

    private void handlePrintButtonAction(ActionEvent event) {
        ObservableList<TablePosition> selectedCells = tableView.getSelectionModel().getSelectedCells();
        if (!selectedCells.isEmpty()) {
            TablePosition selectedCell = selectedCells.get(0);
            int rowIndex = selectedCell.getRow();
            int columnIndex = selectedCell.getColumn();
            ObservableList<SimpleStringProperty> row = tableView.getItems().get(rowIndex);
            SimpleStringProperty cellValue = row.get(columnIndex);
            System.out.println("Selected Cell: " + cellValue.get());
        }
    }
}
