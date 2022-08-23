package com.sparta.cloneproject.controller;

import com.sparta.cloneproject.service.HeartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/like")
@CrossOrigin(origins = "*", allowedHeaders = "*",exposedHeaders = "*")
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
