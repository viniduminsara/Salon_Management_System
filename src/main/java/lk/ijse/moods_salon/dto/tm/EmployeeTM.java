package lk.ijse.moods_salon.dto.tm;

import com.jfoenix.controls.JFXButton;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString

public class EmployeeTM {
    private String employeeId;
    private String name;
    private String jobRole;
    private Double salary;
    private JFXButton button;
    private String contact;
    private String address;
}
