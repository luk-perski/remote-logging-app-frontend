package models.api.v1;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Builder
public class ApiLogWork {
    Long id;
    @NotNull
    Long timeSpend;
    String comment;
    @NotNull
    Long taskId;
    String taskName;
    @NotNull
    Long userId;
    String userDisplayName;
}
