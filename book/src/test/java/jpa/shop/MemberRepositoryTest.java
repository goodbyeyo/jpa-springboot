package jpa.shop;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    @Transactional
    @Rollback(false)
    @DisplayName("Member 저장 테스트")
    public void testMember() {
        // given java bean setter pattern
//        Member member = new Member();
//        member.setUsername("memberA");

        // given builder pattern
        Members member = Members.builder().username("memberB").build();

        // when
        Long savedId = memberRepository.save(member);
        Members findMember = memberRepository.find(savedId);

        // then
        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        
        // /JPA 엔티티 동일성 보장
        System.out.println("findMember == member ::" + (findMember == member));
        assertThat(findMember).isEqualTo(member);
    }
}