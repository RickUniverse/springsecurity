package org.springsecurity.security;

import com.servicebase.utils.utils.R;
import com.servicebase.utils.utils.ResponseUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 退出处理器
 * @author lijichen
 * @date 2021/2/10 - 17:48
 */
public class TokenLogoutHandler implements LogoutHandler {

    private TokenManager tokenManager;
    private RedisTemplate redisTemplate;

    public TokenLogoutHandler(TokenManager tokenManager, RedisTemplate redisTemplate) {
        this.tokenManager = tokenManager;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        // 1，从header中获取token
        // 2，如果token不为空，移除token。从redis删除token
        String token = request.getHeader("token");

        if (null != token) {
            // 移除
            tokenManager.removeToken(token);

            // 从token获取用户名
            String username = tokenManager.getUserInfoFromToken(token);
            redisTemplate.delete(username);
        }
        // 处理成功
        ResponseUtil.out(response, R.ok());
    }
}
