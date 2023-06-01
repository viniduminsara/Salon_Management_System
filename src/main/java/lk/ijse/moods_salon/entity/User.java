package lk.ijse.moods_salon.entity;

import lombok.*;
import java.io.InputStream;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString

public class User {
    private String userId;
    private String fullName;
    private String userName;
    private String type;
    private String gmail;
    private String password;
    private InputStream inputStream;
    private String receipt_folder;
}
