package hello.proxy.proxyfactory;

import hello.proxy.common.advice.TimeAdvice;
import hello.proxy.common.service.ConcreteService;
import hello.proxy.common.service.ServiceImpl;
import hello.proxy.common.service.ServiceInterface;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.AopUtils;

import static org.assertj.core.api.Assertions.assertThat;

/**
 *  프록시 팩토리의 서비스 추상화 덕분에 구체적인 CGLIB, JDK 동적 프록시 기술에 의존하지 않고 편리하게 동적 프록시를 생성할 수 있다.
 *  프록시의 부가 기능 로직도 특정 기술에 종속적이지 않게 (Advice) MethodInterceptor 인터페이스를 구현하여 사용할 수 있다.
 *
 *  주의사항
 *  - 스프링부트는 AOP 를 적용할 때 기본적으로 proxyFactory.setProxyTargetClass(true); 를 사용한다.
 *    따라서 인터페이스가 있어도 항상 CGLIB 를 사용해서 구체 클래스 기반으로 프록시를 생성한다.
 */
@Slf4j
public class ProxyFactoryTest {

    @Test
    @DisplayName("인터페이스가 있다면 JDK 동적 프록시 사용")
    void interfaceProxy() {
        ServiceInterface target = new ServiceImpl();
        ProxyFactory proxyFactory = new ProxyFactory(target);
        proxyFactory.addAdvice(new TimeAdvice());
        ServiceInterface proxy = (ServiceInterface) proxyFactory.getProxy();
        log.info("targetClass={}", target.getClass());
        log.info("proxyClass={}", proxy.getClass());

        proxy.save();

        boolean isProxy = AopUtils.isAopProxy(proxy);// proxy 객체인지 판별해준다. (ProxyFactory 를 통해 생성할 경우 가능)
        assertThat(isProxy).isTrue();

        boolean isDynamicProxy = AopUtils.isJdkDynamicProxy(proxy); // jdk dynamic proxy 객체인지 판별해준다. (ProxyFactory 를 통해 생성할 경우 가능)
        assertThat(isDynamicProxy).isTrue();

        boolean isCglibProxy = AopUtils.isCglibProxy(proxy); // cglib proxy 객체인지 판별해준다. (ProxyFactory 를 통해 생성할 경우 가능)
        assertThat(isCglibProxy).isFalse(); // cglib 객체가 아니다. why? proxy 가 인터페이스이기 때문이다.
    }

    @Test
    @DisplayName("구체 클래스만 있으면 CGLIB 사용")
    void concreteProxy() {
        ConcreteService target = new ConcreteService();
        ProxyFactory proxyFactory = new ProxyFactory(target);
        proxyFactory.addAdvice(new TimeAdvice());
        ConcreteService proxy = (ConcreteService) proxyFactory.getProxy();
        log.info("targetClass={}", target.getClass());
        log.info("proxyClass={}", proxy.getClass());

        proxy.call();

        boolean isProxy = AopUtils.isAopProxy(proxy);// proxy 객체인지 판별해준다. (ProxyFactory 를 통해 생성할 경우 가능)
        assertThat(isProxy).isTrue();

        boolean isDynamicProxy = AopUtils.isJdkDynamicProxy(proxy); // jdk dynamic proxy 객체인지 판별해준다. (ProxyFactory 를 통해 생성할 경우 가능)
        assertThat(isDynamicProxy).isFalse();

        boolean isCglibProxy = AopUtils.isCglibProxy(proxy); // cglib proxy 객체인지 판별해준다. (ProxyFactory 를 통해 생성할 경우 가능)
        assertThat(isCglibProxy).isTrue();
    }

    @Test
    @DisplayName("ProxyTargetClass 옵션을 사용하면 인터페이스가 있어도 CGLIB 를 사용하고, 클래스 기반 프록시 사용")
    void proxyTargetClass() {
        ServiceInterface target = new ServiceImpl();
        ProxyFactory proxyFactory = new ProxyFactory(target);

        proxyFactory.setProxyTargetClass(true); // 인터페이스여도 jdk dynamic proxy 가 아닌 cglib 기반으로 프록시를 만들 수 있다.

        proxyFactory.addAdvice(new TimeAdvice());
        ServiceInterface proxy = (ServiceInterface) proxyFactory.getProxy();
        log.info("targetClass={}", target.getClass());
        log.info("proxyClass={}", proxy.getClass());

        proxy.save();

        boolean isProxy = AopUtils.isAopProxy(proxy);// proxy 객체인지 판별해준다. (ProxyFactory 를 통해 생성할 경우 가능)
        assertThat(isProxy).isTrue();

        boolean isDynamicProxy = AopUtils.isJdkDynamicProxy(proxy); // jdk dynamic proxy 객체인지 판별해준다. (ProxyFactory 를 통해 생성할 경우 가능)
        assertThat(isDynamicProxy).isFalse();

        boolean isCglibProxy = AopUtils.isCglibProxy(proxy); // cglib proxy 객체인지 판별해준다. (ProxyFactory 를 통해 생성할 경우 가능)
        assertThat(isCglibProxy).isTrue(); // cglib 객체가 아니다. why? proxy 가 인터페이스이기 때문이다.
    }
}
