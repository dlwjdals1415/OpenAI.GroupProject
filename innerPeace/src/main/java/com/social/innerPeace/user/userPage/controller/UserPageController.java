package com.social.innerPeace.user.userPage.controller;

import com.social.innerPeace.board.post.service.BoardPostService;
import com.social.innerPeace.dto.MemberDTO;
import com.social.innerPeace.dto.PostDTO;
import com.social.innerPeace.user.userPage.service.UserPageService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@Slf4j
@RequestMapping("user/userpage")
public class UserPageController {
    @Autowired
    UserPageService healerUserPageService;
    @Autowired
    BoardPostService boardPostService;

    @GetMapping("/{healer_nickname}")
    public String userpage(Model model, @PathVariable("healer_nickname") String healerNickname, HttpSession session) {
        log.info("userpage call");
        String loginedHealer = (String) session.getAttribute("loginedHealer");
        List<PostDTO> dtoList = boardPostService.listBymemberNickname(healerNickname);

        MemberDTO dto = healerUserPageService.findHealer(healerNickname, loginedHealer);
        model.addAttribute("dtoList", dtoList);
        model.addAttribute("dto",dto);
        return "userpage";
    }

}
