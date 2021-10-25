# Redis 字符串操作

## 1. String字符串的大小

String类型是Redis最基本的数据类型，一个Redis中字符串value最多可以是512M

## 2. 常用方法

### 1. 字符串操作

#### （1）单Key操作

- set  <key><value>添加键值对                 

*NX：当数据库中key不存在时，可以将key-value添加数据库

*XX：当数据库中key存在时，可以将key-value添加数据库，与NX参数互斥

*EX：key的超时秒数

*PX：key的超时毫秒数，与EX互斥

 

- get  <key>查询对应键值

- append <key><value>将给定的<value> 追加到原值的末尾

- strlen <key>获得值的长度

- setnx <key><value>只有在 key 不存在时  设置 key 的值

#### （2）多Key操作

- mset <key1><value1><key2><value2> ..... 

同时设置一个或多个 key-value对 

- mget <key1><key2><key3> .....

同时获取一个或多个 value 

- msetnx <key1><value1><key2><value2> ..... 

同时设置一个或多个 key-value 对，当且仅当所有给定 key 都不存在。

#### （3）范围操作

- getrange <key><起始位置><结束位置>

获得值的范围，类似java中的substring，**前包，后包**

- setrange <key><起始位置><value>

用 <value> 覆写<key>所储存的字符串值，从<起始位置>开始(**索引从0****开始**)。

#### （4）其他

- **setex <key><****过期时间****><value>**

设置键值的同时，设置过期时间，单位秒。

- getset <key><value>

以新换旧，设置了新值同时获得旧值。





###  2. Integer类型操作

- incr <key>

将 key 中储存的数字值增1

只能对数字值操作，如果为空，新增值为1



- decr <key>

将 key 中储存的数字值减1

只能对数字值操作，如果为空，新增值为-1



- incrby / decrby <key><步长>

将 key 中储存的数字值增减。自定义步长。