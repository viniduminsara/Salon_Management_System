package lk.ijse.moods_salon.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString

public class InventoryOrderDetailDTO {
    private String orderId;
    private String inventoryId;
    private Integer qty;
}
