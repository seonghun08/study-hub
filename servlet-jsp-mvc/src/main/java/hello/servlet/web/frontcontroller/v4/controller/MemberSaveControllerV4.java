package hello.servlet.web.frontcontroller.v4.controller;

import hello.servlet.domain.member.Member;
import hello.servlet.domain.member.MemberRepository;
import hello.servlet.web.frontcontroller.v4.ControllerV4;

import java.util.Map;

public class MemberSaveControllerV4 implements ControllerV4 {

    private final MemberRepository memberRepository = MemberRepository.getInstance();

    @Override
    public String process(Map<String, String> paramMap, Map<String, Object> model) {
        Member member = Member.builder()
                .username(paramMap.get("username"))
                .age(Integer.parseInt(paramMap.get("age")))
                .build();

        memberRepository.save(member);

        model.put("member", member);
        return "save-result";
    }
}
