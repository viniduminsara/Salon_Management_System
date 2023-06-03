package lk.ijse.moods_salon.bo.custom;

import lk.ijse.moods_salon.dto.ServiceDTO;

import java.sql.SQLException;
import java.util.ArrayList;

public interface ServiceBO {

    boolean serviceExists(String id) throws SQLException;

    boolean addService(ServiceDTO dto) throws SQLException;

    boolean deleteService(String id) throws SQLException;

    boolean updateService(ServiceDTO dto) throws SQLException;

    ArrayList<ServiceDTO> getAllServices() throws SQLException;

}
