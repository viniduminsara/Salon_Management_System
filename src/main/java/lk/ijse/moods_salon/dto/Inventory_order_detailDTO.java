package lk.ijse.moods_salon.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString

public class Inventory_order_detailDTO {
    private String inventoryId;
    private Integer qty;
}
