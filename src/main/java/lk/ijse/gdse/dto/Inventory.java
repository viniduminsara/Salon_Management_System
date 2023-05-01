package lk.ijse.gdse.dto;

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
