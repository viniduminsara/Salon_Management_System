package lk.ijse.moods_salon.dao.custom.impl;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lk.ijse.moods_salon.dao.SQLUtil;
import lk.ijse.moods_salon.dao.custom.AppointmentDAO;
import lk.ijse.moods_salon.entity.Appointment;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AppointmentDAOImpl implements AppointmentDAO {


    @Override
    public boolean add(Appointment entity) throws SQLException {
        String query = "INSERT INTO appointment VALUES (?,?,?,?,?)";
        return SQLUtil.execute(query,entity.getAppointmentId(),entity.getDate(),entity.getTime(),entity.getStatus(),entity.getCustomerId());
    }

    @Override
    public boolean delete(String s) throws SQLException {
        return false;
    }

    @Override
    public boolean update(Appointment entity) throws SQLException {
        return false;
    }

    @Override
    public boolean exists(String s) throws SQLException {
        return false;
    }

    @Override
    public ArrayList<Appointment> getAll() throws SQLException {
        String query = "SELECT * FROM appointment ORDER BY appointmentId DESC";
        ResultSet rs = SQLUtil.execute(query);
        ArrayList<Appointment> appointments = new ArrayList<>();
        while (rs.next()){
            appointments.add(new Appointment(rs.getString(1),rs.getDate(2),rs.getString(3),rs.getString(4),rs.getString(5)));
        }
        return appointments;
    }

    @Override
    public boolean cancel(String id) throws SQLException {
        String query = "UPDATE appointment SET status = 'Canceled' WHERE appointmentId = ?";
        return SQLUtil.execute(query,id);
    }

    @Override
    public String generateId() throws SQLException {
        String query = "SELECT appointmentId FROM appointment ORDER BY appointmentId DESC LIMIT 1";
        ResultSet rs = SQLUtil.execute(query);
        return rs.next() ? String.format("A%03d", (Integer.parseInt(rs.getString(1).replace("A", "")) + 1)) : "A001";
    }

    @Override
    public ObservableList<String> getPendingIds() throws SQLException {
        String query = "SELECT appointmentId FROM appointment WHERE status = 'Pending'";
        ResultSet rs = SQLUtil.execute(query);
        ObservableList<String> ids = FXCollections.observableArrayList();
        while (rs.next()){
            ids.add(rs.getString(1));
        }
        return ids;
    }

    @Override
    public boolean updateStatus(String id) throws SQLException {
        String query = "UPDATE appointment SET status = 'Completed' WHERE appointmentId = ?";
        return SQLUtil.execute(query,id);
    }

    @Override
    public String getTotal() throws SQLException {
        String query = "SELECT COUNT(appointmentId) FROM appointment";
        ResultSet resultSet = SQLUtil.execute(query);

        if (resultSet.next()){
            return resultSet.getString(1);
        }
        return null;
    }
}
