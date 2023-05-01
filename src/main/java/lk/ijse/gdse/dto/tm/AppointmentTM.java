package lk.ijse.gdse.dto.tm;

import com.jfoenix.controls.JFXButton;
import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString

public class AppointmentTM {
    private String appointmentId;
    private String customerId;
    private Date date;
    private String time;
    private String status;
    private JFXButton button;
}
