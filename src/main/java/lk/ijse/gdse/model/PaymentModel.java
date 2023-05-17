package lk.ijse.gdse.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import lk.ijse.gdse.dto.tm.PaymentTM;
import lk.ijse.gdse.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PaymentModel {

    public static XYChart.Series<String,Double> findIncome() throws SQLException {
        String query = "SELECT MONTHNAME(payment.date) , SUM(amount) FROM payment\n" +
                "JOIN appointment on appointment.appointmentId = payment.appointmentId\n" +
                "WHERE status = 'Completed' GROUP BY MONTHNAME(payment.date) ORDER BY MONTHNAME(payment.date) DESC";
        ResultSet resultSet = CrudUtil.execute(query);

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

    public static String generateId() throws SQLException {
        String query = "SELECT paymentId FROM payment ORDER BY paymentId DESC LIMIT 1";
        ResultSet rs = CrudUtil.execute(query);
        if (rs.next()){
            return splitPaymentId(rs.getString(1));
        }
        return splitPaymentId(null);
    }

    private static String splitPaymentId(String Id) {
        if (Id != null){
            String[] currentId = Id.split("P0");
            int num = Integer.parseInt(currentId[1]);
            num++;
            if (num < 10){
                return "P00"+num;
            }else {
                return "P0"+num;
            }
        }
        return "P001";
    }

    public static ObservableList<PaymentTM> getAll() throws SQLException {
        String query = "SELECT paymentId,date,fullName,amount FROM payment\n" +
                "join user on user.userId = payment.userId\n" +
                "ORDER BY paymentId DESC";
        ResultSet rs =  CrudUtil.execute(query);
        ObservableList<PaymentTM> payment = FXCollections.observableArrayList();
        while (rs.next()){
            payment.add(new PaymentTM(rs.getString(1),rs.getDate(2),rs.getString(3),rs.getDouble(4)));
        }
        return payment;
    }

    public static boolean savePayment(String paymentId, double amount, String date, String userId, String appointmentId) throws SQLException {
        String query = "INSERT INTO payment VALUES (?,?,?,?,?)";
        return CrudUtil.execute(query,paymentId,amount,date,userId,appointmentId);
    }

    public static String getEmail(String paymentId) throws SQLException {
        String query = "SELECT gmail FROM customer\n" +
                "JOIN appointment a on customer.customerId = a.customerId\n" +
                "JOIN payment p on a.appointmentId = p.appointmentId\n" +
                "WHERE paymentId = ?";
        ResultSet rs = CrudUtil.execute(query,paymentId);
        if(rs.next()){
            return rs.getString(1);
        }
        return null;
    }
}
