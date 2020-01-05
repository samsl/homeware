package ella.sam.wechat;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Map;

public class WechatUtils {
    private static final String SECRET = "3e5859ffed34c1e431d3b92fbb13f109";
    private static final String APP_ID = "wx468049a7e394ce4e";
    private static final String WX_URL = "https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code";

    public static String getOpenid(String code) {
        String url = String.format(WX_URL, APP_ID, SECRET, code);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> responseBody;
        try {
            responseBody = mapper.readValue(response.getBody(), new TypeReference<Map<String, Object>>(){});
            if (responseBody.containsKey("openid")) {
                return (String) responseBody.get("openid");
            } else
                return "";
        } catch (IOException e) {
            throw new RuntimeException("Cannot parse wechat openid", e);
        }


    }

    public static void main(String[] args) {
        getOpenid("061juOvq02a08m1yjrwq0dNFvq0juOvz");
    }
}
