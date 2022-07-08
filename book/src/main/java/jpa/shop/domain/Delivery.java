package jpa.shop.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Delivery {

    @Id
    @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    @OneToOne(mappedBy = "delivery", fetch = LAZY)
    private Order order;

    @Embedded
    private Address address;

    // default : ORDINAL : 1, 2, 3, 4
    @Enumerated(EnumType.STRING)   // STRING 은 순서애 의해서 밀리는게 없다
    private DeliveryStatus status;

    /*@Builder
    public Delivery(Long id, Order order, Address address, DeliveryStatus status) {
        this.id = id;
        this.order = order;
        this.address = address;
        this.status = status;
    }*/
}
