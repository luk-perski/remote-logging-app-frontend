package models.db.remote.logging;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.ebean.Model;
import lombok.Data;
import models.db.user.User;
import utils.Constant;

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
    @JoinColumn(name = "manager_id", nullable = false)
    @ManyToOne
    User manager;
    @Column(nullable = false, columnDefinition = "DATETIME DEFAULT NOW()")
    @JsonFormat(pattern = Constant.DATE_FORMAT)
    Date createdDate;
}
