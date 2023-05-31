package lk.ijse.moods_salon.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lk.ijse.moods_salon.dto.tm.UpcomingAppointmentTM;
import lk.ijse.moods_salon.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class AppointmentModel {

    public static ObservableList<UpcomingAppointmentTM> findUpcomingAppointment() throws SQLException {
        String query = "SELECT date,time,name FROM appointment\n" +
                "JOIN customer on customer.customerId = appointment.customerId\n" +
                "    WHERE status = 'Pending' GROUP BY appointmentId ORDER BY date";
        ResultSet resultSet = CrudUtil.execute(query);
        ObservableList<UpcomingAppointmentTM> data = FXCollections.observableArrayList();

        while (resultSet.next()){
            data.add(new UpcomingAppointmentTM(resultSet.getDate(1),resultSet.getString(2),resultSet.getString(3)));
        }
        return data;
    }

    public static int findTotalAppointments() throws SQLException {
        String query = "SELECT COUNT(appointmentId) FROM appointment";
        ResultSet resultSet = CrudUtil.execute(query);

        if (resultSet.next()){
            return resultSet.getInt(1);
        }
        return 0;
    }

    public static String generateId() throws SQLException {
        String query = "SELECT appointmentId FROM appointment ORDER BY appointmentId DESC LIMIT 1";
        ResultSet rs = CrudUtil.execute(query);
        if (rs.next()){
            return splitAppointmentId(rs.getString(1));
        }
        return splitAppointmentId(null);
    }

    private static String splitAppointmentId(String id) {
        if (id != null){
            String[] currentId = id.split("A0");
            int newId = Integer.parseInt(currentId[1]);
            newId++;
            if (newId < 10){
                return "A00" + newId;
            }else {
                return "A0" + newId;
            }
        }
        return "A001";
    }

    public static boolean saveAppointment(String appointmentId, Date date, String time, String status, String customerId) throws SQLException {
        String query = "INSERT INTO appointment VALUES (?,?,?,?,?)";
        return CrudUtil.execute(query,appointmentId,date,time,status,customerId);
    }

    public static ResultSet getAll() throws SQLException {
        String query = "SELECT appointmentId,customerId,date,time,status FROM appointment ORDER BY appointmentId DESC";
        return CrudUtil.execute(query);
    }

    public static boolean cancelAppointment(String id) throws SQLException {
        String query = "UPDATE appointment SET status = 'Canceled' WHERE appointmentId = ?";
        return CrudUtil.execute(query,id);
    }

    public static ObservableList<String> getPendingAppointments() throws SQLException {
        String query = "SELECT appointmentId FROM appointment WHERE status = 'Pending'";
        ResultSet rs = CrudUtil.execute(query);
        ObservableList<String> ids = FXCollections.observableArrayList();
        while (rs.next()){
            ids.add(rs.getString(1));
        }
        return ids;
    }

    public static String findAmount(String id) throws SQLException {
        String query = "SELECT SUM(price) FROM service\n" +
                "JOIN service_details sd on service.serviceId = sd.serviceId\n" +
                "JOIN appointment on appointment.appointmentId = sd.appointmentId WHERE appointment.appointmentId = ?";
        ResultSet rs = CrudUtil.execute(query,id);
        if (rs.next()){
            return rs.getString(1);
        }
        return "0.00";
    }

    public static String getCustomer(String id) throws SQLException {
        String query = "SELECT name FROM appointment\n" +
                "join customer on customer.customerId = appointment.customerId WHERE appointmentId = ?";
        ResultSet rs = CrudUtil.execute(query,id);
        if (rs.next()){
            return rs.getString(1);
        }
        return null;
    }

    public static boolean updateStatus(String appointmentId) throws SQLException {
        String query = "UPDATE appointment SET status = 'Completed' WHERE appointmentId = ?";
        return CrudUtil.execute(query,appointmentId);
    }
}
