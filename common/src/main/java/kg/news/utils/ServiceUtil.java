package kg.news.utils;

import kg.news.context.BaseContext;
import kg.news.enumration.OperationType;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;

public class ServiceUtil {
    /**
     * 加密密码
     * @param password 密码
     * @return 加密后的密码
     */
    public static String encryptPassword(String password) {
        return DigestUtils.md5DigestAsHex(password.getBytes());
    }

    public static <S> void autoFill(S entity, OperationType type) {
        enum MethodName {
            SET_CREATE_TIME("setCreateTime"),
            SET_UPDATE_TIME("setUpdateTime"),
            SET_CREATE_USER("setCreateUser"),
            SET_UPDATE_USER("setUpdateUser");

            MethodName(String ignoredMethodName) {
            }
        }

        LocalDateTime now = LocalDateTime.now();
        Long currentId = BaseContext.getCurrentId();
        Class<?> entityClass = entity.getClass();
        if (type == OperationType.INSERT) {
            // 为4个公共字段赋值
            try {
                entityClass.getDeclaredMethod(MethodName.SET_CREATE_TIME.name(), LocalDateTime.class).invoke(entity, now);
                entityClass.getDeclaredMethod(MethodName.SET_UPDATE_TIME.name(), LocalDateTime.class).invoke(entity, now);
                entityClass.getDeclaredMethod(MethodName.SET_CREATE_USER.name(), Long.class).invoke(entity, currentId);
                entityClass.getDeclaredMethod(MethodName.SET_UPDATE_USER.name(), Long.class).invoke(entity, currentId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (type == OperationType.UPDATE) {
            // 为2个公共字段赋值
            try {
                entityClass.getDeclaredMethod(MethodName.SET_UPDATE_TIME.name(), LocalDateTime.class).invoke(entity, now);
                entityClass.getDeclaredMethod(MethodName.SET_UPDATE_USER.name(), Long.class).invoke(entity, currentId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
