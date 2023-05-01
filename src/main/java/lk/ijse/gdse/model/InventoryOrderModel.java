package lk.ijse.gdse.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lk.ijse.gdse.dto.tm.InventoryOrderTM;
import lk.ijse.gdse.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class InventoryOrderModel {

    public static String generateId() throws SQLException {
        String query = "SELECT orderId FROM inventory_order ORDER BY orderId DESC LIMIT 1";
        ResultSet rs = CrudUtil.execute(query);
        if (rs.next()){
            return splitOrderId(rs.getString(1));
        }
        return splitOrderId(null);
    }

    private static String splitOrderId(String lastId) {
        if (lastId != null){
            String[] id = lastId.split("O-0");
            int num = Integer.parseInt(id[1]);
            num++;;
            if (num < 10){
                return "O-00" + num;
            }else {
                return "O-0" + num;
            }
        }
        return "O-001";
    }

    public static boolean saveOrder(String orderId, Date date, String supplierId, String userId) throws SQLException {
        String query = "INSERT INTO inventory_order VALUES (?,?,?,?)";
        return CrudUtil.execute(query,orderId,date,supplierId,userId);
    }

    public static ObservableList<InventoryOrderTM> getAll() throws SQLException {
        String query = "SELECT orderId,name,date,fullName FROM inventory_order\n" +
                "    JOIN user on user.userId = inventory_order.userId\n" +
                "    JOIN supplier on supplier.supplierId = inventory_order.supplierId ORDER BY orderId";
        ResultSet rs =  CrudUtil.execute(query);
        ObservableList<InventoryOrderTM> orders = FXCollections.observableArrayList();
        while (rs.next()){
            orders.add(new InventoryOrderTM(rs.getString(1),rs.getString(2),rs.getDate(3),rs.getString(4)));
        }
        return orders;
    }
}
