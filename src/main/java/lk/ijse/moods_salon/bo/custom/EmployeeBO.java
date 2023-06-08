package lk.ijse.moods_salon.bo.custom;

import javafx.collections.ObservableList;
import lk.ijse.moods_salon.bo.SuperBO;
import lk.ijse.moods_salon.dto.AttendanceDTO;
import lk.ijse.moods_salon.dto.EmployeeDTO;
import lk.ijse.moods_salon.dto.tm.AttendanceTM;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public interface EmployeeBO extends SuperBO {

    boolean existsEmployee(String id) throws SQLException;

    boolean addEmployee(EmployeeDTO dto) throws SQLException;

    boolean deleteEmployee(String id) throws SQLException;

    boolean updateEmployee(EmployeeDTO dto) throws SQLException;

    String getEmployeeId(String name) throws SQLException;

    String getEmployeeName(String id) throws SQLException;

    ObservableList<String> getAllEmployeeNames() throws SQLException;

    ArrayList<EmployeeDTO> getAllEmployees() throws SQLException;

    boolean existsAttendance(LocalDate date) throws SQLException;

    String generateAttendanceId() throws SQLException;

    boolean saveAttendance(ArrayList<AttendanceDTO> attendance) throws SQLException;

    ObservableList<AttendanceTM> getAttendanceOfDay(String date) throws SQLException;

}
