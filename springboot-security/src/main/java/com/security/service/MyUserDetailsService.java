package com.security.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.security.mapper.UsersMapper;
import com.security.pojo.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * @author lijichen
 * @date 2021/2/9 - 18:28
 */
@Service("userDetailsService")
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UsersMapper usersMapper;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("username",username);

        // 查询数据库
        Users users = usersMapper.selectOne(queryWrapper);

        if (null == users) {
            throw new UsernameNotFoundException("用户名不存在！");
        }

        // 如果是角色需要加上 ROLE_ 前缀
        Collection<? extends GrantedAuthority> authorities =
                AuthorityUtils.commaSeparatedStringToAuthorityList("admin,ROLE_sale");
        return new User(users.getUsername(),
                new BCryptPasswordEncoder().encode(users.getPassword()),
                authorities);
    }
}
