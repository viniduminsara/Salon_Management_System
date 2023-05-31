package lk.ijse.moods_salon.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString


public class CustomerDTO {
    private String customerId;
    private String name;
    private String gmail;
    private String contact;
    private String address;
}
