package lk.ijse.moods_salon.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString

public class InventoryDTO {
    private String inventoryId;
    private String name;
    private Integer qtyOnHand;
    private Double unitPrice;
}
