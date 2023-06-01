package lk.ijse.moods_salon.entity;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString

public class Inventory_details {
    private String appointmentId;
    private String inventoryId;
    private Integer usedQty;
}
