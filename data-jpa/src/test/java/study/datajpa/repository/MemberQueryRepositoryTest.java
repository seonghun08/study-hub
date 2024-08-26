package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDTO;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class MemberQueryRepositoryTest {

    @Autowired TeamRepository teamRepository;
    @Autowired MemberRepository memberRepository;
    @Autowired MemberQueryRepository memberQueryRepository;

    @PersistenceContext EntityManager em;

    @Test
    public void findAllMembers() throws Exception {
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);
        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member1", 10, teamB);
        Member member3 = new Member("member1", 10, teamB);
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        em.flush();
        em.clear();

        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println("====================");

        List<MemberDTO> memberDTOs = memberQueryRepository.findAllMembers();
        memberDTOs.forEach(m -> {
            System.out.println(m);
        });

    }

}