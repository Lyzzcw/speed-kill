package lyzzcw.work.speedkill.user.application.service;


import lyzzcw.work.speedkill.user.domain.entity.User;

/**
 * @author lzy
 * @version 1.0
 * Date: 2023/7/5 10:44
 * Description: No Description
 */
public interface UserService {

    /**
     * 根据用户名获取用户信息
     */
    User getUserByUserName(String userName);

    /**
     * 账号密码登录
     */
    User login(String userName, String password);
}
