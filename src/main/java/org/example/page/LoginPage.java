package org.example.page;

import lombok.AllArgsConstructor;
import org.example.DTO.User;
import org.example.InputUtil;
import org.example.Service.LoginService;
import org.example.SessionManager;


public class LoginPage implements Page{
    public LoginPage(LoginService loginService){
        this.loginService = loginService;
        sessionManager = SessionManager.();
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
                case 3 -> signin();
                case 4 -> setLicense()
                case 0 -> System.exit(0);
            }
        }
    }
    private void login(){
        String userId = InputUtil.getLine("아이디를 입력하시오");
        String password = InputUtil.getLine("비밀번호를 입력하시오");
        User user = loginService.login(userId,password);
        if(user!=null){
            sessionManager.setUser(user);
        }else{
            System.out.println("잘못된 회원정보");
        }
    }
    private void logout(){
        if(sessionManager.getUser()==null){
            System.out.println("이미 로그아웃 상태이다");
        }else{
            sessionManager.setUser(null);
            System.out.println("로그아웃 되었다");
        }
    }
    private void signIn(){
        String userId = InputUtil.getLine("아이디를 입력하시오");
        String password = InputUtil.getLine("비밀번호를 입력하시오");
        if(loginService.signIn(userId,password)){
            System.out.println("회원가입 되었다");
        }else{

        }
    }
}
