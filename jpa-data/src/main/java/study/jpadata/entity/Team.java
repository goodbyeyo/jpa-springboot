package study.jpadata.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "username"})
public class Team {

    @Id
    @GeneratedValue
    @Column(name = "team_id")
    private Long id;
    private String username;

    // mappedBy 는 foreginKey가 없는쪽에 건다
    @OneToMany(mappedBy = "team")
    List<Member> members = new ArrayList<>();

    public Team(String username) {
        this.username = username;
    }
}
