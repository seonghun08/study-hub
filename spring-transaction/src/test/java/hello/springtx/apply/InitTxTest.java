package hello.springtx.apply;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.annotation.PostConstruct;

@Slf4j
@SpringBootTest
public class InitTxTest {

    @Autowired
    Hello hello;

    @Test
    void go() {
        // 초기화 코드는 스프링이 초기화 시점에 호출한다.
    }

    @TestConfiguration
    static class InitTxTestConfig {

        @Bean
        public Hello hello() {
            return new Hello();
        }
    }

    static class Hello {

        @PostConstruct
        @Transactional
        public void initV1() {
            // @PostConstruct -> @Transactional AOP 적용 순으로 호출되기 때문에 active=false 이다.
            boolean active = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("hello init @PostConstruct tx active={}", active);
        }

        @EventListener(ApplicationReadyEvent.class)
        @Transactional
        public void initV2() {
            // @EventListener(ApplicationReadyEvent.class) 스프링이 완전히 올라온 시점부터 호출 해준다.
            boolean active = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("hello init @ApplicationReadyEvent tx active={}", active);
        }
    }
}
