package study.jpadata.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import study.jpadata.dto.MemberDto;
import study.jpadata.entity.Member;
import study.jpadata.repository.MemberRepository;

import javax.annotation.PostConstruct;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;

    @GetMapping("/members/{id}")
    public String findMember(@PathVariable("id") Long id) {
        Member member = memberRepository.findById(id).get();
        return member.getUsername();
    }

    // 도메인 클래스 컨버터
    @GetMapping("/members2/{id}")
    public String findMember(@PathVariable("id") Member member) {
        return member.getUsername();
    }

    @GetMapping("/members")
//    public Page<Member> list(Pageable pageable) {
    public Page<Member> list(@PageableDefault(size = 5) Pageable pageable) {    // 특별설정
        Page<Member> page = memberRepository.findAll(pageable);
        page.map(MemberDto::new);
//        page.map(member -> new MemberDto(member.getId(), member.getUsername(), null));
        return page;
    }


//    @PostConstruct
    public void init() {
        for (int i = 0; i < 100; i++) {
            memberRepository.save(new Member("user" + i, i));
        }
    }
}

