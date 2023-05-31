package lk.ijse.moods_salon.dto.tm;

import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString

public class PaymentTM {
    private String paymentId;
    private Date date;
    private String user;
    private Double amount;
}
