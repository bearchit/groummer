package controllers;

import play.*;
import play.mvc.*;

import java.util.*;

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
	
	public static void sign_up(String nickname, String fullname, String pwd ) {
		User user = User.find("byNickname", nickname).first();
		String success = "";
		if(user == null){
			new User(nickname, fullname, pwd).save();
			success = "회원가입이 완료되었습니다. 로그인 해주세요.";
		}else{
			success = "닉네임이 존재합니다";
		}
		renderTemplate("Secure/login.html", success);
	}
	
	public static void status_select(Long status_id){
		User user = User.find("byNickname", Security.connected()).first();
		if(status_id != null ){
			Status status = Status.findById(status_id);
			user.status = status;
			user.save();
		}
		Application.index();
	}
	
	public static void delete_status(){
		User user = User.find("byNickname", Security.connected()).first();
		user.status = null;
		user.save();
		Application.index();
	}
	
}
