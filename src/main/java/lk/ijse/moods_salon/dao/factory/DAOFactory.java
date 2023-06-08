package lk.ijse.moods_salon.dao.factory;

import lk.ijse.moods_salon.dao.SuperDAO;
import lk.ijse.moods_salon.dao.custom.impl.*;

public class DAOFactory {

    public static DAOFactory daoFactory;

    public DAOFactory() {
    }

    public static DAOFactory getDaoFactory() {
        return (daoFactory == null) ? new DAOFactory() : daoFactory;
    }

    public SuperDAO getDAO(DAOTypes types){
        switch (types){
            case APPOINTMENT:
                return new AppointmentDAOImpl();
            case ATTENDANCE:
                return new AttendanceDAOImpl();
            case CUSTOMER:
                return new CustomerDAOImpl();
            case EMPLOYEE:
                return new EmployeeDAOImpl();
            case EMPLOYEE_DETAILS:
                return new Employee_detailsDAOImpl();
            case INVENTORY:
                return new InventoryDAOImpl();
            case INVENTORY_DETAILS:
                return new Inventory_detailsDAOImpl();
            case INVENTORY_ORDER:
                return  new InventoryOrderDAOImpl();
            case INVENTORY_ORDER_DETAILS:
                return new InventoryOrderDetailsDAOImpl();
            case PAYMENT:
                return new PaymentDAOImpl();
            case QUERY:
                return new QueryDAOImpl();
            case SERVICE:
                return new ServiceDAOImpl();
            case SERVICE_DETAILS:
                return new Service_detailsDAOImpl();
            case SUPPLIER:
                return new SupplierDAOImpl();
            case USER:
                return new UserDAOImpl();
            default:
                return null;
        }
    }
}
