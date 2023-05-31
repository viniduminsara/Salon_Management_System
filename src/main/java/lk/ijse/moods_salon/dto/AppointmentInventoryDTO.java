package lk.ijse.moods_salon.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString

public class AppointmentInventoryDTO {
    private String appointmentId;
    private String inventoryId;
    private Integer usedQty;
}
