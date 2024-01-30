package hello.proxy.common.advice;

import hello.proxy.common.service.Obj;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Method;

@Slf4j
public class TimeAdvice implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {

        log.info("TimeProxy 실행");
        long startTime = System.currentTimeMillis();

        Object[] arguments = invocation.getArguments();
        for (Object argument : arguments) {
            log.info("argument={}", argument);

            if (argument instanceof Obj) {

            }

        }

        // Object result = methodProxy.invoke(target, args);
        Object result = invocation.proceed();

        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;
        log.info("TimeProxy 종료 resultTime={}", resultTime);

        return result;
    }
}
