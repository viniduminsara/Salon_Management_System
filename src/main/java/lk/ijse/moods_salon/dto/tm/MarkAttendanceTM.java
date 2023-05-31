package lk.ijse.moods_salon.dto.tm;

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
