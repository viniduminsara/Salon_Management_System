package lk.ijse.moods_salon.bo.custom;

import lk.ijse.moods_salon.bo.SuperBO;
import lk.ijse.moods_salon.dto.UserDTO;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.SQLException;

public interface ProfileBO extends SuperBO {

    boolean updateUser(UserDTO dto) throws SQLException;

    boolean updateUserWithoutImage(UserDTO dto) throws SQLException;

    boolean updateUserPassword(String id,String newPassword) throws SQLException;

    UserDTO getUser(String id) throws SQLException, FileNotFoundException;

    InputStream getUserImage(String id) throws SQLException, FileNotFoundException;

}
