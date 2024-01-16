package hello.advanced.trace.strategy;

import hello.advanced.trace.strategy.code.strategy.ContextV2;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class ContextV2Test {

    /**
     * 전략 패턴 적용 + 파라미터로 받기
     */
    @Test
    void strategyV1() {
        ContextV2 ctxV2 = new ContextV2();
        ctxV2.execute(() -> log.info("비즈니스 로직1 실행"));
        ctxV2.execute(() -> log.info("비즈니스 로직2 실행"));
    }
}
