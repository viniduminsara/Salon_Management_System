package lk.ijse.moods_salon.dao.custom;

import lk.ijse.moods_salon.dao.CrudDAO;
import lk.ijse.moods_salon.entity.User;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.SQLException;

public interface UserDAO extends CrudDAO<User,String> {

    String generateId() throws SQLException;

    User find(String username, String password) throws SQLException, FileNotFoundException;

    boolean updateWithoutImage(User entity) throws SQLException;

    boolean updatePassword(String id,String password) throws SQLException;

    InputStream getImage(String id) throws SQLException, FileNotFoundException;

    User get(String id) throws SQLException, FileNotFoundException;

    boolean saveFilePath(String filepath,String id) throws SQLException;

    String getFilePath(String id) throws SQLException;

}
