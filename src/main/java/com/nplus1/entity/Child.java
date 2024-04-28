package com.nplus1.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter @Getter
@NoArgsConstructor
public class Child {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "child_id")
    private Long id;
    private String name;

    @ManyToOne
    @JoinColumn(name = "parent_id") // 연관관계 주인을 지정합니다.
    private Parent parent;

    public Child(String name) {
        this.name = name;
    }

    public void changeParent(Parent parent) {
        parent.getChildList().add(this);
        this.setParent(parent);
    }
}
