package lyzzcw.work.speedkill.user.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author lzy
 * @version 1.0
 * Date: 2023/7/5 9:46
 * Description: No Description
 */
@Getter
@AllArgsConstructor
public enum UserStatus {

    NORMAL(1),
    FREEZE(2);

    private final Integer code;

    public static boolean isNormal(Integer status) {
        return NORMAL.getCode().equals(status);
    }

}
