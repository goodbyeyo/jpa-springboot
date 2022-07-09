package jpa.shop.repository;

import jpa.shop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

//    @PersistenceContext
// SpringBoot가 Autowired 어노테이션으로도 의존성 주입하도록 지원해준다
// 원래는 PersistenceContext만 가능
    @Autowired  
    private final EntityManager em;

//    public MemberRepository(EntityManager em) {
//        this.em = em;
//    }

    /* factory 직접 주입 가능 */
    /*
    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;
    */

    public void save(Member member) {
        // persist() : 영속성 컨텍스트에 객체를 넣는다, id(pk)값이 key가 된다
        em.persist(member); 
    }

    public Member findOne(Long id) {
        // 단건 조회
        return em.find(Member.class, id);
    }

    public List<Member> findAll() {
        // JPQL : from의 대상이 Entity 객체
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name=:name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }


}

