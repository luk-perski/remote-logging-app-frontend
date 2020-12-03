package models.db.remote.logging;

import io.ebean.Model;
import lombok.Data;
import models.db.user.User;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "remote_logging_task")
public class Task extends Model {

    @Id
    Long id;
    @Column(nullable = false)
    String name;
    @Column(nullable = false)
    Long projectId;
    @OneToMany
    @Column(nullable = false)
    User creator;
    @Column(nullable = false)
    int priority;
    @OneToMany
    User assignee;
    int category;
    @Column(columnDefinition = "DATETIME DEFAULT NOW()")
    Date cratedDate;
    String description;
    Long estimate;
    Date resolvedDate;
    Long timeSpent;
    Date runStart;
    Date runEnd;
}
