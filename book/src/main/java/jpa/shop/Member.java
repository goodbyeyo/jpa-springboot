package jpa.shop;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor
//@Setter
public class Member {

    @Id
    @GeneratedValue
    private Long id;
    private String username;

    @Builder
    public Member(Long id, String username) {
        this.id = id;
        this.username = username;
    }
}
