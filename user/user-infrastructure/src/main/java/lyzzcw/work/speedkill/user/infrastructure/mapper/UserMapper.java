/**
 * Copyright 2022-9999 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package lyzzcw.work.speedkill.user.infrastructure.mapper;

import lyzzcw.work.speedkill.user.domain.entity.User;
import org.apache.ibatis.annotations.Param;


/**
 * Created with IntelliJ IDEA.
 * @author: lzy
 * Date: 2023/7/5 9:46
 * Time: 10:42
 * Description:
 */
public interface UserMapper {

    /**
     * 根据用户名获取用户信息
     */
    User getUserByUserName(@Param("userName") String userName);

    /**
     * 账号密码登录
     */
    User login(@Param("userName")String userName, @Param("password")String password);
}
