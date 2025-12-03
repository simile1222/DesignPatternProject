package org.example.page;


import org.example.DTO.User;
import org.example.Exception.ExitPageException;
import org.example.InputUtil;
import org.example.Service.LoginService;
import org.example.SessionManager;


public class LoginPage implements Page{
    public LoginPage(){
        loginService = LoginService.getInstance();
        sessionManager = SessionManager.INSTANCE;

    }
    private LoginService loginService;
    private SessionManager sessionManager;
    @Override
    public void showPage() {
        while(true){
            int input =InputUtil.getInt("로그인","로그아웃","회원가입","내정보");
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
            String userId = InputUtil.getLine("아이디를 입력하세요 :");
            String password = InputUtil.getLine("비밀번호를 입력하세요 :");
            user = loginService.login(userId,password);
        }catch (ExitPageException e){
            return;
        }
        if(user!=null){
            sessionManager.setUser(user);
            System.out.println("=============== Wellcome to ===============\n");
            System.out.println("로그인이 완료되었습니다 굿카(GoodCar)에 어서오세요!\n");
            System.out.println("================= GoodCar ==================");
        } else {
            System.out.println("잘못된 회원정보입니다.");
        }
    }
    private void logout(){
        try{
            loginService.checkLogIn();
            sessionManager.setUser(null);
            System.out.println("로그아웃 되었습니다. 이용해주셔서 감사합니다.");
            System.out.println("================= Goodbye ==================");
        }catch (ExitPageException e){
            return;
        }
    }
    private void signIn(){
        try{
            loginService.checkLogOut();
            String userId = InputUtil.getLine("아이디를 입력하세요 :");
            if(loginService.isDuplicated(userId)){
                System.out.println("중복된 아이디입니다");
                return;
            }
            String password = InputUtil.getLine("비밀번호를 입력하세요 :");
            if(loginService.signIn(userId,password)){
                System.out.println("회원가입 되었습니다!");
            }else{
                System.out.println("[회원 가입 실패] 다시 시도해주세요");
            }
        }catch (ExitPageException e){
            return;
        }
    }
    private void setLicense(){
        try{
            loginService.checkLogIn();
            String license = InputUtil.getLine("면허 번호를 등록하세요 :");
            if(license.isEmpty()){
                System.out.println("[면허 번호 입력 실패] 다시 시도해주세요");
            }
            if(loginService.updateLicense(sessionManager.getUser().getId(), true)){
                System.out.println("면허가 저장 완료!");
            }else {
                System.out.println("면허가 저장되지 않았습니다. 다시 시도해주세요");
            }
        }catch (ExitPageException e){
            return;
        }
    }
}
