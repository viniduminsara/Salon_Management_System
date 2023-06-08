package lk.ijse.moods_salon.bo.custom.impl;

import javafx.collections.ObservableList;
import lk.ijse.moods_salon.bo.custom.AppointmentBO;
import lk.ijse.moods_salon.dao.custom.*;
import lk.ijse.moods_salon.dao.factory.DAOFactory;
import lk.ijse.moods_salon.dao.factory.DAOTypes;
import lk.ijse.moods_salon.db.DBConnection;
import lk.ijse.moods_salon.dto.AppointmentDTO;
import lk.ijse.moods_salon.dto.Employee_detailsDTO;
import lk.ijse.moods_salon.dto.Inventory_detailsDTO;
import lk.ijse.moods_salon.dto.Service_detailsDTO;
import lk.ijse.moods_salon.entity.Appointment;
import lk.ijse.moods_salon.entity.Employee_details;
import lk.ijse.moods_salon.entity.Inventory_details;
import lk.ijse.moods_salon.entity.Service_details;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class AppointmentBOImpl implements AppointmentBO {

    AppointmentDAO appointmentDAO = (AppointmentDAO) DAOFactory.getDaoFactory().getDAO(DAOTypes.APPOINTMENT);
    ServiceDAO serviceDAO = (ServiceDAO) DAOFactory.getDaoFactory().getDAO(DAOTypes.SERVICE);
    CustomerDAO customerDAO = (CustomerDAO) DAOFactory.getDaoFactory().getDAO(DAOTypes.CUSTOMER);
    EmployeeDAO employeeDAO = (EmployeeDAO) DAOFactory.getDaoFactory().getDAO(DAOTypes.EMPLOYEE);
    InventoryDAO inventoryDAO = (InventoryDAO) DAOFactory.getDaoFactory().getDAO(DAOTypes.INVENTORY);
    Service_detailsDAO service_detailsDAO = (Service_detailsDAO) DAOFactory.getDaoFactory().getDAO(DAOTypes.SERVICE_DETAILS);
    Employee_detailsDAO employee_detailsDAO = (Employee_detailsDAO) DAOFactory.getDaoFactory().getDAO(DAOTypes.EMPLOYEE_DETAILS);
    Inventory_detailsDAO inventory_detailsDAO = (Inventory_detailsDAO) DAOFactory.getDaoFactory().getDAO(DAOTypes.INVENTORY_DETAILS);

    @Override
    public String getCustomerName(String id) throws SQLException {
        return customerDAO.getName(id);
    }

    @Override
    public Double getServicePrice(String name) throws SQLException {
        return serviceDAO.getPrice(name);
    }

    @Override
    public String getServiceId(String name) throws SQLException {
        return serviceDAO.getId(name);
    }

    @Override
    public String getEmployeeId(String name) throws SQLException {
        return employeeDAO.getId(name);
    }

    @Override
    public String getInventoryId(String name) throws SQLException {
        return inventoryDAO.getId(name);
    }

    @Override
    public String getCustomerEmail(String id) throws SQLException {
        return customerDAO.getEmail(id);
    }

    @Override
    public ArrayList<AppointmentDTO> getAllAppointments() throws SQLException {
        ArrayList<Appointment> all = appointmentDAO.getAll();
        ArrayList<AppointmentDTO> appointmentDTOS = new ArrayList<>();
        for (Appointment entity : all) {
            appointmentDTOS.add(new AppointmentDTO(entity.getAppointmentId(),entity.getDate(),entity.getTime(),entity.getStatus(),entity.getCustomerId()));
        }
        return appointmentDTOS;
    }

    @Override
    public boolean cancelAppointment(String id) throws SQLException {
        return appointmentDAO.cancel(id);
    }

    @Override
    public String generateAppointmentId() throws SQLException {
        return appointmentDAO.generateId();
    }

    @Override
    public ObservableList<String> getInventoryNames() throws SQLException {
        return inventoryDAO.getNames();
    }

    @Override
    public ObservableList<String> getEmployeeNames() throws SQLException {
        return employeeDAO.getAllNames();
    }

    @Override
    public ObservableList<String> getServiceNames() throws SQLException {
        return serviceDAO.getNames();
    }

    @Override
    public ObservableList<String> getCustomerIds() throws SQLException {
        return customerDAO.getIds();
    }

    @Override
    public boolean placeAppointment(AppointmentDTO appointmentDTO, ArrayList<Service_detailsDTO> services, ArrayList<Employee_detailsDTO> employees, ArrayList<Inventory_detailsDTO> inventories) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();

        try {
            connection.setAutoCommit(false);

            //save Appointment
            boolean isAppointmentSaved = appointmentDAO.add(new Appointment(appointmentDTO.getAppointmentId(), appointmentDTO.getDate(), appointmentDTO.getTime(), appointmentDTO.getStatus(), appointmentDTO.getCustomerId()));
            if (!isAppointmentSaved){
                connection.rollback();
                connection.setAutoCommit(true);
                return false;
            }

            //save Service details
            for (Service_detailsDTO service : services) {
                boolean isServicesSaved = service_detailsDAO.add(new Service_details(service.getAppointmentId(), service.getServiceId()));
                if (!isServicesSaved){
                    connection.rollback();
                    connection.setAutoCommit(true);
                    return false;
                }
            }

            //save Employee details
            for (Employee_detailsDTO employee : employees) {
                boolean isEmployeesSaved = employee_detailsDAO.add(new Employee_details(employee.getAppointmentId(), employee.getEmployeeId()));
                if (!isEmployeesSaved){
                    connection.rollback();
                    connection.setAutoCommit(true);
                    return false;
                }
            }

            //save Inventory details
            for (Inventory_detailsDTO inventory : inventories) {
                boolean isInventoriesSaved = inventory_detailsDAO.add(new Inventory_details(inventory.getAppointmentId(), inventory.getInventoryId(), inventory.getUsedQty()));
                if (!isInventoriesSaved){
                    connection.rollback();
                    connection.setAutoCommit(true);
                    return false;
                }

                //update Inventory qty
                boolean isUpdated = inventoryDAO.updateUsedQty(inventory.getInventoryId(), inventory.getUsedQty());
                if (!isUpdated){
                    connection.rollback();
                    connection.setAutoCommit(true);
                    return false;
                }
            }

            connection.commit();
            connection.setAutoCommit(true);
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            connection.rollback();
            connection.setAutoCommit(true);
            return false;
        }
    }
}
