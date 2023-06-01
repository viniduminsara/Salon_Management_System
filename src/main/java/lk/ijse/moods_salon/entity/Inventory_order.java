package lk.ijse.moods_salon.entity;

import lombok.*;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString

public class Inventory_order {
    private String orderId;
    private Date date;
    private String supplierId;
    private String userId;
}
