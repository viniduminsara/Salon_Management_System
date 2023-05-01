package lk.ijse.gdse.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString

public class AppointmentInventory {
    private String appointmentId;
    private String inventoryId;
    private Integer usedQty;
}
