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

  public static void post(String content) {
    User user = User.find("byNickname", Security.connected()).first();
    if(user != null) {
        new Post(user, content).save();
        flash("message", "등록 완료");
    }else{
    	flash("message", "글쓰기는 로그인 후에 가능합니다");
    }
    index();
  }

  public static void postComment(Long postId, String content) {
    User user = User.find("byNickname", Security.connected()).first();
    if(user != null) {
      Post post = Post.findById(postId);
      post.addComment(user, content);
      flash("message", "등록 완료");
    }else{
    	flash("message", "덧글은 로그인 후 가능합니다");
    }
    index();
  }
  
  public static void PostLike(Long postId) {
	User user = User.find("byNickname", Security.connected()).first();
	Post post = Post.findById(postId);
	PostLike post_like = PostLike.find("byUserAndPost",user, post).first();
	if(user != null && post_like == null && post.user != user){ //내가 추천한적 없고 글도 내가 안쓴 정상 flow
		post.addPostLike(user);
	}else if(post.user == user){//본인 글
		flash("message", "본인 글에는 추천할 수 없습니다"); 
  	}else if(post_like != null){ //추천한적 있는 경우
  		flash("message", "추천은 한번만 가능합니다");
  	}else{
  		flash("message", "추천은 로그인 후 가능합니다");
  	}
    index();
  }
  
  public static void CommentLike(Long commentId) {
	User user = User.find("byNickname", Security.connected()).first();
	Comment comment = Comment.findById(commentId);
	CommentLike comment_like = CommentLike.find("byUserAndComment",user, comment).first();
	if(comment_like == null && comment.user != user){ //내가 추천한적 없고 글도 내가 안쓴 정상 flow
		comment.addCommentLike(user);
	}else if(comment.user == user){//본인 글
		flash("message", "본인 글에는 추천할 수 없습니다"); 
  	}else if(comment_like != null){ //추천한적 있는 경우
  		flash("message", "추천은 한번만 가능합니다");
  	}else{
  		flash("message", "추천은 로그인 후 가능합니다");
  	}
	
    index();
  }

}
