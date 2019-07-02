package ella.sam.shiro.redis;

import ella.sam.shiro.jwt.JwtUtil;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class RedisCache<K, V> implements Cache<K, V> {
    private static final Logger LOGGER = LoggerFactory.getLogger(RedisCache.class);
    private final static String PERFIX = "shiro-cache:";
    private long globExpire = 30 * 60;

    private RedisClient redisClient;

    public RedisCache(RedisClient redisClient) {
        this.redisClient = redisClient;
    }

    private String getCacheKey(Object k) {
        return this.PERFIX + JwtUtil.getUsername(k.toString());
    }

    @Override
    public Object get(Object k) throws CacheException {
        LOGGER.debug("Read data from key: " + k);
        if (!redisClient.hasKey(this.getCacheKey(k))) {
            return null;
        }
        return redisClient.get(getCacheKey(k));
    }

    @Override
    public Object put(Object k, Object v) throws CacheException {
        return redisClient.set(this.getCacheKey(k), v, globExpire);
    }

    @Override
    public Object remove(Object k) throws CacheException {
        if (!redisClient.hasKey(getCacheKey(k))) {
            return null;
        }
        return redisClient.del(getCacheKey(k));
    }

    @Override
    public void clear() throws CacheException {
        redisClient.del(keys());
    }

    @Override
    public int size() {
        return keys().size();
    }

    @Override
    public Set keys() {
        return redisClient.getKeys(PERFIX + "*");
    }

    @Override
    public Collection values() {
        Set keys = keys();
        List<Object> list = new ArrayList<>();
        for (Object k : keys) {
            list.add(get(k));
        }
        return list;
    }
}
