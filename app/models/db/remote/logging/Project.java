package models.db.remote.logging;

import io.ebean.Model;
import lombok.Data;
import models.db.user.User;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "remote_logging_project")

public class Project extends Model {

    @Id
    Long id;
    @Column(nullable = false)
    String name;
    @Column(nullable = false)
    @OneToMany
    User manager;
    @Column(columnDefinition = "tinyint(1) default 0")
    Boolean isActive;
    String description;
    @Column(columnDefinition = "DATETIME DEFAULT NOW()")
    Date cratedDate;
    Date startDate;
    Date endDate;
}
