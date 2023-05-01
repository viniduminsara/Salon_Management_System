package lk.ijse.gdse.dto.tm;

import com.jfoenix.controls.JFXButton;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString

public class SupplierTM {
    private String supplierId;
    private String name;
    private String contact;
    private String address;
    private JFXButton button;
}
