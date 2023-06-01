package lk.ijse.moods_salon.entity;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString

public class Service {
    private String serviceId;
    private String description;
    private Double price;
    private String category;
}
