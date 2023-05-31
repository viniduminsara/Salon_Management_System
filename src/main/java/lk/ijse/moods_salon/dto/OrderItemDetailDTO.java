package lk.ijse.moods_salon.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString

public class OrderItemDetailDTO {
    private String inventoryId;
    private Integer qty;
}
