package study.jpadata.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.jpadata.dto.MemberDto;
import study.jpadata.entity.Member;
import study.jpadata.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@SpringBootTest
@Transactional
@Rollback(false)    // 테스트 종료후 롤백 옵션 : 취소
class MemberRepositoryTest {

    /* 모두 같은 EntityManager 를 사용 */
    @Autowired MemberRepository memberRepository;
    @Autowired TeamRepository teamRepository;
    @PersistenceContext EntityManager em;

    @Test
    public void testMember() {
        Team teamA = new Team("teamA");
        Member member = new Member("memberA", 10, teamA);
        Member saveMember = memberRepository.save(member);

        // Optaional : null 일 경우 NoSuchElementException
        Member findMember = memberRepository.findById(saveMember.getId()).get();

        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        // 영속성 컨텍스트의 동일성이 보장됨 -> 같은 인스턴스임 :: 1차캐시 기능
        assertThat(findMember).isEqualTo(member);
    }

    @Test
    public void basicCRUDTest() {

        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberRepository.save(member1);
        memberRepository.save(member2);

        // 단건조회 검증
        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();
        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        findMember1.setUsername("memberAAA");   // 변경감지(durty checking)

        // 리스트 조회 검증
        List<Member> all = memberRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        // 카운트 검증
        long count = memberRepository.count();
        assertThat(count).isEqualTo(2);

        // 삭제 검증
        memberRepository.delete(member1);
        memberRepository.delete(member2);

        long deletedCount = memberRepository.count();
        assertThat(deletedCount).isEqualTo(0);
    }

    @Test
    public void findHelloBy() {
        List<Member> helloBy = memberRepository.findTop3HelloBy();
    }

    @Test
    public void testNameQuery() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);

        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByUsername("AAA");
        Member findMember = result.get(0);
        assertThat(result.get(0)).isEqualTo(m1);
    }

    @Test
    public void findUsernameList() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        Member m3 = new Member("CCC", 30);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<String> result = memberRepository.findUsernameList();
        result.forEach(s -> {
            System.out.println("result = " + s);
        });
    }

    @Test
    public void findMemberDto() {
        Team team = new Team("TeamAAA");
        teamRepository.save(team);

        Member m1 = new Member("AAA", 10);
        m1.setTeam(team);
        memberRepository.save(m1);

        List<MemberDto> result = memberRepository.findMemberDto();
        result.forEach(s -> {
            System.out.println("result = " + s);
        });
    }

    @Test
    public void findByNames() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 10);
        Member m3 = new Member("CCC", 10);
        memberRepository.save(m1);
        memberRepository.save(m2);
        memberRepository.save(m3);

        List<Member> result = memberRepository.findByNames(Arrays.asList("AAA","BBB","CCC"));
        result.forEach(s -> {
            System.out.println("result = " + s);
        });
    }

    @Test
    public void bulkUpdate() throws Exception {
        //given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 19));
        memberRepository.save(new Member("member3", 20));
        memberRepository.save(new Member("member4", 21));
        memberRepository.save(new Member("member5", 40));
        //when
        int resultCount = memberRepository.bulkAgePlus(20);
        //then
        assertThat(resultCount).isEqualTo(3);
    }

    @Test
    public void findListByUsername() {
        Member m1 = new Member("AAA", 10);
        memberRepository.save(m1);

        List<Member> result = memberRepository.findListByUsername("AAA");
        result.forEach(s -> {
            System.out.println("result = " + s);
        });

        List<Member> nullResult = memberRepository.findListByUsername("AAABBB");
        // 사이즈가 0인 경우 empty Collection 을 반환, null 아니기때문에 그냥 받으면 된다
        System.out.println("nullResult = " + result.size());    // 사이즈 0
    }

    @Test
    public void findMemberByUsername() {
        Member m1 = new Member("AAA", 10);
        memberRepository.save(m1);

        Member findMember = memberRepository.findMemberByUsername("AAA");
        assertThat(findMember.getUsername()).isEqualTo(m1.getUsername());

        Member findMember2 = memberRepository.findMemberByUsername("213123123");
        // 스프링 JPA는 null 을 반환한다
        System.out.println("findMember2 = " + findMember2);

//        assertThatThrownBy(() -> {
//            memberRepository.findMemberByUsername("AAA111");
//        }).isInstanceOf(NullPointerException.class);
    }

    @Test
    public void findOptionalByUsername() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 10);
        memberRepository.save(m1);

        Optional<Member> result = memberRepository.findOptionalByUsername("AAA");
        System.out.println("result = " + result);
        assertThat(result.get().getUsername()).isEqualTo(m1.getUsername());
    }

    @Test
    public void Ex_findOptionalByUsername() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 10);
        memberRepository.save(m1);
        memberRepository.save(m2);

        assertThatThrownBy(() -> {
            Optional<Member> result = memberRepository.findOptionalByUsername("AAA");
        }).isInstanceOf(IncorrectResultSizeDataAccessException.class);
        // NonUniqueResultException -> IncorrectResultSizeDataAccessException 변환
    }

    @Test
    public void paging() {
        memberRepository.save(new Member("member99", 10));
        memberRepository.save(new Member("member88", 10));
        memberRepository.save(new Member("member77", 10));
        memberRepository.save(new Member("member66", 10));
        memberRepository.save(new Member("member65", 10));
        memberRepository.save(new Member("member63", 10));
        memberRepository.save(new Member("member55", 10));
        memberRepository.save(new Member("member49", 10));
        memberRepository.save(new Member("member43", 10));
        memberRepository.save(new Member("member23", 10));
        memberRepository.save(new Member("member11", 10));

        int age = 10;
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "username"));

        //when
        Slice<Member> page = memberRepository.findByAge(age, pageRequest);

        Slice<MemberDto> toMap = page.map(m -> new MemberDto(m.getId(), m.getUsername(), null));

        //then
        List<MemberDto> content = toMap.getContent();
