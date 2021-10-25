package DataFormat;

import redis.clients.jedis.Jedis;

import java.util.HashMap;

public class Test_Hash {
    public static void main(String[] args) {
        // 创建Jedis对象
        Jedis jedis = new Jedis("192.168.137.128", 6379);
        // 创建HashMap
        jedis.hset("users", "age", "18");

        // 取值
        String age = jedis.hget("users", "age");
        System.out.println(age);

        // 创建HashMap 直接将map类型放进redis
        HashMap<String, String> map = new HashMap<>();
        map.put("name", "zehan");

        // 取值
        jedis.hmset("users2", map);
        System.out.println(jedis.hget("users2", "name"));

    }
}
