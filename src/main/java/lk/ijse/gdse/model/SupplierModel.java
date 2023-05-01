package lk.ijse.gdse.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lk.ijse.gdse.dto.Supplier;
import lk.ijse.gdse.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Queue;

public class SupplierModel {
    public static ResultSet getAll() throws SQLException {
        String query = "SELECT * FROM supplier";
        return CrudUtil.execute(query);
    }

    public static boolean addSupplier(Supplier supplier) throws SQLException {
        String query = "INSERT INTO supplier VALUES (?,?,?,?)";
        return CrudUtil.execute(query,supplier.getSupplierId(),supplier.getName(),supplier.getContact(),supplier.getAddress());
    }

    public static boolean deleteSupplier(String id) throws SQLException {
        String query = "DELETE FROM supplier WHERE supplierId = ?";
        return CrudUtil.execute(query,id);
    }

    public static boolean updateSupplier(Supplier supplier) throws SQLException {
        String query = "UPDATE supplier SET name = ?,contact = ?,address = ? WHERE supplierId = ?";
        return CrudUtil.execute(query,supplier.getName(),supplier.getContact(),supplier.getAddress(),supplier.getSupplierId());
    }

    public static boolean isExists(String id) throws SQLException {
        String query = "SELECT supplierId FROM supplier WHERE supplierId = ?";
        ResultSet rs = CrudUtil.execute(query,id);
        if (rs.next()){
            return true;
        }
        return false;
    }

    public static ObservableList<String> getSupplierIds() throws SQLException {
        String query = "SELECT supplierId FROM supplier ORDER BY supplierId";
        ResultSet rs = CrudUtil.execute(query);
        ObservableList<String> supplier = FXCollections.observableArrayList();
        while (rs.next()){
            supplier.add(rs.getString(1));
        }
        return supplier;
    }

    public static String getSupplierName(String supplier) throws SQLException {
        String query = "SELECT name FROM supplier WHERE supplierId = ?";
        ResultSet rs = CrudUtil.execute(query,supplier);
        if (rs.next()){
            return rs.getString(1);
        }
        return null;
    }
}
