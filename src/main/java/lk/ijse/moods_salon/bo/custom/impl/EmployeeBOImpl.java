package lk.ijse.moods_salon.bo.custom.impl;

import javafx.collections.ObservableList;
import lk.ijse.moods_salon.bo.custom.EmployeeBO;
import lk.ijse.moods_salon.dao.custom.AttendanceDAO;
import lk.ijse.moods_salon.dao.custom.EmployeeDAO;
import lk.ijse.moods_salon.dao.custom.QueryDAO;
import lk.ijse.moods_salon.dao.factory.DAOFactory;
import lk.ijse.moods_salon.dao.factory.DAOTypes;
import lk.ijse.moods_salon.dto.AttendanceDTO;
import lk.ijse.moods_salon.dto.EmployeeDTO;
import lk.ijse.moods_salon.dto.tm.AttendanceTM;
import lk.ijse.moods_salon.entity.Attendance;
import lk.ijse.moods_salon.entity.Employee;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class EmployeeBOImpl implements EmployeeBO {

    EmployeeDAO employeeDAO = (EmployeeDAO) DAOFactory.getDaoFactory().getDAO(DAOTypes.EMPLOYEE);
    AttendanceDAO attendanceDAO = (AttendanceDAO) DAOFactory.getDaoFactory().getDAO(DAOTypes.ATTENDANCE);
    QueryDAO queryDAO = (QueryDAO) DAOFactory.getDaoFactory().getDAO(DAOTypes.QUERY);

    @Override
    public boolean existsEmployee(String id) throws SQLException {
        return employeeDAO.exists(id);
    }

    @Override
    public boolean addEmployee(EmployeeDTO dto) throws SQLException {
        return employeeDAO.add(new Employee(dto.getEmployeeId(),dto.getName(),dto.getAddress(),dto.getContact(),dto.getJobRole(),dto.getSalary()));
    }

    @Override
    public boolean deleteEmployee(String id) throws SQLException {
        return employeeDAO.delete(id);
    }

    @Override
    public boolean updateEmployee(EmployeeDTO dto) throws SQLException {
        return employeeDAO.update(new Employee(dto.getEmployeeId(),dto.getName(),dto.getAddress(),dto.getContact(),dto.getJobRole(),dto.getSalary()));
    }

    @Override
    public String getEmployeeId(String name) throws SQLException {
        return employeeDAO.getId(name);
    }

    @Override
    public String getEmployeeName(String id) throws SQLException {
        String name = employeeDAO.getName(id);
        return (name != null) ? name : "Invalid QR Code";
    }

    @Override
    public ObservableList<String> getAllEmployeeNames() throws SQLException {
        return employeeDAO.getAllNames();
    }

    @Override
    public ArrayList<EmployeeDTO> getAllEmployees() throws SQLException {
        ArrayList<Employee> all = employeeDAO.getAll();
        ArrayList<EmployeeDTO> employees = new ArrayList<>();
        for (Employee employee : all) {
            employees.add(new EmployeeDTO(employee.getEmployeeId(),employee.getName(),employee.getAddress(),employee.getContact(),employee.getJobRole(),employee.getSalary()));
        }
        return employees;
    }

    @Override
    public boolean existsAttendance(LocalDate date) throws SQLException {
        return attendanceDAO.exists(date);
    }

    @Override
    public String generateAttendanceId() throws SQLException {
        return attendanceDAO.generateId();
    }

    @Override
    public boolean saveAttendance(ArrayList<AttendanceDTO> attendance) throws SQLException {
        for (AttendanceDTO attend : attendance) {
            attend.setAttendanceId(attendanceDAO.generateId());
            if (!attendanceDAO.add(new Attendance(attend.getAttendanceId(),attend.getDate(),attend.getStatus(),attend.getEmployeeId()))){
                return false;
            }
        }
        return true;
    }

    @Override
    public ObservableList<AttendanceTM> getAttendanceOfDay(String date) throws SQLException {
        return queryDAO.get(date);
    }
}
