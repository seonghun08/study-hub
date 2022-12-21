# data-jpa
스프링 데이터 JPA 실무 사용법

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
@Modifying(clearAutomatically = true)
@Query("update Member m set m.age = m.age + 1 where m.age >= :age")
int bulkAgePlus(@Param("age") int age);
```
벌크 연산을 보내고 다음 로직이 같은 트랜젝션에서 벌어지면 영속성 컨텍스트 내에 있는 데이터를 지워야 한다.<br/>
__영속성 컨텍스트를 무시하고 바로 DB에 방영하기 때문에 영속성 컨텍스트는 변경된 값을 알 수 없다.__<br/>
