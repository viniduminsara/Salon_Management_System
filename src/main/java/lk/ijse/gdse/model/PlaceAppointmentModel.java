package lk.ijse.gdse.model;

import lk.ijse.gdse.db.DBConnection;
import lk.ijse.gdse.dto.AppointmentInventory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class PlaceAppointmentModel {
    public static boolean placeAppointment(String appointmentId, Date date, String time, String customerId, List<String> services, List<String> employees, List<AppointmentInventory> inventoryList) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();

        try {
            connection.setAutoCommit(false);
            boolean isAppointmentSaved = AppointmentModel.saveAppointment(appointmentId,date,time,"Pending",customerId);
            if (isAppointmentSaved){
                boolean isServicesSaved = ServiceDetailModel.saveServiceDetails(appointmentId,services);
                if (isServicesSaved){
                    boolean isEmployeeSaved = EmployeeDetailModel.saveEmployeeDetails(appointmentId,employees);
                    if (isEmployeeSaved){
                        boolean isInventoryUpdated = InventoryModel.updateQty(inventoryList);
                        if (isInventoryUpdated){
                            boolean isInventorySaved = InventoryDetailsModel.saveInventoryDetails(appointmentId,inventoryList);
                            if (isInventorySaved){
                                connection.commit();
                                return true;
                            }
                        }
                    }
                }
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            connection.rollback();
            return false;
        }finally {
            connection.setAutoCommit(true);
        }
    }
}
