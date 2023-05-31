package lk.ijse.moods_salon.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString


public class AttendanceDTO {
    private String attendanceId;
    private String date;
    private String status;
    private String employeeId;
}
