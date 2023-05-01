package lk.ijse.gdse.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString

public class Employee {
    private String employeeId;
    private String name;
    private String address;
    private String contact;
    private String jobRole;
    private Double salary;
}
