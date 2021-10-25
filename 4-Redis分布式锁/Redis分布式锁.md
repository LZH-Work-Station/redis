# 分布式锁

## setnx key value

setnx 和 set的区别是，setnx 只有当key不存在的时候才会成功执行，而set是会覆盖老版本的值

### 简单的分布式锁

```java
Boolean result = stringRedisTemplate.opsForValue().setIfAbsent("lockKey", "zehan");  // 等同于setnx
// 如果result为false则加锁失败
if(!result){
    return "error code"
}
```

### 原理讲解

因为redis的本质是单线程，所以不管你有多少请求过来，redis都会按照先来后到的顺序进行排队，所以当第一个人执行了setnx之后，第二个人执行的时候就会失败。自然result是false，然后再让前端去给用户一个友好的提示。 

### 可能出现的问题

- 其实上面我们的代码并不完美，因为如果获得了锁之后，但是在中间的代码块里面抛出了异常，就会导致锁永远不会释放，所以可以采用try catch finally

```java
try{
    Boolean result = stringRedisTemplate.opsForValue().setIfAbsent("lockKey", "zehan");  // 等同于setnx
    // 如果result为false则加锁失败
    if(!result){
        return "error code"
    }
}finally{
    stringRedisTemplate.delete((locakKey); // 释放锁即删除这个key
}
```

