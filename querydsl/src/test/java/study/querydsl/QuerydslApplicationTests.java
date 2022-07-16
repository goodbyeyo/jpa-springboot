package study.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.querydsl.entity.Hello;
import study.querydsl.entity.QHello;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(false)
class QuerydslApplicationTests {

    // @Autowired == @PersistenceContext 스프링 최신 버전에서는 같은 기능
    @PersistenceContext
    EntityManager em;

    @Test
    void contextLoads() {
        Hello hello = new Hello();
        em.persist(hello);

        JPAQueryFactory query = new JPAQueryFactory(em);
        QHello qHello = QHello.hello;   // Querydsl Q타입 동작 확인

        Hello result = query
                .selectFrom(qHello)
                .fetchOne();
        assertThat(result).isEqualTo(hello);
        if (result != null) {
            assertThat(result.getId()).isEqualTo(hello.getId());
        }
    }


}
