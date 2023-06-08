package lk.ijse.moods_salon.bo.custom.impl;

import lk.ijse.moods_salon.bo.custom.CreateAccountBO;
import lk.ijse.moods_salon.dao.custom.UserDAO;
import lk.ijse.moods_salon.dao.factory.DAOFactory;
import lk.ijse.moods_salon.dao.factory.DAOTypes;
import lk.ijse.moods_salon.dto.UserDTO;
import lk.ijse.moods_salon.entity.User;

import java.sql.SQLException;

public class CreateAccountBOImpl implements CreateAccountBO {

    UserDAO userDAO = (UserDAO) DAOFactory.getDaoFactory().getDAO(DAOTypes.USER);

    @Override
    public String generateUserId() throws SQLException {
        return userDAO.generateId();
    }

    @Override
    public boolean addUser(UserDTO dto) throws SQLException {
        return userDAO.add(new User(dto.getUserId(),dto.getFullName(),dto.getUserName(),dto.getType(),dto.getGmail(),dto.getPassword(),dto.getInputStream(),dto.getReceipt_folder()));
    }
}
