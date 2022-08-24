package com.sparta.cloneproject.controller;

import com.sparta.cloneproject.dto.requestDto.ArticleRequestDto;
import com.sparta.cloneproject.dto.responseDto.ArticleResponseDto;
import com.sparta.cloneproject.dto.responseDto.SearchResponseDto;
import com.sparta.cloneproject.model.Article;
import com.sparta.cloneproject.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/article")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*",exposedHeaders = "*")
public class ArticleController {

    private final ArticleService articleService;

    /**
     * 게시글 전체 조회
     */
//    @AuthenticationPrincipal UserDetails userDetails
    @GetMapping
    public List<ArticleResponseDto> getArticleList() {
//        System.out.println(userDetails.getUsername() + " AuthenticationPrincipal");
//        System.out.println(userDetails.getUsername() + " 유저네임");
        return articleService.getArticleList();
    }
//    @AuthenticationPrincipal UserDetailsImpl userDetails
    /**
     * 게시글 상세 페이지 조회
     */
    @GetMapping("/{id}")
    private ArticleResponseDto getDetail(@PathVariable Long id) {
        return articleService.getDetail(id);
    }

    /**
     * 게시글 작성
     */
    @PostMapping(value = "/auth", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    //생성은 해당 주소로 post방식으로 들어올것고 그렇게 들어오면 아래를 실행한다.
    public Article creatMemo(@RequestPart(value = "dto") ArticleRequestDto requestDto,
                             @RequestPart(required = false) List<MultipartFile> multipartFile) throws IOException {   //메모를 생성하려면 데이터를 물고다닐 Dto가 필요하다.  // 날아오는 녀석을 그대로 requestDto에 넣어주기 위해서 해당 어노테이션을 씀
        System.out.println(requestDto.getContent()+"  게시글 작성 컨트롤러");
        return articleService.createArticle(requestDto,multipartFile);
    }

    /**
     * 게시글 수정
     */
    @PatchMapping("/auth/{id}")
    public Article updateArticle(@PathVariable Long id,@RequestBody ArticleRequestDto requestDto) {
        System.out.println(requestDto.getPrice());
        return articleService.updateArticle(id,requestDto);
    }
//    /**
//     * 게시글 수정
//     */
//    @PatchMapping("/auth/{id}")
//    public Article updateArticle(@PathVariable Long id,@RequestPart(value = "dto") ArticleRequestDto requestDto,
//                                 @RequestPart(required = false) List<MultipartFile> multipartFile)throws IOException {
//        return articleService.updateArticle(id,requestDto,multipartFiles);
//    }


    /**
     * 게시글 삭제
     */
    @DeleteMapping("/auth/{id}")
    public boolean deleteArticle(@PathVariable Long id) {
        return articleService.delete(id);
    }

    /**
     * 카테고리 조회
     */
    @GetMapping("/search")
    public List<SearchResponseDto> searchCategory(@RequestParam(required = false) String region, @RequestParam(required = false) String category) {
        System.out.println("지역 : "+region);
        System.out.println("카테고리 : "+category);
        System.out.println("검색컨트롤러");
        return articleService.searchCategory(region,category);
    }


}
