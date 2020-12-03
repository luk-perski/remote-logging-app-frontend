package models.db.remote.logging;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Date;

@Data
@Entity
@Table(name = "remote_logging_project")

public class Project {

    @Id
    Long id;
    @Column(nullable = false)
    String name;
    ArrayList<String> taskList;
    @Column(nullable = false)
    Long managerId;
    @Column(columnDefinition="tinyint(1) default 0")
    Boolean isActive;
    String description;
    @Column(columnDefinition = "DATETIME DEFAULT NOW()")
    Date cratedDate;
    Date startDate;
    Date endDate;
}
