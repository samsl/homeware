package ella.sam.controllers;

import ella.sam.models.Constant;
import ella.sam.models.LoginForm;
import ella.sam.models.ResponseBean;
import ella.sam.models.User;
import ella.sam.services.UserService;
import ella.sam.shiro.jwt.JwtUtil;
import ella.sam.shiro.redis.RedisClient;
import ella.sam.wechat.WechatUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;


@RestController
public class LoginController {
    @Autowired
    private RedisClient redisClient;

    @Autowired
    private UserService userService;

    @Value("${refreshTokenExpireTime}")
    private String refreshTokenExpireTime;

    @PostMapping("/login")
    public ResponseBean login(@RequestBody LoginForm loginForm, HttpServletRequest request, HttpServletResponse response) {
        UsernamePasswordToken token = new UsernamePasswordToken(loginForm.getUsername(), loginForm.getPassword());
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(token);
            User user = (User) subject.getPrincipal();
            long currentTimeMillis =  System.currentTimeMillis();

            String jwtToken = JwtUtil.sign(user.getUsername(), user.getSalt(), currentTimeMillis);
            redisClient.set(Constant.PREFIX_SHIRO_REFRESH_TOKEN + user.getUsername(), currentTimeMillis, Long.valueOf(refreshTokenExpireTime));
            response.setHeader("x-auth-token", jwtToken);

            return new ResponseBean(HttpStatus.OK.value(), "Login Success.", user);
        } catch(IncorrectCredentialsException | UnknownAccountException e) {
           throw new RuntimeException("Login failed", e);
        }
    }

    @PostMapping("/wxLogin")
    public ResponseBean login(@RequestBody Map<String, String> wxLogin, @RequestHeader Map<String, String> headers, HttpServletResponse response) {
        String openid = WechatUtils.getOpenid(wxLogin.get("loginCode"));
        if (openid.isEmpty()) {
            throw new RuntimeException("Cannot get openid");
        }
        User user = userService.findByOpenid(openid);
        if (user == null) {
            user = new User();
            user.setOpenid(openid);
            user = userService.createUser(user);
        }
        long currentTimeMillis =  System.currentTimeMillis();
        String wxToken = JwtUtil.signForWx(openid, user.getSalt(), currentTimeMillis);
        redisClient.set(Constant.PREFIX_SHIRO_REFRESH_TOKEN + user.getOpenid(), currentTimeMillis, Long.valueOf(refreshTokenExpireTime));
        response.setHeader("x-auth-token", wxToken);
        return new ResponseBean(HttpStatus.CREATED.value(), "Success", user);
    }

    @PostMapping("/register")
    public ResponseBean createUser(@RequestBody User user) {
        return new ResponseBean(HttpStatus.CREATED.value(),"Success", userService.createUser(user));
    }

}
