package lk.ijse.moods_salon.dao.custom;

import lk.ijse.moods_salon.dao.CrudDAO;
import lk.ijse.moods_salon.entity.Appointment;

import java.sql.SQLException;

public interface AppointmentDAO extends CrudDAO<Appointment,String> {

    boolean cancel(String id) throws SQLException;

    String generateId() throws SQLException;

}
