package hello.aop.proxyvs;

import hello.aop.member.MemberService;
import hello.aop.member.MemberServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.ProxyFactory;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * JDK dynamic proxy 는 대상 객체인 MemberServiceImpl 로 캐스팅 할 수 없다.
 * CGLIB proxy 는 대상 객체인 MemberServiceImpl 로 캐스팅 할 수 있다.
 */
@Slf4j
public class ProxyCastingTest {

    @Test
    void jdkProxy() {
        MemberServiceImpl target = new MemberServiceImpl();
        ProxyFactory proxyFactory = new ProxyFactory(target);
        proxyFactory.setProxyTargetClass(false); // jdk dynamic proxy set

        // 프록시를 인터페이스로 캐스팅 성공 (인터페이스로 만들기 때문에 캐스팅이 가능)
        MemberService memberServiceProxy = (MemberService) proxyFactory.getProxy();

        // 프록시를 구현 클래스로 캐스팅 실패 (ClassCastException 예외)
        assertThatThrownBy(() -> {
            MemberServiceImpl castingMemberService = (MemberServiceImpl) memberServiceProxy;
        }).isInstanceOf(ClassCastException.class);
    }

    @Test
    void cglibProxy() {
        MemberServiceImpl target = new MemberServiceImpl();
        ProxyFactory proxyFactory = new ProxyFactory(target);
        proxyFactory.setProxyTargetClass(true); // cglib proxy set

        // 프록시를 인터페이스로 캐스팅 성공
        MemberService memberServiceProxy = (MemberService) proxyFactory.getProxy();

        // 프록시를 구현 클래스로 캐스팅 성공 (cglib 프록시는 구체 클래스로 만들기 때문에 가능)
        MemberServiceImpl castingMemberService = (MemberServiceImpl) memberServiceProxy;
    }
}
