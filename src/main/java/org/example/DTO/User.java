package org.example.DTO;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class User {
    private String userId;
    private String password;
    private String licenceId;
    private String carId;
}
