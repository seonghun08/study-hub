package study.querydsl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.querydsl.dto.MemberDTO;
import study.querydsl.dto.QMemberDTO;
import study.querydsl.dto.UserDTO;
import study.querydsl.entity.Member;
import study.querydsl.entity.QMember;
import study.querydsl.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import java.util.List;

import static com.querydsl.jpa.JPAExpressions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static study.querydsl.entity.QMember.member;
import static study.querydsl.entity.QTeam.team;

@SpringBootTest
@Transactional
public class BasicTest {

    @PersistenceContext
    EntityManager em;

    JPAQueryFactory queryFactory;

    @BeforeEach
    public void before() {
        queryFactory = new JPAQueryFactory(em);
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
    }

    @Test
    public void startJPQL() throws Exception {
        String username = "member1";
        String qlString =
                "select m from Member m" +
                        " where m.username = :username";
        Member findMember = em.createQuery(qlString, Member.class)
                .setParameter("username", username)
                .getSingleResult();

        assertEquals(findMember.getUsername(), "member1");
    }

    @Test
    public void startQueryDSL() throws Exception {
        Member findMember = queryFactory
                .select(member)
                .from(member)
                .where(member.username.eq("member1"))
                .fetchOne();

        assertEquals(findMember.getUsername(), "member1");
    }

    @Test
    public void search() throws Exception {
        Member findMember = queryFactory
                .selectFrom(member)
                .where(member.username.eq("member1")
                        .and(member.age.eq(10)))
                .fetchOne();

        System.out.println("findMember = " + findMember);
    }

    @Test
    public void searchAndParam() throws Exception {
        Member findMember = queryFactory
                .selectFrom(member)
                .where(
                        // and 를 ,로 생략할 수도 있다.
                        member.username.eq("member1"),
                        member.age.between(10, 30)
                )
                .fetchOne();

        System.out.println("findMember = " + findMember);
    }

    /**
     * fetch() : 조회 대상이 여러 건 일 경우. 컬렉션 반환
     * fetchOne() : 조회 대상이 1건일 경우(1건 이상일 경우 에러). generic에 지정한 타입으로 반환
     * fetchFirst() : 조회 대상이 1건이든 1건 이상이든 무조건 1건만 반환. 내부에 보면 return limit(1).fetchOne() 으로 되어있다.
     * fetchCount() : 개수 조회. long 타입 반환
     * fetchResults() : 조회한 리스트 + 전체 개수를 포함한 QueryResults 반환. count 쿼리가 추가로 실행된다.
     * <p>
     * fetchResult(), fetchCount() => Deprecated
     * 간단한 쿼리는 잘 동작하는데, 복잡한 쿼리(다중그룹 쿼리)는 잘 작동하지 않는다고 한다.
     * 별도로 count 쿼리를 구해야 한다.
     */
    @Test
    public void resultFetch() throws Exception {
        List<Member> fetch = queryFactory
                .selectFrom(member)
                .fetch();

        Member fetchOne = queryFactory
                .selectFrom(member)
                // fetchOne() 값이 여러개 나올 경우 예외처리된다.
                .where(member.username.eq("member1"))
                .fetchOne();

        Member fetchFirst = queryFactory
                .selectFrom(member)
                .fetchFirst();


        QueryResults<Member> fetchResults = queryFactory
                .selectFrom(member)
                .fetchResults();

        long fetchCount = queryFactory
                .selectFrom(member)
                .fetchCount();
    }

    /**
     * 회원 정렬 순서
     * 1. 회원 나이 내림차순(desc)
     * 2. 회원 이름 올림차순(asc)
     * 단 2에서 회원 이름이 없으면 마지막에 출력(nulls last)한다.
     */
    @Test
    public void sort() throws Exception {
        em.persist(new Member(null, 100));
        em.persist(new Member("member5", 100));
        em.persist(new Member("member6", 100));

        List<Member> result = queryFactory
                .selectFrom(member)
                .where(member.age.eq(100))
                .orderBy(member.age.desc(), member.username.asc().nullsLast())
                .fetch();

        result.forEach(m -> {
            System.out.println(m);
        });

        Member member5 = result.get(0);
        Member member6 = result.get(1);
        Member memberNull = result.get(2);

        assertEquals(member5.getUsername(), "member5");
        assertEquals(member6.getUsername(), "member6");
        assertEquals(memberNull.getUsername(), null);
    }

