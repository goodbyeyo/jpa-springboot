package study.jpadata.entity;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(false)
class MemberTest {

    @PersistenceContext
    EntityManager em;

    @Test
    public void testEntity() {

        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");

        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member("member1", 23, teamA);
        Member member2 = new Member("member2", 25, teamA);
        Member member3 = new Member("member3", 34, teamB);
        Member member4 = new Member("member4", 35, teamB);

        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);

        // 초기화
        em.flush(); // 강제로 DB에 INSERT 쿼리 실행
        em.clear(); // JPA 영속성 컨텍스트에 있는 캐시를 모두 제거한다

        List<Member> members = em.createQuery("select m from Member m", Member.class)
                .getResultList();
        members.forEach(o-> {
            System.out.println("member = " + o.getId());
            System.out.println("member = " + o.getUsername());
            System.out.println("member = " + o.getAge());
            System.out.println("member = " + o.getTeam());
        });
    }


}