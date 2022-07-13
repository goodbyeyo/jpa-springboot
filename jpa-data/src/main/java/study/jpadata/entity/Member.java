package study.jpadata.entity;

import lombok.*;
import org.springframework.util.StringUtils;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // pretected 생성자 생성
@ToString(of = {"id", "username", "age"})   // 연관관계에 있는 객체는 출력하지 않도록 주의 (무한루프..)
@NamedQuery(
        name = "Member.findByUsername",
        query = "select m from Member m where m.username = :username"
)
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String username;
    private int age;

    @ManyToOne(fetch = LAZY)    // 지연로딩 설정 : 가짜 객체로 가지고 있다가 실제로 객체로를 조회할때 사용...
    @JoinColumn(name = "team_id")
    private Team team;


    // JPA 표준 스팩 : 생성자가 한개 있어야 한다
    // protected Member() {
    // }

    public Member(String username) {
        this(username, 0);
    }

    public Member(String username, int age) {
        this(username, age, null);
    }

    public Member(String username, int age, Team team) {
        this.username = username;
        this.age = age;
        if (team != null) {
            changeTeam(team);
        }
    }

    public void changeTeam(Team team) {
        this.team = team;
        team.getMembers().add(this);
    }

//    public void changeUsername(String username) {
//        this.username = username;
//    }

}
