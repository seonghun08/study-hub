package hello.aop.exam.internalcall;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * 프록시 내부 호출 문제
 * 대안2: 지연 조회
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CallServiceV2 {

    // private final ApplicationContext applicationContext;
    private final ObjectProvider<CallServiceV2> callServiceV2ObjectProvider;

    public void external() {
        log.info("call external");

        // ApplicationContext 를 통해 스프링 컨테이너에 올라간 시점 이후, 컨테이너에 다시 꺼내서 사용한다.
        // CallServiceV2 callServiceV2 = applicationContext.getBean(CallServiceV2.class);

        // ObjectProvider 를 사용한다. (ApplicationContext 는 너무 무겁기 때문에 좀 더 가벼운 대체제 사용)
        CallServiceV2 callServiceV2 = callServiceV2ObjectProvider.getObject();

        callServiceV2.internal();
    }

    public void internal() {
        log.info("call internal");
    }
}
