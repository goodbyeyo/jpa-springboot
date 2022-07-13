package jpa.shop.service;

import jpa.shop.domain.Member;
import jpa.shop.repository.MemberRepositoryOld;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
//@Transactional()
//@javax.transaction.Transactional
//@AllArgsConstructor // 생성자 만들어준다
@Transactional(readOnly = true) // 읽기전용 : 조회하는곳에서 성능 최적화
@RequiredArgsConstructor // final 있는 필드를 가지고 생성자를 만들어준다
public class MemberServiceOld {


//    @Autowired  // field injection
    private final MemberRepositoryOld memberRepository;

    // Test Code 작성할때 직접 주입해줄수 있다
    /*
    @Autowired
    public void setMemberRepository(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }
    */

    /* Constructor injection */
    // 생성자가 하나만 있는 경우에는 자동으로 주입한다
    // @Autowired
//    public MemberService(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }

    /**
     * 회원가입
     */
    @Transactional  // 우선권 (쓰기가능)
    public Long join(Member member) {
        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    /**
     * 회원 전체 조회
     */
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    /**
     * 회원 단건 조회
     */
    public Member findMember(Long memberId) {
        return memberRepository.findOne(memberId);
    }

    private void validateDuplicateMember(Member member) {
        // multy thread 환경에서는 문제될수 있으므로 database에 member.name을 유늬크하게 제약조건으로 잡는다
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다");
        }
    }

    //
    @Transactional
    public void update(Long id, String name) {
        // 영속성 컨텍스트에서 Member 객체에 할당
        // setName()메서드로 변경하면 트랜잭션이 종료될때 Spring AOP가 동작(Commit 실행)
        // JPA 변경감지에 의해서 -> flush -> commit
        Member member = memberRepository.findOne(id);
        member.setName(name);

    }

    public Member findOne(Long id) {
        return memberRepository.findOne(id);
    }
}
