# Redis 列表

## 1. 列表结构

Redis 列表是简单的字符串列表，按照插入顺序排序。你可以添加一个元素到列表的头部（左边）或者尾部（右边）。

它的底层实际是个双向链表，对两端的操作性能很高，通过索引下标的操作中间的节点性能会较差

## 2. 常用命令

- lpush/rpush <key><value1><value2><value3> .... 从左边/右边插入一个或多个值。

- lpop/rpop <key>从左边/右边删除并输出一个值。值在键在，值光键亡。

 

- rpoplpush <key1><key2>从<key1>列表右边吐出一个值，插到<key2>列表左边。

 

- lrange <key><start><stop>

按照索引下标获得元素(从左到右)

- **lrange mylist 0 -1**  0左边第一个，-1右边第一个，（0，-1表示获取所有列表元素）

- lindex <key><index>按照索引下标获得元素(从左到右)

- llen <key>获得列表长度 

 

- linsert <key> before <value><newvalue>在<value>的后面插入<newvalue>插入值

- lrem <key><n><value>从左边删除n个value(从左到右)

- lset<key><index><value>将列表key下标为index的值替换成value