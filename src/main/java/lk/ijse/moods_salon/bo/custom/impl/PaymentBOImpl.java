package lk.ijse.moods_salon.bo.custom.impl;

import javafx.collections.ObservableList;
import lk.ijse.moods_salon.bo.custom.PaymentBO;
import lk.ijse.moods_salon.dao.custom.AppointmentDAO;
import lk.ijse.moods_salon.dao.custom.PaymentDAO;
import lk.ijse.moods_salon.dao.custom.QueryDAO;
import lk.ijse.moods_salon.dao.custom.UserDAO;
import lk.ijse.moods_salon.dao.factory.DAOFactory;
import lk.ijse.moods_salon.dao.factory.DAOTypes;
import lk.ijse.moods_salon.db.DBConnection;
import lk.ijse.moods_salon.dto.PaymentDTO;
import lk.ijse.moods_salon.dto.tm.PaymentTM;
import lk.ijse.moods_salon.entity.Payment;

import java.sql.Connection;
import java.sql.SQLException;

public class PaymentBOImpl implements PaymentBO {

    PaymentDAO paymentDAO = (PaymentDAO) DAOFactory.getDaoFactory().getDAO(DAOTypes.PAYMENT);
    QueryDAO queryDAO = (QueryDAO) DAOFactory.getDaoFactory().getDAO(DAOTypes.QUERY);
    UserDAO userDAO = (UserDAO) DAOFactory.getDaoFactory().getDAO(DAOTypes.USER);
    AppointmentDAO appointmentDAO = (AppointmentDAO) DAOFactory.getDaoFactory().getDAO(DAOTypes.APPOINTMENT);

    @Override
    public String getPaymentAmount(String id) throws SQLException {
        return queryDAO.getAmount(id);
    }

    @Override
    public boolean saveFilePath(String filepath, String id) throws SQLException {
        return userDAO.saveFilePath(filepath,id);
    }

    @Override
    public String getFilePath(String id) throws SQLException {
        return userDAO.getFilePath(id);
    }

    @Override
    public String getEmail(String id) throws SQLException {
        return queryDAO.getEmail(id);
    }

    @Override
    public ObservableList<PaymentTM> getAllPayments() throws SQLException {
        return queryDAO.getAllPayments();
    }

    @Override
    public ObservableList<String> getPendingAppointments() throws SQLException {
        return appointmentDAO.getPendingIds();
    }

    @Override
    public String generatePaymentId() throws SQLException {
        return paymentDAO.generateId();
    }

    @Override
    public boolean placePayment(PaymentDTO dto) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();

        try {
            connection.setAutoCommit(false);

            //save payment
            boolean isSaved = paymentDAO.add(new Payment(dto.getPaymentId(), dto.getAmount(), dto.getDate(), dto.getUserId(), dto.getAppointmentId()));
            if (!isSaved){
                connection.rollback();
                connection.setAutoCommit(true);
                return false;
            }
            //update appointment status
            boolean isUpdated = appointmentDAO.updateStatus(dto.getAppointmentId());
            if (!isUpdated){
                connection.rollback();
                connection.setAutoCommit(true);
                return false;
            }

            connection.commit();
            connection.setAutoCommit(true);
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            connection.rollback();
            connection.setAutoCommit(true);
            return false;
        }
    }

    @Override
    public String getCustomerName(String id) throws SQLException {
        return queryDAO.getCustomerName(id);
    }
}