    @Test
    public void paging() throws Exception {
        List<Member> result = queryFactory
                .selectFrom(member)
                .orderBy(member.username.desc())
                .offset(1)
                .limit(2)
                .fetch();

        Long totalCount = queryFactory
                .select(member.count())
                .from(member)
                .fetchFirst();

        assertEquals(result.size(), 2);
        assertEquals(totalCount, 4);
    }

    @Test
    public void aggregation() throws Exception {
        List<Tuple> result = queryFactory
                .select(
                        member.count(),
                        member.age.sum(),
                        member.age.avg(),
                        member.age.max(),
                        member.age.min()
                )
                .from(member)
                .fetch();

        Tuple tuple = result.get(0);
        assertEquals(tuple.get(member.count()), 4);
        assertEquals(tuple.get(member.age.sum()), 100);
        assertEquals(tuple.get(member.age.avg()), 25);
        assertEquals(tuple.get(member.age.max()), 40);
        assertEquals(tuple.get(member.age.min()), 10);
    }

    /**
     * 팀의 이름과 각 팀의 평균 연령을 구한다.
     */
    @Test
    public void group() throws Exception {
        List<Tuple> result = queryFactory
                .select(team.name, member.age.avg())
                .from(member)
                .join(member.team, team)
                .groupBy(team.name)
                .fetch();

        Tuple teamA = result.get(0);
        Tuple teamB = result.get(1);

        assertEquals(teamA.get(team.name), "teamA");
        assertEquals(teamA.get(member.age.avg()), 15);
        assertEquals(teamB.get(team.name), "teamB");
        assertEquals(teamB.get(member.age.avg()), 35);

        System.out.println("result = " + result);
        System.out.println("teamA = " + teamA);
        System.out.println("teamA.get(team.name) = " + teamA.get(team.name));
        System.out.println("teamA.get(member.age.avg()) = " + teamA.get(member.age.avg()));
    }

    /**
     * "teamA"에 소속된 모든 회원 조회
     */
    @Test
    public void join() throws Exception {
        List<Member> result = queryFactory
                .selectFrom(member)
                .join(member.team, team)
                .where(team.name.eq("teamA"))
                .fetch();

        assertThat(result.size()).isEqualTo(2);
        assertThat(result)
                .extracting("username")
                .contains("member1", "member2");

        result.forEach(m -> {
            System.out.println(m);
        });
    }

    /**
     * theta join (세타 조인)
     * 회원의 이름이 팀 이름과 같은 회원 조회
     */
    @Test
    public void theta_join() throws Exception {
        em.persist(new Member("teamA"));
        em.persist(new Member("teamB"));
        em.persist(new Member("teamC"));

        List<Member> result = queryFactory
                .select(member)
                .from(member, team)
                .where(member.username.eq(team.name))
                .fetch();

        assertThat(result)
                .extracting("username")
                .containsExactly("teamA", "teamB");
    }

    /**
     * ex) 회원과 팀을 조인하면서, 팀 이름이 teamA인 팀만 조인, 회원은 모두 조회
     * JPQL: select m, t
     * from Member m
     * left join m.team t on t.name = 'teamA'
     */
    @Test
    public void join_on_filtering() throws Exception {
        List<Tuple> result = queryFactory
                .select(member, team)
                .from(member)
                // leftJoin 으로 Member 를 전부 가져오고 팀이 teamA 인 경우만 가져오고 나머진 null
                .leftJoin(member.team, team).on(team.name.eq("teamA"))
                .fetch();
        // on 절을 활용해 조인 대상을 필터링 할 경우 inner join 을 사용하면, where 절을 사용하도록 하자.
        // leftJoin, 즉 외부조인이 필요한 경우에만 on 절 기능을 사용하자.

        for (Tuple tuple : result) {
            System.out.println("tuple = " + tuple);
        }
    }

    /**
     * 연관관계가 없는 Entity 외부 조인
     * 회원의 이름이 팀 이름과 같은 대상 외부 조인
     */
    @Test
    public void join_on_no_relation() throws Exception {
        em.persist(new Member("teamA"));
        em.persist(new Member("teamB"));
        em.persist(new Member("teamC"));

        List<Tuple> result = queryFactory
                .select(member, team)
                .from(member)
                .leftJoin(team).on(member.username.eq(team.name))
                .fetch();

        for (Tuple tuple : result) {
            System.out.println("tuple = " + tuple);
        }
    }

    @PersistenceUnit
    EntityManagerFactory emf;

