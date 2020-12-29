package models.api.v1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import models.db.user.UserRole;

import java.util.List;

@Getter
@Setter
@Builder
@JsonIgnoreProperties(value = "localPwd")
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
