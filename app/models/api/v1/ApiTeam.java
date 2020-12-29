package models.api.v1;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ApiTeam {
    Long id;
    String name;
    ApiUser manager;
}
//todo add field validation