package ella.sam.shiro;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ella.sam.models.Constant;
import ella.sam.models.ResponseBean;
import ella.sam.services.UserService;
import ella.sam.shiro.jwt.JwtToken;
import ella.sam.shiro.jwt.JwtUtil;
import ella.sam.shiro.redis.RedisClient;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class JWTFilter extends AuthenticatingFilter {

    private static final Logger LOG = LoggerFactory.getLogger(JWTFilter.class);


    @Value("${refreshTokenExpireTime}")
    protected String refreshTokenExpireTime;

    @Autowired
    protected RedisClient redisClient;

    @Autowired
    protected UserService userService;

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        if (this.isLoginRequest(request, response)) {
            return true;
        }
        boolean allowed = false;
        try {
            allowed = executeLogin(request, response);
        } catch (IllegalStateException e) {
            LOG.error("Not found any token");
            response401(response, "Not found any token");
        } catch (TokenExpiredException e) {
            if (refreshToken(request, response)) {
                allowed = true;
            } else {
                LOG.error("Both access token and refresh token are expired");
                response401(response, "Both access token and refresh token are expired");
            }
        } catch (Exception e) {
            LOG.error("Error occurs when login {0}", e);
            response401(response, "Error occurs when login");
        }
        return allowed || super.isPermissive(mappedValue);
    }

    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name()))
            return false;
        return super.preHandle(request, response);
    }

    @Override
    protected void postHandle(ServletRequest request, ServletResponse resposne) {
        fillCorsHeader((HttpServletRequest) request, (HttpServletResponse) resposne);
        request.setAttribute("jwtFilter.FILTERED", true);
    }

    @Override
    protected AuthenticationToken createToken(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        String jwtToken = getAuthorizationHeader(servletRequest);
        if (jwtToken != null && !jwtToken.isEmpty()) {
            if (!JwtUtil.isTokenExpired(jwtToken)) {
                return new JwtToken(jwtToken);
            } else {
                throw new TokenExpiredException("Access token is expired");
            }
        }
        return null;
    }

    protected String getAuthorizationHeader(ServletRequest request) {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String header = httpRequest.getHeader("x-auth-token");
        return header;
    }

    private boolean refreshToken(ServletRequest request, ServletResponse response) {
        String token = getAuthorizationHeader(request);
        String username = JwtUtil.getUsername(token);
        if (redisClient.hasKey(Constant.PREFIX_SHIRO_REFRESH_TOKEN + username)) {
            String currentTimeMillisRedis = redisClient.get(Constant.PREFIX_SHIRO_REFRESH_TOKEN + username).toString();
            if (JwtUtil.getSignTime(token).equals(currentTimeMillisRedis)) {
                long currentMillis = System.currentTimeMillis();
                redisClient.set(Constant.PREFIX_SHIRO_REFRESH_TOKEN + username, currentMillis, Long.valueOf(refreshTokenExpireTime));
                String secret = userService.findUserByUsername(username).getSalt();
                token = JwtUtil.sign(username, secret, currentMillis);
                JwtToken jwtToken = new JwtToken(token);
                //check subject to get user
                this.getSubject(request, response).login(jwtToken);
                HttpServletResponse httpServletResponse = (HttpServletResponse) response;
                httpServletResponse.setHeader("x-auth-token", token);
                return true;
            }
        }
        return false;
    }

    protected void fillCorsHeader(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        httpServletResponse.setHeader("Access-Control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Access-Headers"));
    }

    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        HttpServletResponse httpResposne = (HttpServletResponse) servletResponse;
        httpResposne.setCharacterEncoding("UTF-8");
        httpResposne.setContentType("application/json;charset=UTF-8");
        httpResposne.setStatus(HttpStatus.UNAUTHORIZED.value());
        fillCorsHeader((HttpServletRequest) servletRequest, httpResposne);
        return false;
    }

    private void response401(ServletResponse response, String msg) {
        HttpServletResponse httpResposne = (HttpServletResponse) response;
        httpResposne.setCharacterEncoding("UTF-8");
        httpResposne.setContentType("application/json;charset=UTF-8");
        httpResposne.setStatus(HttpStatus.UNAUTHORIZED.value());
        ObjectMapper mapper = new ObjectMapper();
        String json = null;
        try {
            json = mapper.writeValueAsString(new ResponseBean(HttpStatus.UNAUTHORIZED.value(), "Unauthorized: " + msg));
        } catch (JsonProcessingException e) {
            LOG.error("Convert response bean to json failed {0}", e);
        }
        try (PrintWriter out = httpResposne.getWriter()) {
            out.append(json);
        } catch (IOException e) {
            LOG.error("Get response failed", e);
            throw new ShiroException();
        }
    }

    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        return true;
    }
}
