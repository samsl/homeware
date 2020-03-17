package ella.sam.shiro.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WxJwtCredentialsMatcher implements CredentialsMatcher {

    private static final Logger LOG = LoggerFactory.getLogger(WxJwtCredentialsMatcher.class);

    @Override
    public boolean doCredentialsMatch(AuthenticationToken authenticationToken, AuthenticationInfo authenticationInfo) {
        String token = (String) authenticationToken.getCredentials();
        Object stored = authenticationInfo.getCredentials();
        String salt = stored.toString();

        String openid = (String) authenticationInfo.getPrincipals().getPrimaryPrincipal();
        try {
            Algorithm algorithm = Algorithm.HMAC256(salt);
            JWTVerifier verifier = JWT.require(algorithm).withClaim("openid", openid).build();
            verifier.verify(token);
            return true;
        } catch (JWTVerificationException e) {
            LOG.error("Token Error:{}", e.getMessage());
        }
        return false;
    }
}
