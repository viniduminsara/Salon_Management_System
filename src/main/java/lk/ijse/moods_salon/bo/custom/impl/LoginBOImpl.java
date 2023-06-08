package lk.ijse.moods_salon.bo.custom.impl;

import lk.ijse.moods_salon.bo.custom.LoginBO;
import lk.ijse.moods_salon.dao.custom.UserDAO;
import lk.ijse.moods_salon.dao.factory.DAOFactory;
import lk.ijse.moods_salon.dao.factory.DAOTypes;
import lk.ijse.moods_salon.dto.UserDTO;
import lk.ijse.moods_salon.entity.User;

import java.io.FileNotFoundException;
import java.sql.SQLException;

public class LoginBOImpl implements LoginBO {

    UserDAO userDAO = (UserDAO) DAOFactory.getDaoFactory().getDAO(DAOTypes.USER);

    @Override
    public UserDTO getUser(String username, String password) throws SQLException, FileNotFoundException {
        User user = userDAO.find(username, password);
        return (user != null) ? new UserDTO(user.getUserId(),user.getFullName(),user.getUserName(),user.getType(),user.getGmail(),user.getPassword(),user.getInputStream(),user.getReceipt_folder()) : null;
    }
}
