package lk.ijse.gdse.dto.tm;

import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString

public class UpcomingAppointmentTM {
    private Date date;
    private String time;
    private String customerName;
}
