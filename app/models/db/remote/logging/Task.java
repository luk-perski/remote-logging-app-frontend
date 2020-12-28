package models.db.remote.logging;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.ebean.Model;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import models.db.user.User;
import utils.Constant;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Builder
@Entity
@Table(name = "remote_logging_task")
public class Task extends Model {

    @Id
    Long id;
    @JoinColumn(name = "project_id", nullable = false)
    @ManyToOne
    Project project;
    @Column(nullable = false)
    String name;
    @JoinColumn(name = "creator_id", nullable = false)
    @ManyToOne
    User creator;
    @Column(nullable = false)
    int priority;
    @ManyToOne
    @JoinColumn(name = "assignee_id")
    User assignee;
    @ManyToOne
    @JoinColumn(name = "category_id")
    Category category;
    @JsonFormat(pattern = Constant.DATE_FORMAT)
    @Column(nullable = false, columnDefinition = "DATETIME DEFAULT NOW()")
    Date cratedDate;
    String description;
    Long estimate;
    Date resolvedDate;
    /**
     * Time in ms.
     */
    Long timeSpent;
    Date runStart;
    Date runEnd;
    int taskType;
}
