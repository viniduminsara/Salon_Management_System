package lk.ijse.moods_salon.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString

public class ServiceDTO {
    private String serviceId;
    private String description;
    private Double price;
    private String category;
}
