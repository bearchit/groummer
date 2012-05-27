package controllers;

import play.*;	
import play.mvc.*;

import java.util.*;

import models.*;

public class Application extends Controller {

    public static void index() {
    	List<Post> posts = Post.find("order by createdAt desc").fetch();
    	render(posts);
    }

}