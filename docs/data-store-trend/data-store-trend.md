# 漫谈数据存储数据库发展

> 用发展的眼光看问题（盗用大佬话语）但是如何实践还是很难的

## 概览

文本文件(csv, xml, json)

二进制文件(*.dat, excel)(RCFile, Avro, parquet, orc)

sql rdb

no sql

redis <key, value>

mongo (partition, shard)

big table {HBase} （row, column family, time）, slice, tablet

es  (cluster 、 node 、 shard{ Lucene })

图数据库 {Neo4J} (node - relation)

OceanBase？

TiDB，CockroachDB

### RDBMS vs NoSQL

#### RDBMS

RDBMS特点

* 高度组织化结构化数据
* 结构化查询语言（SQL） (SQL)
* 数据和关系都存储在单独的表中。
* 数据操纵语言，数据定义语言
* 严格的一致性
* 基础事务ACID

过一下RDBMS有哪些

* ms access db
* FoxPro
* SQL Server
* DB2
* Oracle
* MySQL (MariaDB, Percona)
* PostgreSQL
* sqlite3
* H2
* hsqldb

#### NoSQL(Not Only)

NoSQL特点

* 代表着不仅仅是SQL
* 没有声明性查询语言
* 没有预定义的模式
* 键 - 值对存储，列存储，文档存储，图形数据库
* 最终一致性，而非ACID属性
* 非结构化和不可预知的数据
* 高性能，高可用性和可伸缩性

#### NoSQL 数据库分类

|     类型      |                        部分代表                         |                                 特点                                  |
| ----------- | --------------------------------------------------- | ------------------------------------------------------------------- |
|     列存储     |        Hbase，Cassandra，Hypertable，ClickHouse        | 顾名思义，是按列存储数据的。最大的特点是方便存储结构化和半结构化数据，方便做数据压缩，对针对某一列或者某几列的查询有非常大的IO优势。 |
|    文档存储     |                   MongoDB，CouchDB                   |    文档存储一般用类似json的格式存储，存储的内容是文档型的。这样也就有机会对某些字段建立索引，实现关系数据库的某些功能。     |
| key-value存储 | Tokyo Cabinet / Tyrant，Berkeley DB，MemcacheDB，Redis |      可以通过key快速查询到其value。一般来说，存储不管value的格式，照单全收。（Redis包含了其他功能）       |
|     图存储     |                    Neo4J，FlockDB                    |               图形关系的最佳存储。使用传统关系数据库来解决的话性能低下，而且设计使用不方便。               |
|    对象存储     |                    db4o，Versant                     |                   通过类似面向对象语言的语法操作数据库，通过对象的方式存取数据。                   |
|   xml数据库    |                Berkeley DB XML，BaseX                |              高效的存储XML数据，并支持XML的内部查询语法，比如XQuery,Xpath。               |

#### NoSQL的优点/缺点

优点:

* 高可扩展性
* 分布式计算
* 低成本
* 架构的灵活性，半结构化数据
* 没有复杂的关系

缺点:

* 没有标准化
* 有限的查询功能（到目前为止）
* 最终一致是不直观的程序

### NewSQL

NewSQL 是一种新方式关系数据库，意在整合 RDBMS 所提供的ACID事务特性（即原子性、一致性、隔离性和可持久性），以及 NoSQL 提供的横向可扩展性。

比如，MyRocks，TiDB，参考F1/Spanner。

(了解不多，请大家一起研究)

## 数据如何存储的

### 行模式 vs 列模式

![[row-column-store.jpeg|row-column-store.jpeg]](row-column-store.jpeg)

### 存储引擎（MySQL发扬的概念）以及索引组织方式

Memory

MyISAM(My索引顺序存取方法)(最初是IBM公司发展起来的一个文件系统)

InnoDB

Merge

tokuDB

MyRocks

#### 索引组织方式

Hash

RTree

Fractal树(分形树)

B+Tree

Log Structured Merge Trees(LSM) {MemTable, SSTable(Sorted String Table)} (From BigTable)

@B+Tree结构图

![[B_Tree_Structure.png|B_Tree_Structure.png]](B_Tree_Structure.png)

@LSM Tree 结构图

![[LSM-Tree.jpeg|LSM-Tree.jpeg]](LSM-Tree.jpeg)

### 分布式

读写分类

![[read-write-separate.png|read-write-separate.png]](read-write-separate.png)

mongo分片集合

![[mongo-sharded-collection.png|mongo-sharded-collection.png]](mongo-sharded-collection.png)

elastic节点分区和复制（elastic_nodes_0204）

![[elastic_nodes_0204.png|elastic_nodes_0204.png]](elastic_nodes_0204.png)

#### CAP定理（CAP theorem）

在计算机科学中, CAP定理（CAP theorem）, 又被称作 布鲁尔定理（Brewer's theorem）, 它指出对于一个分布式计算系统来说，不可能同时满足以下三点:

* 一致性(Consistency) (所有节点在同一时间具有相同的数据)
* 可用性(Availability) (保证每个请求不管成功或者失败都有响应)
* 分隔容忍(Partition tolerance) (系统中任意信息的丢失或失败不会影响系统的继续运作)

CAP理论的核心是：一个分布式系统不可能同时很好的满足一致性，可用性和分区容错性这三个需求，最多只能同时较好的满足两个。 因此，根据 CAP 原理将 NoSQL 数据库分成了满足 CA 原则、满足 CP 原则和满足 AP 原则三 大类：

* CA - 单点集群，满足一致性，可用性的系统，通常在可扩展性上不太强大。
* CP - 满足一致性，分区容忍性的系统，通常性能不是特别高。
* AP - 满足可用性，分区容忍性的系统，通常可能对一致性要求低一些。

#### 分布式算法

* Paxos算法: 各副本竞争提议权，然后让议案在各副本间达成一致
* Raft算法: 先选举出leader，leader完全负责replicated log的管理。
* 一致性hash算法(归位分布式算法有些牵强): 以前点映射改为区段映射，使得数据节点变更后其他数据节点变动尽可能小
* EPaxos（Egalitarian Paxos）

NOTE: 分布式部署一般不保证事务，这是NEWSQL要挑战的难点。Google的Spanner/F1提出了一种TrueTime API，使得事务序列化满足外部一致性。

## 参考:

<https://www.cnblogs.com/hdc520/p/13718470.html>

<https://www.runoob.com/mongodb/nosql.html>

<http://dblab.xmu.edu.cn/post/google-bigtable/>

<https://cloud.tencent.com/developer/article/1131036>

<https://segmentfault.com/a/1190000009707788>

<https://static.googleusercontent.com/media/research.google.com/zh-CN//archive/bigtable-osdi06.pdf>

<http://static.googleusercontent.com/media/research.google.com/en//archive/spanner-osdi2012.pdf>

<http://static.googleusercontent.com/media/research.google.com/en//pubs/archive/41344.pdf>
