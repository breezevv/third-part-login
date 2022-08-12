package com.example.backend.web;

import cn.hutool.http.HttpUtil;
import com.example.backend.Cache;
import com.example.backend.dto.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/auth/")
@CrossOrigin("*")
public class AuthController {

    String clientId= "b8ce581c2e108df37e6ff0c06a07996538ba10dc111f9c723896f11bf79bf505";
    String clientSecret = "f21d79c79aa8d0b3f4ce98d25d7ffae1426569d38a09a1d6543460de248d7612";
    String redirectUrl = "http://localhost:8080/auth/callback/gitee";

    @Resource
    private ObjectMapper objectMapper;

    /**
     * 返回授权地址
     * @param source
     * @return
     */
    @GetMapping("url/{source}")
    public Result getAuthorizationUrl(@PathVariable String source) {
        Result result = null;
        if ("gitee".equals(source)) {
            String url = String.format("https://gitee.com/oauth/authorize?response_type=code&" +
                    "client_id=%s&redirect_uri=%s", clientId, redirectUrl);
            result = Result.success(url);
        }
        return result;
    }

    @GetMapping("callback/gitee")
    public void callback(String code, HttpServletResponse response) throws IOException {
        // 回调会携带code，通过 code 请求 access_token
        String accessTokenUrl = String.format("https://gitee.com/oauth/token?grant_type=authorization_code&" +
                "code=%s&client_id=%s&redirect_uri=%s&client_secret=%s", code, clientId, redirectUrl, clientSecret);
        // 获得 access_token，并解析
        String accessTokenStr = HttpUtil.post(accessTokenUrl, "");
        Map<String, String> accessTokenMap = objectMapper.readValue(accessTokenStr, Map.class);
        // 去资源服务器获取用户资源，携带 access_token
        String userInfoStr = HttpUtil.get("https://gitee.com/api/v5/user?access_token=" + accessTokenMap.get("access_token"));
        // 获取到了用户资源
        Map<String, String> userInfo = objectMapper.readValue(userInfoStr, Map.class);
        // 执行自己的业务逻辑，可以将用户信息存在数据库里
        Cache.USER_INFO_MAP.put("avatar", userInfo.get("avatar_url"));
        Cache.USER_INFO_MAP.put("nickname", userInfo.get("name"));
        Cache.USER_INFO_MAP.put("roles", "['admin']");
        Cache.USER_INFO_MAP.put("introduction", "I am a super administrator");
        // 创建自己应用的 token
        String token = "abc";

        // 让前端获取到 token
        response.sendRedirect("http://localhost:9527/#/auth-redirect?token=" + token);
    }
}
