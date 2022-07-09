package jpa.shop;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class TestRepository {

    @PersistenceContext
    EntityManager em;

    public Long save(Members member) {
        em.persist(member);
        return member.getId();
    }

    public Members find(Long id) {
        return em.find(Members.class, id);
    }
}
