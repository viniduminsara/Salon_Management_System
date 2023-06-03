package lk.ijse.moods_salon.dao.custom.impl;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lk.ijse.moods_salon.dao.SQLUtil;
import lk.ijse.moods_salon.dao.custom.QueryDAO;
import lk.ijse.moods_salon.dto.tm.AttendanceTM;

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
}
