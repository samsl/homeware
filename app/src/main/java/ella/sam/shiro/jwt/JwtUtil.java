package ella.sam.shiro.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Component;


import java.util.Calendar;
import java.util.Date;
@Component
public class JwtUtil {

    private static final Long ACCESS_TOKEN_EXPIRE_TIME = 1800L;

    public static boolean verify(String token, String username, String secret) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm).withClaim("username", username).build();
            DecodedJWT jwt = verifier.verify(token);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    private static String getClaim(String token, String claim) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim(claim).asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    public static String getOpenid(String token) {
        return getClaim(token, "openid");
    }
    public static String getUsername(String token) {
       return getClaim(token, "username");
    }

    public static String getSignTime(String token) {
        return getClaim(token, "signTime");
    }

    public static String signWx(String openid, String secret, long currentTimeMillis) {
        Date currentTime = new Date(currentTimeMillis);
        Date date = new Date(currentTime.getTime() + ACCESS_TOKEN_EXPIRE_TIME * 1000);
        Algorithm algorithm = Algorithm.HMAC256(secret);
        return JWT.create().withClaim("openid", openid).withClaim("signTime", String.valueOf(currentTimeMillis)).withExpiresAt(date).sign(algorithm);

    }

    public static String sign(String username, String secret, long currentTimeMillis) {
        Date currentTime = new Date(currentTimeMillis);
        Date date = new Date(currentTime.getTime() + ACCESS_TOKEN_EXPIRE_TIME * 1000);
        Algorithm algorithm = Algorithm.HMAC256(secret);
        return JWT.create().withClaim("username", username).withClaim("signTime", String.valueOf(currentTimeMillis)).withExpiresAt(date).sign(algorithm);

    }

    public static String signForWx(String openid, String secret, long currentTimeMillis) {
        Date currentTime = new Date(currentTimeMillis);
        Date date = new Date(currentTime.getTime() + ACCESS_TOKEN_EXPIRE_TIME * 1000);
        Algorithm algorithm = Algorithm.HMAC256(secret);
        return JWT.create().withClaim("openid", openid).withClaim("signTime", String.valueOf(currentTimeMillis)).withExpiresAt(date).sign(algorithm);
    }

    public static boolean isTokenExpired(String token) {
        Date now = Calendar.getInstance().getTime();
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getExpiresAt().before(now);
    }
}
