package org.example.Service;

import org.example.DTO.User;
import org.example.Repository.UserRepository;
import org.example.SessionManager;

public class LoginService {
    UserRepository userRepository = new UserRepository();
    public Boolean isDuplicated(String userId){
        return userRepository.isThereSameName(userId);
    }
    public User login(String userId,String password){
        return userRepository.findUser(userId,password);
    }
    public Boolean signIn(String userId,String password){
        return null;
    }
    public void updateUser(){
        SessionManager sessionManager=SessionManager.INSTANCE;

        if(sessionManager.getUser()==null){
            return;
        }
        userRepository.upsertUser(sessionManager.getUser());
    }
}
