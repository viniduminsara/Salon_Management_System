package lk.ijse.moods_salon.dto.tm;

import com.jfoenix.controls.JFXButton;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString

public class CustomerTM {
    private String customerId;
    private String name;
    private String address;
    private String contact;
    private String gmail;
    private JFXButton remove;
}
