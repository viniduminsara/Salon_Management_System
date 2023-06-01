package lk.ijse.moods_salon.entity;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString

public class Attendance {
    private String attendanceId;
    private String date;
    private String status;
    private String employeeId;
}
