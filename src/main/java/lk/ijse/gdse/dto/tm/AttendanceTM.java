package lk.ijse.gdse.dto.tm;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString

public class AttendanceTM {
    private String employeeId;
    private String name;
    private String jobRole;
    private String attendance;
}
