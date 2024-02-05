package com.social.innerPeace.board.comment.controller;

import com.social.innerPeace.board.comment.service.BoardCommentService;
import com.social.innerPeace.dto.CommentDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comment")
public class BoardCommentController {
    private final BoardCommentService boardCommentService;

    @PostMapping("/save")

    public ResponseEntity save(@ModelAttribute CommentDTO commentDTO, @SessionAttribute(name = "logined Healer") String healer) {
        System.out.println("commentDTO = " + commentDTO);
        if (healer==null) {
            return null;
        }
        commentDTO.setHealerEmail(healer);
        Long saveResult = boardCommentService.save(commentDTO);
        if (saveResult != null) {
            List<CommentDTO> commentDTOList = boardCommentService.findAll(commentDTO.getPost_no());
            return new ResponseEntity<>(commentDTOList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("해당 게시글이 존재하지 않습니다",HttpStatus.NOT_FOUND);
        }
    }
}
