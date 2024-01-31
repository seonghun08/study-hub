package hello.proxy.config.v3_proxyfactory;

import hello.proxy.app.v2.OrderControllerV2;
import hello.proxy.app.v2.OrderRepositoryV2;
import hello.proxy.app.v2.OrderServiceV2;
import hello.proxy.config.v3_proxyfactory.advice.LogTraceAdvice;
import hello.proxy.trace.logtrace.LogTrace;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.Advisor;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class ProxyFactoryConfigV2 {

    @Bean
    public OrderControllerV2 OrderControllerV2(LogTrace logTrace) {
        OrderControllerV2 target = new OrderControllerV2(OrderServiceV2(logTrace));
        ProxyFactory factory = getProxyFactory(logTrace, target);
        OrderControllerV2 proxy = (OrderControllerV2) factory.getProxy();
        log.info("ProxyFactory proxy={}, target={}", proxy.getClass(), target.getClass());
        return proxy;
    }

    @Bean
    public OrderServiceV2 OrderServiceV2(LogTrace logTrace) {
        OrderServiceV2 target = new OrderServiceV2(OrderRepositoryV2(logTrace));
        ProxyFactory factory = getProxyFactory(logTrace, target);
        OrderServiceV2 proxy = (OrderServiceV2) factory.getProxy();
        log.info("ProxyFactory proxy={}, target={}", proxy.getClass(), target.getClass());
        return proxy;
    }

    @Bean
    public OrderRepositoryV2 OrderRepositoryV2(LogTrace logTrace) {
        OrderRepositoryV2 target = new OrderRepositoryV2();
        ProxyFactory factory = getProxyFactory(logTrace, target);
        OrderRepositoryV2 proxy = (OrderRepositoryV2) factory.getProxy();
        log.info("ProxyFactory proxy={}, target={}", proxy.getClass(), target.getClass());
        return proxy;
    }

    private ProxyFactory getProxyFactory(LogTrace logTrace, Object target) {
        ProxyFactory proxy = new ProxyFactory(target);
        proxy.addAdvisor(getAdvisor(logTrace));
        return proxy;
    }

    private Advisor getAdvisor(LogTrace logTrace) {
        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedNames("request*", "order*", "save*");
        LogTraceAdvice advice = new LogTraceAdvice(logTrace);
        return new DefaultPointcutAdvisor(pointcut, advice);
    }
}
