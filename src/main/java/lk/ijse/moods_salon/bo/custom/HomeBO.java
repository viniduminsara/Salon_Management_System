package lk.ijse.moods_salon.bo.custom;

import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import lk.ijse.moods_salon.bo.SuperBO;
import lk.ijse.moods_salon.dto.tm.UpcomingAppointmentTM;

import java.sql.SQLException;

public interface HomeBO extends SuperBO {

    String getTotalCustomers() throws SQLException;

    String getTotalAppointments() throws SQLException;

    String getTotalEmployees() throws SQLException;

    XYChart.Series<String,Double> getIncome() throws SQLException;

    ObservableList<UpcomingAppointmentTM> getAllUpcomingAppointments() throws SQLException;

}
