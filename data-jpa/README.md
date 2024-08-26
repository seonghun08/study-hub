# data-jpa
스프링 데이터 JPA 실무 적용 정리

## @NamedQuery
```java
// Member Entity
@NamedQuery(
        name = "Member.findByUsername",
        query = "select m from Member m where m.username = :username"
)
```

```java
// JpaRepository<Member, Long>
@Query(name = "Member.findByUsername")
List<Member> findByUsername(String username);
```
일반적으로 NamedQuery는 실무에서 사용하지 않는다.<br/>
JPA를 직접 호출 할 경우 EntityManager.createNamedQuery()를 사용하면 된다.



## @Query
```java
// JpaRepository<Member, Long>
@Query("select m from Member m where m.username = :username and m.age = :age")
List<Member> findUser(@Param("username") String username, @Param("age") int age);
```
@Query는 정적 쿼리를 직접 작성하므로 이름 없는 NamedQuery라고 할 수 있다.<br/>
애플리케이션 실행 시점 문법 오류를 발견할 수 있는 장점을 가지고 있다.<br/>
__코드 가독성과 유지보수를 위해 위치 기반이 아닌 이름 기반 파라미터 바인딩을 사용하자.__

```java
// JpaRepository<Member, Long>
@Query("select new study.datajpa.dto.MemberDTO(m.id, m.username, t.name) from Member m join m.team t")
List<MemberDTO> findMemberDTO();
```
DTO로 직접 조회하려면 new를 사용해야하며 위와 같이 생성자가 맞는 DTO가 필요하다.<br/>



## 페이징과 정렬
```java
// JpaRepository<Member, Long>
@Query(
        value = "select m from Member m left outer join fetch m.team t",
        countQuery = "select count(m.username) from Member m"
)
Page<Member> findByAge(@Param("age") int age, Pageable pageable);
```
쿼리에 left outer join을 쓴 건 Member 객체에 Team 객체가 null일 경우도 값이 채워지기 위해 사용했다.<br/>

```java
// JpaRepositoryTest
PageRequest pageRequest = PageRequest.of(0, 3, Sort.Direction.DESC, "id");
Page<Member> page = memberRepository.findByAge(age, pageRequest);

// 현재 페이지 내 요소 개수
page.stream().count();
// 총 요소 개수
page.getTotalElements();
// 현재 페이지
page.getNumber();
// 총 페이지 개수
page.getTotalPages();
// 첫번째 페이가 맞는가
page.isFirst();
// 다음 페이지가 있는가
page.hasNext();
```
countQuery는 count를 얻는 쿼리를 최적화할 수 있게 분리할 수 있다.<br/>

```java
Page<MemberDTO> map = page.map(p -> new MemberDTO(p.getId(), p.getUsername(), p.getTeam() == null ? null : p.getTeam().getName()));
```
API로 보낼 경우 Entity 객체로 반환하면 안되기 때문에 page를 얻고 따로 page.map을 통해 DTO로 변환해야한다.<br/>
이와 다른 방식으로 Slice가 있는데 Page를 Slice를 바꿔주면 된다.<br/>
__Page :: 게시판처럼 총 데이터 갯수가 필요한 환경에서 사용__<br/>
__Slice :: 모바일과 같이 총 데이터 갯수가 필요없는 환경 즉 무한스크롤 등에 사용__<br/>



## 벌크성 수정쿼리
DB의 데이터를 수정하는 경우 JPA는 Entity 객체를 사용하기 때문에 객체마다 쿼리를 발생시킨다.<br/>
벌크성 수정쿼리는 이를 한번에 수정해주는 역할을 한다.<br/>
```java
// JpaRepository<Member, Long>
@Modifying(clearAutomatically = true)
@Query("update Member m set m.age = m.age + 1 where m.age >= :age")
int bulkAgePlus(@Param("age") int age);
```
벌크 연산을 보내고 다음 로직이 같은 트랜젝션에서 벌어지면 영속성 컨텍스트 내에 있는 데이터를 지워야 한다.<br/>
__영속성 컨텍스트를 무시하고 바로 DB에 방영하기 때문에 영속성 컨텍스트는 변경된 값을 알 수 없다.__<br/>



## @EntityGraph
```java
// JpaRepository<Member, Long>
@EntityGraph(attributePaths = {"team"})
List<Member> findByUsername(String username)
```

