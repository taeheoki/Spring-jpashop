package taeheoki.jpabook.jpashop.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import taeheoki.jpabook.jpashop.domain.Order;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;

    public void save(Order order) {
        em.persist(order);
    }

    public Order findOne(Long id) {
        return em.find(Order.class, id);
    }

     public List<Order> findAllByString(OrderSearch orderSearch) {
        String jpql = "select o from Order o join o.member m";
        boolean isFristCondition = true;

        // 주문 상태 검색
        if (orderSearch.getOrderStatus() != null) {
            if (isFristCondition) {
                jpql += " where";
                isFristCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " o.status = :status";
        }

        // 회원 이름 검색
         if (StringUtils.hasText(orderSearch.getMemberName())) {
             if (isFristCondition) {
                 jpql += " where";
                 isFristCondition = false;
             } else {
                 jpql += " and";
             }
             jpql += " m.name like :name";
         }

         TypedQuery<Order> query = em.createQuery(jpql, Order.class)
                 .setMaxResults(1000);// 최대 1000건
         if (orderSearch.getOrderStatus() != null) {
             query.setParameter("status", orderSearch.getOrderStatus());
         }
         if (StringUtils.hasText(orderSearch.getMemberName())) {
             query.setParameter("name", orderSearch.getMemberName());
         }
         return query.getResultList();
     }
}
