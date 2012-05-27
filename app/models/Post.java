package models;

import java.util.*;
import javax.persistence.*;

import play.db.jpa.*;

@Entity
public class Post extends Model {

  @ManyToOne
  public User user;

  @Lob
  public String content;

  @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
  public List<Comment> comments;

  public Date createdAt;

  public Post(User user, String content) {
    this.comments = new ArrayList<Comment>();
    this.user = user;
    this.content = content;
    this.createdAt = new Date();
  }

  public Post addComment(User user, String content) {
    Comment newComment = new Comment(this, user, content).save();
    this.comments.add(newComment);
    this.save();
    return this;
  }
}
