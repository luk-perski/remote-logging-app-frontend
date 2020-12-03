package models.db.remote.logging;

import io.ebean.Model;
import lombok.Data;
import models.db.user.User;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "remote_logging_team")
public class Team extends Model {

    @Id
    Long id;
    @Column(nullable = false)
    String name;
    @OneToMany
    @Column(nullable = false)
    User manager;
    @Column(columnDefinition = "DATETIME DEFAULT NOW()")
    Date cratedDate;
}
