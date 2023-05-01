package lk.ijse.gdse.dto.tm;

import com.jfoenix.controls.JFXButton;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString

public class OrderCartTM {
    private String supplierId;
    private String inventoryId;
    private String name;
    private Double unitPrice;
    private Integer qty;
    private JFXButton button;
}
