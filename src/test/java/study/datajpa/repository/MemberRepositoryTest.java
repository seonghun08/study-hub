package study.datajpa.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Transactional
@SpringBootTest
class MemberRepositoryTest {

    @Autowired MemberRepository memberRepository;
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
}
