package study.datajpa.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import study.datajpa.dto.MemberDTO;
import study.datajpa.entity.Member;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberQueryRepository {

    private final EntityManager em;

    List<MemberDTO> findAllMembers() {
        return em.createQuery(
                        "select new study.datajpa.dto.MemberDTO(m.id, m.username, t.name)" +
                                " from Member m" +
                                " join m.team t", MemberDTO.class
                )
                .getResultList();
    }
}
