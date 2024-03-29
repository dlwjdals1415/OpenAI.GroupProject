package com.social.innerPeace.board.post.controller;

import com.social.innerPeace.board.comment.service.BoardCommentService;
import com.social.innerPeace.board.post.service.BoardPostService;
import com.social.innerPeace.dto.CommentDTO;
import com.social.innerPeace.dto.PostDTO;
import com.social.innerPeace.entity.Post;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.IntStream;

@Controller
@Slf4j
public class BoardPostController {

    @Autowired
    private BoardPostService boardPostService;

    @Autowired
    private BoardCommentService boardCommentService;

    private String getLoginedmember(HttpSession session) {
        return (String) session.getAttribute("loginedMember");
    }

    private void addPostAndCommentToModel(Model model, Long postNo, String memberNickname) {
        PostDTO dto = boardPostService.detail(postNo, memberNickname);
        List<CommentDTO> comment = boardCommentService.findAll(postNo);
        model.addAttribute("dto", dto);
        model.addAttribute("comment", comment);
    }

    @GetMapping("board/post/list")
    public String postlist(Model model) {
        log.info("call postlist");
        List<PostDTO> dtoList = boardPostService.list();
        model.addAttribute("dtoList", dtoList);
        return "postlist";
    }

    @PostMapping("board/post/list_scroll")
    @ResponseBody
    public ResponseEntity<Object> postlistscroll(@RequestBody PostDTO dto) {
        log.info("call postlistscroll post_no : " + dto.getPost_no());
        List<PostDTO> dtoList = boardPostService.scrollList(dto.getPost_no());
        return ResponseEntity.status(HttpStatus.OK).body(dtoList);
    }

    @PostMapping("board/post/search_scroll")
    @ResponseBody
    public ResponseEntity<Object> postsearchscroll(@RequestParam("post_no") Long post_no, @RequestParam("search") String search) {
        log.info("call postlistscroll post_no : {} search : {}", post_no, search);
        List<PostDTO> dtoList = boardPostService.searchScrollList(post_no, search);
        return ResponseEntity.status(HttpStatus.OK).body(dtoList);
    }

    @GetMapping("board/post/detail/{post_no}")
    public String postdetail(Model model, @PathVariable("post_no") Long postNo, HttpSession session) {
        log.info("call boarddetail");
        String member_nickname = getLoginedmember(session);
        addPostAndCommentToModel(model, postNo, member_nickname);
        return "postdetail";
    }

    @PostMapping("board/post/write")
    public String postwrite(PostDTO dto) {
        String write = boardPostService.write(dto);
        return "redirect:/board/post/list";
    }

    @PostMapping("/board/post/modify")
    @ResponseBody
    public ResponseEntity<Object> postModify(PostDTO dto, HttpSession session) {
        String loginmember = getLoginedmember(session);
        PostDTO postDTO = boardPostService.modify(dto, loginmember);
        if(postDTO == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("수정에 실패하였습니다");
        }
        else{
            return ResponseEntity.status(HttpStatus.OK).body("http://localhost:8080/board/post/detail/" + postDTO.getPost_no());
        }
    }

    @PostMapping("/board/post/delete")
    @ResponseBody
    public ResponseEntity<Object> postDelete(@RequestBody PostDTO postDTO, HttpSession session){
        String loginmember = getLoginedmember(session);
        int result = boardPostService.deletePost(postDTO,loginmember);
        HashMap<Object,Object> res = new HashMap<>();
        res.put("result",result);
        if(result>0){
            res.put("msg","삭제가 되었습니다.");
        }else{
            res.put("msg","삭제를 하지 못했습니다.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @GetMapping("/board/post/search")
    public String postsearch(Model model, @RequestParam("searchkey") String searchkey) {
        log.info("call postsearch search : {}", searchkey);
        List<PostDTO> dtoList = boardPostService.search(searchkey);
        model.addAttribute("search", searchkey);
        model.addAttribute("dtoList", dtoList);
        return "postsearchlist";
    }

    @PostMapping("/board/post/like")
    @ResponseBody
    public ResponseEntity<Object> post_like(@RequestParam("post_no") Long post_no, HttpSession session) {
        log.info("call post_like post_no : {}", post_no);
        String member_nickname = getLoginedmember(session);
        String like = boardPostService.like(post_no, member_nickname);
        int likeCount = boardPostService.likeCount(post_no);
        if (like == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("like", like);
            response.put("likeCount", likeCount);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
    }
}
