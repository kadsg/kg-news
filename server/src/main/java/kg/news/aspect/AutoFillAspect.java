package kg.news.aspect;

import kg.news.annotation.AutoFill;
import kg.news.context.BaseContext;
import kg.news.enumration.OperationType;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * 自定义切面，实现公共字段自动填充处理逻辑
 */
@Component
@Aspect
public class AutoFillAspect {
    // 方法名
    private enum MethodName {
        SET_CREATE_TIME("setCreateTime"),
        SET_UPDATE_TIME("setUpdateTime"),
        SET_CREATE_USER("setCreateUser"),
        SET_UPDATE_USER("setUpdateUser");

        MethodName(String ignoredMethodName) {
        }
    }

    // 作用范围
    private final String POINT_CUT = "@annotation(kg.news.annotation.AutoFill)";

    // 切入点
    @Pointcut(POINT_CUT)
    public void autoFillPointCut() {
    }

    // 前置通知
    @Before("autoFillPointCut()")
    public void autoFill(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature(); // 方法签名对象
        Method method = signature.getMethod(); // 获取方法对象
        AutoFill autoFill = method.getAnnotation(AutoFill.class); // 获得方法上的注解对象
        OperationType type = autoFill.value(); // 获取数据库操作类型
        Object[] args = joinPoint.getArgs(); // 获取到当前被拦截的方法的参数
        if (args == null || 0 == args.length) {
            return;
        }
        Object entity = args[0];
        LocalDateTime now = LocalDateTime.now();
        Long currentId = BaseContext.getCurrentId();
        if (type == OperationType.INSERT) {
            // 为4个公共字段赋值
            try {
                entity.getClass().getDeclaredMethod(MethodName.SET_CREATE_TIME.name(), LocalDateTime.class).invoke(entity, now);
                entity.getClass().getDeclaredMethod(MethodName.SET_UPDATE_TIME.name(), LocalDateTime.class).invoke(entity, now);
                entity.getClass().getDeclaredMethod(MethodName.SET_CREATE_USER.name(), Long.class).invoke(entity, currentId);
                entity.getClass().getDeclaredMethod(MethodName.SET_UPDATE_USER.name(), Long.class).invoke(entity, currentId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (type == OperationType.UPDATE) {
            // 为2个公共字段赋值
            try {
                entity.getClass().getDeclaredMethod(MethodName.SET_UPDATE_TIME.name(), LocalDateTime.class).invoke(entity, now);
                entity.getClass().getDeclaredMethod(MethodName.SET_UPDATE_USER.name(), Long.class).invoke(entity, currentId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
