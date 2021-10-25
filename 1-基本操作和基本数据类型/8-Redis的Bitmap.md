# Redis 的Bitmap

## 1. 简介

Bitmap是key对应了一个数组，数组是由0，1组成，数组的位置叫做偏移量

![image-20210719213727348](D:\Tutorial\Redis\image\image-20210719213727348.png)



### 1. 使用的案例

![image-20210720163833382](D:\Tutorial\Redis\image\image-20210720163833382.png)

## 2. 使用方法

- setbit 设置Bitmaps中某个偏移量的值（0或1）

  setbit<key><offset><value>

  

- getbit 获取Bitmaps中某个偏移量的值

  getbit<key><offset>                

  获取键的第offset位的值（从0开始算）

  

- bitcount

  统计**字符串**被设置为1的bit数。一般情况下，给定的整个字符串都会被进行计数，通过指定额外的 start 或 end 参数，可以让计数只在特定的位上进行。

  （1）格式（8位算一个字节，start和end是以字节为单位的，如果是0, 2就代表计算从0到15这个区间的1的个数）

  bitcount<key>[start end] 统计字符串从start字节到end字节比特值为1的数量

  

- bitop

  (1)格式

  bitop and(or/not/xor) <destkey> [key…]

  bitop是一个复合操作， 它可以做多个Bitmaps的and（交集） 、 or（并集） 、 not（非） 、 xor（异或） 操作并将结果保存在destkey中。

  例如：

  我们有一个Key记录着星期一的访问用户，另一个Key记录着星期二的访问用户，然后我们对这两个key的bitmap进行交集运算，计算连续两天登陆的用户

​                               
