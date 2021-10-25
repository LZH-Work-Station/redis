# **HyperLogLog**

## 简介

HyperLogLog是一个**去重的**，而且所需内存很少的集合

## 基本方法

1、pfadd  **添加**

（1）格式

pfadd <key>< element> [element ...]  添加指定元素到 HyperLogLog 中

（2）实例

![image-20210720195129210](D:\Tutorial\Redis\image\image-20210720195129210.png)

2、pfcount  **计数**

（1）格式

pfcount<key> [key ...] 计算HLL的近似基数，可以计算多个HLL，比如用HLL存储每天的UV，计算一周的UV可以使用7天的UV合并计算即可

![image-20210720195203914](D:\Tutorial\Redis\image\image-20210720195203914.png)

 

3、pfmerge  **合并**

（1）格式

pfmerge<destkey><sourcekey> [sourcekey ...] 将一个或多个HLL合并后的结果存储在另一个HLL中，比如每月活跃用户可以使用每天的活跃用户来合并计算可得

![image-20210720195228408](D:\Tutorial\Redis\image\image-20210720195228408.png)

 