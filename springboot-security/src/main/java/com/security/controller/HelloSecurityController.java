package com.security.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lijichen
 * @date 2021/2/9 - 17:17
 */
@RestController
public class HelloSecurityController {

    @GetMapping("/hello")
    public Object hello() {
        return "hello";
    }

    // 登陆成功后页面
    @GetMapping("/test/index")
    public Object index() {
        return "登陆成功！！！！！！";
    }


    @GetMapping("/update")
    // 有其中任何一个角色都可以访问，开启注解@EnableGlobalMethodSecurity(securedEnabled = true)
//    @Secured({"ROLE_sale","ROLE_manager"})
    // 在方法执行之前检查权限，需要开启注解 @EnableGlobalMethodSecurity(securedEnabled = true,prePostEnabled = true)
    @PreAuthorize("hasAnyAuthority('admin')")
    // 在方法执行之后检查权限，需要开启注解 @EnableGlobalMethodSecurity(securedEnabled = true,prePostEnabled = true)
//    @PostAuthorize("hasAnyAuthority('admin')")
    public Object update() {
        return "update！！！！！！";
    }
}
