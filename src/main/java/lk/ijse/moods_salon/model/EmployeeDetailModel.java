package lk.ijse.moods_salon.model;

import lk.ijse.moods_salon.util.CrudUtil;

import java.sql.SQLException;
import java.util.List;

public class EmployeeDetailModel {
    public static boolean saveEmployeeDetails(String appointmentId, List<String> employees) throws SQLException {
        for (String employee : employees) {
            if (!save(appointmentId,employee)){
                return false;
            }
        }
        return true;
    }

    private static boolean save(String appointmentId, String employee) throws SQLException {
        String query = "INSERT INTO employee_details VALUES (?,?)";
        return CrudUtil.execute(query,appointmentId,employee);
    }
}
