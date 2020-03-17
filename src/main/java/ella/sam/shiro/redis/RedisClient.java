package ella.sam.shiro.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class RedisClient {

    private static Logger logger = LoggerFactory.getLogger(RedisClient.class);
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public boolean expire(String key, long time) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            logger.debug("Setting expire failed {0}", e);
            return false;
        }
    }

    public Long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    public boolean hasKey(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch(Exception e) {
            logger.debug("Exception when finding key {0}", e);
            return false;
        }
    }

    public Object del(String key) {
        Object old = get(key);
        redisTemplate.delete(key);
        return old;
    }

    public Object del(Set<String> keys) {
        return redisTemplate.delete(keys);
    }

    public Object get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    public boolean set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            logger.debug("Set value error {0}", e);
            return false;
        }
    }

    public boolean set(String key, Object value, long time) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            logger.debug("Set value with expired time failed {0}", e);
            return false;
        }
    }

    public Set<String> getKeys(String pattern) {
        return redisTemplate.keys(pattern);
    }

    public Collection<Object> values(String pattern) {
        Set<String> keys = getKeys(pattern);
        List<Object> values = new ArrayList<>();
        for (String key : keys) {
            values.add(get(key));
        }
        return values;
    }
}
