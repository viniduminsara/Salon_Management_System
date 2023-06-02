package lk.ijse.moods_salon.dao.custom.impl;

import lk.ijse.moods_salon.dao.SQLUtil;
import lk.ijse.moods_salon.dao.custom.AttendanceDAO;
import lk.ijse.moods_salon.entity.Attendance;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class AttendanceDAOImpl implements AttendanceDAO {

    @Override
    public boolean add(Attendance entity) throws SQLException {
        String query = "INSERT INTO attendance VALUES (?,?,?,?)";
        return SQLUtil.execute(query,entity.getAttendanceId(),entity.getDate(),entity.getStatus(),entity.getEmployeeId());
    }

    @Override
    public boolean delete(LocalDate localDate) throws SQLException {
        return false;
    }

    @Override
    public boolean update(Attendance entity) throws SQLException {
        return false;
    }

    @Override
    public boolean exists(LocalDate localDate) throws SQLException {
        String query = "SELECT attendanceId FROM attendance WHERE date = ?";
        ResultSet rs = SQLUtil.execute(query,localDate);
        return rs.next();
    }

    @Override
    public ArrayList<Attendance> getAll() throws SQLException {
        return null;
    }

    @Override
    public String generateId() throws SQLException {
        String query = "SELECT attendanceId FROM attendance ORDER BY attendanceId DESC LIMIT 1";
        ResultSet rs = SQLUtil.execute(query);
        return rs.next() ? String.format("Att%03d", (Integer.parseInt(rs.getString(1).replace("Att", "")) + 1)) : "Att001";
    }
}
