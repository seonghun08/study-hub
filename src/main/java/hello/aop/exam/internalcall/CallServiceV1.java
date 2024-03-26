package hello.aop.exam.internalcall;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 프록시 내부 호출 문제
 * 대안1: 자기 자신을 주입하여 사용
 */
@Slf4j
@Component
public class CallServiceV1 {

    private CallServiceV1 callServiceV1;

    /**
     * 스프링은 스프링 빈을 생성하는 단계가 있고 setter 로 주입하는 단계가 따로 분리되어 있다.
     * 그렇기 때문에 생성하는 단계 이후, setter 를 통해 자기 자신을 주입할 수 있다.
     */
    @Autowired
    public void setCallServiceV1(CallServiceV1 callServiceV1) {
        log.info("callServiceV1 setter={}", callServiceV1.getClass()); // 스프링 컨테이너 로딩 시점에 로그가 찍힘
        this.callServiceV1 = callServiceV1;
    }

    public void external() {
        log.info("call external");
        callServiceV1.internal(); // 내부 메서드 호출(this.internal())
    }

    public void internal() {
        log.info("call internal");
    }
}
