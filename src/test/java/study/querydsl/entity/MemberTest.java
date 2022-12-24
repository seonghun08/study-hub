package study.querydsl.entity;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
@Rollback(value = false)
class MemberTest {

    @PersistenceContext EntityManager em;

    @Test
    public void Member() throws Exception {
        Team teamA = new Team("teamA");
        em.persist(teamA);

        Member member = Member.createMember("member", 10, teamA);
        em.persist(member);
    }
}