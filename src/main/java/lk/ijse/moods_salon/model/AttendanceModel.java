package lk.ijse.moods_salon.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lk.ijse.moods_salon.dto.AttendanceDTO;
import lk.ijse.moods_salon.dto.tm.AttendanceTM;
import lk.ijse.moods_salon.dto.tm.MarkAttendanceTM;
import lk.ijse.moods_salon.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class AttendanceModel {

    public static String generateId() throws SQLException {
        String query = "SELECT attendanceId FROM attendance ORDER BY attendanceId DESC LIMIT 1";
        ResultSet rs = CrudUtil.execute(query);
        if(rs.next()){
            return splitId(rs.getString(1));
        }
        return splitId(null);
    }

    private static String splitId(String currentId) {
        if (currentId != null) {
            String[] id = currentId.split("Att0");
            int num = Integer.parseInt(id[1]);
            num++;
            if (num < 10) {
                return "Att00" + num;
            }
            return "Att0" + num;
        }
        return "Att001";
    }

    public static boolean saveAttendance(ArrayList<AttendanceDTO> attendances) throws SQLException {
        for (AttendanceDTO attend : attendances) {
            attend.setAttendanceId(AttendanceModel.generateId());
            if (!save(attend)){
                return false;
            }
        }
        return true;
    }

    private static boolean save(AttendanceDTO attend) throws SQLException {
        String query = "INSERT INTO attendance VALUES (?,?,?,?)";
        return CrudUtil.execute(query,attend.getAttendanceId(),attend.getDate(),attend.getStatus(),attend.getEmployeeId());
    }

    public static boolean findExists(LocalDate date) throws SQLException {
        String query = "SELECT attendanceId FROM attendance WHERE date = ?";
        ResultSet rs = CrudUtil.execute(query,date);
        if (rs.next()){
            return true;
        }
        return false;
    }

    public static ObservableList<AttendanceTM> getAttendance(String date) throws SQLException {
        String query = "SELECT employee.employeeId,name,jobRole,status FROM employee\n" +
                "JOIN attendance on employee.employeeId = attendance.employeeId\n" +
                "WHERE date = ?";
        ResultSet rs = CrudUtil.execute(query,date);
        ObservableList<AttendanceTM> attendance = FXCollections.observableArrayList();
        while (rs.next()){
            attendance.add(new AttendanceTM(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4)));
        }
        return attendance;
    }

    public static ObservableList<MarkAttendanceTM> getMarkedAttendence(LocalDate date) throws SQLException {
        String query = "SELECT employee.employeeId,name,jobRole,status FROM employee\n" +
                "JOIN attendance a on employee.employeeId = a.employeeId\n" +
                "WHERE date = ?";
        ResultSet rs = CrudUtil.execute(query,date);
        ObservableList<MarkAttendanceTM> marked = FXCollections.observableArrayList();
        while (rs.next()){
            marked.add(new MarkAttendanceTM(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4)));
        }
        return marked;
    }
}
