package models;

import java.util.*;

import javax.persistence.*;

import play.Logger;
import play.db.jpa.*;

@Entity
public class Comment extends Model {

  @ManyToOne
  public User user;

  @Lob
  public String content;

  @ManyToOne
  public Post post;

  @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL)
  public List<CommentLike> comment_likes;
  
  public Date createdAt;

  public Comment(Post post, User user, String content) {
    this.post = post;
    this.user = user;
    this.content = content;
    this.createdAt = new Date();
  }
  
  public Comment addCommentLike(User user){
	  new CommentLike(user, this).save();
	  return this;
  }
  
}
