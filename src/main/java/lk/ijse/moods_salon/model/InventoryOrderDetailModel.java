package lk.ijse.moods_salon.model;

import lk.ijse.moods_salon.dto.InventoryOrderDetailDTO;
import lk.ijse.moods_salon.util.CrudUtil;

import java.sql.SQLException;
import java.util.ArrayList;

public class InventoryOrderDetailModel {
    public static boolean saveOrderDetails(String orderId, ArrayList<InventoryOrderDetailDTO> items) throws SQLException {
        for (InventoryOrderDetailDTO inventory : items) {
            if (!save(orderId,inventory)){
                return false;
            }
        }
        return true;
    }

    private static boolean save(String orderId, InventoryOrderDetailDTO inventory) throws SQLException {
        String query = "INSERT INTO inventory_order_details VALUES (?,?,?)";
        return CrudUtil.execute(query,orderId,inventory.getInventoryId(),inventory.getQty());
    }
}
