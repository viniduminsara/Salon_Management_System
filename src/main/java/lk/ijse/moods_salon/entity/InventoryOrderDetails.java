package lk.ijse.moods_salon.entity;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString

public class InventoryOrderDetails {
    private String OrderId;
    private String inventoryId;
    private Integer qty;
}
