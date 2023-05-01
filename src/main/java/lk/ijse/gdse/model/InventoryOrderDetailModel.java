package lk.ijse.gdse.model;

import lk.ijse.gdse.dto.OrderItemDetail;
import lk.ijse.gdse.util.CrudUtil;

import java.sql.SQLException;
import java.util.ArrayList;

public class InventoryOrderDetailModel {
    public static boolean saveOrderDetails(String orderId, ArrayList<OrderItemDetail> items) throws SQLException {
        for (OrderItemDetail inventory : items) {
            if (!save(orderId,inventory)){
                return false;
            }
        }
        return true;
    }

    private static boolean save(String orderId, OrderItemDetail inventory) throws SQLException {
        String query = "INSERT INTO inventory_order_details VALUES (?,?,?)";
        return CrudUtil.execute(query,orderId,inventory.getInventoryId(),inventory.getQty());
    }
}
