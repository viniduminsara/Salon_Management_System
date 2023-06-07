package lk.ijse.moods_salon.dao.custom.impl;

import lk.ijse.moods_salon.dao.SQLUtil;
import lk.ijse.moods_salon.dao.custom.PaymentDAO;
import lk.ijse.moods_salon.entity.Payment;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PaymentDAOImpl implements PaymentDAO {
    @Override
    public boolean add(Payment entity) throws SQLException {
        String query = "INSERT INTO payment VALUES (?,?,?,?,?)";
        return SQLUtil.execute(query,entity.getPaymentId(),entity.getAmount(),entity.getDate(),entity.getUserId(),entity.getAppointmentId());
    }

    @Override
    public boolean delete(String s) throws SQLException {
        return false;
    }

    @Override
    public boolean update(Payment entity) throws SQLException {
        return false;
    }

    @Override
    public boolean exists(String s) throws SQLException {
        return false;
    }

    @Override
    public ArrayList<Payment> getAll() throws SQLException {
        return null;
    }

    @Override
    public String generateId() throws SQLException {
        String query = "SELECT paymentId FROM payment ORDER BY paymentId DESC LIMIT 1";
        ResultSet rs = SQLUtil.execute(query);
        return rs.next() ? String.format("P%03d", (Integer.parseInt(rs.getString(1).replace("P", "")) + 1)) : "P001";
    }
}
