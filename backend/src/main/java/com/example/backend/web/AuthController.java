package com.example.backend.web;

import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.example.backend.constant.Cache;
import com.example.backend.dto.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth/")
@CrossOrigin("*")
public class AuthController {



    @Value("${custom.frontend.redirectUrl}")
    private String frontendRedirectUrl;

    @Value("${custom.backend.host}")
    private String backendHost;

    @Value("${custom.backend.port}")
    private String backendPort;

    @Value("${oauth2.gitee.clientId}")
    private String giteeClientId;

    @Value("${oauth2.gitee.clientSecret}")
    private String giteeClientSecret;

    @Value("${oauth2.gitee.redirectUrl}")
    private String giteeRedirectUrl;

    @Value("${oauth2.github.clientId}")
    private String githubClientId;

    @Value("${oauth2.github.clientSecret}")
    private String githubClientSecret;

    @Resource
    private ObjectMapper objectMapper;

    /**
     * 返回授权地址
     *
     * @param source
     * @return
     */
    @GetMapping("url/{source}")
    public Result getAuthorizationUrl(@PathVariable String source) {
        String url = null;
        if ("gitee".equals(source)) {
            url = String.format("https://gitee.com/oauth/authorize?response_type=code&" +
                    "client_id=%s&redirect_uri=%s", giteeClientId, giteeRedirectUrl);
        }
        if ("github".equals(source)) {
            url = String.format("https://github.com/login/oauth/authorize?client_id=%s",
                    githubClientId);
        }
        return Result.success(url);
    }

    @GetMapping("callback/gitee")
    public void giteeCallback(String code, HttpServletResponse response) throws IOException {
        // 回调会携带code，通过 code 请求 access_token
        String accessTokenUrl = String.format("https://gitee.com/oauth/token?grant_type=authorization_code&" +
                "code=%s&client_id=%s&redirect_uri=%s&client_secret=%s", code, giteeClientId, giteeRedirectUrl, giteeClientSecret);
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
        response.sendRedirect(getFrontendRedirectLocation(token));
    }

    /**
     * github
     * @param code
     * @param response
     * @throws IOException
     */
    @GetMapping("callback/github")
    public void githubCallback(String code, HttpServletResponse response) throws IOException {
        String url = String.format("https://github.com/login/oauth/access_token?client_id=%s&client_secret=%s&code=%s",
                githubClientId, githubClientSecret, code);
        String accessToken = HttpUtil.post(url, "");
        String[] fields = accessToken.split("&");
        Map<String, String> map = new HashMap<>();
        for (String field : fields) {
            String[] kv = field.split("=");
            if (kv.length > 1) {
                map.put(kv[0], kv[1]);
            }
        }
        System.out.println(map.get("access_token"));
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "token " + map.get("access_token"));
        HttpResponse userInfoResponse = HttpUtil.createGet("https://api.github.com/user").addHeaders(headers).execute();
        String userInfoStr = userInfoResponse.body();
        Map<String, String> userInfo = objectMapper.readValue(userInfoStr, Map.class);
        Cache.USER_INFO_MAP.put("avatar", userInfo.get("avatar_url"));
        Cache.USER_INFO_MAP.put("nickname", userInfo.get("login"));
        Cache.USER_INFO_MAP.put("roles", "['admin']");
        Cache.USER_INFO_MAP.put("introduction", "I am a super administrator");
        // 创建自己应用的 token
        String token = "abc";


        // 让前端获取到 token
        response.sendRedirect(getFrontendRedirectLocation(token));
    }

    /**
     * 前端的回调地址，携带 token
     * @param token
     * @return
     */
    private String getFrontendRedirectLocation(String token) {
        return frontendRedirectUrl + "?token" + token;
    }

    /**
     * 测试
     * @param source
     * @return
     */
    @GetMapping("/hello")
    public List<String> hello(String source) {
        List<String> list = new ArrayList<>();
        list.add(giteeClientId);
        list.add(giteeClientSecret);
        list.add(giteeRedirectUrl);
        list.add(githubClientId);
        list.add(githubClientSecret);
        return list;
    }
}
