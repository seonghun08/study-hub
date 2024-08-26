package hello.aop.pointcut;

import hello.aop.member.MemberServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;

import java.lang.reflect.Method;

/**
 * <pre>
 * pointcut args: 인자가 주어진 타입의 인스턴스인 조인 포인트 매칭 (기본 문법은 "execution"의 'args' 부분과 같다.
 *
 * "execution"과 "args" 차이점
 * "execution"은 파라미터 타입이 정확하게 매칭되어야 한다. "execution"은 클래스에 선언된 정보를 기반으로 한다.
 * "args"는 부모 타입을 허용하며 "args"는 실제 넘어온 파라미터 객체 인스턴스를 보고 판단한다.
 * </pre>
 */
@Slf4j
public class argsTest {

    Method helloMethod;

    @BeforeEach
    public void init() throws NoSuchMethodException {
        helloMethod = MemberServiceImpl.class.getMethod("hello", String.class);
    }

    private AspectJExpressionPointcut pointcut(String expression) {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression(expression);
        return pointcut;
    }

    @Test
    void args() throws NoSuchMethodException {
        Assertions.assertThat(pointcut("args(String)")
                .matches(helloMethod, MemberServiceImpl.class)).isTrue();
        Assertions.assertThat(pointcut("args(Object)") // 모든 타입 가능
                .matches(helloMethod, MemberServiceImpl.class)).isTrue();
        Assertions.assertThat(pointcut("args(..)")
                .matches(helloMethod, MemberServiceImpl.class)).isTrue();
        Assertions.assertThat(pointcut("args(*)")
                .matches(helloMethod, MemberServiceImpl.class)).isTrue();
        Assertions.assertThat(pointcut("args(String, ..)")
                .matches(helloMethod, MemberServiceImpl.class)).isTrue();

        // 원시 타입은?
        Method method = MemberServiceImpl.class.getMethod("integer", int.class);
        Assertions.assertThat(pointcut("execution(* *(int))")
                .matches(method, MemberServiceImpl.class)).isTrue();

        Assertions.assertThat(pointcut("args()")
                .matches(helloMethod, MemberServiceImpl.class)).isFalse();
        Assertions.assertThat(pointcut("args(*, *)")
                .matches(helloMethod, MemberServiceImpl.class)).isFalse();
    }

    /**
     * execution(* *(java.io.Serializable)): 메서드의 시그니처로 판단 (정적)
     * args(java.io.Serializable)): 런타임에 전달된 인수로 판단 (동적)
     */
    @Test
    void argsVsExecution() {
        // args
        Assertions.assertThat(pointcut("args(String)")
                .matches(helloMethod, MemberServiceImpl.class)).isTrue();
        Assertions.assertThat(pointcut("args(java.io.Serializable)")
                .matches(helloMethod, MemberServiceImpl.class)).isTrue();
        Assertions.assertThat(pointcut("args(..)")
                .matches(helloMethod, MemberServiceImpl.class)).isTrue();

        // execution
        Assertions.assertThat(pointcut("execution(* *(String))")
                .matches(helloMethod, MemberServiceImpl.class)).isTrue();
        Assertions.assertThat(pointcut("execution(* *(java.io.Serializable))") // 매칭 실패
                .matches(helloMethod, MemberServiceImpl.class)).isFalse();
        Assertions.assertThat(pointcut("execution(* *(Object))") // 매칭 실패
                .matches(helloMethod, MemberServiceImpl.class)).isFalse();
    }
}
