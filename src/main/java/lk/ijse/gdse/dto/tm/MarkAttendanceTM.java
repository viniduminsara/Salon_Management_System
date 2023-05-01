package lk.ijse.gdse.dto.tm;

import com.jfoenix.controls.JFXCheckBox;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString

public class MarkAttendanceTM {

    private String empId;
    private String empName;
    private String job;
    private String status;
}
