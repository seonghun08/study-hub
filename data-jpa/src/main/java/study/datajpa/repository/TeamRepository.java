package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Team;

@Transactional(readOnly = true)
public interface TeamRepository extends JpaRepository<Team, Long> {
}
