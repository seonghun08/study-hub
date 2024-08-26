package com.hun.book.springboot.web;

import com.hun.book.springboot.config.auth.LoginUser;
import com.hun.book.springboot.config.auth.dto.SessionUser;
import com.hun.book.springboot.dto.PostsResponseDto;
import com.hun.book.springboot.service.PostsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@Controller
@RequiredArgsConstructor
public class IndexController {

    private final PostsService postsService;

    @GetMapping("/")
    public String index(@LoginUser SessionUser user, Model model) {
        if (user != null) {
            model.addAttribute("userName", user.getName());
        }
        model.addAttribute("posts", postsService.findAllDesc());
        return "index";
    }

    @GetMapping("/posts/save")
    public String postsSave() {
        return "posts-save";
    }

    @GetMapping("/posts/update/{id}")
    public String postsUpdate(@PathVariable Long id, Model model) {
        PostsResponseDto dto = postsService.findById(id);
        model.addAttribute("post", dto);
        return "posts-update";
    }
}
