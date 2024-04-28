package com.nplus1.entity;

import com.nplus1.respositroy.ChildRepository;
import com.nplus1.respositroy.ParentRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class ParentAndChildTest {

    @Autowired
    ChildRepository childRepository;

    @Autowired
    ParentRepository parentRepository;

    @Autowired
    EntityManager em;

    @BeforeEach
    public void beforeEach() {
        Parent parent1 = new Parent("parent1");
        Parent parent2 = new Parent("parent2");

        for (int i = 0; i < 10; i++) {
            Child child = new Child("child" + i);
            child.changeParent(parent1);
        }

        for (int i = 0; i < 10; i++) {
            Child child = new Child("child" + i);
            child.changeParent(parent2);
        }

        parentRepository.save(parent1); // 영속성 전이로 부모 엔티티만 영속화
        parentRepository.save(parent2); // 영속성 전이로 부모 엔티티만 영속화

        em.flush();
        em.clear();
    }

    @AfterEach
    public void afterEach() {
//        parentRepository.deleteAll();
//        childRepository.deleteAll();
    }

    @Test
    @Commit
    void test() {
        System.out.println("----------------------------------------------------------------------------------------");
        List<Parent> findParents = parentRepository.findAll();

        List<String> childNames = findParents.stream()
                .flatMap(parent -> parent.getChildList().stream()
                        .map(Child::getName)).toList();
//        assertThat(findParents).isNotEmpty();
    }

    @Test
    void test2() {
        System.out.println("----------------------------------------------------------------------------------------");
//        List<Parent> findParents = parentRepository.findAll();
//        assertThat(findParents).isNotEmpty();
//
//        List<String> childNames = findParents.stream()
//                .flatMap(parent -> parent.getChildList().stream()
//                        .map(Child::getName)).toList();
//        assertThat(childNames).isEmpty();

        List<Parent> findParents = em.createQuery(
                        "select p from Parent p join fetch p.childList c", Parent.class)
                .getResultList();

        List<String> childNames = findParents.stream()
                .flatMap(parent -> parent.getChildList().stream()
                        .map(Child::getName)).toList();
    }

    @Commit
    @Test
    void findAllEntityGraph() {
        System.out.println("----------------------------------------------------------------------------------------");
        List<Parent> findParents = parentRepository.findAllEntityGraph();
        List<String> childNames = findParents.stream()
                .flatMap(parent -> parent.getChildList().stream()
                        .map(Child::getName)).toList();
    }
}