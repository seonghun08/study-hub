package study.querydsl.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter(AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "username", "age"})
public class Member extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "member_id")
    private Long id;

    private String username;

    public Member(String username) {
        this.username = username;
    }

    private int age;

    public Member(String username, int age) {
        this.username = username;
        this.age = age;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;


    public static Member createMember(String username, int age, Team team) {
        Member member = new Member();
        member.setUsername(username);
        member.setAge(age);
        if (team != null) {
            member.changeTeam(team);
        }
        return member;
    }

    public void changeTeam(Team team) {
        this.setTeam(team);
        team.getMembers().add(this);
    }
}
