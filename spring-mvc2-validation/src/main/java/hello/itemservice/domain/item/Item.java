package hello.itemservice.domain.item;

import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.ScriptAssert;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
// @ScriptAssert 사용을 권장하지는 않는다. (실무에 사용하기엔 부족한 부분이 많음)
// @ScriptAssert(lang = "javascript", script = "_this.price * _this.quantity >= 10000", message = "총 수량과 가격의 합이 10,000원 이상이어야 합니다.")
public class Item {

    // @NotNull(groups = UpdateCheck.class)
    private Long id;

    // @NotBlank(groups = {SaveCheck.class, UpdateCheck.class})
    private String itemName;

    // @NotNull(groups = {SaveCheck.class, UpdateCheck.class})
    // @Range(min = 1000, max = 1000000, groups = {SaveCheck.class, UpdateCheck.class})
    private Integer price;

    // @NotNull(groups = {SaveCheck.class, UpdateCheck.class})
    // @Max(value = 9999, groups = SaveCheck.class)
    private Integer quantity;

    public Item() {
    }

    @Builder
    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
