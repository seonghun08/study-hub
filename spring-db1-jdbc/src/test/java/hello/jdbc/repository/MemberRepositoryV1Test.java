package hello.jdbc.repository;

import com.zaxxer.hikari.HikariDataSource;
import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.sql.SQLException;
import java.util.NoSuchElementException;

import static hello.jdbc.connection.ConnectionConst.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
class MemberRepositoryV1Test {

    MemberRepositoryV1 repository;

    @BeforeEach
    void beforeEach() throws SQLException {
        // 기본 DriverManager - 항상 새로운 커넥션 획득
        // DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);

        // 커넥션 풀링
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(URL);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);
        dataSource.setMaximumPoolSize(10);
        dataSource.setPoolName("MyPool");

        repository = new MemberRepositoryV1(dataSource);
        repository.deleteAll();
    }

    @Test
    void CRUD() throws SQLException, InterruptedException {
        Member member = new Member("spring", 10000);
        repository.save(member);

        Member findMember = repository.findById("spring");
        assertThat(findMember).isEqualTo(member);

        repository.update("spring", 20000);
        findMember = repository.findById("spring");
        assertThat(findMember.getMoney()).isEqualTo(20000);

        repository.delete("spring");
        assertThatThrownBy(() -> {
            repository.findById("spring");
        })
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("member not found memberId=spring");

        // Thread.sleep(1000);
    }
}