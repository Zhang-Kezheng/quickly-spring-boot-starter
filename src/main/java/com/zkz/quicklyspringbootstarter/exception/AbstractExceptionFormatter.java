package com.zkz.quicklyspringbootstarter.exception;

/**
 * 抽象类.
 * 用于定义异常拦截的处理办法,以及处理顺序
 */
public abstract class AbstractExceptionFormatter {

    /**
     * 权重,决定处理异常的顺序
     *
     * @return 整数. 中间件中的返回值为int的最大值, 其他实现类此方法的返回值最好小于此值, 以保证中间件的方法最后执行, 否则异常可能无法被正确处理
     */
    abstract public int weight();

    /**
     * 对异常转化为BaseException的方法
     *
     * @return BaseException.如果没有匹配, 则应该返回null
     */
    abstract public BaseException format(Exception ex);
}
