package study.querydsl.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.querydsl.dto.MemberSearchCondition;
import study.querydsl.dto.MemberTeamDTO;
import study.querydsl.entity.Member;
import study.querydsl.entity.Team;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberJpaRepositoryTest {

    @PersistenceContext EntityManager em;

    @Autowired MemberJpaRepository memberJpaRepository;

    @Test
    public void basicTest() throws Exception {
        Member member = new Member("member1", 10);
        memberJpaRepository.save(member);

        Member findMember = memberJpaRepository.findById(member.getId()).orElseThrow();
        assertThat(findMember.getId()).isEqualTo(member.getId());

        List<Member> findMembers = memberJpaRepository.findAll();
        assertThat(findMembers).containsExactly(member);

        List<Member> usernames = memberJpaRepository.findByUsername("member1");
        assertThat(usernames).containsExactly(member);
    }

    @Test
    public void basicTest_dsl() throws Exception {
        Member member = new Member("member1", 10);
        memberJpaRepository.save(member);

        Member findMember = memberJpaRepository.findById_dsl(member.getId()).orElseThrow();
        assertThat(findMember.getId()).isEqualTo(member.getId());

        List<Member> findMembers = memberJpaRepository.findAll_dsl();
        assertThat(findMembers).containsExactly(member);

        List<Member> usernames = memberJpaRepository.findByUsername_dsl("member1");
        assertThat(usernames).containsExactly(member);
    }

    @Test
    public void searchTest() throws Exception {
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        em.persist(teamA);
        em.persist(teamB);
        Member member1 = Member.createMember("member1", 10, teamA);
        Member member2 = Member.createMember("member2", 20, teamA);
        Member member3 = Member.createMember("member3", 30, teamB);
        Member member4 = Member.createMember("member4", 40, teamB);
        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);
        em.flush();
        em.clear();

        MemberSearchCondition condition = new MemberSearchCondition();
        condition.setAgeGoe(15);
        condition.setAgeLoe(35);
        condition.setTeamName("teamB");

        List<MemberTeamDTO> memberTeamDTOs = memberJpaRepository.searchByBuilder(condition);

        memberTeamDTOs.forEach(d -> {
            System.out.println(d);
        });

        assertThat(memberTeamDTOs).extracting("username").containsExactly("member3");
    }
}