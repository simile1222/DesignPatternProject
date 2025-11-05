package org.example.Service;

import org.example.DTO.User;
import org.example.Repository.UserRepository;

public class LoginService {
    UserRepository userRepository = new UserRepository();
    public User login(String userId,String password){
        return userRepository.findUser(userId,password);
    }
    public Boolean signIn(String userId,String password){
        return null;
    }
}
