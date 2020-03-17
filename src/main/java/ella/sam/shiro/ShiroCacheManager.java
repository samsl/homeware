package ella.sam.shiro;

import ella.sam.shiro.redis.RedisCache;
import ella.sam.shiro.redis.RedisClient;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;


public class ShiroCacheManager implements CacheManager {

    private RedisClient redisClient;
    public ShiroCacheManager(RedisClient redisClient) {
        this.redisClient = redisClient;
    }

    @Override
    public <K, V> Cache<K, V> getCache(String name) throws CacheException {
        return new RedisCache<>(redisClient);
    }
}
