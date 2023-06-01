package lk.ijse.moods_salon.entity;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString

public class Supplier {
    private String supplierId;
    private String name;
    private String contact;
    private String address;
}
