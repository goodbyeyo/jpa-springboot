package jpa.shop.domain;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Member {
    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;

    @Embedded   // 내장 타입 , @Embedded와 @Embeddable 둘중에 하나만 있으면 된다
    private Address address;

    // 컬렉션을 생성하고 나서 다른곳에서 변경하지 말것
    @OneToMany(mappedBy = "member") // 읽기 전용의 표현, 값을 넣어도 forigenKey가 변경되지 않는다
    private List<Order> orders = new ArrayList<>();

    public Member(String name) {
        this.name = name;
    }

    /*@Builder
    public Member(Long id, String name, Address address, List<Order> orders) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.orders = orders;
    }*/
}
