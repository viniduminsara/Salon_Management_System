package lk.ijse.moods_salon.bo.custom.impl;

import lk.ijse.moods_salon.bo.custom.ServiceBO;
import lk.ijse.moods_salon.dao.custom.ServiceDAO;
import lk.ijse.moods_salon.dao.custom.impl.ServiceDAOImpl;
import lk.ijse.moods_salon.dto.ServiceDTO;
import lk.ijse.moods_salon.entity.Service;

import java.sql.SQLException;
import java.util.ArrayList;

public class ServiceBOImpl implements ServiceBO {

    ServiceDAO serviceDAO = new ServiceDAOImpl();

    @Override
    public boolean serviceExists(String id) throws SQLException {
        return serviceDAO.exists(id);
    }

    @Override
    public boolean addService(ServiceDTO dto) throws SQLException {
        return serviceDAO.add(new Service(dto.getServiceId(),dto.getDescription(),dto.getPrice(),dto.getCategory()));
    }

    @Override
    public boolean deleteService(String id) throws SQLException {
        return serviceDAO.delete(id);
    }

    @Override
    public boolean updateService(ServiceDTO dto) throws SQLException {
        return serviceDAO.update(new Service(dto.getServiceId(),dto.getDescription(),dto.getPrice(),dto.getCategory()));
    }

    @Override
    public ArrayList<ServiceDTO> getAllServices() throws SQLException {
        ArrayList<Service> all = serviceDAO.getAll();
        ArrayList<ServiceDTO> services = new ArrayList<>();
        for (Service entity : all) {
            services.add(new ServiceDTO(entity.getServiceId(),entity.getDescription(),entity.getPrice(),entity.getCategory()));
        }
        return services;
    }
}
