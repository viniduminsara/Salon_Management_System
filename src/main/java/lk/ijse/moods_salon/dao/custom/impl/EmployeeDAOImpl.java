package lk.ijse.moods_salon.dao.custom.impl;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lk.ijse.moods_salon.dao.SQLUtil;
import lk.ijse.moods_salon.dao.custom.EmployeeDAO;
import lk.ijse.moods_salon.entity.Employee;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class EmployeeDAOImpl implements EmployeeDAO {
    @Override
    public boolean add(Employee entity) throws SQLException {
        String query = "INSERT INTO employee VALUES (?,?,?,?,?,?)";
        return SQLUtil.execute(query,entity.getEmployeeId(),entity.getName(),entity.getAddress(),entity.getContact(),entity.getJobRole(),entity.getSalary());
    }

    @Override
    public boolean delete(String s) throws SQLException {
        String query = "DELETE FROM employee WHERE employeeId = ?";
        return SQLUtil.execute(query,s);
    }

    @Override
    public boolean update(Employee entity) throws SQLException {
        String query = "UPDATE employee SET name = ?,address = ?,contact = ?,jobRole = ?,salary = ? WHERE employeeId = ?";
        return SQLUtil.execute(query,entity.getName(),entity.getAddress(),entity.getContact(),entity.getJobRole(),entity.getSalary(),entity.getEmployeeId());
    }

    @Override
    public boolean exists(String s) throws SQLException {
        String query = "SELECT employeeId FROM employee WHERE employeeId = ?";
        ResultSet rs = SQLUtil.execute(query,s);
        return rs.next();
    }

    @Override
    public ArrayList<Employee> getAll() throws SQLException {
        String query = "SELECT employeeId,name,address,contact,jobRole,salary FROM employee ORDER BY employeeId";
        ResultSet rs = SQLUtil.execute(query);
        ArrayList<Employee> employees = new ArrayList<>();
        while (rs.next()){
            employees.add(new Employee(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getDouble(6)));
        }
        return employees;
    }

    @Override
    public String getId(String name) throws SQLException {
        String query = "SELECT employeeId FROM employee WHERE name = ?";
        ResultSet rs = SQLUtil.execute(query,name);
        if (rs.next()){
            return rs.getString(1);
        }
        return null;
    }

    @Override
    public String getName(String id) throws SQLException {
        String query = "SELECT name FROM employee WHERE employeeId = ?";
        ResultSet rs = SQLUtil.execute(query,id);
        if (rs.next()){
            return rs.getString(1);
        }
        return null;
    }

    @Override
    public ObservableList<String> getAllNames() throws SQLException {
        String query="SELECT name FROM employee ORDER BY employeeId";
        ResultSet rs = SQLUtil.execute(query);
        ObservableList<String> employees = FXCollections.observableArrayList();
        while (rs.next()){
            employees.add(rs.getString(1));
        }
        return employees;
    }

    @Override
    public String getTotal() throws SQLException {
        String query = "SELECT COUNT(employeeId) FROM employee";
        ResultSet resultSet = SQLUtil.execute(query);

        if(resultSet.next()){
            return resultSet.getString(1);
        }
        return null;
    }
}
