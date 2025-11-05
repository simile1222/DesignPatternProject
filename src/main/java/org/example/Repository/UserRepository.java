package org.example.Repository;

import org.example.DTO.User;

import java.util.ArrayList;
import java.util.List;

public class UserRepository {

    private static UserRepository instance;

    public static UserRepository getInstance() {
        if (instance==null){
            instance = new UserRepository();
        }
        return instance;
    }
    public Boolean isThereSameName(){
        return null;
    }
    public User findUser(String userId,String password){
        return null;
    }
    public Boolean upsertUser(User user){return null;}
}
