package lk.ijse.moods_salon.dao.custom;

import lk.ijse.moods_salon.dao.CrudDAO;
import lk.ijse.moods_salon.entity.Payment;

import java.sql.SQLException;

public interface PaymentDAO extends CrudDAO<Payment,String> {

    String generateId() throws SQLException;

}
