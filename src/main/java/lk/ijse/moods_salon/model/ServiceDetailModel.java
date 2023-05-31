package lk.ijse.moods_salon.model;

import lk.ijse.moods_salon.util.CrudUtil;

import java.sql.SQLException;
import java.util.List;

public class ServiceDetailModel {
    public static boolean saveServiceDetails(String appointmentId, List<String> services) throws SQLException {
        for (String service: services) {
            if (!save(appointmentId,service)){
                return false;
            }
        }
        return true;
    }

    private static boolean save(String appointmentId, String service) throws SQLException {
        String query = "INSERT INTO service_details VALUES (?,?)";
        return CrudUtil.execute(query,appointmentId,service);
    }
}
