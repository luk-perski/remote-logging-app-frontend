package models.api.v1;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ApiUser {
    private Long id;
    private String name;
    private String displayName;
    private String username;
    private String email;
    private String localPwd;
    private Long teamId;
    private List<Long> roles;
}
