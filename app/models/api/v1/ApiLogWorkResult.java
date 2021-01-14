package models.api.v1;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiLogWorkResult {
    private ApiLogWork logWork;
    private ApiTask task;
}
