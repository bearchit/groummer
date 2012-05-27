package models;

import java.util.*;
import javax.persistence.*;

import play.db.jpa.*;

@Entity
public class Comment extends Model {

  @ManyToOne
  public User user;

  @Lob
  public String content;

  @ManyToOne
  public Post post;

  public Date createdAt;

  public Comment(Post post, User user, String content) {
    this.post = post;
    this.user = user;
    this.content = content;
    this.createdAt = new Date();
  }
}
