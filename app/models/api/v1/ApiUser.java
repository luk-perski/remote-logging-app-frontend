package models.api.v1;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import models.db.user.UserRole;

import java.util.List;

@Getter
@Setter
@Builder
public class ApiUser {
    private Long id;
    private String name;
    private String displayName;
    private String username;
    private String email;
    private String localPwd;
    private Long teamId;
    private List<UserRole> roles;
}
