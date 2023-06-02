package lk.ijse.moods_salon.dao.custom;

import lk.ijse.moods_salon.dao.CrudDAO;
import lk.ijse.moods_salon.entity.Attendance;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public interface AttendanceDAO extends CrudDAO<Attendance, LocalDate> {

    String generateId() throws SQLException;

}
