package lk.ijse.moods_salon.model;

import lk.ijse.moods_salon.db.DBConnection;

import java.sql.Connection;
import java.sql.SQLException;

public class PlacePaymentModel {
    public static boolean placePayment(String paymentId, double amount, String date, String userId, String appointmentId) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();

        try {
            connection.setAutoCommit(false);
            boolean isPaymentSaved = PaymentModel.savePayment(paymentId,amount,date,userId,appointmentId);
            if (isPaymentSaved){
                boolean isUpdated = AppointmentModel.updateStatus(appointmentId);
                if (isUpdated){
                    connection.commit();
                    return true;
                }
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            connection.rollback();
            return false;
        } finally {
            connection.setAutoCommit(true);
        }
    }
}
