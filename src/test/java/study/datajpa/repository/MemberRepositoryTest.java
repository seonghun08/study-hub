package study.datajpa.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDTO;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Transactional
@SpringBootTest
class MemberRepositoryTest {

    @Autowired MemberRepository memberRepository;
    @Autowired TeamRepository teamRepository;
//    @PersistenceContext EntityManager em;

    @Test
    public void testMember() throws Exception {
        Member member = new Member("winter", 27);
        Member saveMember = memberRepository.save(member);
        memberRepository.flush();

        Member findMember = memberRepository.findById(saveMember.getId()).orElseThrow();
        assertEquals(saveMember, findMember);
    }
    
    @Test
    public void basicCRUD() throws Exception {
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberRepository.save(member1);
        memberRepository.save(member2);

        // 단건 조회
        Member findMember1 = memberRepository.findById(member1.getId()).orElseThrow();
        Member findMember2 = memberRepository.findById(member2.getId()).orElseThrow();
        assertEquals(member1, findMember1);
        assertEquals(member2, findMember2);

        // 리스트 조회
        List<Member> members = memberRepository.findAll();
        assertEquals(members.size(), 2);
        assertTrue(members.contains(member1));
        assertTrue(members.contains(member2));

        // 삭제 검증
        memberRepository.delete(member1);
        memberRepository.delete(member2);
        long count = memberRepository.count();
        assertEquals(count, 0);
    }

    @Test
    public void findByUsernameAndAgeGreaterThen() throws Exception {
        Member member1 = new Member("member1", 15);
        Member member2 = new Member("member1", 20);
        Member member3 = new Member("member3", 12);
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);

        List<Member> members = memberRepository.findByUsernameAndAgeGreaterThan("member1", 13);
        assertTrue(members.size() == 2);
        members.forEach(m -> {
            assertEquals(m.getUsername(), "member1");
            assertTrue(m.getAge() > 13);
        });
    }

    @Test
    public void findUserQuery() throws Exception {
        Member member1 = new Member("member1", 15);
        Member member2 = new Member("member1", 20);
        Member member3 = new Member("member3", 12);
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);

        List<Member> findMembers = memberRepository.findUser("member3", 12);
        Member findMember = findMembers.get(0);
        assertEquals(findMembers.size(), 1);
        assertEquals(findMember, member3);
    }

    @Test
    public void findUsernameListQuery() throws Exception {
        Member member1 = new Member("member1", 15);
        Member member2 = new Member("member2", 20);
        Member member3 = new Member("member3", 12);
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);

        List<String> usernameList = memberRepository.findUsernameList();
        usernameList.forEach(u -> {
            System.out.println("username = " + u);
        });
    }

    @Test
    public void findMemberDTOQuery() throws Exception {
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");

        teamRepository.save(teamA);
        teamRepository.save(teamB);

        Member member1 = new Member("member1", 15, teamA);
        Member member2 = new Member("member2", 20, teamB);
        Member member3 = new Member("member3", 12, teamB);

        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);

        List<MemberDTO> memberDTO = memberRepository.findMemberDTO();
        memberDTO.forEach(m -> {
            System.out.println(m);
        });
    }

    @Test
    public void findByNamesQuery() throws Exception {
        Member member1 = new Member("member1", 15);
        Member member2 = new Member("member2", 20);
        Member member3 = new Member("member3", 12);
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);

        List<String> list = Arrays.asList("member1", "member3");
        List<Member> members = memberRepository.findByNames(list);
        assertTrue(members.size() == 2);
        members.forEach(m -> {
            System.out.println(m);
        });
    }


}

