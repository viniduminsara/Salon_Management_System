package lk.ijse.gdse.dto.tm;

import com.jfoenix.controls.JFXButton;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString

public class AppointmentCartTM {
    private String service;
    private Double price;
    private String employee;
    private String inventory;
    private Integer qty;
    private JFXButton button;
}
