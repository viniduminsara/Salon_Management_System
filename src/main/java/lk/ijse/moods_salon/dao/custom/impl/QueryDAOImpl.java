package lk.ijse.moods_salon.dao.custom.impl;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import lk.ijse.moods_salon.dao.SQLUtil;
import lk.ijse.moods_salon.dao.custom.QueryDAO;
import lk.ijse.moods_salon.dto.tm.AttendanceTM;
import lk.ijse.moods_salon.dto.tm.InventoryOrderTM;
import lk.ijse.moods_salon.dto.tm.PaymentTM;
import lk.ijse.moods_salon.dto.tm.UpcomingAppointmentTM;

import java.sql.ResultSet;
import java.sql.SQLException;

public class QueryDAOImpl implements QueryDAO {
    @Override
    public ObservableList<AttendanceTM> get(String date) throws SQLException {
        String query = "SELECT employee.employeeId,name,jobRole,status FROM employee\n" +
                "JOIN attendance on employee.employeeId = attendance.employeeId\n" +
                "WHERE date = ?";
        ResultSet rs = SQLUtil.execute(query,date);
        ObservableList<AttendanceTM> attendance = FXCollections.observableArrayList();
        while (rs.next()){
            attendance.add(new AttendanceTM(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4)));
        }
        return attendance;
    }

    @Override
    public ObservableList<InventoryOrderTM> getAllOrders() throws SQLException {
        String query = "SELECT orderId,name,date,fullName FROM inventory_order\n" +
                "    JOIN user on user.userId = inventory_order.userId\n" +
                "    JOIN supplier on supplier.supplierId = inventory_order.supplierId ORDER BY orderId";
        ResultSet rs =  SQLUtil.execute(query);
        ObservableList<InventoryOrderTM> orders = FXCollections.observableArrayList();
        while (rs.next()){
            orders.add(new InventoryOrderTM(rs.getString(1),rs.getString(2),rs.getDate(3),rs.getString(4)));
        }
        return orders;
    }

    @Override
    public String getAmount(String id) throws SQLException {
        String query = "SELECT SUM(price) FROM service\n" +
                "JOIN service_details sd on service.serviceId = sd.serviceId\n" +
                "JOIN appointment on appointment.appointmentId = sd.appointmentId WHERE appointment.appointmentId = ?";
        ResultSet rs = SQLUtil.execute(query,id);
        if (rs.next()){
            return rs.getString(1);
        }
        return "0.00";
    }

    @Override
    public String getEmail(String id) throws SQLException {
        String query = "SELECT gmail FROM customer\n" +
                "JOIN appointment a on customer.customerId = a.customerId\n" +
                "JOIN payment p on a.appointmentId = p.appointmentId\n" +
                "WHERE paymentId = ?";
        ResultSet rs = SQLUtil.execute(query,id);
        if(rs.next()){
            return rs.getString(1);
        }
        return null;
    }

    @Override
    public ObservableList<PaymentTM> getAllPayments() throws SQLException {
        String query = "SELECT paymentId,date,fullName,amount FROM payment\n" +
                "join user on user.userId = payment.userId\n" +
                "ORDER BY paymentId DESC";
        ResultSet rs =  SQLUtil.execute(query);
        ObservableList<PaymentTM> payment = FXCollections.observableArrayList();
        while (rs.next()){
            payment.add(new PaymentTM(rs.getString(1),rs.getDate(2),rs.getString(3),rs.getDouble(4)));
        }
        return payment;
    }

    @Override
    public XYChart.Series<String, Double> getIncome() throws SQLException {
        String query = "SELECT MONTHNAME(payment.date) , SUM(amount) FROM payment\n" +
                "JOIN appointment on appointment.appointmentId = payment.appointmentId\n" +
                "WHERE status = 'Completed' GROUP BY MONTHNAME(payment.date) ORDER BY MONTHNAME(payment.date) DESC";
        ResultSet resultSet = SQLUtil.execute(query);

        XYChart.Series<String, Double> data = new XYChart.Series<>();
        String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        double[] income = new double[12];
        while (resultSet.next()){
            for (int i=0; i< months.length; i++){
                if (resultSet.getString(1).equals(months[i])){
                    income[i] = resultSet.getDouble(2);
                }
            }
        }
        int i = 0;
        while (months.length > i){
            data.getData().add(new XYChart.Data<>(months[i],income[i]));
            i++;
        }
        return data;
    }

    @Override
    public ObservableList<UpcomingAppointmentTM> getUpcomingAppointments() throws SQLException {
        String query = "SELECT date,time,name FROM appointment\n" +
                "JOIN customer on customer.customerId = appointment.customerId\n" +
                "    WHERE status = 'Pending' GROUP BY appointmentId ORDER BY date";
        ResultSet resultSet = SQLUtil.execute(query);
        ObservableList<UpcomingAppointmentTM> data = FXCollections.observableArrayList();

        while (resultSet.next()){
            data.add(new UpcomingAppointmentTM(resultSet.getDate(1),resultSet.getString(2),resultSet.getString(3)));
        }
        return data;
    }

    @Override
    public String getCustomerName(String id) throws SQLException {
        String query = "SELECT name FROM appointment\n" +
                "join customer on customer.customerId = appointment.customerId WHERE appointmentId = ?";
        ResultSet rs = SQLUtil.execute(query,id);
        if (rs.next()){
            return rs.getString(1);
        }
        return null;
    }
}
