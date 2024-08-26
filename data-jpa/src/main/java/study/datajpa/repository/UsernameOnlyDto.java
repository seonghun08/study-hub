package study.datajpa.repository;

import lombok.Getter;

@Getter
public class UsernameOnlyDto {

    private final String username;

    // JPA 가 파라미터를 보고 분석해서 값을 넣어준다.
    public UsernameOnlyDto(String username) {
        this.username = username;
    }
}
