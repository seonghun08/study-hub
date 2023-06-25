package hello.servlet.domain.member;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Member {

    private Long id;
    private String username;
    private int age;

    @Builder
    public Member(String username, int age) {
        this.username = username;
        this.age = age;
    }
}
