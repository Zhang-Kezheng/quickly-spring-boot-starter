package com.zkz.quicklyspringbootstarter.log;

import com.zkz.quicklyspringbootstarter.exception.BaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日志工具类
 */
public class LogUtils {
    private static final String thisClassName = LogUtils.class.getName();
    private static final Logger defaultLogger = LoggerFactory.getLogger(LogUtils.class);

    /**
     * 获取Logger对象。该方法通过方法栈中的信息获取除当前类（{@link LogUtils}）之外的第一个类，即直接调用者。
     * 以直接调用者生成日志对象进行日志打印，这样在日志中即可打印出实际打印日志的地方。
     * 如果无法获取直接调用者，则以当前工具类为准
     *
     * @return 日志对象
     */
    private static Logger getLogger() {
        for (StackTraceElement element : new Throwable().getStackTrace()) {
            if (thisClassName.equals(element.getClassName())) {
                continue;
            }
            try {
                Class<?> aClass = Class.forName(element.getClassName());
                return LoggerFactory.getLogger(aClass);
            } catch (ClassNotFoundException e) {
                return defaultLogger;
            }
        }
        return defaultLogger;
    }

    public static void info(String msg) {
        getLogger().info(msg);
    }

    public static void info(String format, Object... arguments) {
        getLogger().info(format, arguments);
    }

    public static void warn(String msg) {
       getLogger().warn(msg);
    }

    public static void error(String msg) {
        getLogger().error(msg);
    }

    public static void error(BaseException baseException) {
        getLogger().error(baseException.toString());
    }

    public static void error(String format, Object... arguments) {
        getLogger().error(format, arguments);
    }
}
