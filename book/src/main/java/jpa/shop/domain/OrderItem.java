package jpa.shop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jpa.shop.domain.item.Item;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

@Entity
@Getter
@Setter
public class OrderItem {

    @Id
    @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "order_id")
    @JsonIgnore // 양뱡향 연관관계 해결(제거)
    private Order order;

    private int orderPrice;
    private int count;

    // JPA는 pretected 까지 기본생성자를 만들수 있게 허용해준다
    protected OrderItem() {
    }

    //생성 메서드
    public static OrderItem createOrder(Item item, int orderPrice, int count) {
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);

        item.removeStock(count);
        return orderItem;
    }

    //비지니스 로직
    public void cancel() {  // 재수수량을 원복
        getItem().addStock(count);
    }

    // 조회 로직
    // 주문 상품 전체 가격 조회
    public int getTotalPrice() {
        return getOrderPrice() * getCount();
    }
}
