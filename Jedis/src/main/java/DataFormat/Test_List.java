package DataFormat;

import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Set;

public class Test_List {
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
        // 创建并加入到list中
        jedis.lpush("key1", "Lucy", "Mary", "Jack");
        // 获取全部的list
        List<String> values = jedis.lrange("key1", 0, -1);
        System.out.println(values);
    }
}