    @Test
    public void fetchJoinNo() throws Exception {
        em.flush();
        em.clear();

        Member findMember = queryFactory
                .selectFrom(member)
                .where(member.username.eq("member1"))
                .fetchOne();

        // 로딩된 Entity 인지 아닌지 확인하는 방법
        boolean loaded = emf.getPersistenceUnitUtil().isLoaded(findMember.getTeam());
        assertThat(loaded).as("페치 조인 미적용").isFalse();
    }

    @Test
    public void fetchJoinUse() throws Exception {
        em.flush();
        em.clear();

        Member findMember = queryFactory
                .selectFrom(member)
                // fetchJoin
                .join(member.team, team).fetchJoin()
                .where(member.username.eq("member1"))
                .fetchOne();

        // 로딩된 Entity 인지 아닌지 확인하는 방법
        boolean loaded = emf.getPersistenceUnitUtil().isLoaded(findMember.getTeam());
        assertThat(loaded).as("페치 조인 적용").isTrue();
    }

    /**
     * 나이가 가장 많은 회원 조회
     */
    @Test
    public void subQuery() throws Exception {
        QMember memberSub = new QMember("memberSub");

        List<Member> result = queryFactory
                .selectFrom(member)
                .where(member.age.eq(
                        // 서브쿼리는 값이 중복되면 안된다.
                        // memberSub 새로 생성
                        select(memberSub.age.max())
                                .from(memberSub)
                ))
                .fetch();

        assertThat(result)
                .extracting("age")
                .containsExactly(40);
    }

    /**
     * 나이가 평균 이상인 회원 조회
     */
    @Test
    public void subQueryGoe() throws Exception {
        QMember memberSub = new QMember("memberSub");

        List<Member> result = queryFactory
                .selectFrom(member)
                .where(member.age.goe(
                        select(memberSub.age.avg())
                                .from(memberSub)
                ))
                .fetch();

        assertThat(result)
                .extracting("age")
                .containsExactly(30, 40);
    }

    @Test
    public void subQueryIn() throws Exception {
        QMember memberSub = new QMember("memberSub");

        List<Member> result = queryFactory
                .selectFrom(member)
                .where(member.age.in(
                        select(memberSub.age)
                                .from(memberSub)
                                .where(memberSub.age.gt(10))
                ))
                .fetch();

        assertThat(result)
                .extracting("age")
                .containsExactly(20, 30, 40);
    }

    /**
     * from 절에는 서브쿼리를 사용할 수 없는 한계가 있다.
     * 이를 해결하는 방법으로...
     * <p>
     * 1. 서브쿼리를 join 으로 변경한다. (가능한 상황도 있고, 불가능한 상황도 있음)
     * 2. 쿼리를 2번 분리해서 실행한다.
     * 3. 도저히 해결히 안될 경우 NativeSQL 을 사용한다.
     */
    @Test
    public void selectSubQuery() throws Exception {
        QMember memberSub = new QMember("memberSub");

        List<Tuple> result = queryFactory
                .select(member.username,
                        select(memberSub.age.avg())
                                .from(memberSub)
                )
                .from(member)
                .fetch();

        result.forEach(m -> {
            System.out.println(m);
        });
    }

    @Test
    public void basicCase() throws Exception {
        List<String> result = queryFactory
                .select(member.age
                        .when(10).then("열살")
                        .when(20).then("스무살")
                        .otherwise("기타")
                )
                .from(member)
                .fetch();

        result.forEach(s -> {
            System.out.println(s);
        });
    }

    /**
     * 보통은 데이터를 최소화하여 가져오는 것에 집중하고, DB 내 계산하는 것보다 데이터를 뽑아와서 직접 계산하는 것이 좋다.
     */
    @Test
    public void complexCase() throws Exception {
        List<String> result = queryFactory
                // 복잡한 케이스는 CaseBuilder() 사용한다.
                .select(new CaseBuilder()
                        .when(member.age.between(0, 20)).then("0~20살")
                        .when(member.age.between(21, 30)).then("21~30살")
                        .otherwise("기타")
                )
                .from(member)
                .fetch();

        result.forEach(s -> {
            System.out.println(s);
        });
    }

    @Test
    public void constant() throws Exception {
        List<Tuple> result = queryFactory
                .select(member.username, Expressions.constant("임의 값"))
                .from(member)
                .fetch();

        result.forEach(m -> {
            System.out.println(m);
        });
    }

