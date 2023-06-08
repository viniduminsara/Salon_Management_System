package lk.ijse.moods_salon.bo.custom.impl;

import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import lk.ijse.moods_salon.bo.custom.HomeBO;
import lk.ijse.moods_salon.dao.custom.AppointmentDAO;
import lk.ijse.moods_salon.dao.custom.CustomerDAO;
import lk.ijse.moods_salon.dao.custom.EmployeeDAO;
import lk.ijse.moods_salon.dao.custom.QueryDAO;
import lk.ijse.moods_salon.dao.factory.DAOFactory;
import lk.ijse.moods_salon.dao.factory.DAOTypes;
import lk.ijse.moods_salon.dto.tm.UpcomingAppointmentTM;

import java.sql.SQLException;

public class HomeBOImpl implements HomeBO {

    CustomerDAO customerDAO = (CustomerDAO) DAOFactory.getDaoFactory().getDAO(DAOTypes.CUSTOMER);
    AppointmentDAO appointmentDAO = (AppointmentDAO) DAOFactory.getDaoFactory().getDAO(DAOTypes.APPOINTMENT);
    EmployeeDAO employeeDAO = (EmployeeDAO) DAOFactory.getDaoFactory().getDAO(DAOTypes.EMPLOYEE);
    QueryDAO queryDAO = (QueryDAO) DAOFactory.getDaoFactory().getDAO(DAOTypes.QUERY);

    @Override
    public String getTotalCustomers() throws SQLException {
        return customerDAO.getTotal();
    }

    @Override
    public String getTotalAppointments() throws SQLException {
        return appointmentDAO.getTotal();
    }

    @Override
    public String getTotalEmployees() throws SQLException {
        return employeeDAO.getTotal();
    }

    @Override
    public XYChart.Series<String, Double> getIncome() throws SQLException {
        return queryDAO.getIncome();
    }

    @Override
    public ObservableList<UpcomingAppointmentTM> getAllUpcomingAppointments() throws SQLException {
        return queryDAO.getUpcomingAppointments();
    }
}
