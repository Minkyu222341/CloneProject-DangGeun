package com.sparta.cloneproject.util;

import com.sparta.cloneproject.service.ArticleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class Scheduler {
    private final ArticleService articleService;

//    @Scheduled(cron = "1 * * * * *")
    public void removeImage() {
        log.info("S3이미지 삭제 완료");
        articleService.removeS3Image();
    }
}
