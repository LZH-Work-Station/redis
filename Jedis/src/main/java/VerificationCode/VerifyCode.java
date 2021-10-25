package VerificationCode;

import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.util.Random;

public class VerifyCode {
    /**
     * 1、输入手机号，点击发送后随机生成6位数字码，2分钟有效
     * 2、输入验证码，点击验证，返回成功或失败
     * 3、每个手机号每天只能输入3次
     */

    public static void main(String[] args) {
        String phone = "13212139632";
        //SetRedisCode(phone);
        verifyCode(phone, "367543");
    }

    public static void verifyCode(String phone, String code){
        // 创建Jedis对象
        Jedis jedis = new Jedis("192.168.137.128", 6379);

        String redisCode = jedis.get("VerifyCode" + phone + ":code");

        // 判断
        if(redisCode.equals(code)){
            System.out.println("成功");
        }else{
            System.out.println("失败");
        }
        jedis.close();
    }

    // 3、每个手机号每天只能发送3次，验证码放到redis中，设置过期时间120s
    public static void SetRedisCode(String phone){
        // 创建Jedis对象
        Jedis jedis = new Jedis("192.168.137.128", 6379);

        // 拼接key
        // 手机发送次数key
        String countKey = "VerifyCode" + phone + ":count";
        // 验证码key
        String codeKey = "VerifyCode" + phone + ":code";

        // 每个手机只能发三次
        String count = jedis.get(countKey);
        if(count==null){
            // 没有发送次数
            // 设置发送次数为1
            // 过期时间 24小时
            jedis.setex(countKey, 24*60*60, "1");
        }else if(Integer.parseInt(count)<=2){
            // 发送次数 + 1
            jedis.incr(countKey);
            // 发送验证码到redis里
            String vcode = getCode();
            // 过期时间2分钟
            System.out.println(vcode);

            jedis.setex(codeKey, 120, vcode);
        }else if(Integer.parseInt(count) >= 3){
            System.out.println("不能再发送了");
            jedis.close();
        }
        jedis.close();
    }

    public static String getCode(){
        Random random = new Random();
        String code = "";
        for(int i=0;i<6;i++){
            int rand = random.nextInt(10);
            code += rand;
        }
        return code;
    }

    @Test
    public void demo1(){
        // 创建Jedis对象
        Jedis jedis = new Jedis("192.168.137.128", 6379);

        String s = jedis.get("VerifyCode13212139632:code");
        System.out.println(s);
    }
}
