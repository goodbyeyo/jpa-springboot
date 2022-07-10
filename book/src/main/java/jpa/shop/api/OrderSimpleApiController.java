package jpa.shop.api;

import jpa.shop.domain.Address;
import jpa.shop.domain.Order;
import jpa.shop.domain.OrderStatus;
import jpa.shop.repository.OrderRepository;
import jpa.shop.repository.OrderSearch;
import jpa.shop.repository.order.simplequery.OrderSimpleQueryDto;
import jpa.shop.repository.order.simplequery.OrderSimpleQueryRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.*;

/**
 * xToOne(ManyToOne, OneToOne)
 * Order
 * Order -> Member
 * Order -> Delivery
 */

@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;
    private final OrderSimpleQueryRepository orderSimpleQueryRepository;

    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1() {
        List<Order> orderList = orderRepository.findAllByCriteria(new OrderSearch());
        orderList.forEach(order -> {
            order.getMember().getName();    // Lazy 강제 초기화
            order.getDelivery().getAddress();   // Lazy 강제 초기화
        });
        return orderList;
    }

    /**
     * 오더 2개 : N + 1 -> 1 + 회원 N + 배송 N
     * order 조회 1번(order 조회 결과 수가 N이 된다.)
     * order -> member 지연 로딩 조회 N 번
     * order -> delivery 지연 로딩 조회 N 번
     * 예) order의 결과가 2개면 최악의 경우 1 + 2 + 2번 실행된다.(최악의 경우)
     * 지연로딩은 영속성 컨텍스트에서 조회하므로, 이미 조회된 경우 쿼리를 생략한다
     */
    @GetMapping("/api/v2/simple-orders")
    public Object ordersV2() {
        return new Result(orderRepository.findAllByCriteria((new OrderSearch())).stream()
                .map(SimpleOrderDto::new)
                .collect(toList()));
        // List<Order> orders = orderRepository.findAllByCriteria((new OrderSearch()));
        // List<SimpleOrderDto> collect = orders.stream()
        //        .map(o -> new SimpleOrderDto(o))  // map 은 A -> B로 바꾸는것 생성자에서 쿼리 2번씩 실행
        //        .collect(Collectors.toList());
        // return new Result(collect);
    }

    /**
     * 재사용성이 높다 : Entity 조회 -> 변경 가능
     * 엔티티를 페치 조인(fetch join)을 사용해서 쿼리 1번에 조회
     * 페치 조인으로 order -> member , order -> delivery 는 이미 조회 된 상태 이므로 지연 로딩 X
     */
    @GetMapping("/api/v3/simple-orders")
    public Object orderV3() {
        return new Result(orderRepository.findAllWithMemberDelivery().stream()
                .map(SimpleOrderDto::new)
                .collect(toList()));
        // List<Order> orderList = orderRepository.findAllWithMemberDelivery();
        // List<SimpleOrderDto> result = orderList.stream()
        //        .map(o -> new SimpleOrderDto(o))
        //        .collect(toList());
        // return new Result(result);
    }

    /**
     * 재사용성이 낮다 : DTO 조회 -> 변경 불가능 애플리케이션 네트웍 용량 최적화(생각보다 미비함)
     * 리포지토리 재사용성 떨어짐, API 스펙에 맞춘 코드가 리포지토리에 들어가는 단점
     */
    @GetMapping("/api/v4/simple-orders")
    public List<OrderSimpleQueryDto> orderV4() {
        return orderSimpleQueryRepository.findOrderDtos();
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }

    @Data
    static class SimpleOrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public SimpleOrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName(); // LAZY 초기화
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress(); // LAZY 초기화
        }
    }
}
