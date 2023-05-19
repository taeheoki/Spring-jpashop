//package taeheoki.jpabook.jpashop;
//
//import org.springframework.stereotype.Repository;
//
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//
//@Repository
//public class MemberRepository {
//
//    @PersistenceContext
//    private EntityManager em;
//
//    public Long save(Member member) {
//        em.persist(member);
//        return member.getId();
//    }
//    // Member를 반환하지 않는 이유, cmd와 쿼리를 분리하라.
//    // 저장의 경우 사이드 이펙트를 일으킬 수 있는 커맨드 성이기 때문에 리턴값을 가급적 만들지 않는다.
//    // id 정도만 반환해줌으로써 재조회를 하게끔한다.
//
//    public Member find(Long id) {
//        return em.find(Member.class, id);
//    }
//
//}
