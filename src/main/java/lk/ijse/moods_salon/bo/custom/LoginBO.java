package lk.ijse.moods_salon.bo.custom;

import lk.ijse.moods_salon.dto.UserDTO;

import java.io.FileNotFoundException;
import java.sql.SQLException;

public interface LoginBO {

    UserDTO getUser(String username,String password) throws SQLException, FileNotFoundException;

}
