package com.jojoldu.book.springboot.web;

import com.jojoldu.book.springboot.config.auth.dto.SessionUser;
import com.jojoldu.book.springboot.service.posts.PostsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Controller
public class IndexController {

    private final PostsService postsService;
    private final HttpSession httpSession;

    @GetMapping("/")
    public String index(Model model){
        model.addAttribute("posts", postsService.findAllDesc());
        SessionUser user = (SessionUser) httpSession.getAttribute("user");

        if(user != null){
            model.addAttribute("userName", user.getName());
        }
        return "index";
    }
}
