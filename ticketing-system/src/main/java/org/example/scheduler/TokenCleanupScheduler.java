package org.example.scheduler;

import org.example.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TokenCleanupScheduler {

    @Autowired
    private MemberService memberService;

    @Scheduled(fixedRate = 3600000) // 1시간마다 실행
    public void deleteExpiredTokens() {
        int deletedCount = memberService.deleteExpiredTokens();
        System.out.println("[Scheduler] 만료된 토큰 " + deletedCount + "개 삭제 완료");
    }
}
