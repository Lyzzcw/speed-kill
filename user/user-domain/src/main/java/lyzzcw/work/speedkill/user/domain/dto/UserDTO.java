package lyzzcw.work.speedkill.user.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author lzy
 * @version 1.0
 * Date: 2023/7/6 9:29
 * Description: No Description
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO implements Serializable {
    private static final long serialVersionUID = 1576119726547415227L;
    //用户名
    private String userName;
    //密码
    private String password;
}
