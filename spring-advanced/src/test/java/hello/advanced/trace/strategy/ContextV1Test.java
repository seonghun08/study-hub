package hello.advanced.trace.strategy;

import hello.advanced.trace.strategy.code.strategy.ContextV1;
import hello.advanced.trace.strategy.code.strategy.Strategy;
import hello.advanced.trace.strategy.code.strategy.StrategyLogic1;
import hello.advanced.trace.strategy.code.strategy.StrategyLogic2;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class ContextV1Test {

    @Test
    void strategyV0() {
        logic1();
        logic2();
    }

    /**
     * 전략 패턴 사용
     */
    @Test
    void strategyV1() {
        Strategy strategyLogic1 = new StrategyLogic1();
        ContextV1 ctx1 = new ContextV1(strategyLogic1);
        ctx1.execute();

        Strategy strategyLogic2 = new StrategyLogic2();
        ContextV1 ctx2 = new ContextV1(strategyLogic2);
        ctx2.execute();
    }

    /**
     * 전략 패턴 사용 + 익명 내부 클래스 (람다 사용)
     */
    @Test
    void strategyV2() {
        ContextV1 ctx1 = new ContextV1(() -> log.info("비즈니스 로직1 실행"));
        ctx1.execute();

        ContextV1 ctx2 = new ContextV1(() -> log.info("비즈니스 로직2 실행"));
        ctx2.execute();
    }

    private void logic1() {
        long startTime = System.currentTimeMillis();

        /* 비즈니스 로직 실행 */
        log.info("비즈니스 로직1 실행");
        /* 비즈니스 로직 종료 */

        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;
        log.info("resultTime={}", resultTime);
    }

    private void logic2() {
        long startTime = System.currentTimeMillis();

        /* 비즈니스 로직 실행 */
        log.info("비즈니스 로직2 실행");
        /* 비즈니스 로직 종료 */

        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;
        log.info("resultTime={}", resultTime);
    }
}
