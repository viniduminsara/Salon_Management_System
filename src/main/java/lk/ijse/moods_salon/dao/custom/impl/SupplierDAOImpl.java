package lk.ijse.moods_salon.dao.custom.impl;

import lk.ijse.moods_salon.dao.SQLUtil;
import lk.ijse.moods_salon.dao.custom.SupplierDAO;
import lk.ijse.moods_salon.entity.Supplier;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SupplierDAOImpl implements SupplierDAO {

    @Override
    public boolean add(Supplier entity) throws SQLException {
        String query = "INSERT INTO supplier VALUES (?,?,?,?)";
        return SQLUtil.execute(query,entity.getSupplierId(),entity.getName(),entity.getContact(),entity.getAddress());
    }

    @Override
    public boolean delete(String s) throws SQLException {
        String query = "DELETE FROM supplier WHERE supplierId = ?";
        return SQLUtil.execute(query,s);
    }

    @Override
    public boolean update(Supplier entity) throws SQLException {
        String query = "UPDATE supplier SET name = ?,contact = ?,address = ? WHERE supplierId = ?";
        return SQLUtil.execute(query,entity.getName(),entity.getContact(),entity.getAddress(),entity.getSupplierId());
    }

    @Override
    public boolean exists(String s) throws SQLException {
        String query = "SELECT supplierId FROM supplier WHERE supplierId = ?";
        ResultSet rs = SQLUtil.execute(query,s);
        return rs.next();
    }

    @Override
    public ArrayList<Supplier> getAll() throws SQLException {
        String query = "SELECT supplierId,name,contact,address FROM supplier ORDER BY supplierId";
        ResultSet rs = SQLUtil.execute(query);
        ArrayList<Supplier> suppliers = new ArrayList<>();
        while (rs.next()){
            suppliers.add(new Supplier(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4)));
        }
        return suppliers;
    }
}
