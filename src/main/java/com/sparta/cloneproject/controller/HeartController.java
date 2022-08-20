package com.sparta.cloneproject.controller;

import com.sparta.cloneproject.service.HeartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/like")
public class HeartController {

    private final HeartService heartService;

    /**
     * 찜 하기
     */
    @PostMapping("/auth/{id}")
    public boolean addLike(@PathVariable Long id) {
        return heartService.addLike(id);
    }

}
