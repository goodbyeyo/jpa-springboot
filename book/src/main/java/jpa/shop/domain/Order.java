package jpa.shop.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.*;

@Entity
@Table(name = "orders")
@Getter
@Setter
//@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    // 지연로딩 -> Proxy객체 생성 -> new ByteBuddyInterceptor() // 실제데아터 조회시 프록시 조기화
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;  // =  new ByteBuddyInterceptor()

    // cascade = CascadeType.ALL ::: persist를 전파한다....
    // 즉 orderItems에 값만 넣고 Order를 persist하면 모든 Collection을 다 저장한다
    // @BatchSize(size = 1000) // application.yml : default_batch_fetch_size 의 세부설정
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    // Access 를 많이 하는곳에 ForineKey(연관 관계의 주인)를 둔다
    @OneToOne(fetch = LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;


    // Hiberbate가 dateType 자등으로 지원
    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status; // 주문상태 [ORDER, CANCEL]

    /* 연관관계 메서드 */
    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }

    /*
    public static void main(String[] args) {
        Member member = new Member();
        Order order = new Order();
        member.getOrders().add(order);
        Order.builder().member(member);
    }
    */

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
        // OrderItem.builder().order(this).build();
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
        // Delivery.builder().order(this).build();
    }

    // 생성 메서드
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) {
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);

        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }

        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    // 비지니스 로직
    /**
     * 주문취소
     */
    public void cancel() {
        if (delivery.getStatus() == DeliveryStatus.COMP) {
            throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다");
        }

        this.setStatus(OrderStatus.CANCEL);
        for (OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }
    }

    // 조회 로직
    /**
     * 전체 주문 가격 조회
     */
    public int getTotalPrice() {
        return orderItems.stream()
                .mapToInt(OrderItem::getTotalPrice)
                .sum();
        /*
        int totalPrice = 0;
        for (OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
        */
    }




    /*
    @Builder
    public Order(Long id, Member member, List<OrderItem> orderItems, Delivery delivery, LocalDateTime orderDate, OrderStatus status) {
        this.id = id;
        this.member = member;
        this.orderItems = orderItems;
        this.delivery = delivery;
        this.orderDate = orderDate;
        this.status = status;
    }
     */
}
