package study.datajpa.entity;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.repository.MemberRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.SQLOutput;
import java.util.List;

@Transactional
@SpringBootTest
@Rollback(value = false)
public class MemberTest {

    @Autowired MemberRepository memberRepository;
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
    
    @Test
    public void eventBaseEntity() throws Exception {
        Member member = new Member("member");
        memberRepository.save(member);

        Thread.sleep(1000);
        member.changeUsername("admin");

        em.flush();
        em.clear();

        Member findMember = memberRepository.findById(member.getId()).get();
        System.out.println("findMember.getCreatedDate() = " + findMember.getCreatedDate());
        System.out.println("findMember.getUpdateDate() = " + findMember.getLastModifiedDate());
        System.out.println("findMember.getCreatedBy() = " + findMember.getCreatedBy());
        System.out.println("findMember.getLastModifiedBy() = " + findMember.getLastModifiedBy());
    }
}


