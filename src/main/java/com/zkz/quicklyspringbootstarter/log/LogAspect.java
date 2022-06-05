package com.zkz.quicklyspringbootstarter.log;

import com.zkz.quicklyspringbootstarter.exception.BaseException;
import com.zkz.quicklyspringbootstarter.exception.GlobalExceptionHandler;
import com.zkz.quicklyspringbootstarter.jwt.TokenUtils;
import com.zkz.quicklyspringbootstarter.utils.httprequest.RequestUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
public class LogAspect {
    public LogAspect() {
        LogUtils.info("启用接口日志记录");
    }

    //controller切入点
    @Pointcut("execution(* com.*..controller..*.*(..))")
    public void controllerPoint() {
    }

    //记录接口正常时执行时间
    @Around("controllerPoint()")
    public Object timeCount(ProceedingJoinPoint pjp) throws Throwable {
        long time = System.currentTimeMillis();
        try {
            //返回对象
            return pjp.proceed();
        } finally {
            HttpServletRequest request = RequestUtils.getHttpServletRequest();
            //记录接口和耗时,token和参数
            LogUtils.info("{} {} {}; process: {}ms; token: {}; params: {}",
                    request.getMethod(), request.getRequestURL(), RequestUtils.getIp(), System.currentTimeMillis() - time,
                    TokenUtils.getToken(), pjp.getArgs());
        }
    }


    //记录接口异常时日志
    @AfterThrowing(throwing = "ex", pointcut = "controllerPoint()")
    public void doThrowing(Throwable ex) {
        if (ex instanceof Exception) {
            HttpServletRequest request = RequestUtils.getHttpServletRequest();
            //异常信息
            BaseException generalBaseException = GlobalExceptionHandler.getGeneralBaseException((Exception) ex);
            //记录日志
            LogUtils.error("{} {} {}; errorCode:{} errorMsg:{} rawMsg:{}",
                    request.getMethod(), request.getRequestURL(), RequestUtils.getIp(),
                    generalBaseException.getCode(), generalBaseException.getMessage(), ex.getMessage());
        } else {
            LogUtils.error(ex.getMessage());
        }
    }
}
