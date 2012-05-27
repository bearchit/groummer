package controllers;

import play.*;
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
    render(posts);
  }

  public static void post(String email, String content) {
    User user = (User)User.find("byEmail", email).fetch().get(0);
    new Post(user, content).save();
    index();
  }

}
