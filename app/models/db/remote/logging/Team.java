package models.db.remote.logging;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "remote_logging_team")
public class Team {

    @Id
    Long iD;
    @Column(nullable = false)
    String name;
    @Column(nullable = false)
    Long managerId;
    List<Long> teamIds;
    @Column(columnDefinition = "DATETIME DEFAULT NOW()")
    Date cratedDate;
}
