package lyzzcw.work.speedkill.user.interfaces.controller;

import cn.dev33.satoken.stp.SaLoginConfig;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lyzzcw.work.component.common.http.entity.Result;
import lyzzcw.work.speedkill.user.application.service.UserService;
import lyzzcw.work.speedkill.user.domain.convert.UserConverter;
import lyzzcw.work.speedkill.user.domain.dto.UserDTO;
import lyzzcw.work.speedkill.user.domain.entity.User;
import lyzzcw.work.speedkill.user.domain.vo.UserVO;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

/**
 * Created with IntelliJ IDEA.
 * @author: lzy
 * Date: 2023/7/5 9:46
 * Time: 10:42
 * Description: 用户相关接口
 */
@RestController
@RequestMapping(value = "/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    final UserService userService;

    /**
     * 系统测试
     * @param userName
     * @return
     */
    @GetMapping( "/get")
    public Result<User> getUser(String userName){

        String currentUser = StpUtil.getExtra("userName").toString();

        log.info("{} -> {}","当前登录账户",currentUser);

        return Result.ok(userService.getUserByUserName(userName));
    }

    /**
     * 登录
     * @param userDTO
     * @return
     */
    @PostMapping( "/doLogin")
    public Result<UserVO> login(@RequestBody UserDTO userDTO){
        User user = UserConverter.INSTANCE.userDTO2User(userDTO);
        User loginUser = userService.login(user.getUserName(),user.getPassword());
        Assert.notNull(loginUser,"用户不存在");
        //sa-token 登录
        StpUtil.login(loginUser.getId(),
                //为生成的 Token 追加扩展参数name
                SaLoginConfig.setExtra("userName", loginUser.getUserName()));

        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
        log.info("tokenInfo:{}",tokenInfo);
        UserVO result = UserConverter.INSTANCE.user2UserVO(loginUser);
        result.setToken("Bearer " + tokenInfo.getTokenValue());
        return Result.ok(result);
    }
}
