# Key操作

## 1. Key的命令行命令

- keys *查看当前库所有key  (匹配：keys *1)

- exists key判断某个key是否存在

- type key 查看你的key是什么类型

- del key    删除指定的key数据

- unlink key  **根据value选择非阻塞删除，仅将keys从keyspace元数据中删除，真正的删除会在后续异步操作。**

- expire key 10  10秒钟：为给定的key设置过期时间

- ttl key 查看还有多少秒过期，-1表示永不过期，-2表示已过期

##  2. 数据库操作

- select命令切换数据库

- dbsize查看当前数据库的key的数量

- flushdb清空当前库

- flushall通杀全部库

