package lk.ijse.moods_salon.dao.custom.impl;

import lk.ijse.moods_salon.dao.SQLUtil;
import lk.ijse.moods_salon.dao.custom.UserDAO;
import lk.ijse.moods_salon.entity.User;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserDAOImpl implements UserDAO {
    @Override
    public boolean add(User entity) throws SQLException {
        String query = "INSERT INTO user VALUES (?,?,?,?,?,?,?,?)";
        return SQLUtil.execute(query,entity.getUserId(),entity.getFullName(),entity.getUserName(),entity.getType(),entity.getGmail(),entity.getPassword(),entity.getInputStream(),entity.getReceipt_folder());
    }

    @Override
    public boolean delete(String s) throws SQLException {
        return false;
    }

    @Override
    public boolean update(User entity) throws SQLException {
        String query = "UPDATE user SET fullName = ?, userName = ?, gmail = ?, image = ? WHERE userId = ?";
        return SQLUtil.execute(query,entity.getFullName(),entity.getUserName(),entity.getGmail(),entity.getInputStream(),entity.getUserId());
    }

    @Override
    public boolean exists(String s) throws SQLException {
        return false;
    }

    @Override
    public ArrayList<User> getAll() throws SQLException {
        return null;
    }

    @Override
    public String generateId() throws SQLException {
        String query = "SELECT userId FROM user ORDER BY userId DESC LIMIT 1";
        ResultSet rs = SQLUtil.execute(query);
        return rs.next() ? String.format("U%03d", (Integer.parseInt(rs.getString(1).replace("U", "")) + 1)) : "U001";
    }

    @Override
    public User find(String username, String password) throws SQLException, FileNotFoundException {
        String query = "SELECT * FROM user WHERE userName = ? AND password = ?";
        ResultSet rs = SQLUtil.execute(query,username,password);
        if (rs.next()){
            Blob blob = rs.getBlob(7);
            InputStream inputStream;
            if (blob != null) {
                inputStream = blob.getBinaryStream();
            }else {
                inputStream = new FileInputStream("src/main/resources/img/images.png");
            }
            return new User(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), inputStream,rs.getString(8));
        }
        return null;
    }

    @Override
    public boolean updateWithoutImage(User entity) throws SQLException {
        String query = "UPDATE user SET fullName = ?, userName = ?, gmail = ? WHERE userId = ?";
        return SQLUtil.execute(query,entity.getFullName(),entity.getUserName(),entity.getGmail(),entity.getUserId());
    }

    @Override
    public boolean updatePassword(String id, String password) throws SQLException {
        String query = "UPDATE user SET password = ? WHERE userId = ?";
        return SQLUtil.execute(query,password,id);
    }

    @Override
    public InputStream getImage(String id) throws SQLException, FileNotFoundException {
        String query = "SELECT image FROM user WHERE userId = ?";
        ResultSet rs = SQLUtil.execute(query,id);
        if (rs.next()){
            if (rs.getBlob(1) != null) {
                return rs.getBlob(1).getBinaryStream();
            }else {
                return new FileInputStream("src/main/resources/img/images.png");
            }
        }
        return null;
    }

    @Override
    public User get(String id) throws SQLException, FileNotFoundException {
        String query = "SELECT * FROM user WHERE userId = ?";
        ResultSet rs = SQLUtil.execute(query,id);
        if (rs.next()) {
            Blob blob = rs.getBlob(7);
            InputStream inputStream;
            if (blob != null) {
                inputStream = blob.getBinaryStream();
            } else {
                inputStream = new FileInputStream("src/main/resources/img/images.png");
            }
            return new User(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), inputStream,rs.getString(8));
        }
        return null;
    }

    @Override
    public boolean saveFilePath(String filepath, String id) throws SQLException {
        String query = "UPDATE user SET receipt_folder = ? WHERE userId = ?";
        return SQLUtil.execute(query,filepath,id);
    }

    @Override
    public String getFilePath(String id) throws SQLException {
        String query = "SELECT receipt_folder FROM user WHERE userId = ?";
        ResultSet rs = SQLUtil.execute(query,id);
        if (rs.next()){
            return rs.getString(1);
        }
        return null;
    }
}
