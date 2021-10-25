package DataFormat;

import redis.clients.jedis.Jedis;

import java.util.Set;

public class Test_Set {
    public static void main(String[] args) {
        // 创建Jedis对象
        Jedis jedis = new Jedis("192.168.137.128", 6379);

        // 创建set
        jedis.sadd("name1", "Lucy", "Jack", "Lucy");
        // 获取set
        Set<String> names = jedis.smembers("name1");
        System.out.println(names);
        // 删除某个元素
        jedis.srem("name1", "Lucy");
        Set<String> newNames = jedis.smembers("name1");
        System.out.println(newNames);

    }
}
