package models.api.v1;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ApiSignInData {
    private String userName;
    private String localPwd;
}
