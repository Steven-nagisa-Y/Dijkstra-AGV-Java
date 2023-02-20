# 模拟 AGV 小车路径规划

## 简介

企业实训项目，要求：使用 Dijkstra 算法得出 AGV 小车在货物堆场从起点到终点的最优路径。并要求实现 AGV 小车之间的防碰撞。

本程序模拟两台 AGV，默认是 AGV2 让 AGV1。

## 使用方法

`data.txt`用于描述整个地图，使用图的邻接表存储。

第一行为`n`顶点数，第二行为`m`边数。
之后共`m`行，描述从顶点$n_i$到顶点$n_j$之间的边和权重。
例如：

```plain
4      ----顶点数
5      ----边数
1 2 2  ----顶点1到顶点2之间有一条边，其边权为2
1 3 5  ----顶点1到顶点3之间有一条边，其边权为5
1 4 2
2 3 3
3 4 3
```

## 运行示例

使用`data1.txt`数据：

```plain
请输入AGV1起点：2
请输入AGV1终点：9
请输入AGV2起点：3
请输入AGV2终点：9
第     1 分钟:AGV1在    2   下一点    4   | AGV2在    3   下一点    4
AGV1与AGV2路径冲突，AGV2等待：
第     2 分钟:AGV1在    4   下一点    7   | AGV2在    3   下一点    4
第     3 分钟:AGV1在    7   下一点    9   | AGV2在    4   下一点    7
第     4 分钟:AGV1在    9   下一点   -1   | AGV2在    7   下一点    9
========
总用时 5 分钟
AGV1路程：9；AGV2路程：7
```