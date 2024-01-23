package hello.proxy.pureproxy.proxy;

import hello.proxy.pureproxy.proxy.code.CacheProxy;
import hello.proxy.pureproxy.proxy.code.ProxyPatternClient;
import hello.proxy.pureproxy.proxy.code.RealSubject;
import hello.proxy.pureproxy.proxy.code.Subject;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class ProxyPatternTest {

    @Test
    void noProxyTest() {
        ProxyPatternClient client = new ProxyPatternClient(new RealSubject());
        client.execute();
        client.execute();
        client.execute();
    }

    @Test
    void cacheProxyTest() {
        Subject subject = new RealSubject();
        CacheProxy proxy = new CacheProxy(subject);
        ProxyPatternClient client = new ProxyPatternClient(proxy);

        client.execute();
        client.execute();
        client.execute();
    }
}
