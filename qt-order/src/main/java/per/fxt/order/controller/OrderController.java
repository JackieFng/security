package per.fxt.order.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Fangxiaoting
 * @date 2021/7/15 17:47
 */
@RestController
@RequestMapping("order")
public class OrderController {

    @GetMapping("index")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String index() {
        OAuth2Authentication oAuth2Authentication = (OAuth2Authentication)SecurityContextHolder.getContext().getAuthentication();
        return "hello" + oAuth2Authentication.getUserAuthentication().getPrincipal() + "!";
    }
}
