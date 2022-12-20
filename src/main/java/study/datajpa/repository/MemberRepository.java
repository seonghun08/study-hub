package study.datajpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDTO;
import study.datajpa.entity.Member;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.Collection;
import java.util.List;

@Transactional(readOnly = true)
public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {

    List<Member> findByUsername(String username);

    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    @Query(
            "select m from Member m" +
                    " where m.username = :username" +
                    " and m.age = :age"
    )
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    @Query("select m.username from Member m")
    List<String> findUsernameList();

    @Query(
            "select new study.datajpa.dto.MemberDTO(m.id, m.username, t.name)" +
                    " from Member m" +
                    " join m.team t"
    )
    List<MemberDTO> findMemberDTO();

    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") Collection<String> names);

    @Query(
            value = "select m from Member m where m.age in :age",
            // countQuery 를 위한 최적화 기능을 제공한다.
            countQuery = "select count(m.username) from Member m"
    )
    Page<Member> findByAge(@Param("age") int age, Pageable pageable);

    @Query(
            value = "select m from Member m left outer join fetch m.team t",
            countQuery = "select count(m.username) from Member m")
    Page<Member> findDTOByAge(Pageable pageable);

    @Query(value = "select m from Member m join fetch m.team t")
    List<Member> findSampleByAge(int age);

    Slice<Member> findSliceByAge(int age, Pageable pageable);

    // clearAutomatically = true :: em.clear()
    @Modifying(clearAutomatically = true)
    @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);

    @Query("select m from Member m left outer join m.team t")
    List<Member> findExAll(Pageable pageable);

    @Override
    // @EntityGraph() => fetch join 이라고 생각하면 된다.
    @EntityGraph(attributePaths = {"team"})
    List<Member> findAll();

    @EntityGraph(attributePaths = {"team"})
    @Query("select m from Member m")
    List<Member> findMemberEntityGraph();

//    @EntityGraph(attributePaths = {"team"})
    @EntityGraph(value = "Member.all")
    List<Member> findEntityGraphByUsername(@Param("username") String username);

    @Transactional(readOnly = true)
//    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))
    Member findReadOnlyByUsername(String username);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Member findLockByUsername(String username);
}
