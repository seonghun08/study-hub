package hello.servlet.basic;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class HelloData {

    private String username;
    private int age;

    @Builder
    public HelloData(String username, int age) {
        this.username = username;
        this.age = age;
    }
}
