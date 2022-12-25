# queryDSL

### **Q 파일 생성 장소**

Q타입은 컴파일 시점에 자동 생성되므로 git에서 버전관리(GIT)에 포함하지 않는 것이 좋다.

라이브러리 버전이 올라가면 세부적인 부분이 달라질 수 있다.

```java
def querydslDir = "$buildDir/generated/querydsl"
```

이를 해결하는 방법으로 builde 폴더는 보통 ignore로 해두기 때문에 git에 올라가지 않게 해둔다.

### **JPAqueryFactory 사용 예시**

```java
@Bean
	JPAQueryFactory jpaQueryFactory(EntityManager entityManager) {
		return new JPAQueryFactory(entityManager);
	}
```

```java
@RequiredArgsConstructor
public class RepositoryImpl... {

**private final JPAQueryFactory queryFactory;**
...

	public void findUsername(String username) {
		// QMember m = new QMember("m");
		QMember m = QMember.member;

		Member findMember = queryFactory
			.selectFrom(m)
			.where(m.username.eq(username))
			.fetchOne();
		...
	}
}
```

@Bean으로 등록하고, @Lombok을 통해 주입받아서 사용한다.

### **queryDSL 검색 조건 정리**

```java
member.username.eq("member") // username = 'member'
member.username.ne("member") // username != 'member'
member.username.eq("member").not() // username != 'member'

member.username.isNotNull() //이름이 is not null

member.age.in(10, 20) // age in (10,20)
member.age.notIn(10, 20) // age not in (10, 20)
member.age.between(10,30) // between 10, 30

member.age.goe(30) // age >= 30
member.age.gt(30) // age > 30
member.age.loe(30) // age <= 30
member.age.lt(30) // age < 30

member.username.like("member%") // like 검색
member.username.contains("member") // like ‘%member%’ 검색
member.username.startsWith("member") // like ‘member%’ 검색
...
```

### fetch 종류

```java
public void fetch() throws Exception {
	List<Member> fetch = queryFactory
		.selectFrom(member)
		.orderBy(member.age.desc(), member.username.asc().nullsLast())
		.offset(0) // 0 부터 시작(zero index)
		.limit(2) // 최대 2건 조회
		.fetch();

	Member fetchOne = queryFactory
		.selectFrom(member)
		.where(member.username.eq("user"))
		// fetchOne() => 결과값이 1개를 초과하면 예외처리된다.
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
```

fetch() : List 조회, 데이터 없으면 빈 리스트로 반환
fetchOne() : 단건 조회
* 결과가 없으면 : null
* 결과가 둘 이상이면 : com.querydsl.core.NonUniqueResultException
fetchFirst(): limit(1).fetchOne();

`Deprecated 간단한 쿼리는 잘 동작하는데, 복잡한 쿼리(다중그룹 쿼리)는 잘 작동하지 않는다.` **
fetchResults() : 페이징 정보 포함, total count 쿼리 추가 실행
fetchCount() : count 쿼리로 변경해서 count 수 조회

## Join 문법

```java
.join(조인 대상, 별칭으로 사용할 Q타입)
```

| join() , innerJoin() | 내부 조인(inner join) |
| --- | --- |
| leftJoin() | left 외부 조인(left outer join) |
| rightJoin() | rigth 외부 조인(rigth outer join) |

**join**

- “teamA”에 소속된 회원 조회

```java
List<Member> result = queryFactory
	.selectFrom(member)
	.join(member.team,team)
	.where(team.name.eq("teamA"))
	.fetch();
```

**theta join** 세타조인**:** 연관관계가 없는 필드로 조인

- 회원의 이름이 팀 이름과 같은 회원 조회(세타조인)

```java
List<Member> result = queryFactory
	.select(member)
	.from(member, team)
	.where(member.username.eq(team.name))
	.fetch();
```

**left join, on**

- 회원과 팀을 조인하면서, 팀 이름이 “teamA”인 팀만 조인, 회원은 모두 조회

```java
List<Tuple> result = queryFactory
	.select(member, team)
	.from(member)
	.leftJoin(member.team, team).on(team.name.eq("teamA"))
	.fetch();
```

on 절을 활용해 조인 대상을 필터링 할 경우 inner join이면 where절을 사용하도록 하자.

left join, 즉 외부조인이 필요한 경우만 on절 기능을 사용하자.

- 연관관계 없는 엔티티 외부조인, 회원 이름과 팀 이름이 같은 대상 외부조인

```java
List<Tuple> result = queryFactory
	.select(member, team)
	.from(member)
	.leftJoin(team).on(member.username.eq(team.name))
	.fetch();
```

hibernate5.1 부터 on을 사용해서 서로 관계가 없는 필드로 외부조인을 할 수 있게 됐다. 물론 내부 조인도 가능하다.

**일반적인 조인과 다르게 join 부분에 엔티티 하나만 들어간다.**

```java
일반조인: leftJoin(member.team, team)
**on** 조인: from(member).leftJoin(team).on(xxx)
```

**fetch join**

- 즉시로딩으로 Member, Team sql 쿼리 조인으로 한번에 조회

```java
Member findMember = queryFactory
	.selectFrom(member)
	.join(member.team, team).fetchJoin()
	.where(member.username.eq("user"))
	.fetchOne();
```

