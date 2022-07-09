package jpa.shop.service;

import jpa.shop.domain.Address;
import jpa.shop.domain.Member;
import jpa.shop.domain.Order;
import jpa.shop.domain.OrderStatus;
import jpa.shop.domain.item.Book;
import jpa.shop.domain.item.Item;
import jpa.shop.exception.NotEnoutgStockException;
import jpa.shop.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Slf4j
class OrderServiceTest {

    @Autowired EntityManager em;
    @Autowired OrderService orderService;
    @Autowired OrderRepository orderRepository;

    @Test
    public void 상품주문() throws Exception {
        //given
        Member member = createMember();

        Book book = createBook("이것이자바다", 20000, 10);

        int orderCount = 2;

        //when
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        //then
        Order getOrder = orderRepository.findOne(orderId);

        assertEquals(OrderStatus.ORDER, getOrder.getStatus(), "상품 주문시 상태는 ORDER");
        assertEquals(1, getOrder.getOrderItems().size(), "주문한 상품 종류 수가 정확해야 한다");
        assertEquals(20000 * orderCount, getOrder.getTotalPrice(), "주문 가격은 가격 * 수량이다");
        assertEquals(8, book.getStockQuantity(), "주문 수량만큼 재고가 줄어야 한다");

    }

    // @org.junit.Test(expected = NotEnoutgStockException.class) :: Junit4
    @Test
    public void 상품주문_재고수량초과() throws Exception {

        Member member = createMember();
        Item item = createBook("이것이자바다", 20000, 10);

        int orderCount = 11;

        Assertions.assertThrows(NotEnoutgStockException.class, () -> {
            log.info("재고 수량 부족 예외가 발생해야 한다");
            orderService.order(member.getId(), item.getId(), orderCount);
        });

        // Junit4
        // fail("재고 수량 부족 예외가 발생해야 한다");
    }

    @Test
    public void 주문취소() throws Exception {
        //given
        Member member = createMember();
        Book item = createBook("이것이자바다", 20000, 10);
        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);

        //when
        orderService.cancelOrder(orderId);

        //then
        Order getOrder = orderRepository.findOne(orderId);

        assertEquals(OrderStatus.CANCEL, getOrder.getStatus(), "주문취소 시 상태는 CANCEL 이다");
        assertEquals(10, item.getStockQuantity(), "주문이 취소된 상품은 재고가 그만큼 증가해야한다");
    }

    private Book createBook(String name, int price, int stockQuantity) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("회원A");
        member.setAddress(new Address("서울", "가로수길", "123-456"));
        em.persist(member);
        return member;
    }
}