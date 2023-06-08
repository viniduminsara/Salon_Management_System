package lk.ijse.moods_salon.bo.custom;

import lk.ijse.moods_salon.bo.SuperBO;
import lk.ijse.moods_salon.dto.UserDTO;

import java.io.FileNotFoundException;
import java.sql.SQLException;

public interface LoginBO extends SuperBO {

    UserDTO getUser(String username,String password) throws SQLException, FileNotFoundException;

}
