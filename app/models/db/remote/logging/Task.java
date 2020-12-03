package models.db.remote.logging;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Entity
@Table(name = "remote_logging_task")
public class Task {

    @Id
    Long iD;
    @Column(nullable = false)
    String name;
    @Column(nullable = false)
    Long projectId;
    @Column(nullable = false)
    Long creatorId;
    @Column(nullable = false)
    int priority;
    Long assigneeId;
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
