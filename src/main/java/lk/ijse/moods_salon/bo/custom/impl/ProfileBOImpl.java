package lk.ijse.moods_salon.bo.custom.impl;

import lk.ijse.moods_salon.bo.custom.ProfileBO;
import lk.ijse.moods_salon.dao.custom.UserDAO;
import lk.ijse.moods_salon.dao.factory.DAOFactory;
import lk.ijse.moods_salon.dao.factory.DAOTypes;
import lk.ijse.moods_salon.dto.UserDTO;
import lk.ijse.moods_salon.entity.User;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.SQLException;

public class ProfileBOImpl implements ProfileBO {

    UserDAO userDAO = (UserDAO) DAOFactory.getDaoFactory().getDAO(DAOTypes.USER);

    @Override
    public boolean updateUser(UserDTO dto) throws SQLException {
        return userDAO.update(new User(dto.getUserId(),dto.getFullName(),dto.getUserName(),dto.getType(),dto.getGmail(),dto.getPassword(),dto.getInputStream(),dto.getReceipt_folder()));
    }

    @Override
    public boolean updateUserWithoutImage(UserDTO dto) throws SQLException {
        return userDAO.updateWithoutImage(new User(dto.getUserId(),dto.getFullName(),dto.getUserName(),dto.getType(),dto.getGmail(),dto.getPassword(),dto.getInputStream(),dto.getReceipt_folder()));
    }

    @Override
    public boolean updateUserPassword(String id, String newPassword) throws SQLException {
        return userDAO.updatePassword(id,newPassword);
    }

    @Override
    public UserDTO getUser(String id) throws SQLException, FileNotFoundException {
        User user = userDAO.get(id);
        return (user != null) ? new UserDTO(user.getUserId(),user.getFullName(),user.getUserName(),user.getType(),user.getGmail(),user.getPassword(),user.getInputStream(),user.getReceipt_folder()) : null;
    }

    @Override
    public InputStream getUserImage(String id) throws SQLException, FileNotFoundException {
        return userDAO.getImage(id);
    }
}
