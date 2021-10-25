package DataFormat;

import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Set;

public class Test_String {
    public static void main(String[] args) {
        // 创建Jedis对象
        Jedis jedis = new Jedis("192.168.137.128", 6379);

        // 测试 返回PONG代表连接成功
        String value = jedis.ping();
        System.out.println(value);
    }

    // 操作Key
    @Test
    public void demo1(){
        // 创建Jedis对象
        Jedis jedis = new Jedis("192.168.137.128", 6379);

        // 添加
        jedis.set("name", "lucy");

        // 获取
        String name = jedis.get("name");
        System.out.println(name);

        // 设置多个key-value
        jedis.mset("k1", "v1", "k2", "v2");
        List<String> mget = jedis.mget("k1", "k2");
        System.out.println(mget);

        // 获得redis中所有的key
        Set<String> keys = jedis.keys("*");
        for(String s : keys) System.out.println(s);
    }
}
