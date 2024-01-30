package hello.proxy.common.service;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Service2Impl implements Service2Interface {

    @Override
    public void save(String s, Obj obj) {
        log.info("save 호출");
    }

    @Override
    public void find() {
        log.info("find 호출");
    }
}
