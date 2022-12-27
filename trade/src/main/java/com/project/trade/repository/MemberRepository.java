package com.project.trade.repository;

import com.project.trade.controller.MemberForm;
import com.project.trade.domain.Member;
import java.util.Optional;

public interface MemberRepository {

    Member save(Member member);
    Optional<Member> findByMail(String mail);
    Member find(MemberForm form);

}
