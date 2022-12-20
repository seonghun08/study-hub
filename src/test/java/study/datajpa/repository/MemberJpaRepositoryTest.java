package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
//@Rollback(value = false)
class MemberJpaRepositoryTest {

    @Autowired MemberJpaRepository memberJpaRepository;

    @Test
    public void paging() throws Exception {
        // given 이러한 데이터가 있을 때
        memberJpaRepository.save(new Member("member1", 10));
        memberJpaRepository.save(new Member("member2", 10));
        memberJpaRepository.save(new Member("member3", 10));
        memberJpaRepository.save(new Member("member4", 10));
        memberJpaRepository.save(new Member("member5", 10));
        memberJpaRepository.save(new Member("member6", 10));

        int age = 10;
        int offset = 0;
        int limit = 3;

        // when 이렇게 하면
        List<Member> members = memberJpaRepository.findByPage(age, offset, limit);
        long totalCount = memberJpaRepository.totalCount(age);

        // then
        assertEquals(members.size(), 3);
        assertEquals(totalCount, 6);

        members.forEach(m -> {
            System.out.println(m);
        });
    }

    @Test
    public void bulkUpdate() throws Exception {
        for (int i = 1; i < 10; i++) {
            memberJpaRepository.save(new Member("member" + i, 10+i));
        }

        List<Member> members = memberJpaRepository.findAll();
        members.forEach(m -> {
            System.out.println(m);
        });

        int i = memberJpaRepository.bulkAgePlus(10);
        assertEquals(i, 9);
    }
}