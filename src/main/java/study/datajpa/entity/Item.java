package study.datajpa.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item extends BaseEntity implements Persistable<String> {

//    @Id @GeneratedValue
//    private Long id;

    /**
     * 일반적으로 @GeneratedValue 사용하여 persist 를 통해 식별자 값을 받는다고 한다면
     * 예외적으로 아래와 같은 String 타입 등으로 직접 id를 할당하는 경우도 있다.
     * 이러한 경우 JPA 는 save 를 호출 할 때 persist 가 아닌 merge 로 값을 넣게 되기 때문에
     * isNew() 를 직접 수정해서 써야 한다.
     *
     * merge 는 비영속성 컨텍스트를 다시 끌어올리는 용도처럼 정말 특별한 경우가 아니면 쓰면 안된다.
     * 또한 이미 등록된 데이터가 변경될 때도 꼭 변경감지 기능을 쓰도록 하자.
     */

    @Id
    private String id;

    // @EntityListeners(AuditingEntityListener.class) 필요
//    @CreatedDate
//    private LocalDateTime createdDate;

    public Item(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        // createdDate 값이 없다면 새로운 엔티티 객체로 인식
        return super.getCreatedDate() == null;
    }
}
