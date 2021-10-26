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

- 如果在try执行的一半还没有执行到finally机器就宕机了就不会执行finally去释放锁，所以就要加上一个过期时间

```java
try{
    Boolean result = stringRedisTemplate.opsForValue().setIfAbsent("lockKey", "zehan");  // 等同于setnx
    stringRedisTemplate.expire("lockKey", 10, TimeUnit.SECONDS);
    // 如果result为false则加锁失败
    if(!result){
        return "error code"
    }
   
}finally{
    stringRedisTemplate.delete((locakKey); // 释放锁即删除这个key
}
```

- 但如果 第一行代码刚刚setnx 然后刚开始执行expire命令的时候 机器挂了 就又完了，所以我们执行新命令。在第一行之后加上expire time，在redis底层保证了setnx和设置过期时间的原子性，所以不用担心宕机导致的没有设置成功expire Time的问题

```java
try{
    Boolean result = stringRedisTemplate.opsForValue().setIfAbsent("lockKey", "zehan", 10, TimeUnit.SECONDS);
    // 如果result为false则加锁失败
    if(!result){
        return "error code"
    }
   
}finally{
    stringRedisTemplate.delete((locakKey); // 释放锁即删除这个key
}
```

- 这时候已经初具雏形但是还是有一个问题就是过期时间的设定
  - 例如当一个redis的查询需要执行15s，但是我们的锁只有10s的话，过了10s后，锁过期，另外一个线程会拿到这个锁，但是5s之后一个线程执行finally，反而把第二个线程的锁给删掉了。

```java
String clientId = UUID.randomUUID().toString();

try{
    Boolean result = stringRedisTemplate.opsForValue().setIfAbsent("lockKey", clientId, 10, TimeUnit.SECONDS);
    // 如果result为false则加锁失败
    if(!result){
        return "error code"
    }
   
}finally{
    if(clientId.equals(stringRedisTemplate.opsForValue().get(locakKey))){ //这样保证删除的锁是我们自己创建的
    	stringRedisTemplate.delete((locakKey); // 释放锁即删除这个key
    }
}
```

- 仍有的问题：这个过期时间不好确定设置多少合适，例如我们设置15s但是他线程需要20s才能跑完就会不完全，所以我们采用Redission框架

## Redission

Redission和jedis很相似，是一个redis的template

```java
RLock redissionLocak = redission.getLock(lockKey);
redissionLock.lock(); # 加锁
redissionLock.unlock(); # 释放锁
```

### Redission底层逻辑

- 在一个线程加了锁之后（底层就是加一个setnx命令）。
- 线程二发现锁被占用之后就会自旋，即while循环一直尝试加锁(自旋).
- 有一个后台线程，举例每10s（时间为设置锁的时间的1/3）检查锁是否还存在，如果存在就延长锁的时间，代表这个锁还在被使用

### Redission源码分析

Redission使用LUA脚本，这个脚本的本质就是我们在执行这个命令的时候将这个脚本发给redis服务端，redis可以执行这个脚本来满足一些比较复杂的逻辑。

脚本逻辑：

- 如果key不存在就使用hset设置一个hash（key: （key: value）这样的结构，就是redis key的value是键值对而不是一个单独的value）

  然后设置一个过期时间（**LUA具备原子性 所以不用担心执行的过程中宕机**） 

- 上面的加锁任务是一个Future线程，然后有另外一个线程作为 addKeyFuture.addListener（即为加锁命令添加一个Listener），然后实现operationComplete方法来使得在加锁命令成功执行完成之后再开始Listener。然后在operationComplete里面 每过去 过期时间的1/3时就重新给redis锁重置过期时间

### 主从架构锁失效问题

如果Redis master加上key之后，从库去同步这个key之前，master挂了，这个时候从库没有这个key导致锁失效

#### 原因（zookeeper和redis的区别）

因为redis满足的时AP，A是可用性。Zookeeper是满足CP，C是一致性。Redis只要主节点写入成功后，就会告诉客户端，而Zookeeper是集群内一半以上的follower都同步成功才会告诉客户端写入成功。所以Zookeeper可以避免这个问题，因为即使主节点挂了，zookeeper会找到同步数据最完整的从节点（?不确定）重新成为从节点。所以因为需要同步所以会导致可用性下降，因为同步需要时间没有办法立即返回给客户端。

#### Redis解决办法(RedLock)

RedLock的底层手段和我们的zookeeper类似，我们的setnx命令会同时给多个redis节点执行这个加锁命令，即不采用数据同步的方式，而是直接在从节点做加锁操作。超过半数的redis节点加锁成功才会返回给客户端加锁成功（对我们的可用性有牺牲）。

## 高并发分布式锁的优化

 现在有个问题是 因为我们加锁的操作导致并发执行的任务变成了串行，所以会严重影响我们的并发场景。所以我们要提出一些优化的点

- 例如如果我们有100个库存，我们把100个库存分成10份，即 使用10个锁然后做负载均衡去加这些锁  => **相当于分段加锁 类似concurrentHashMap**

