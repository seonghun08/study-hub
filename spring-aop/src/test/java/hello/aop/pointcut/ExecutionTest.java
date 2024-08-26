package hello.aop.pointcut;

import hello.aop.member.MemberServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * <pre>
 * pointcut execution
 * hello.aop.member.*(1).*(2)
 * (1): 타입, (2): 메서드 이름
 *
 * .  -> 해당 위치의 패키지만
 * .. -> 해당 위치의 패키지와 그 하위 패키지도 포함
 *
 * execution 파라미터 매칭 규칙
 * (String)     -> 정확히 String 파라미터 하나만
 * ()           -> 파라미터 존재 X
 * (*)          -> 정확히 하나의 파라미터만 (단, 모든 타입 허용)
 * (*, *)       -> 정확히 두 개의 파라미터만 (단, 모든 타입 허용)
 * (..)         -> 모든 경우의 수 허용
 * (String, ..) -> String 타입으로 하나 + 나머지 모든 경우의 수 허용 (String), (String, Xxx), (String, Xxx, Xxx)
 * </pre>
 */
@Slf4j
public class ExecutionTest {

    AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
    Method helloMethod;

    @BeforeEach
    public void init() throws NoSuchMethodException {
        helloMethod = MemberServiceImpl.class.getMethod("hello", String.class);
    }

    @Test
    void printMethod() {
        // helloMethod=public java.lang.String hello.aop.member.MemberServiceImpl.hello(java.lang.String)
        log.info("helloMethod={}", helloMethod);
    }

    @DisplayName("최소 단위")
    @Test
    void exactMatch() {
        // public java.lang.String hello.aop.member.MemberServiceImpl.hello(java.lang.String)
        // pointcut -> execution(접근제어자 반환타입 선언타입.메서드이름(파라미터) 예외(생략 가능))
        pointcut.setExpression("execution(public String hello.aop.member.MemberServiceImpl.hello(String))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @DisplayName("모든 범위")
    @Test
    void allMatch() {
        pointcut.setExpression("execution(* *(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @DisplayName("메서드 이름")
    @Test
    void nameMatch() {
        pointcut.setExpression("execution(* hello(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @DisplayName("메서드 이름 + 패턴 매칭1")
    @Test
    void nameMatchPatten1() {
        pointcut.setExpression("execution(* hell*(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @DisplayName("메서드 이름 + 패턴 매칭2")
    @Test
    void nameMatchPatten2() {
        pointcut.setExpression("execution(* *el*(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @DisplayName("매칭 실패")
    @Test
    void nameMatchFalse() {
        pointcut.setExpression("execution(* noting(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isFalse();
    }

    @DisplayName("패키지 매칭1")
    @Test
    void packageExactMatch1() {
        pointcut.setExpression("execution(* hello.aop.member.MemberServiceImpl.hello(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @DisplayName("패키지 매칭2")
    @Test
    void packageExactMatch2() {
        pointcut.setExpression("execution(* hello.aop.member.*.*(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @DisplayName("패키지 매칭 실패")
    @Test
    void packageExactFalse() {
        // hello.aop.*.*(..) -> hello.aop 패키지 내에 있는 범위 만 적용되기 때문에 하위 범위까지 포함시켜줘야 한다.
        pointcut.setExpression("execution(* hello.aop.*.*(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isFalse();
    }

    @DisplayName("서브 패키지 매칭1")
    @Test
    void packageExactMatchSubPackage1() {
        // hello.aop.member..*.*(..) -> hello.aop.member 패키지 내 뿐만이 아닌 하위 패키지 범위까지 포함시킨다.
        pointcut.setExpression("execution(* hello.aop.member..*.*(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @DisplayName("서브 패키지 매칭2")
    @Test
    void packageExactMatchSubPackage2() {
        // hello.aop..* -> hello.aop 패키지 내 뿐만이 아닌 하위 패키지 범위까지 포함시킨다.
        pointcut.setExpression("execution(* hello.aop..*(..))");
        // pointcut.setExpression("execution(* hello.aop..*.*(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @DisplayName("타입")
    @Test
    void typeExactMatch() {
        pointcut.setExpression("execution(* hello.aop.member.MemberServiceImpl.*(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @DisplayName("부모 타입")
    @Test
    void typeMatchSuperType() {
        pointcut.setExpression("execution(* hello.aop.member.MemberService.*(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @DisplayName("타입(internal)")
    @Test
    void typeMatchInternal() throws NoSuchMethodException {
        pointcut.setExpression("execution(* hello.aop.member.MemberServiceImpl.*(..))");
        Method method = MemberServiceImpl.class.getMethod("internal", String.class);
        assertThat(pointcut.matches(method, MemberServiceImpl.class)).isTrue();
    }

    @DisplayName("부모 타입에 없는 타입(Internal)은 적용이 안됨")
    @Test
    void typeMatchNoSuperTypeMethodFalse() throws NoSuchMethodException {
        pointcut.setExpression("execution(* hello.aop.member.MemberService.*(..))");
        Method method = MemberServiceImpl.class.getMethod("internal", String.class);
        assertThat(pointcut.matches(method, MemberServiceImpl.class)).isFalse();
    }

    @DisplayName("파라미터 타입(String)")
    @Test
    void argsMatch() {
        pointcut.setExpression("execution(* *(String))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @DisplayName("파라미터 타입()")
    @Test
    void argsMatchNoArgs() {
        pointcut.setExpression("execution(* *())");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isFalse();
    }

    @DisplayName("파라미터 타입(?) 하나의 파라미터만")
    @Test
    void argsMatchStar() {
        pointcut.setExpression("execution(* *(*))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @DisplayName("숫자와 무관, 모든 파라미터, 모든 타입 허용")
    @Test
    void argsMatchAll() {
        pointcut.setExpression("execution(* *(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @DisplayName("String 타입으로 시작, 갯수 무관 및 모든 파라미터, 타입 허용")
    @Test
    void argsMatchComplex() {
        pointcut.setExpression("execution(* *(String, ..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }
}
