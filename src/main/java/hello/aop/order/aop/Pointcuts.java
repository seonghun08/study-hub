package hello.aop.order.aop;

import org.aspectj.lang.annotation.Pointcut;

public class Pointcuts {

    @Pointcut("execution(* hello.aop.order..*(..))")
    public void allOrder() {} // hello.aop.order 패키지와 하위 패키지

    @Pointcut("execution(* *..*Service.*(..))")
    public void allService() {} // 클래스 이름 패턴이 *Service

    @Pointcut("allOrder() && allService()")
    public void orderAndService() {} // allOrder && allService
}
