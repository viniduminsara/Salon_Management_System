package lk.ijse.moods_salon.dao.custom;

import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import lk.ijse.moods_salon.dao.SuperDAO;
import lk.ijse.moods_salon.dto.tm.AttendanceTM;
import lk.ijse.moods_salon.dto.tm.InventoryOrderTM;
import lk.ijse.moods_salon.dto.tm.PaymentTM;
import lk.ijse.moods_salon.dto.tm.UpcomingAppointmentTM;

import java.sql.SQLException;

public interface QueryDAO extends SuperDAO {

    ObservableList<AttendanceTM> get(String date) throws SQLException;

    ObservableList<InventoryOrderTM> getAllOrders() throws SQLException;

    String getAmount(String id) throws SQLException;

    String getEmail(String id) throws SQLException;

    ObservableList<PaymentTM> getAllPayments() throws SQLException;

    XYChart.Series<String,Double> getIncome() throws SQLException;

    ObservableList<UpcomingAppointmentTM> getUpcomingAppointments() throws SQLException;

}
