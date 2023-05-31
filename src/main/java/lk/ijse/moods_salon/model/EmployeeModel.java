package lk.ijse.moods_salon.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lk.ijse.moods_salon.dto.EmployeeDTO;
import lk.ijse.moods_salon.dto.tm.MarkAttendanceTM;
import lk.ijse.moods_salon.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EmployeeModel {

    public static int findTotalEmployees() throws SQLException {
        String query = "SELECT COUNT(employeeId) FROM employee";
        ResultSet resultSet = CrudUtil.execute(query);

        if(resultSet.next()){
            return resultSet.getInt(1);
        }
        return 0;
    }

    public static ResultSet getAllEmployees() throws SQLException {
        String query = "SELECT employeeId,name,jobRole,salary,contact,address FROM employee";
        return CrudUtil.execute(query);
    }

    public static boolean addEmployee(EmployeeDTO employee) throws SQLException {
        String query = "INSERT INTO employee VALUES (?,?,?,?,?,?)";
        return CrudUtil.execute(query,employee.getEmployeeId(),employee.getName(),employee.getAddress(),employee.getContact(),employee.getJobRole(),employee.getSalary());
    }

    public static boolean deleteEmployee(String id) throws SQLException {
        String query = "DELETE FROM employee WHERE employeeId = ?";
        return CrudUtil.execute(query,id);
    }

    public static boolean updateEmployee(EmployeeDTO employee) throws SQLException {
        String query = "UPDATE employee SET name = ?,address = ?,contact = ?,jobRole = ?,salary = ? WHERE employeeId = ?";
        return CrudUtil.execute(query,employee.getName(),employee.getAddress(),employee.getContact(),employee.getJobRole(),employee.getSalary(),employee.getEmployeeId());
    }

    public static ObservableList<String> getEmployeeNames() throws SQLException {
        String query="SELECT name FROM employee ORDER BY employeeId";
        ResultSet rs = CrudUtil.execute(query);
        ObservableList<String> employees = FXCollections.observableArrayList();
        while (rs.next()){
            employees.add(rs.getString(1));
        }
        return employees;
    }

    public static String getEmployeeId(String name) throws SQLException {
        String query = "SELECT employeeId FROM employee WHERE name = ?";
        ResultSet rs = CrudUtil.execute(query,name);
        if (rs.next()){
            return rs.getString(1);
        }
        return null;
    }

    public static boolean findIfExists(String id) throws SQLException {
        String query = "SELECT employeeId FROM employee";
        ResultSet rs = CrudUtil.execute(query);
        while (rs.next()){
            if (rs.getString(1).equals(id)){
                return true;
            }
        }
        return false;
    }

    public static ObservableList<MarkAttendanceTM> getEmployees() throws SQLException {
        String query = "SELECT employeeId,name,jobRole FROM employee ORDER BY employeeId";
        ResultSet rs = CrudUtil.execute(query);
        ObservableList<MarkAttendanceTM> employees = FXCollections.observableArrayList();
        while (rs.next()){
            employees.add(new MarkAttendanceTM(rs.getString(1),rs.getString(2),rs.getString(3),"Not Marked"));
        }
        return employees;
    }

    public static String findEmployee(String id) throws SQLException {
        String query = "SELECT name FROM employee WHERE employeeId = ?";
        ResultSet rs = CrudUtil.execute(query,id);
        if (rs.next()){
            return rs.getString(1);
        }
        return "Invalid QR code";
    }
}
