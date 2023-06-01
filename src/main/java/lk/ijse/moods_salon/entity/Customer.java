package lk.ijse.moods_salon.entity;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString

public class Customer {
    private String customerId;
    private String name;
    private String gmail;
    private String contact;
    private String address;
}
