package org.example.scheduler;

import org.example.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TokenCleanupScheduler {

    @Autowired
    private MemberService memberService;

    @Scheduled(fixedRate = 60 * 60 * 1000) // 1시간마다 실행
    public void deleteExpiredTokens() {
        int deletedCount = memberService.deleteExpiredTokens();
        System.out.println("[Scheduler] " + deletedCount + "expired token deleted complete.");
    }
}