//        long totalElements = page.getTotalElements();
//        System.out.println("totalElements = " + totalElements);
        content.forEach(o-> {
            System.out.println("content = " + o);
        });

        assertThat(content.size()).isEqualTo(10);
//        assertThat(page.getTotalElements()).isEqualTo(11);
        assertThat(toMap.getNumber()).isEqualTo(0);
//        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(toMap.isFirst()).isTrue();
        assertThat(toMap.hasNext()).isTrue();
    }

    @Test
    public void bulkAgePlus() throws Exception {
        //given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 19));
        memberRepository.save(new Member("member3", 20));
        memberRepository.save(new Member("member4", 21));
        memberRepository.save(new Member("member5", 40));

        //when
        int resultCount = memberRepository.bulkAgePlus(20);
        em.flush(); // 변경되지 않는 내용을 DB에 반영
        em.clear(); // 영속성 컨텍스트에 남은 내용 clear, 
        /* 즉 벌크연산이후에는 반드시 영속성 컨텍스트 내용 제거할것 */
        /* 같은 트랜잭션안에서 추가 로직이 있을 경우 데이터 정합성이 깨질수 있다 */

        List<Member> result = memberRepository.findByUsername("member5");
        // 영속성 컨텍스트에는 40, DB 41으로 Update 되어 있음
        Member member5 = result.get(0);
        System.out.println("member5 = " + member5);

        //then
        assertThat(resultCount).isEqualTo(3);
    }

    @Test
    public void findMemberLazy() throws Exception {
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);
        memberRepository.save(new Member("member1", 10, teamA));
        memberRepository.save(new Member("member2", 20, teamB));
        em.flush();
        em.clear();

        // select Member 1 ( N+1 )
        List<Member> members = memberRepository.findMemberEntityGraph();
        //then
        for (Member member : members) {
            member.getTeam().getUsername();
        }
    }

    @Test
    public void queryHint() {
        Member member1 = memberRepository.save(new Member("member1", 10));
        em.flush();
        em.clear();

        Member findMember = memberRepository.findReadOnlyByUsername("member1");
        findMember.setUsername("member2");
        em.flush();
    }

    @Test
    public void lock_test() {
        Member member1 = memberRepository.save(new Member("member1", 10));
        em.flush();
        em.clear();

        List<Member> memberList = memberRepository.findLockByUsername("member1");
//        em.flush();
    }

    @Test
    public void callCustom() {
        memberRepository.findMemberCustom();
    }


}