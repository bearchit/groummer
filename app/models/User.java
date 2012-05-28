package models;

import java.util.*;
import javax.persistence.*;

import models.User;

import play.db.jpa.*;
import play.libs.Crypto;

@Entity
public class User extends Model {
  public String nickname;
  public String password;
  public String fullname;

  // Status ID needed
  public Date statusUpdatedAt;

  public Blob profileImage;
  public Date createdAt;
  public Date updatedAt;
  
  @ManyToOne
	public Status status;

  public User(String nickname, String fullname, String password) {
    this.nickname = nickname;
    this.password = Crypto.passwordHash(password);
    this.fullname = fullname;
  }

  public static User connect(String nickname, String password) {
	String hash_pwd = Crypto.passwordHash(password);
    return find("byNicknameAndPassword", nickname, hash_pwd).first();
  }
}