    @Test
    public void concat() throws Exception {
        List<String> result = queryFactory
                // {username}_{age}
                // stringValue() => 문자가 아닌 다른 타입들을 문자로 변환할 수 있다.
                // 특히 Enum 을 처리할 때 자주 사용한다!!
                .select(member.username.concat("_").concat(member.age.stringValue()))
                .from(member)
                .fetch();

        result.forEach(m -> {
            System.out.println(m);
        });
    }

    @Test
    public void simpleProjections() throws Exception {
        List<String> result = queryFactory
                .select(member.username)
                .from(member)
                .fetch();

        result.forEach(m -> {
            System.out.println(m);
        });
    }

    /**
     * 가급적으로 Repository 안에서만 쓰고 DTO 로 변환해서 사용하자.
     */
    @Test
    public void tupleProjections() throws Exception {
        List<Tuple> result = queryFactory
                .select(member.username, member.age)
                .from(member)
                .fetch();

        result.forEach(t -> {
            String username = t.get(member.username);
            Integer age = t.get(member.age);
            System.out.println("username = " + username);
            System.out.println("age = " + age);
        });
    }

    @Test
    public void findDtoByJPQL() throws Exception {
        List<MemberDTO> result = em.createQuery(
                        "select new study.querydsl.dto.MemberDTO(m.username, m.age) " +
                                " from Member m", MemberDTO.class
                )
                .getResultList();

        result.forEach(m -> {
            System.out.println(m);
        });
    }

    /**
     * DTO 조회 방식 3가지
     * Projections
     * 1. bean() :: 기본 생성자를 만들고 setter 로 값을 넣어주는 방식 (필수: 기본생성자, setter)
     * 2. fields() :: 기본 생성자를 만들고 파라미터에 맞는 생성자에 값을 넣어주는 방식 (필수: 기본생성자, 파라미터에 맞는 생성자)
     * 3. constructor() :: 파라미터에 맞는 생성자에 바로 값을 넣어주는 방식 (필수: 파라미터에 맞는 생성자)
     */
    @Test
    public void findDtoBySetter() throws Exception {
        List<MemberDTO> result = queryFactory
                // bean() 는 기본 생성자를 만든 후 setter 로 값을 넣어주기 때문에 MemberDTO 의 기본 생성자가 있어야 한다.
                .select(Projections.bean(MemberDTO.class, member.username, member.age))
                .from(member)
                .fetch();

        result.forEach(m -> {
            System.out.println(m);
        });
    }

    @Test
    public void findDtoByField() throws Exception {
        List<MemberDTO> result = queryFactory
                // fields() 는 getter, setter 없이 바로 값을 넣어준다.
                .select(Projections.fields(MemberDTO.class, member.username, member.age))
                .from(member)
                .fetch();

        result.forEach(m -> {
            System.out.println(m);
        });
    }

    @Test
    public void findDtoByConstructor() throws Exception {
        List<MemberDTO> result = queryFactory
                // constructor() 는 파라미터에 맞는 생성자에 바로 값을 넣기 때문에 기본 생성자가 필요하지 않는다.
                .select(Projections.constructor(
                        MemberDTO.class,
                        member.username,
                        member.age
                ))
                .from(member)
                .fetch();

        result.forEach(m -> {
            System.out.println(m);
        });
    }

    /**
     * fields()로 DTO 조회를 할 경우 필드명이 서로 같아야 한다.
     * 이를 해결하는 방식으로 원하는 값을 넣을 때 as() 를 이용하면 된다.
     */
    @Test
    public void findUserDtoField() throws Exception {
        QMember memberSub = new QMember("memberSub");

        List<UserDTO> result = queryFactory
                .select(Projections.fields(
                        UserDTO.class,

                        // 서로 같은 방식
                        // ExpressionUtils.as(member.username, "name"),
                        member.username.as("name"),

                        // member.age 대신 서브쿼리로 age.max 값을 넣으려면 ExpressionUtils.as() 사용해야 한다.
                        ExpressionUtils.as(
                                select(memberSub.age.max())
                                .from(memberSub), "age")

                ))
                .from(member)
                .fetch();

        result.forEach(m -> {
            System.out.println(m);
        });
    }

    /**
     * constructor()는 타입을 보고 생성자에 넣어주기 때문에 필드명이 달라도 상관없다.
     */
    @Test
    public void findUserDtoConstructor() throws Exception {
        QMember memberSub = new QMember("memberSub");

        List<UserDTO> result = queryFactory
                .select(Projections.constructor(
                        UserDTO.class,
                        // name
                        member.username,
                        // age
                        ExpressionUtils.as(
                                select(memberSub.age.max())
                                        .from(memberSub), "age")
                ))
                .from(member)
                .fetch();

        result.forEach(m -> {
            System.out.println(m);
        });
    }

