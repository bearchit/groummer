package models;

import java.util.*;
import javax.persistence.*;

import play.db.jpa.*;

@Entity
public class Status extends Model {
  public String status;

  public Status(String status) {
    this.status = status;
  }
}
