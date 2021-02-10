package org.aciservice.service.impl;

import org.aciservice.entity.User;
import org.aciservice.service.PermissionService;
import org.aciservice.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springsecurity.entity.SecurityUser;

import java.util.List;

/**
 * @author lijichen
 * @date 2021/2/10 - 19:46
 */
// 要跟org.springsecurity.config.TokenWebSecurityConfig 的 private UserDetailsService userDetailsService; 属性名一致
@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Autowired
    private PermissionService permissionService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 根据用户名查询数据
        User user = userService.selectByUsername(username);

        // 判断
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        org.springsecurity.entity.User curUser = new org.springsecurity.entity.User();
        BeanUtils.copyProperties(user,curUser);

        // 根据用户查询用户权限列表
        List<String> permissionValueList = permissionService.selectPermissionValueByUserId(user.getId());
        SecurityUser securityUser = new SecurityUser();
        // 这一步不要忘了
        securityUser.setCurrentUserInfo(curUser);
        securityUser.setPermissionValueList(permissionValueList);
        return securityUser;
    }
}
