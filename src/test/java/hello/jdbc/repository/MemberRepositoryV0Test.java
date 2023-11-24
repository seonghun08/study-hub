package hello.jdbc.repository;

import hello.jdbc.domain.Member;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

class MemberRepositoryV0Test {

    MemberRepositoryV0 memberRepositoryV0 = new MemberRepositoryV0();

    @Test
    void save() {
        try {
            Member member = new Member("spring", 10000);
            memberRepositoryV0.save(member);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}