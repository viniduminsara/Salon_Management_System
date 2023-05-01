package lk.ijse.gdse.dto;

import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString

public class Appointment {
    private String appointmentId;
    private Date date;
    private String time;
    private String status;
    private String customerId;
}
