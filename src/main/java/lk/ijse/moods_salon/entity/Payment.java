package lk.ijse.moods_salon.entity;

import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString

public class Payment {
    private String paymentId;
    private Double amount;
    private LocalDate date;
    private String userId;
    private String appointmentId;
}
