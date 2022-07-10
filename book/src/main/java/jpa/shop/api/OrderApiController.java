package jpa.shop.api;

import jpa.shop.domain.Address;
import jpa.shop.domain.Order;
import jpa.shop.domain.OrderItem;
import jpa.shop.domain.OrderStatus;
import jpa.shop.repository.OrderRepository;
import jpa.shop.repository.OrderSearch;
import jpa.shop.repository.order.query.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.*;

@RestController
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderRepository orderRepository;
    private final OrderQueryRepository orderQueryRepository;

    /**
     * 엔티티 직접 노출
     * 엔티티가 변하면 API 스펙이 변한다.
     * 트랜잭션 안에서 지연 로딩 필요
     * 양방향 연관관계 문제
     */
    @GetMapping("/api/v1/orders")
    public Result ordersV1() {
        orderRepository.findAllByString(new OrderSearch()).
                forEach(order -> {
                    order.getMember().getName(); //Lazy 강제 초기화
                    order.getDelivery().getAddress(); //Lazy 강제 초기화
                    order.getOrderItems()
                            .forEach(o -> o.getItem().getName()); //Lazy 강제 초기화
                });
        return new Result(orderRepository.findAllByString(new OrderSearch()));
        /*
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        for (Order order : all) {
            order.getMember().getName(); //Lazy 강제 초기화
            order.getDelivery().getAddress(); //Lazy 강제 초기화

            List<OrderItem> orderItems = order.getOrderItems();
            orderItems.stream().forEach(o -> o.getItem().getName()); //Lazy 강제 초기화
        }
        return new Result(all);
        */
    }

    /**
     * 엔티티를 조회해서 DTO로 변환 : fetch join 사용 X
     * 트랜잭션 안에서 지연 로딩 필요
     * Entity 를 외부로 노출하지 않도록 Dto 생성자를 통해서 변환
     * 영속성 컨텍스트에 없는 경우에만 DB에서 조회
     */
    @GetMapping("/api/v2/orders")
    public Result ordersV2() {
        return new Result(orderRepository.findAllByCriteria(new OrderSearch()).stream()
                .map(order -> new OrderDto(order))
                .collect(toList()));
/*
        List<Order> orders = orderRepository.findAllByCriteria(new OrderSearch());
        for (Order order : orders){
            System.out.println("order ref=" + order + " id=" + order.getId());
        }
        List<OrderDto> collect = orders.stream()
                .map(o -> new OrderDto(o))
                .collect(Collectors.toList());
        return collect;
*/
    }

    /**
     * 엔티티를 조회해서 DTO로 변환 : fetch join 사용 O
     * 단점 : 페이징 불가능 ★★★
     * 대신에 batch fetch size? 옵션 주면 N -> 1 쿼리로 변경 가능)
     */
    @GetMapping("/api/v3/orders")
    public Result ordersV3() {
        return new Result(orderRepository.findAllWithItem().stream()
                .map(order -> new OrderDto(order))
                .collect(toList()));
    }

    @GetMapping("/api/v3.1/orders")
    public Result ordersV3_page(
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "limit", defaultValue = "100") int limit) {
        return new Result(orderRepository.findAllWithMemberDelivery(offset, limit).stream()
                .map(order -> new OrderDto(order))
                .collect(toList()));
    }

    @GetMapping("/api/v4/orders")
    public Result ordersV4() {
        List<OrderQueryDto> orders = orderQueryRepository.findOrderQueryDtos();
        return new Result(orders);
    }

    @GetMapping("/api/v5/orders")
    public Result ordersV5() {
        List<OrderQueryDto> orders = orderQueryRepository.findAllByDto_optimization();
        return new Result(orders);
    }

    @GetMapping("/api/v6/orders")
    public Result ordersV6() {
        List<OrderFlatDto> flats = orderQueryRepository.findAllByDto_flat();
        List<OrderQueryOneDto> result = flats.stream()
                .collect(
                        groupingBy(o -> new OrderQueryDto(o.getOrderId(), o.getName(), o.getOrderDate(), o.getOrderStatus(), o.getAddress()),
                        mapping(o -> new OrderItemQueryDto(o.getOrderId(), o.getItemName(), o.getOrderPrice(), o.getCount()), toList())
                )).entrySet().stream()
                .map(e -> new OrderQueryOneDto(e.getKey().getOrderId(), e.getKey().getName(), e.getKey().getOrderDate(), e.getKey().getOrderStatus(), e.getKey().getAddress(), e.getValue()))
                .collect(toList());
        return new Result(result);
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }

    @Data
    static class OrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;
        private List<OrderItemDto> orderItems;

        /* Entity 를 외부로 노출하지 않도록 Dto 생성자를 통해서 변환*/
        public OrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName(); // LAZY 초기화
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress(); // LAZY 초기화
            orderItems = order.getOrderItems().stream()
                    .map(orderItem -> new OrderItemDto(orderItem))
                    .collect(toList());

            // Entity를 외부로 바로 노출하는것은 좋은 개발 방법이 아니다
            /*
            order.getOrderItems().stream()
                    .forEach(o -> o.getItem().getName());   // LAZY 초기화
            orderItems = order.getOrderItems();
            */
        }
    }

    @Getter
    static class OrderItemDto {
        private String itemName;    // 상품명
        private int orderPrice; // 주문가격
        private int count;  // 주문수량

        public OrderItemDto(OrderItem orderItem) {
            itemName = orderItem.getItem().getName();
            orderPrice = orderItem.getOrderPrice();
            count = orderItem.getCount();
        }
    }



}
