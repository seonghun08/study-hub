package com.nplus1.respositroy;

import com.nplus1.entity.Parent;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ParentRepository extends JpaRepository<Parent, Long> {

    @EntityGraph(attributePaths = "childList")
    @Query("select p from Parent p")
    List<Parent> findAllEntityGraph();
}
