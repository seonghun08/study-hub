package hello.jdbc.repository;

import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
class MemberRepositoryV0Test {

    MemberRepositoryV0 memberRepositoryV0 = new MemberRepositoryV0();

    @BeforeEach
    void beforeEach() throws SQLException {
        memberRepositoryV0.deleteAll();
    }

    @Test
    void CRUD() throws SQLException {
        Member member = new Member("spring", 10000);
        memberRepositoryV0.save(member);

        Member findMember = memberRepositoryV0.findById("spring");
        assertThat(findMember).isEqualTo(member);

        memberRepositoryV0.update("spring", 20000);
        findMember = memberRepositoryV0.findById("spring");
        assertThat(findMember.getMoney()).isEqualTo(20000);

        memberRepositoryV0.delete("spring");
        assertThatThrownBy(() -> {
            memberRepositoryV0.findById("spring");
        })
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("member not found memberId=spring");

    }
}