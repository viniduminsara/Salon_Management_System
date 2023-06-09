package lk.ijse.moods_salon.controller;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
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
import lk.ijse.moods_salon.bo.custom.HomeBO;
import lk.ijse.moods_salon.bo.factory.BOFactory;
import lk.ijse.moods_salon.bo.factory.BOTypes;
import lk.ijse.moods_salon.dto.tm.UpcomingAppointmentTM;

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

    @FXML
    private ImageView image;

    @FXML
    private Label lblTable;

    HomeBO homeBO = (HomeBO) BOFactory.getBoFactory().getBO(BOTypes.HOME);

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
            String totalCustomer = homeBO.getTotalCustomers();
            String totalAppointments = homeBO.getTotalAppointments();
            String totalEmployees = homeBO.getTotalEmployees();
            lblCustomer.setText(totalCustomer);
            lblAppointment.setText(totalAppointments);
            lblEmployee.setText(totalEmployees);
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Something went wrong!").show();
        }
    }

    private void loadBarChart() {
        try {
            XYChart.Series<String,Double> series = homeBO.getIncome();
            barChart.setLegendVisible(false);
            if (!series.getData().isEmpty()){
                barChart.getData().addAll(series);
            }
            for (XYChart.Data<String, Double> data : series.getData()) {
                TranslateTransition tt = new TranslateTransition(Duration.seconds(0.7), data.getNode());
                tt.setFromY(400);
                tt.setToY(0);
                tt.play();
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
            ObservableList<UpcomingAppointmentTM> data = homeBO.getAllUpcomingAppointments();
            if (!data.isEmpty()){
                tblUpcoming.setItems(data);
                image.setVisible(false);
                lblTable.setVisible(false);
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
