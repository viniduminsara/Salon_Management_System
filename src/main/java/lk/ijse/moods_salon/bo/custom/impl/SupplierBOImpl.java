package lk.ijse.moods_salon.bo.custom.impl;

import lk.ijse.moods_salon.bo.custom.SupplierBO;
import lk.ijse.moods_salon.dao.custom.SupplierDAO;
import lk.ijse.moods_salon.dao.factory.DAOFactory;
import lk.ijse.moods_salon.dao.factory.DAOTypes;
import lk.ijse.moods_salon.dto.SupplierDTO;
import lk.ijse.moods_salon.entity.Supplier;

import java.sql.SQLException;
import java.util.ArrayList;

public class SupplierBOImpl implements SupplierBO {

    SupplierDAO supplierDAO = (SupplierDAO) DAOFactory.getDaoFactory().getDAO(DAOTypes.SUPPLIER);

    @Override
    public boolean existsSupplier(String id) throws SQLException {
        return supplierDAO.exists(id);
    }

    @Override
    public boolean addSupplier(SupplierDTO dto) throws SQLException {
        return supplierDAO.add(new Supplier(dto.getSupplierId(),dto.getName(),dto.getContact(),dto.getAddress()));
    }

    @Override
    public boolean deleteSupplier(String id) throws SQLException {
        return supplierDAO.delete(id);
    }

    @Override
    public boolean updateSupplier(SupplierDTO dto) throws SQLException {
        return supplierDAO.update(new Supplier(dto.getSupplierId(),dto.getName(),dto.getContact(),dto.getAddress()));
    }

    @Override
    public ArrayList<SupplierDTO> getAllSuppliers() throws SQLException {
        ArrayList<Supplier> all = supplierDAO.getAll();
        ArrayList<SupplierDTO> suppliers = new ArrayList<>();
        for (Supplier entity : all) {
            suppliers.add(new SupplierDTO(entity.getSupplierId(),entity.getName(),entity.getContact(),entity.getAddress()));
        }
        return suppliers;
    }
}
