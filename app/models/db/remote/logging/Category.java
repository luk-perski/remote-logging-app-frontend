package models.db.remote.logging;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.ebean.Model;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import utils.Constant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Builder
@Getter
@Setter
@Entity
@Table(name = "remote_logging_category")
public class Category extends Model {
    @Id
    Long id;
    String name;
    @JsonFormat(pattern = Constant.DATE_FORMAT)
    @Column(nullable = false, columnDefinition = "DATETIME DEFAULT NOW()")
    Date cratedDate;
}