    /**
     * @QueryProjection :: QXxxDTO 생성
     * 단점 (Projections 보다 편한 기능을 제공하지만)
     * 1. Q 파일을 따로 생성해야한다.
     * 2. 순수 DTO 객체가 queryDSL 에 의존적이게 된다.
     */
    @Test
    public void findDtoByQueryProjection() throws Exception {
        List<MemberDTO> result = queryFactory
                .select(new QMemberDTO(member.username, member.age))
                .from(member)
                .fetch();

        result.forEach(m -> {
            System.out.println(m);
        });
    }

    /**
     * 동적 쿼리 BooleanBuilder 방식
     * BooleanBuilder builder = new BooleanBuilder();
     */
    @Test
    public void dynamicQuery_BooleanBuilder() throws Exception {
        String usernameCond = "member1";
        Integer ageCond = 10;

        List<Member> result = searchMember1(usernameCond, ageCond);

        for (Member m : result) {
            System.out.println(m);
        }
        assertThat(result.size()).isEqualTo(1);
    }

    private List<Member> searchMember1(String usernameCond, Integer ageCond) {
        BooleanBuilder builder = new BooleanBuilder();
//        BooleanBuilder builder = new BooleanBuilder(member.username.eq(usernameCond));

        if (usernameCond != null) {
            builder.and(member.username.eq(usernameCond));
        }

        if (ageCond != null) {
            builder.and(member.age.eq(ageCond));
        }

        return queryFactory
                .selectFrom(member)
                .where(builder)
                .fetch();
    }

    /**
     * 동적쿼리 메서드로 값을 뽑는 방식
     * whereParam
     */
    @Test
    public void dynamicQuery_WhereParam() throws Exception {
        String usernameCond = "member1";
        Integer ageCond = 10;

        List<Member> result = searchMember2(usernameCond, ageCond);

        for (Member m : result) {
            System.out.println(m);
        }
        assertThat(result.size()).isEqualTo(1);
    }

    private List<Member> searchMember2(String usernameCond, Integer ageCond) {
        return queryFactory
                .selectFrom(member)
//                .where(usernameEq(usernameCond), ageEq(ageCond))
                .where(allEq(usernameCond, ageCond))
                .fetch();
    }

    private BooleanExpression usernameEq(String usernameCond) {
        return usernameCond != null ? member.username.eq(usernameCond) : null;
    }

    private BooleanExpression ageEq(Integer ageCond) {
        return ageCond != null ? member.age.eq(ageCond) : null;
    }

    private BooleanExpression allEq(String usernameCond, Integer ageCond) {
        return usernameEq(usernameCond).and(ageEq(ageCond));
    }

    /**
     * 벌크성 수정 쿼리
     */
    @Test
    public void bulkUpdate() {
        // count => 영향을 받은 로우 수
        long count = queryFactory
                .update(member)
                .set(member.username, "비회원")
                .where(member.age.lt(28))
                .execute();

        em.flush();
        em.clear();

        List<Member> result = queryFactory
                .selectFrom(member)
                .fetch();

        result.forEach(m -> {
            System.out.println(m);
        });

        assertThat(count).isEqualTo(2);
    }
    
    @Test
    public void bulkAdd() throws Exception {
        long count = queryFactory
                .update(member)
                .set(member.age, member.age.add(1))
//                .set(member.age, member.age.multiply(2))
                .execute();
    }

    @Test
    public void bulkDelete() throws Exception {
        long count = queryFactory
                .delete(member)
                .where(member.age.gt(18))
                .execute();
    }

    @Test
    public void sqlFunction() throws Exception {
        List<String> result = queryFactory
                .select(Expressions.stringTemplate(
                        "function('replace', {0}, {1}, {2})",
                        member.username, "member", "M"
                ))
                .from(member)
                .fetch();

        result.forEach(m -> {
            System.out.println(m);
        });
    }

    @Test
    public void sqlFunction2() throws Exception {
        List<String> result = queryFactory
                .select(member.username)
                .from(member)
//                .where(member.username.eq(Expressions.stringTemplate(
//                        "function('lower', {0})",
//                        member.username
//                )))
                .where(member.username.eq(member.username.lower()))
                .fetch();

        result.forEach(m -> {
            System.out.println(m);
        });
    }
}

