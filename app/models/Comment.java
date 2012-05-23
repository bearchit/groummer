package models;

import java.util.*;
import javax.persistence.*;

import play.db.jpa.*;

@Entity
public class Comment extends Model {

  @ManyToOne
  public User author;

  @Lob
  public String content;

  @ManyToOne
  public Post post;

  public Date createdAt;

  public Comment(Post post, User author, String content) {
    this.post = post;
    this.author = author;
    this.content = content;
    this.createdAt = new Date();
  }
}
