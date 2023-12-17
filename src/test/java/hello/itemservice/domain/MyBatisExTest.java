package hello.itemservice.domain;

import hello.itemservice.repository.mybatis.MyBatisItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;

@Slf4j
@RequiredArgsConstructor
@Transactional
@SpringBootTest
public class MyBatisExTest {

    @Autowired
    MyBatisItemRepository myBatisItemRepository;

    @Test
    void test() {
        HashMap<String, Object> input = new HashMap<>();
        input.put("itemName", "");
        input.put("maxPrice", 20000);

        ArrayList<HashMap<String, Object>> output = myBatisItemRepository.findAllToMap(input);
        for (HashMap<String, Object> map : output) {
            log.info("id={}", map.get("ID"));
            log.info("itemName={}", map.get("ITEM_NAME"));
            log.info("price={}", map.get("PRICE"));
            log.info("quantity={}", map.get("QUANTITY"));
        }
    }
}
