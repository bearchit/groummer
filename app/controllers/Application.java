package controllers;

import play.*;
import play.libs.Crypto;
import play.mvc.*;

import java.util.*;

import models.*;

public class Application extends Controller {

  @Before
  static void addDefaults() {
    renderArgs.put("appName", Play.configuration.getProperty("application.name"));
  }
  
  @Before
  static void setConnectedUser() {
      if(Security.isConnected()) {
          User user = User.find("byNickname", Security.connected()).first();
          renderArgs.put("current_user", user);
          List<Status> all_status = Status.find("order by status").fetch();
          renderArgs.put("all_status", all_status);
      }
  }
  
  public static void index() {
    List<Post> posts = Post.find("order by createdAt desc").fetch();
    List<User> users = User.find("order by createdAt desc").fetch();
    render(posts, users);
  }
  
  public static void post(String nickname, String content) {
    User user = User.find("byNickname", Security.connected()).first();
    if(user != null){
    	new Post(user, content).save();
    }
    index();
  }
  
}
