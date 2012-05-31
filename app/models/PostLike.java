package models;

import java.util.*;
import javax.persistence.*;

import play.db.jpa.*;

@Entity
public class PostLike extends Model {
	@ManyToOne
	public User user;
	
	@ManyToOne
	public Post post;
	
	public PostLike(User user, Post post) {
		this.user = user;
		this.post = post;
	}
}
