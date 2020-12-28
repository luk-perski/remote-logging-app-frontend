package models.api.v1;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import enums.Priority;
import enums.TaskType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import utils.Constant;

import java.util.Date;

@Builder
@Getter
@Setter
public class ApiTask {
    private Long id;
    private Long projectId;
    private String projectName;
    private String name;
    private Long creatorId;
    private String creatorName;
    @JsonValue
    private TaskType taskType;
    @JsonValue
    private Priority priority;
    private Long assigneeId;
    private String assigneeName;
    private ApiCategory category;
    @JsonFormat(pattern = Constant.DATE_FORMAT)
    private Date cratedDate;
    private String description;
    private Long estimate;
    @JsonFormat(pattern = Constant.DATE_FORMAT)
    private Date resolvedDate;
    /**
     * Time in ms.
     */
    private Long timeSpent;
    @JsonFormat(pattern = Constant.DATE_FORMAT)
    private Date runStart;
    @JsonFormat(pattern = Constant.DATE_FORMAT)
    private Date runEnd;

}
