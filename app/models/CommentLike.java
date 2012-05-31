package models;

import java.util.*;
import javax.persistence.*;

import play.db.jpa.*;

@Entity
public class CommentLike extends Model {

	@ManyToOne
	public User user;
	
	@ManyToOne
	public Comment comment;
	
	public CommentLike(User user, Comment comment) {
		this.user = user;
		this.comment = comment;
	}
}
