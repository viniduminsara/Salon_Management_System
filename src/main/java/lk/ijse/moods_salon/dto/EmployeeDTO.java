package lk.ijse.moods_salon.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString

public class EmployeeDTO {
    private String employeeId;
    private String name;
    private String address;
    private String contact;
    private String jobRole;
    private Double salary;
}
