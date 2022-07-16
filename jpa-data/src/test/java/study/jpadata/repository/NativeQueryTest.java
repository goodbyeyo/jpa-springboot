package study.jpadata.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import study.jpadata.entity.Member;
import study.jpadata.entity.Team;

import javax.persistence.EntityManager;
import java.util.List;

@SpringBootTest
@Transactional
public class NativeQueryTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    EntityManager em;

    @Test
    public void nativeQuery() throws Exception {
        //given
        Team teamA = new Team("teamA");
        em.persist(teamA);
        Member m1 = new Member("m1", 0, teamA);
        Member m2 = new Member("m2", 0, teamA);
        em.persist(m1);
        em.persist(m2);
        em.flush();
        em.clear();

//        Member result = memberRepository.findByNativeQuery("m1");
//        System.out.println("result = " + result);

        Page<MemberProjection> result = memberRepository.findByNavtiveProjection(PageRequest.of(0, 5));
        List<MemberProjection> content = result.getContent();
        content.forEach(c->{
            System.out.println("content = " + c.getUsername());
            System.out.println("content = " + c.getTeamUsername());
        });

    }
}
