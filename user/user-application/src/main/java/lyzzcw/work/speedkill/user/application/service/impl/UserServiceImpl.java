package lyzzcw.work.speedkill.user.application.service.impl;

import lombok.RequiredArgsConstructor;
import lyzzcw.work.speedkill.user.application.service.UserService;
import lyzzcw.work.speedkill.user.domain.entity.User;
import lyzzcw.work.speedkill.user.domain.repository.UserRepository;
import org.springframework.stereotype.Service;

/**
 * @author lzy
 * @version 1.0
 * Date: 2023/7/5 10:45
 * Description: No Description
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    final UserRepository userRepository;
    @Override
    public User getUserByUserName(String userName) {
        return userRepository.getUserByUserName(userName);
    }

    @Override
    public User login(String userName, String password) {
        return userRepository.login(userName, password);
    }
}
