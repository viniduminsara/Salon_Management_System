package lk.ijse.gdse.dto.tm;

import com.jfoenix.controls.JFXButton;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString

public class InventoryTM {
    private String inventoryId;
    private String name;
    private Integer qtyOnHand;
    private Double unitPrice;
    private JFXButton button;
}
