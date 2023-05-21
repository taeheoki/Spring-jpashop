package taeheoki.jpabook.jpashop.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import taeheoki.jpabook.jpashop.domain.Address;
import taeheoki.jpabook.jpashop.domain.Member;
import taeheoki.jpabook.jpashop.domain.Order;
import taeheoki.jpabook.jpashop.domain.OrderStatus;
import taeheoki.jpabook.jpashop.domain.item.Book;
import taeheoki.jpabook.jpashop.domain.item.Item;
import taeheoki.jpabook.jpashop.exception.NotEnoughStockException;
import taeheoki.jpabook.jpashop.repository.OrderRepository;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired
    EntityManager em;
    @Autowired
    OrderService orderService;
    @Autowired
    OrderRepository orderRepository;

    @Test
    public void 상품주문() throws Exception {
        // given
        Member member = createMember();
        Item book = createBook("시골 JPA", 10000, 10);

        // when
        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        //then
        // 상품 주문시 상태는 ORDER
        assertThat(orderRepository.findOne(orderId).getStatus()).isEqualTo(OrderStatus.ORDER);
        // 주문한 상품 종류 수가 정확해야 한다.
        assertThat(orderRepository.findOne(orderId).getOrderItems().size()).isEqualTo(1);
        // 주문 가격은 가격 * 수량이다.
        assertThat(orderRepository.findOne(orderId).getTotalPrice()).isEqualTo(book.getPrice() * orderCount);
        // 주문 수량만큼 재고가 줄어야 한다.
        assertThat(book.getStockQuantity()).isEqualTo(8);
    }

    @Test
    public void 상품주문_재고수량초과() throws Exception {
        // given
        Member member = createMember();
        Item book = createBook("시골 JPA", 10000, 10);

        int orderCount = 11;

        // when
        assertThatThrownBy(() -> orderService.order(member.getId(), book.getId(), orderCount))
                .isInstanceOf(NotEnoughStockException.class);

        // then

    }

    @Test
    public void 주문취소() throws Exception {
        // given
        Member member = createMember();
        Item book = createBook("시골 JPA", 10000, 10);

        int orderCount = 2;

        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        // when
        orderService.cancelOrder(orderId);

        // then
        Order order = orderRepository.findOne(orderId);

        // 주문 취소시 상태는 CANCEL이다.
        assertThat(order.getStatus()).isEqualTo(OrderStatus.CANCEL);
        // 주문이 취소된 상품은 그만큼 재고가 증가해야 한다.
        assertThat(book.getStockQuantity()).isEqualTo(10);

    }

    private Item createBook(String name, int price, int stockQuantity) {
        Item book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울", "강가", "123-123"));
        em.persist(member);
        return member;
    }

}