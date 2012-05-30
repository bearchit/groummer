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

  public User(String nickname, String fullname, String pwd, Date current_time) {
    this.nickname = nickname;
    this.password = Crypto.passwordHash(pwd);
    this.fullname = fullname;
    this.createdAt = current_time;
  }

  public static User connect(String nickname, String pwd) {
    String password = Crypto.passwordHash(pwd);
    return find("byNicknameAndPassword", nickname, password).first();
  }

  public static User groummer() {
    return find("byNickname", "groummer").first();
  }

  public static User anonymous() {
    return find("byNickname", "anonymous").first();
  }

  public Boolean isSystemUser() {
    return this.nickname == "groummer" || this.nickname == "anonymous";
  }
}
