package study.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import study.querydsl.entity.Member;
import study.querydsl.entity.QMember;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
@Commit
class AppTests {

	@PersistenceContext EntityManager em;

	@Test
	void contextLoads() {
		Member member = new Member();
		em.persist(member);

		JPAQueryFactory query = new JPAQueryFactory(em);
//		QMember qMember = new QMember("m");
		QMember qMember = QMember.member;

		Member result = query
				.selectFrom(qMember)
				.fetchOne();

		assertEquals(result, member);
		assertEquals(result.getId(), member.getId());
	}

}
