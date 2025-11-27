package org.example.page;


import org.example.DTO.User;
import org.example.Exception.ExitPageException;
import org.example.InputUtil;
import org.example.Service.LoginService;
import org.example.SessionManager;


public class LoginPage implements Page{
    public LoginPage(LoginService loginService){
        this.loginService = loginService;
        sessionManager = SessionManager.INSTANCE;

    }
    private LoginService loginService;
    private SessionManager sessionManager;
    @Override
    public void showPage() {
        while(true){
            int input =InputUtil.getInt("로그인","로그아웃","회원가입","면허등록");
            switch (input) {
                case 1 -> login();
                case 2 -> logout();
                case 3 -> signIn();
                case 4 -> setLicense();
                case 0 -> {
                    return;
                }
            }
        }
    }
    private void login(){
        User user;
        try{
            loginService.checkLogOut();
            String userId = InputUtil.getLine("아이디를 입력하시오");
            String password = InputUtil.getLine("비밀번호를 입력하시오");
            user = loginService.login(userId,password);
        }catch (ExitPageException e){
            return;
        }
        if(user!=null){
            sessionManager.setUser(user);
            System.out.println("로그인이 완료되었다");
        } else {
            System.out.println("잘못된 회원정보");
        }
    }
    private void logout(){
        try{
            loginService.checkLogIn();
            sessionManager.setUser(null);
            System.out.println("로그아웃 되었다");
        }catch (ExitPageException e){
            return;
        }
    }
    private void signIn(){
        try{
            loginService.checkLogOut();
            String userId = InputUtil.getLine("아이디를 입력하시오");
            if(loginService.isDuplicated(userId)){
                System.out.println("중복된 아이디");
                return;
            }
            String password = InputUtil.getLine("비밀번호를 입력하시오");
            if(loginService.signIn(userId,password)){
                System.out.println("회원가입 되었다");
            }else{
                System.out.println("회원 가입 실패");
            }
        }catch (ExitPageException e){
            return;
        }
    }
    private void setLicense(){
        try{
            loginService.checkLogIn();
            String license = InputUtil.getLine("면허 번호를 등록하시오");
            if(license.isEmpty()){
                System.out.println("잘못된 면허번호");
            }
            if(loginService.updateLicense(sessionManager.getUser().getId(), true)){
                System.out.println("면허가 저장되었다");
            }else {
                System.out.println("면허가 저장되지 않았다");
            }
        }catch (ExitPageException e){
            return;
        }
    }
}
