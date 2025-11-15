package org.example.Service;

import org.example.DTO.User;
import org.example.Exception.ExitPageException;
import org.example.SessionManager;
import org.example.Sha256Util;
import org.example.db.UserDAO;

public class LoginService {
    UserDAO userDAO = new UserDAO();
    private SessionManager sessionManager = SessionManager.INSTANCE;
    public Boolean isDuplicated(String userId){
        if(userDAO.getUserById(userId)==null){
            return false;
        }else{
            return true;
        }
    }
    public User login(String userId,String password){
        return userDAO.logIn(userId,Sha256Util.hash(password));
    }
    public Boolean signIn(String userId,String password){
        return userDAO.insertUser(User.builder()
                .id(userId)
                .passwordHash(Sha256Util.hash(password))
                .build());
    }
    public Boolean updateLicense(String id,boolean license){
        return userDAO.updateLicense(id,license);
    }
    /** 로그인 되어있는지 확인*/
    public void checkLogIn(){
        if(sessionManager.getUser()==null) {
            System.out.println("로그인이 필요한 서비스");
            throw new ExitPageException();
        }
    }
    /** 로그아웃 되어있는지 확인*/
    public void checkLogOut(){
        if(sessionManager.getUser()!=null) {
            System.out.println("로그아웃이 필요한 서비스");
            throw new ExitPageException();
        }
    }
    /** 면허 있는지 확인*/
    public void checkLicense(){
        if(!sessionManager.getUser().isLicenseVerified()){
            System.out.println("면허 인증이 필요한 서비스");
            throw new ExitPageException();
        }
    }

}