fetch join은 성능 최적화를 위해 여러번 나가는 sql 쿼리를 한번에 모아서 보낸다.

**서브쿼리**

- 회원의 평균 나이 이상인 회원 조회

```java
QMember memberSub = new QMember("memberSub");

List<Member> result = queryFactory
	.selectFrom(member)
	.where(member.age.goe(
		JPAExpressions
			.select(memberSub.age.avg())
			.from(memberSub)
		))
.fetch();
```

서브 쿼리는 select 절에 사용되는 Q 객체와 동일해서는 안된다. 서브 쿼리를 위한 새로운 Q 객체를 만들어야 한다.

**from 절의 서브쿼리 한계**
JPA JPQL 서브쿼리의 한계로 from 절의 서브쿼리는 지원하지 않는다. 

**from 절의 서브쿼리 해결방안**

1. 서브쿼리를 join으로 변경한다. (가능한 상황도 있고, 불가능한 상황도 있다.)
2. 쿼리를 2번 분리해서 실행한다.
3. 도저히 해결이 안될 경우 NativeSQL을 사용한다.

## DTO 조회 방식

### **Projections**

- **bean()**: 기본 생성자를 만들고 setter로 값을 넣어주는 방식
    - setter가 일치해야 한다.
- **fileds()**: 기본 생성자를 만들고 파라미터에 맞는 생성자에 값을 넣어주는 방식
    - 필드명이 서로 같아야 한다.
    - 이를 해결하는 방식은 매칭할 값에 .as()로 필드명을 붙여준다.
- **constructor()**: 파라미터에 맞는 생성자에 바로 값을 넣어주는 방식
    - 타입을 보고 생성자에 넣어주기 때문에 필드명이 달라도 상관없다.

```java
List<MemberDTO> result = queryFactory
	// 기본 생성자, setter 필수
	.select(Projections.bean(MemberDTO.class, member.username, member.age))
	// 기본생성자, 파라미터에 맞는 생성자
	.select(Projections.fields(MemberDTO.class, member.username, member.age))
	// 파라미터에 맞는 생성자
	.select(Projections.constructor(MemberDTO.class, member.username, member.age))
	.from(member)
	.fetch();
```

### @QueryProjection

```java
...
@QueryProjection
public MemberDto(String username, int age) {
this.username = username;
this.age = age;
}
```

DTO에 설정할 생성자에 @QueryProjection 넣어준다.

```java
List<MemberDto> result = queryFactory
.select(new QMemberDto(member.username, member.age))
.from(member)
.fetch();
```

@QueryProjection는 컴파일러로 타입을 체크할 수 있으므로 가장 안전한 방법이다. 하지만 DTO에 queryDSL에 의존해야 하는 점과 DTO까지 Q파일을 생성해야 하는 단점이 있다.

## 동적쿼리

동적 쿼리를 해결하는 방식은 2가지가 있다.

- **BooleanBuilder**
    - BooleanBuilder builder = new BooleanBuilder();

```java
public void BooleanBuilder() throws Exception {
	String usernameParam = "user";
	Integer ageParam = 10;

	List<Member> result = search_BooleanBuilder(usernameParam, ageParam);
	...
}

private List<Member> search_BooleanBuilder(String usernameCond, Integer ageCond) {
	BooleanBuilder builder = new BooleanBuilder();

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
```

- **Where** 다중 파라미터 사용
    - where 조건에 null 값은 무시가 된다.
    - 메서드를 다른 쿼리에서도 재활용 할 수 있는 장점을 가지고 있다.
    - 쿼리 자체의 가독성이 높아진다.

```java
public void WhereParam() throws Exception {
	String usernameParam = "member1";
	Integer ageParam = 10;
	List<Member> result = search_WhereParam(usernameParam, ageParam);
	...
}

private List<Member> search_WhereParam(String usernameCond, Integer ageCond) {
	return queryFactory
		.selectFrom(member)
		.where(usernameEq(usernameCond), ageEq(ageCond))
		.fetch();
}

private BooleanExpression usernameEq(String usernameCond) {
	return usernameCond != null ? member.username.eq(usernameCond) : null;
}

private BooleanExpression ageEq(Integer ageCond) {
	return ageCond != null ? member.age.eq(ageCond) : null;
}
```

- **조합가능**
    - 조합해서 사용할 경우 null 체크를 따로 해야 한다.

```java
private BooleanExpression allEq(String usernameCond, Integer ageCond) {
	// try{}... null 처리
	return usernameEq(usernameCond).and(ageEq(ageCond));
}
```

**BooleanBuilder는 null을 방지하기 때문에 이러한 경우 합쳐 쓰는 것도 괜찮은 선택일 듯 하다.** 

```java
private BooleanBuilder allEq(MemberSearchCondition condition) {
	BooleanBuilder builder = new BooleanBuilder();

	return builder
		.and(usernameEq(condition.getUsername()))
		.and(ageEq(condition.getAge()));
}
```

## 수정, 삭제 벌크 연산

- 20살인 회원을 모두 삭제

```java
long count = queryFactory
.delete(member)
.where(member.age.gt(20))
.execute();
```

JPQL 배치와 마찬가지로, 영속성 컨텍스트에 있는 엔티티를 무시하고 실행되기 때문에 배치 쿼리를 실행하고 나면 영속성 컨텍스트를 초기화 하는 것이 안전하다.