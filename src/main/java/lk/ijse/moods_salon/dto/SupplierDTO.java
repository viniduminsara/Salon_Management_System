package lk.ijse.moods_salon.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString

public class SupplierDTO {
    private String supplierId;
    private String name;
    private String contact;
    private String address;
}
