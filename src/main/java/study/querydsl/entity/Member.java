package study.querydsl.entity;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "member_id")
    private Long id;
}
