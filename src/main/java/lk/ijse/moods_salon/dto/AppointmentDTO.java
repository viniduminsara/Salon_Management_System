package lk.ijse.moods_salon.dto;

import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString

public class AppointmentDTO {
    private String appointmentId;
    private Date date;
    private String time;
    private String status;
    private String customerId;
}
