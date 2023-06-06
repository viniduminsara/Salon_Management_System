package lk.ijse.moods_salon.bo.custom;

import javafx.collections.ObservableList;
import lk.ijse.moods_salon.dto.AppointmentDTO;
import lk.ijse.moods_salon.dto.Employee_detailsDTO;
import lk.ijse.moods_salon.dto.Inventory_detailsDTO;
import lk.ijse.moods_salon.dto.Service_detailsDTO;

import java.sql.SQLException;
import java.util.ArrayList;

public interface AppointmentBO {

    String getCustomerName(String id) throws SQLException;

    Double getServicePrice(String name) throws SQLException;

    String getServiceId(String name) throws SQLException;

    String getEmployeeId(String name) throws SQLException;

    String getInventoryId(String name) throws SQLException;

    String getCustomerEmail(String id) throws SQLException;

    ArrayList<AppointmentDTO> getAllAppointments() throws SQLException;

    boolean cancelAppointment(String id) throws SQLException;

    String generateAppointmentId() throws SQLException;

    ObservableList<String> getInventoryNames() throws SQLException;

    ObservableList<String> getEmployeeNames() throws SQLException;

    ObservableList<String> getServiceNames() throws SQLException;

    ObservableList<String> getCustomerIds() throws SQLException;

    boolean placeAppointment(AppointmentDTO appointmentDTO, ArrayList<Service_detailsDTO> services, ArrayList<Employee_detailsDTO> employees, ArrayList<Inventory_detailsDTO> inventories) throws SQLException;

}
