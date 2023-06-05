package lk.ijse.moods_salon.dto;

import lombok.*;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString

public class InventoryOrderDTO {

    private String orderId;
    private Date date;
    private String supplierId;
    private String userId;

}
