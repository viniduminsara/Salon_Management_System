package lk.ijse.moods_salon.bo.custom;

import javafx.collections.ObservableList;
import lk.ijse.moods_salon.bo.SuperBO;
import lk.ijse.moods_salon.dto.PaymentDTO;
import lk.ijse.moods_salon.dto.tm.PaymentTM;

import java.sql.SQLException;

public interface PaymentBO extends SuperBO {

    String getPaymentAmount(String id) throws SQLException;

    boolean saveFilePath(String filepath,String id) throws SQLException;

    String getFilePath(String id) throws SQLException;

    String getEmail(String id) throws SQLException;

    ObservableList<PaymentTM> getAllPayments() throws SQLException;

    ObservableList<String> getPendingAppointments() throws SQLException;

    String generatePaymentId() throws SQLException;

    boolean placePayment(PaymentDTO dto) throws SQLException;

    String getCustomerName(String id) throws SQLException;

}
