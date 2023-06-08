package lk.ijse.moods_salon.bo.factory;

import lk.ijse.moods_salon.bo.SuperBO;
import lk.ijse.moods_salon.bo.custom.impl.*;

public class BOFactory {
    private static BOFactory boFactory;

    public BOFactory() {
    }

    public static BOFactory getBoFactory() {
        return (boFactory == null) ? new BOFactory() : boFactory;
    }

    public SuperBO getBO(BOTypes types){
        switch (types){
            case APPOINTMENT:
                return new AppointmentBOImpl();
            case CREATEACCOUNT:
                return new CreateAccountBOImpl();
            case CUSTOMER:
                return new CustomerBOImpl();
            case EMPLOYEE:
                return new EmployeeBOImpl();
            case HOME:
                return new HomeBOImpl();
            case INVENTORY:
                return new InventoryBOImpl();
            case LOGIN:
                return new LoginBOImpl();
            case PAYMENT:
                return new PaymentBOImpl();
            case PROFILE:
                return new ProfileBOImpl();
            case SERVICE:
                return new ServiceBOImpl();
            case SUPPLIER:
                return new SupplierBOImpl();
            default:
                return null;
        }
    }
}
