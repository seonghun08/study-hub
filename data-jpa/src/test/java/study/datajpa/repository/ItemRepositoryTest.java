package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import study.datajpa.entity.Item;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ItemRepositoryTest {

    @Autowired ItemRepository itemRepository;

    @Test
    public void save() throws Exception {
        Item item = new Item("AAAA");

        /**
         *  em.persist(Entity e) 가 실행될 때 PK 값인 id를 넣어준다.
         *  db의 값을 업데이트 할 때 save 를 사용하는 것이 아닌 변경감지 기능을 사용해야 한다!
         *  데이터가 이미 등록된 후에 save 는 merge 로 데이터를 변경하기 때문에 조심!
         */
        itemRepository.save(item);
    }
}