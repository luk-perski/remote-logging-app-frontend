package models.db.remote.logging;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.ebean.Model;
import lombok.Builder;
import lombok.Data;
import models.db.user.User;
import utils.Constant;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

//todo class to link user to worklog registered on task
@Data
@Entity
@Builder
@Table(name = "remote_logging_log_work")

public class LogWork extends Model {
    @Id
    Long id;
    @NotNull
    Long timeSpend;
    @Column(length = 1000)
    String comment;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "task_id")
    Task task;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;
    @JsonFormat(pattern = Constant.DATE_FORMAT)
    @Column(nullable = false, columnDefinition = "DATETIME DEFAULT NOW()")
    Date cratedDate;
}
