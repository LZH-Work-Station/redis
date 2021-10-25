# Redis的哈希

## 1. 简介

Redis hash是一个string类型的field和value的映射表，hash特别适合用于存储对象。

![image-20210719210321938](D:\Tutorial\Redis\image\image-20210719210321938.png)

我们通过key和field两个作为key来定位value。可以通过key获取整个object，也可以使用key加field对value进行指定更改。

## 2. 常用命令

- hset <key><field><value>给<key>集合中的 <field>键赋值<value>

- hget <key1><field>从<key1>集合<field>取出 value 

- hmset <key1><field1><value1><field2><value2>... 批量设置hash的值

- hexists<key1><field>查看哈希表 key 中，给定域 field 是否存在。 

- hkeys <key>列出该hash集合的所有field

- hvals <key>列出该hash集合的所有value

- hincrby <key><field><increment>为哈希表 key 中的域 field 的值加上增量 1  -1

- hsetnx <key><field><value>将哈希表 key 中的域 field 的值设置为 value ，当且仅当域 field 不存在 .

 