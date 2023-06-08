package lk.ijse.moods_salon.bo.custom;

import lk.ijse.moods_salon.bo.SuperBO;
import lk.ijse.moods_salon.dto.SupplierDTO;

import java.sql.SQLException;
import java.util.ArrayList;

public interface SupplierBO extends SuperBO {

    boolean existsSupplier(String id) throws SQLException;

    boolean addSupplier(SupplierDTO dto) throws SQLException;

    boolean deleteSupplier(String id) throws SQLException;

    boolean updateSupplier(SupplierDTO dto) throws SQLException;

    ArrayList<SupplierDTO> getAllSuppliers() throws SQLException;

}
