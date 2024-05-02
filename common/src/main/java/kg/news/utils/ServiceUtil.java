package kg.news.utils;

import kg.news.context.BaseContext;
import kg.news.enumration.OperationType;
import org.springframework.util.DigestUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

public class ServiceUtil {
    /**
     * 加密密码
     *
     * @param password 密码
     * @return 加密后的密码
     */
    public static String encryptPassword(String password) {
        return DigestUtils.md5DigestAsHex(password.getBytes());
    }

/**
     * 自动填充创建时间、更新时间、创建人、更新人
     *
     * @param entity 实体
     * @param type   操作类型
     * @param <S>    实体类型
     * @throws NoSuchMethodException     异常
     * @throws InvocationTargetException 异常
     * @throws IllegalAccessException    异常
     */
    public static <S> void autoFill(S entity, OperationType type) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        enum MethodName {

            SET_CREATE_TIME("setCreateTime"),
            SET_UPDATE_TIME("setUpdateTime"),
            SET_CREATE_USER("setCreateUser"),
            SET_UPDATE_USER("setUpdateUser");

            private final String methodName;

            MethodName(String methodName) {
                this.methodName = methodName;
            }
        }

        LocalDateTime now = LocalDateTime.now();
        Long currentId = BaseContext.getCurrentId();
        Class<?> entityClass = entity.getClass();

        Method setCreateTime = entityClass.getDeclaredMethod(MethodName.SET_CREATE_TIME.methodName, LocalDateTime.class);
        Method setUpdateTime = entityClass.getDeclaredMethod(MethodName.SET_UPDATE_TIME.methodName, LocalDateTime.class);
        Method setCreateUser = entityClass.getDeclaredMethod(MethodName.SET_CREATE_USER.methodName, Long.class);
        Method setUpdateUser = entityClass.getDeclaredMethod(MethodName.SET_UPDATE_USER.methodName, Long.class);

        if (type == OperationType.INSERT) {
            setCreateTime.invoke(entity, now);
            setCreateUser.invoke(entity, currentId);
        }
        setUpdateTime.invoke(entity, now);
        setUpdateUser.invoke(entity, currentId);
    }
}
