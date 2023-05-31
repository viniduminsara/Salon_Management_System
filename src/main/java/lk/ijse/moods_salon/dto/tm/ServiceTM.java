package lk.ijse.moods_salon.dto.tm;

import com.jfoenix.controls.JFXButton;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString

public class ServiceTM {
    private String serviceId;
    private String description;
    private Double price;
    private String category;
    private JFXButton button;
}
