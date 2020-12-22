package models.api.v1;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import utils.Constant;

import java.util.Date;

@Builder
@Getter
@Setter
public class ApiProject {
    private Long id;
    private String name;
    //todo ask if better is store only id or id and name of manager
    private Boolean isActive;
    private Long managerId;
    private String managerName;
    private String description;
    @JsonFormat(pattern = Constant.DATE_FORMAT)
    private Date cratedDate;
    @JsonFormat(pattern = Constant.DATE_FORMAT)
    private Date startDate;
    @JsonFormat(pattern = Constant.DATE_FORMAT)
    private Date endDate;
}
