package c.w.g;

import org.junit.Test;
import redis.clients.jedis.Jedis;

public class RedisTest {

    @Test
    public void testConnection() {
        Jedis jedis = new Jedis("localhost");
        System.out.println("jedis.ping()结果: " + jedis.ping());
    }

}
