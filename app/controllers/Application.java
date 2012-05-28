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

  public static void index() {
    List<Post> posts = Post.find("order by createdAt desc").fetch();
    List<User> users = User.find("order by createdAt desc").fetch();
    render(posts, users);
  }
  
  public static void post(String nickname, String content) {
    User user = (User)User.find("byNickname", nickname).fetch().get(0);
    new Post(user, content).save();
    index();
  }
  
  public static void sign_up(String nickname, String fullname, String pwd ) {
	  User user = new User(nickname, fullname, pwd).save();
	  List<Status> all_status = Status.find("order by status").fetch();
	  renderTemplate("Application/sign_on.html", user, all_status);
  }
	  
  public static void sign_in(String nickname, String pwd ) {
	  User user = User.connect(nickname, pwd);
	  if(user != null) {
		  List<Status> all_status = Status.find("order by status").fetch();
		  renderTemplate("Application/sign_on.html", user, all_status);  
	  } else{
		  renderTemplate("Application/sign_in.html");
	  }
  }
  
  public static void sign_out() {
	  renderTemplate("Application/sign_in.html");
  }
  

}
