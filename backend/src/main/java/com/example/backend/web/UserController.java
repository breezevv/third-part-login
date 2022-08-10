package com.example.backend.web;

import com.example.backend.Cache;
import com.example.backend.dto.Result;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
public class UserController {


    @GetMapping("/user/info")
    public Result getUserInfo() {
        return Result.success(Cache.userInfoMap);
    }
}
