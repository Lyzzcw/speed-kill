package lyzzcw.work.speedkill.user.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author lzy
 * @version 1.0
 * Date: 2023/7/6 18:43
 * Description: No Description
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserVO implements Serializable {
    private static final long serialVersionUID = 1L;
    //用户名
    private String userName;
    //1：正常；2：冻结
    private Integer status;
    //第一次登录时返回,前端在请求时放入Header中后端做校验
    private String token;
}
