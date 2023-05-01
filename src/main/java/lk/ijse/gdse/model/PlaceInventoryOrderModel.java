package lk.ijse.gdse.model;

import lk.ijse.gdse.db.DBConnection;
import lk.ijse.gdse.dto.OrderItemDetail;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

public class PlaceInventoryOrderModel {

    public static boolean placeOrder(String orderId, Date date, String supplierId, String userId, ArrayList<OrderItemDetail> items) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();

        try {
            connection.setAutoCommit(false);
            boolean isOrderSaved = InventoryOrderModel.saveOrder(orderId,date,supplierId,userId);
            if (isOrderSaved){
                boolean isOrderDetailsSaved = InventoryOrderDetailModel.saveOrderDetails(orderId,items);
                if (isOrderDetailsSaved){
                    boolean isQtyUpdated = InventoryModel.updateOrderQty(items);
                    if (isQtyUpdated){
                        connection.commit();
                        return true;
                    }
                }
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            connection.rollback();
            return false;
        } finally {
            connection.setAutoCommit(true);
        }
    }
}
