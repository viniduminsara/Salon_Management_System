package lk.ijse.moods_salon.entity;

import lombok.*;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString

public class Payment {
    private String paymentId;
    private Double amount;
    private Date date;
    private String userId;
    private String appointmentId;
}
