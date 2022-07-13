package jpa.shop.service;

import jpa.shop.domain.Member;
import jpa.shop.repository.MemberRepositoryOld;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
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
public class MemberServiceTest {

    @Autowired
    MemberServiceOld memberServiceOld;

    @Autowired
    MemberRepositoryOld memberRepository;

    @Autowired
    EntityManager em;

    @Test
//    @Rollback(false)
    public void 회원가입() throws Exception {
        // given
        Member member = new Member("Wook");

        // when
        Long saveId = memberServiceOld.join(member);

        // then
        em.flush(); // 영속성 컨텍스트에 있는 내용을 반영. 테스트가 종료되면서 트랜잭션은 롤백
        assertEquals(member, memberRepository.findOne(saveId));

    }


    @Test(expected = IllegalStateException.class)
    public void 중복_회원_예외() throws Exception {
        //given
        Member member = new Member("Wook");
        Member member2 = new Member("Wook");

        //when
        memberServiceOld.join(member);
        memberServiceOld.join(member2);

        //then
        Assertions.fail("예외가 발생해야한다");
    }

}