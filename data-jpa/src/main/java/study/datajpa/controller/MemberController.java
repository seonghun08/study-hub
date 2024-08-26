package study.datajpa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import study.datajpa.dto.MemberDTO;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;
import study.datajpa.repository.MemberRepository;
import study.datajpa.repository.TeamRepository;

import javax.annotation.PostConstruct;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;

    @GetMapping("/members/{id}")
    public String findMember(@PathVariable("id") Long id) {
        Member member = memberRepository.findById(id).orElseThrow();
        return member.getUsername();
    }

    // 도메인 클래스 컨버터를 추천하지는 않는다. 간단한 경우는 쓸 만하지만 실무 관점에서는 사용하기 애매하다?
    // 간단한 조회용으로는 쓸만하다.
    @GetMapping("/members2/{id}")
    public String findMember2(@PathVariable("id") Member member) {
        return member.getUsername();
    }

    // 페이징 정보가 둘 이상이면 접두사로 구분
    // @Qualifier("member") Pageable memberPageable,
    // @Qualifier("order") Pageable orderPageable,
    @GetMapping("/members")
    public Page<MemberDTO> list(@PageableDefault(size = 5) Pageable pageable) {
        return memberRepository.findDTOByAge(pageable)
                .map(m -> new MemberDTO(m.getId(), m.getUsername(), m.getTeam() == null ? null : m.getTeam().getName()));
    }

//    @PostConstruct
//    public void init() {
//        Team teamA = new Team("teamA");
//        Team teamB = new Team("teamB");
//        teamRepository.save(teamA);
//        teamRepository.save(teamB);
//
//        for (int i = 1; i < 50; i++) {
//            memberRepository.save(new Member("member" + i, 10 + i, teamA));
//        }
//        for (int i = 51; i < 100; i++) {
//            memberRepository.save(new Member("member" + i, 10 + i, teamB));
//        }
//    }
}
