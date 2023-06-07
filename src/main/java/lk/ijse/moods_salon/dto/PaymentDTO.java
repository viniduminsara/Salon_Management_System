package lk.ijse.moods_salon.dto;

import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString

public class PaymentDTO {

    private String paymentId;
    private Double amount;
    private LocalDate date;
    private String userId;
    private String appointmentId;

}
