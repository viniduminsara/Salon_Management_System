package lk.ijse.gdse.controller;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;

import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import lk.ijse.gdse.dto.tm.UpcomingAppointmentTM;
import lk.ijse.gdse.model.AppointmentModel;
import lk.ijse.gdse.model.CustomerModel;
import lk.ijse.gdse.model.EmployeeModel;
import lk.ijse.gdse.model.PaymentModel;

public class HomeFormController implements Initializable {
    @FXML
    private Label lblTime;

    @FXML
    private Label lblDate;

    @FXML
    private TableView<UpcomingAppointmentTM> tblUpcoming;

    @FXML
    private TableColumn<?, ?> colDate;

    @FXML
    private TableColumn<?, ?> colTime;

    @FXML
    private TableColumn<?, ?> colEmployee;

    @FXML
    private BarChart<String, Double> barChart;

    @FXML
    private Label lblCustomer;

    @FXML
    private Label lblAppointment;

    @FXML
    private Label lblEmployee;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadDateandTime();
        setValueFactory();
        populateUpcomingTable();
        loadBarChart();
        loadDetails();
    }

    private void loadDetails() {
        try {
            int totalCustomer = CustomerModel.findTotalCustomers();
            int totalAppointments = AppointmentModel.findTotalAppointments();
            int totalEmployees = EmployeeModel.findTotalEmployees();
            lblCustomer.setText(String.valueOf(totalCustomer));
            lblAppointment.setText(String.valueOf(totalAppointments));
            lblEmployee.setText(String.valueOf(totalEmployees));
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Something went wrong!").show();
        }
    }

    private void loadBarChart() {
        try {
            XYChart.Series<String,Double> data = PaymentModel.findIncome();
            barChart.setLegendVisible(false);
            if (data != null){
                barChart.getData().addAll(data);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Something went wrong!").show();
        }
    }

    private void setValueFactory() {
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colTime.setCellValueFactory(new PropertyValueFactory<>("time"));
        colEmployee.setCellValueFactory(new PropertyValueFactory<>("customerName"));
    }

    private void populateUpcomingTable() {
        try {
            ObservableList<UpcomingAppointmentTM> data = AppointmentModel.findUpcomingAppointment();
            if (data != null){
                tblUpcoming.setItems(data);
            }else{
                new Alert(Alert.AlertType.WARNING,"No upcoming appointments!").show();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Something went wrong").show();
        }
    }

    private void loadDateandTime() {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        lblDate.setText(format.format(date));

        Timeline timeline = new Timeline(new KeyFrame(Duration.ZERO, e ->{
                DateTimeFormatter format2 = DateTimeFormatter.ofPattern("HH:mm:ss");
                lblTime.setText(LocalTime.now().format(format2));
            }), new KeyFrame(Duration.seconds(1))
        );

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }
}
