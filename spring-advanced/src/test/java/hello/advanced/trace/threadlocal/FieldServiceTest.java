package hello.advanced.trace.threadlocal;

import hello.advanced.trace.threadlocal.code.FiledService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class FieldServiceTest {

    private FiledService fieldService = new FiledService();

    @Test
    void field() {
        log.info("main start");
        Runnable userA = () -> {
            fieldService.logic("userA");
        };
        Runnable userB = () -> {
            fieldService.logic("userB");
        };

        Thread threadA = new Thread(userA);
        threadA.setName("thread-A");
        Thread threadB = new Thread(userB);
        threadB.setName("thread-B");

        threadA.start();
        sleep(100); // 2초 간격으로 동시성 문제 발생 회피
        threadB.start();

        sleep(3000); // 메인 쓰레드 종료 대기 (생성해둔 쓰레드가 끝나기 전 메인 쓰레드가 끝나는 것을 방지한다.)
        log.info("main exit");
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
