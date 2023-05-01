package lk.ijse.gdse.model;

import lk.ijse.gdse.db.DBConnection;
import lk.ijse.gdse.dto.User;
import lk.ijse.gdse.util.CrudUtil;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.*;

public class UserModel {
    public static String generateUserId() throws SQLException {
        String query = "SELECT userId FROM user ORDER BY userId DESC LIMIT 1";
        ResultSet rs = CrudUtil.execute(query);
        if (rs.next()){
            return splitUserId(rs.getString(1));
        }
        return splitUserId(null);
    }

    private static String splitUserId(String id) {
        if (id != null) {
            String[] currentId = id.split("U0");
            int newId = Integer.parseInt(currentId[1]);
            newId++;
            if (newId < 10) {
                return "U00" + newId;
            } else {
                return "U0" + newId;
            }
        }
        return "U001";
    }

    public static boolean saveUser(User user) throws SQLException {
        String query = "INSERT INTO user VALUES (?,?,?,?,?,?,?)";
        return CrudUtil.execute(query,user.getUserId(),user.getFullName(),user.getUserName(),user.getType(),user.getGmail(),user.getPassword(),null);
    }

    public static User findUser(String userName, String password) throws SQLException, FileNotFoundException {
        String query = "SELECT * FROM user WHERE userName = ? AND password = ?";
        ResultSet rs = CrudUtil.execute(query,userName,password);
        if (rs.next()){
            Blob blob = rs.getBlob(7);
            InputStream inputStream;
            if (blob != null) {
                inputStream = blob.getBinaryStream();
            }else {
                inputStream = new FileInputStream("F:\\1st semester final project\\moods salon\\src\\main\\resources\\img\\images.png");
            }
            return new User(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), inputStream);
        }
        return null;
    }

    public static boolean updateUser(User user) throws SQLException {
        String query = "UPDATE user SET fullName = ?, userName = ?, gmail = ?, image = ? WHERE userId = ?";
        return CrudUtil.execute(query,user.getFullName(),user.getUserName(),user.getGmail(),user.getInputStream(),user.getUserId());
    }

    public static boolean updateWithoutImage(User user) throws SQLException {
        String query = "UPDATE user SET fullName = ?, userName = ?, gmail = ? WHERE userId = ?";
        return CrudUtil.execute(query,user.getFullName(),user.getUserName(),user.getGmail(),user.getUserId());
    }

    public static InputStream getImage(String userId) throws SQLException, FileNotFoundException {
        String query = "SELECT image FROM user WHERE userId = ?";
        ResultSet rs = CrudUtil.execute(query,userId);
        if (rs.next()){
            if (rs.getBlob(1) != null) {
                return rs.getBlob(1).getBinaryStream();
            }else {
                return new FileInputStream("F:\\1st semester final project\\moods salon\\src\\main\\resources\\img\\images.png");
            }
        }
        return null;
    }

    public static User getUser(String userId) throws SQLException, FileNotFoundException {
        String query = "SELECT * FROM user WHERE userId = ?";
        ResultSet rs = CrudUtil.execute(query,userId);
        if (rs.next()) {
            Blob blob = rs.getBlob(7);
            InputStream inputStream;
            if (blob != null) {
                inputStream = blob.getBinaryStream();
            } else {
                inputStream = new FileInputStream("F:\\1st semester final project\\moods salon\\src\\main\\resources\\img\\images.png");
            }
            return new User(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), inputStream);
        }
        return null;
    }

    public static boolean updatePassword(String id, String newPassword) throws SQLException {
        String query = "UPDATE user SET password = ? WHERE userId = ?";
        return CrudUtil.execute(query,newPassword,id);
    }
}
