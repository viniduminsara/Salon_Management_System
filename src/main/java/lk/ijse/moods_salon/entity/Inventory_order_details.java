package lk.ijse.moods_salon.entity;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString

public class Inventory_order_details {
    private String OrderId;
    private String inventoryId;
    private Integer qty;
}
