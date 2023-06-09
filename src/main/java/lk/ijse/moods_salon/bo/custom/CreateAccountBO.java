package lk.ijse.moods_salon.bo.custom;

import lk.ijse.moods_salon.bo.SuperBO;
import lk.ijse.moods_salon.dto.UserDTO;

import java.sql.SQLException;

public interface CreateAccountBO extends SuperBO {

    String generateUserId() throws SQLException;

    boolean addUser(UserDTO dto) throws SQLException;

}
