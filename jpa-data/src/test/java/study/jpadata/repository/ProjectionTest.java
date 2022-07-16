package study.jpadata.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.jpadata.entity.Member;
import study.jpadata.entity.Team;

import javax.persistence.EntityManager;
import java.util.List;

@SpringBootTest
@Transactional
public class ProjectionTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    EntityManager em;

    @Test
    public void projections() throws Exception {
        //given
        Team teamA = new Team("teamA");
        em.persist(teamA);
        Member m1 = new Member("m1", 0, teamA);
        Member m2 = new Member("m2", 0, teamA);
        em.persist(m1);
        em.persist(m2);
        em.flush();
        em.clear();

        //when (close projection)
        // List<UsernameOnly> result =  memberRepository.findProjectionsByUsername("m1");
        // List<UsernameOnlyDto> result =  memberRepository.findProjectionsByUsername("m1");

        // dynamic projection
        /*
        List<UsernameOnlyDto> result =  memberRepository.findProjectionsByUsername("m1", UsernameOnlyDto.class);
        result.forEach(r->{
            System.out.println("usernameOnly = " + r.getUsername());
        });
        //then
        Assertions.assertThat(result.size()).isEqualTo(1);
        */

        // loop projection
        List<NestedClosedProjections> loop =  memberRepository.findProjectionsByUsername("m1", NestedClosedProjections.class);
        loop.forEach(r -> {
            System.out.println("usernameOnly = " + r.getUsername());
            System.out.println("usernameOnly = " + r.getTeam().getUsername());
        });

    }

}
