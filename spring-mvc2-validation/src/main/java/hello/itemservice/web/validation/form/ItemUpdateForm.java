package hello.itemservice.web.validation.form;

import hello.itemservice.domain.item.Item;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ItemUpdateForm {

    @NotNull
    private Long id;

    @NotBlank
    private String itemName;

    @NotNull
    @Range(min = 1000, max = 1000000)
    private Integer price;

    /* 수정일 경우 수량은 자유롭게 변경할 수 있다. (요구사항 예시) */
    private Integer quantity;

    public Item itemBuild() {
        return Item.builder()
                .itemName(itemName)
                .price(price)
                .quantity(quantity)
                .build();
    }
}
