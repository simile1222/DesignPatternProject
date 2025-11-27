package org.example;

import lombok.Data;
import lombok.Getter;
import org.example.DTO.Car;
import org.example.DTO.User;


public enum SessionManager {
    INSTANCE;

    private User user;
    private Car car;

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
