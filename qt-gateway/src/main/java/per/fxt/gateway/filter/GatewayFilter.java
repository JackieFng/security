package per.fxt.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author Fangxiaoting
 * @date 2021/7/20 16:09
 */
@Component
public class GatewayFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //todo 可以配置白、黑名单
        ServerHttpRequest request = exchange.getRequest();
        String token = request.getHeaders().getFirst("Authorization");

        /*ServerHttpRequest request = exchange.getRequest();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof OAuth2Authentication)) {
            return null;
        }

        OAuth2Authentication oAuth2Authentication = (OAuth2Authentication) authentication;
        Authentication userAuthentication = oAuth2Authentication.getUserAuthentication();
        //用户身份
        String principal = userAuthentication.getName();
        //用户权限
        List<String> authorities =new ArrayList<>();
        userAuthentication.getAuthorities().stream().forEach(c->authorities.add(c.getAuthority()));
        //其他请求参数
        Map<String, String> requestParameters = oAuth2Authentication.getOAuth2Request().getRequestParameters();

        Map<String, Object> jsonToken = new HashMap<>(requestParameters);
        if (userAuthentication != null) {
            jsonToken.put("principal", principal);
            jsonToken.put("authorities", authorities);
        }*/
        //将token信息请求头转发给资源服务
        ServerHttpRequest mutableReq = request.mutate().header("Authorization", token).build();
        ServerWebExchange mutableExchange = exchange.mutate().request(mutableReq).build();
        return chain.filter(mutableExchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