```java
// Member Entity
@NamedEntityGraph(
        name = "Member.all",
        attributeNodes = @NamedAttributeNode("team")
)

// JpaRepository<Member, Long>
@EntityGraph(value = "Member.all")
    List<Member> findEntityGraphByUsername(@Param("username") String username);
```
연관된 Entity들을 SQL 한번에 조회하는 방법 __N+1 문제 해결__<br/>
JPQL 없이 fetch join을 사용할 수 있다.



## Hint & Lock
```java
// JpaRepository<Member, Long>
// @Transactional(readOnly = true)
@QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))
Member findReadOnlyByUsername(String username);
```
spring5.1 ver 이후 @Transaction(readOnly=true)로 설정하면, @QueryHint의 readOnly까지 모두 동작한다.<br/>
@Transaction(readOnly=true)는 트랜젝션 커밋 시점에 flush를 하지 않기 때문에 변경감지 비용이 없다.

```java
@Lock(LockModeType.PESSIMISTIC_WRITE)
Member findLockByUsername(String username);
```
락은 동시에 같은 데이터를 수정하는 하는 오류를 방지할 수 있다.<br/>
실무에서 락은 최후의 보루 정도로 생각해야 한다. 락은 내용이 깊기 때문에 따로 더 공부하도록 하자.<br/>
https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-%EB%8D%B0%EC%9D%B4%ED%84%B0-JPA-%EC%8B%A4%EC%A0%84/unit/28020?category=questionDetail&tab=community&q=92603



## Auditing
```java
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Getter
public class BaseEntity {

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdDate;
    
    @LastModifiedDate
    private LocalDateTime lastModifiedDate;
    ......
}
```

```java
// App
@Bean
public AuditorAware<String> auditorProvider() {
    return () -> Optional.of(UUID.randomUUID().toString());
}
```
실무에서는 세션 정보를 넣거나, 스프링 시큐리티 정보에서 ID를 받는다.<br/>
Entity를 저장하거나, 수정할 때 따로 값을 넣을 필요없이 자동으로 값을 넣어준다.



## Projections
Entity 대신 DTO를 편리하게 조회할 때 사용한다.<br/>
전체 Entity가 아니라 간단하게 회원 이름만 조회하고 싶은 경우 유용하다.
```java
public interface UsernameOnly {
    @Value("#{'이름: ' + target.username + ', 나이: ' + target.age}")
    String getUsername();
}
```
조회 할 Entity의 필드를 getter 형식으로 지정하면 해당 필드만 선택해서 조회(Projection)한다.

```java
// JpaRepository<Member, Long>
List<UsernameOnly> findProjectionsByUsername(String username);
```
타입만 지정해주면 사용할 수 있으며, SQL에서 select 절에서 username만 조회된다.<br/>
DTO 형식도 가능하며 Class를 생성하여 생성자의 파라미터로 매칭한다.

```java
// JpaRepository<Member, Long>
<T> List<T> findProjectionsByUsername(String username, Class<T> type);

// JpaRepositoryTest
List<UsernameOnly> result = memberRepository.findProjectionsByUsername("name", UsernameOnly.class);
```
Generic type으로 동적 Projections도 가능하다.
파라미터에 맞는 생성자를 포함한 Class를 넣어주면 된다.

__Entity 단일 조회는 최적화가 되지만 join을 통해 다른 Entity까지 가져올 경우 최적화가 안된다.__<br/>
__위와 같은 한계가 있기 때문에 실무에서 단순할 때만 사용하고, 조금만 복잡해지면 QueryDSL을 사용하자.__



## Native Query 
JPA는 Native Query를 지원한다.
```java
@Query(value = "select * from member where username = ?", nativeQuery =true)
Member findByNativeQuery(String username);
```
Native Query는 JPQL로 해결이 안되고 JdbcTemplate or myBatis로도 도저히 해결이 안될 때 사용하도록 하자.

```java
@Query(
        value = "SELECT m.member_id as id, m.username, t.name as teamName " + 
                "FROM member m left join team t ON m.team_id = t.team_id",
        countQuery = "SELECT count(*) from member",
        nativeQuery = true
)
Page<MemberProjection> findByNativeProjection(Pageable pageable);
```
Native Query + 인터페이스 기반 Projections을 활용하는 방법도 있다.<br/>
Page가 되는 장점을 가졌으니 알아두고 필요해보일 때 써보도록 하자.
