package DataFormat;

import redis.clients.jedis.Jedis;

import java.util.Set;

public class Test_Zset {
    public static void main(String[] args) {
        // 创建Jedis对象
        Jedis jedis = new Jedis("192.168.137.128", 6379);

        // 添加
        jedis.zadd("china", 100d, "Tianjin");
        jedis.zadd("china", 200d, "BeiJing");
        jedis.zadd("china", 300d, "ShangHai");

        // 获取
        Set<String> china = jedis.zrange("china", 0, -1);

        System.out.println(china);
    }
}
