# Redis集合

## 1. 简介

Redis的Set是string类型的无序集合。它底层其实是一个value为null的hash表，所以添加，删除，查找的**复杂度都是**O(1)。

## 2. 常用命令

- sadd <key><value1><value2> ..... 

将一个或多个 member 元素加入到集合 key 中，已经存在的 member 元素将被忽略

- smembers <key>取出该集合的所有值。

- sismember <key><value>判断集合<key>是否为含有该<value>值，有1，没有0

- scard<key>返回该集合的元素个数。

- srem <key><value1><value2> .... 删除集合中的某个元素。

- spop <key>**随机从该集合中吐出一个值。**

- srandmember <key><n>随机从该集合中取出n个值。不会从集合中删除 。

- smove <source><destination>value 把集合中一个值从一个集合移动到另一个集合

- sinter <key1><key2>返回两个集合的交集元素。

- sunion <key1><key2>返回两个集合的并集元素。

- sdiff <key1><key2>返回两个集合的**差集**元素(key1中的，不包含key2中的)