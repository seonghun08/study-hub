package hello.itemservice.service;

import hello.itemservice.domain.Item;
import hello.itemservice.repository.ItemSearchCond;
import hello.itemservice.repository.ItemUpdateDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class ItemServiceV2Test {

    @Autowired
    ItemService itemService;

    @Test
    void save() {
        // given
        Item itemA = new Item("Spring", 10000, 100);
        Item itemB = new Item("JPA", 10000, 100);
        Item itemC = new Item("Java", 10000, 100);

        // when
        Item findItemA = itemService.save(itemA);
        Item findItemB = itemService.save(itemB);
        Item findItemC = itemService.save(itemC);

        // then
        assertThat(itemA).isEqualTo(itemService.findById(findItemA.getId()).get());
        assertThat(itemB).isEqualTo(itemService.findById(findItemB.getId()).get());
        assertThat(itemC).isEqualTo(itemService.findById(findItemC.getId()).get());
    }

    @Test
    void update() {
        //given
        Item item = new Item("item1", 10000, 10);
        Item savedItem = itemService.save(item);
        Long itemId = savedItem.getId();

        //when
        ItemUpdateDto updateParam = new ItemUpdateDto("item2", 20000, 30);
        itemService.update(itemId, updateParam);

        //then
        Item findItem = itemService.findById(itemId).get();
        assertThat(findItem.getItemName()).isEqualTo(updateParam.getItemName());
        assertThat(findItem.getPrice()).isEqualTo(updateParam.getPrice());
        assertThat(findItem.getQuantity()).isEqualTo(updateParam.getQuantity());
    }

    @Test
    void findItems() {
        //given
        Item item1 = new Item("itemA-1", 10000, 10);
        Item item2 = new Item("itemA-2", 20000, 20);
        Item item3 = new Item("itemB-1", 30000, 30);

        itemService.save(item1);
        itemService.save(item2);
        itemService.save(item3);

        //둘 다 없음 검증
        test(null, null, item1, item2, item3);
        test("", null, item1, item2, item3);

        //itemName 검증
        test("itemA", null, item1, item2);
        test("temA", null, item1, item2);
        test("itemB", null, item3);

        //maxPrice 검증
        test(null, 10000, item1);

        //둘 다 있음 검증
        test("itemA", 10000, item1);
    }

    void test(String itemName, Integer maxPrice, Item... items) {
        List<Item> result = itemService.findItems(new ItemSearchCond(itemName, maxPrice));
        assertThat(result).containsExactly(items);
    }
}