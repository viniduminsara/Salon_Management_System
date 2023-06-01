package lk.ijse.moods_salon.entity;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString

public class Inventory {
    private String inventoryId;
    private String name;
    private Integer qtyOnHand;
    private Double unitPrice;
}
