package com.social.innerPeace.board.comment.service;

import com.social.innerPeace.dto.CommentDTO;
import com.social.innerPeace.entity.Comment;
import org.springframework.data.domain.Page;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

public interface BoardCommentService {

    Long write(CommentDTO commentDTO);
    Long modify(CommentDTO commentDTO);
    Long delete(CommentDTO commentDTO);
    List<CommentDTO>findAll(Long postID);
    List<CommentDTO> scroll(Long postNo, Long commentNo);
    default Comment dtoToEntity(CommentDTO dto){
        Comment entity = Comment.builder()
                .commentContent(dto.getComment_content())
                .build();
        return entity;
    }

    default CommentDTO entityToDto(Comment entity){
        CommentDTO dto = CommentDTO.builder()
                .post_no(entity.getPost().getPostNo())
                .comment_no(entity.getCommentNo())
                .comment_content(entity.getCommentContent())
                .comment_regday(LocalDateTime.from(entity.getReg_date()))
                .nickName(entity.getMember().getNickName())
                .profile_image(entity.getMember().getProfileImage())
                .build();
        return dto;
    }

    default List<CommentDTO> toList(Page<Comment> commentList){
        return commentList.stream().map(entity->entityToDto(entity)).collect(Collectors.toList());
    }

    default byte[] readBytesFromFile(String filePath) throws IOException {
        File file = new File(filePath);
        byte[] fileBytes = new byte[(int) file.length()];

        try (FileInputStream fis = new FileInputStream(file)) {
            fis.read(fileBytes);
        }

        return fileBytes;
    }

    default String encodeBytesToBase64(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

}
