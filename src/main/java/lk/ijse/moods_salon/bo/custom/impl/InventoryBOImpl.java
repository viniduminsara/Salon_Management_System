package lk.ijse.moods_salon.bo.custom.impl;

import javafx.collections.ObservableList;
import lk.ijse.moods_salon.bo.custom.InventoryBO;
import lk.ijse.moods_salon.dao.custom.*;
import lk.ijse.moods_salon.dao.custom.impl.*;
import lk.ijse.moods_salon.db.DBConnection;
import lk.ijse.moods_salon.dto.InventoryDTO;
import lk.ijse.moods_salon.dto.InventoryOrderDTO;
import lk.ijse.moods_salon.dto.InventoryOrderDetailDTO;
import lk.ijse.moods_salon.dto.tm.InventoryOrderTM;
import lk.ijse.moods_salon.entity.Inventory;
import lk.ijse.moods_salon.entity.InventoryOrder;
import lk.ijse.moods_salon.entity.InventoryOrderDetails;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class InventoryBOImpl implements InventoryBO {

    InventoryDAO inventoryDAO = new InventoryDAOImpl();
    SupplierDAO supplierDAO = new SupplierDAOImpl();
    InventoryOrderDAO orderDAO = new InventoryOrderDAOImpl();
    InventoryOrderDetailsDAO orderDetailsDAO = new InventoryOrderDetailsDAOImpl();
    QueryDAO queryDAO = new QueryDAOImpl();

    @Override
    public boolean existsInventory(String id) throws SQLException {
        return inventoryDAO.exists(id);
    }

    @Override
    public boolean addInventory(InventoryDTO dto) throws SQLException {
        return inventoryDAO.add(new Inventory(dto.getInventoryId(),dto.getName(),dto.getQtyOnHand(),dto.getUnitPrice()));
    }

    @Override
    public boolean deleteInventory(String id) throws SQLException {
        return inventoryDAO.delete(id);
    }

    @Override
    public boolean updateInventory(InventoryDTO dto) throws SQLException {
        return inventoryDAO.update(new Inventory(dto.getInventoryId(),dto.getName(),dto.getQtyOnHand(),dto.getUnitPrice()));
    }

    @Override
    public InventoryDTO getInventory(String id) throws SQLException {
        Inventory inventory = inventoryDAO.get(id);
        return (inventory != null) ? new InventoryDTO(inventory.getInventoryId(),inventory.getName(),inventory.getQtyOnHand(),inventory.getUnitPrice()) : null;
    }

    @Override
    public String getSupplierName(String id) throws SQLException {
        return supplierDAO.getName(id);
    }

    @Override
    public ObservableList<InventoryOrderTM> getAllOrders() throws SQLException {
        return queryDAO.getAllOrders();
    }

    @Override
    public String generateOrderId() throws SQLException {
        return orderDAO.generateId();
    }

    @Override
    public ObservableList<String> getInventoryIds() throws SQLException {
        return inventoryDAO.getIds();
    }

    @Override
    public ObservableList<String> getSupplierIds() throws SQLException {
        return supplierDAO.getIds();
    }

    @Override
    public ArrayList<InventoryDTO> getAllInventory() throws SQLException {
        ArrayList<Inventory> all = inventoryDAO.getAll();
        ArrayList<InventoryDTO> inventoryDTOS = new ArrayList<>();
        for (Inventory entity : all) {
            inventoryDTOS.add(new InventoryDTO(entity.getInventoryId(),entity.getName(),entity.getQtyOnHand(),entity.getUnitPrice()));
        }
        return inventoryDTOS;
    }

    @Override
    public boolean placeInventoryOrder(InventoryOrderDTO dto, ArrayList<InventoryOrderDetailDTO> items) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();

        try {
            connection.setAutoCommit(false);

            //save Order
            boolean isSaved = orderDAO.add(new InventoryOrder(dto.getOrderId(),dto.getDate(),dto.getSupplierId(),dto.getUserId()));

            if (!isSaved) {
                connection.rollback();
                connection.setAutoCommit(true);
                return false;
            }

            for (InventoryOrderDetailDTO detail : items) {
                //save order details
                boolean isDetailsSaved = orderDetailsDAO.add(new InventoryOrderDetails(detail.getOrderId(),detail.getInventoryId(),detail.getQty()));
                if (!isDetailsSaved) {
                    connection.rollback();
                    connection.setAutoCommit(true);
                    return false;
                }
                //update inventory count
                boolean isUpdated = inventoryDAO.updateQty(detail.getInventoryId(),detail.getQty());

                if (!isUpdated) {
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
