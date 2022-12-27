package com.project.trade.service;

import com.project.trade.controller.MemberForm;
import com.project.trade.domain.Member;
import com.project.trade.repository.MemberRepository;

public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {this.memberRepository = memberRepository;}

    //회원 등록
    public Long join(Member member) {
        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    //중복 아이디 차단
    private void validateDuplicateMember(Member member) {
        memberRepository.findByMail(member.getMail())
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 존재하는 회원입니다.");
                });
    }

    //로그인 검증
    public Member findMember(MemberForm form) {
        Member resmem = memberRepository.find(form);
        System.out.println(resmem.getMail());
        System.out.println(resmem.getPass());
        return resmem;
    }



}
