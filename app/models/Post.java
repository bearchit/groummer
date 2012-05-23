package models;

import java.util.*;
import javax.persistence.*;

import play.db.jpa.*;

@Entity
public class Post extends Model {

  @ManyToOne
  public User author;

  @Lob
  public String content;

  @OneToMany(mappedBy="post", cascade=CascadeType.ALL)
  public List<Comment> comments;

  public Date createdAt;

  public Post(User author, String content) {
    this.author = author;
    this.content = content;
    this.createdAt = new Date();
  }

  public Post addComment(User author, String content) {
    Comment newComment = new Comment(this, author, content).save();
    this.comments.add(newComment);
    this.save();
    return this;
  }
}
