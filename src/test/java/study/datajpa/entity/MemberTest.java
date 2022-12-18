package study.datajpa.entity;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.SQLOutput;
import java.util.List;

@Transactional
@SpringBootTest
public class MemberTest {

    @PersistenceContext EntityManager em;

    @Test
    public void testEntity() throws Exception {
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member("member1", 24, teamA);
        Member member2 = new Member("member2", 28, teamA);
        Member member3 = new Member("member3", 20, teamB);
        Member member4 = new Member("member4", 25, teamB);
        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);

        // 초기화
        em.flush();
        em.clear();
        System.out.println("==========================================");
        List<Member> members = em.createQuery(
                "select m from Member m" +
                        " join fetch m.team t", Member.class)
                .getResultList();

        members.forEach(m -> {
            System.out.println("member = " + m);
            System.out.println("ㄴ> member.team = " + m.getTeam());
        });

    }
}


