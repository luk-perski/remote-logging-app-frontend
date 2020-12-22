package models.api.v1;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ApiCategory {
    private Long id;
    private String name;
}
