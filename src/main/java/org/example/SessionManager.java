package org.example;

import lombok.Data;
import lombok.Getter;
import org.example.DTO.Car;
import org.example.DTO.User;
@Data
public class SessionManager {
    @Getter
    private static SessionManager instance;
    private User user;
    private Car car;

}
