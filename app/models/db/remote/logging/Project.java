package models.db.remote.logging;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.ebean.Model;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import models.db.user.User;
import utils.Constant;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "remote_logging_project")

public class Project extends Model {

    @Id
    Long id;
    @Column(nullable = false)
    String name;
    @JoinColumn(name = "manager_id", nullable = false)
    @ManyToOne
    User manager;
    @Column(columnDefinition = "tinyint(1) default 0")
    Boolean isActive;
    String description;
    @Column(nullable = false, columnDefinition = "DATETIME DEFAULT NOW()")
    @JsonFormat(pattern = Constant.DATE_FORMAT)
    Date cratedDate;
    @JsonFormat(pattern = Constant.DATE_FORMAT)
    Date startDate;
    @JsonFormat(pattern = Constant.DATE_FORMAT)
    Date endDate;
}
