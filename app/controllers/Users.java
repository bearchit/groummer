package controllers;

import play.*;
import play.mvc.*;

import java.io.File;
import java.util.*;

import play.db.jpa.*;
import models.*;


public class Users extends Controller {

        @Before
        static void setConnectedUser() {
                if(Security.isConnected()) {
                        User user = User.find("byNickname", Security.connected()).first();
                        renderArgs.put("current_user", user);
                        List<Status> all_status = Status.find("order by status").fetch();
                        renderArgs.put("all_status", all_status);
                }
        }

    public static void sign_up(String nickname, String fullname, String pwd, String pwd_confirm ) {
        User user = User.find("byNickname", nickname).first();

        if(!pwd.equals(pwd_confirm)) {
            flash("message", "비밀번호가 서로 다릅니다");
        } else {
            if(user == null && !nickname.isEmpty() ){
                Date current_time = new java.util.Date();
                new User(nickname, fullname, pwd, current_time).save();
                flash("message", "회원가입이 완료되었습니다. 로그인 해주세요");
            } else if(nickname.isEmpty()){
                flash("message", "닉네임을 적어주세요");
            } else{
                flash("message", "이미 닉네임이 존재합니다");
            }
        }

        Application.index();
    }

        public static void status_select(Long status_id){
                User user = User.find("byNickname", Security.connected()).first();
                if(status_id != null ){
                        Status status = Status.findById(status_id);
                        user.status = status;
                        Date current_time = new java.util.Date();
                        user.statusUpdatedAt = current_time;
                        user.updatedAt = current_time;
                        user.save();

                        // 감정 변화 포스팅
                        User groummer = User.groummer();

                        String userName = user.nickname;
                        if(!user.fullname.isEmpty()) {
                          userName += "(" + user.fullname + ")";
                        }

                        new Post(groummer, userName + "님의 기분이 " + status.status + "(으)로 바뀌었습니다").save();
                }
                Application.index();
        }

        public static void delete_status(){
                User user = User.find("byNickname", Security.connected()).first();
                user.status = null;
                user.statusUpdatedAt = null;
                user.updatedAt = new java.util.Date();
                user.save();
                Application.index();
        }

        public static void userPhoto(long id) {
           final User user = User.findById(id);
           notFoundIfNull(user);
           response.setContentTypeIfNotSet(user.profileImage.type());
           if( user.profileImage.get() != null ){
                   renderBinary(user.profileImage.get());
           }else{
                   File default_image = new File("public/images/default_profile.png");
                   renderBinary(default_image);
           }

        }

        public static void profileImage_upload(Blob profileImage){
                User user = User.find("byNickname", Security.connected()).first();
                user.profileImage = profileImage;
                user.save();
                Application.index();
        }


}
