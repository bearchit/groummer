package controllers;
 
import models.*;
 
public class Security extends Secure.Security {
	
    static boolean authenticate(String nickname, String password) {
    	return User.connect(nickname, password) != null;
    }
    
//    static boolean check(String profile) {
//    	return false;
//    }
    
    static void onDisconnected() {
        Application.index();
    }
    
    static void onAuthenticated() {
    	Application.index();
    }
    
    
}