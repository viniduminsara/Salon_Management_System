package lk.ijse.gdse.dto.tm;

import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString

public class InventoryOrderTM {
    private String orderId;
    private String supplier;
    private Date date;
    private String user;
}
