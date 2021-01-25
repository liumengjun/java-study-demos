# JPA query 的几种写法

## jpql

> JPQL is hql? jpa 是javaee的一部分规范，spring data jpa 采用hibernate实现，eclipse也有实现JPA，等等
> 
> https://zh.wikipedia.org/wiki/Java%E6%8C%81%E4%B9%85%E5%8C%96API
> https://jcp.org/aboutJava/communityprocess/final/jsr338/index.html

### interfaces

- findXxx
- findBy
- count
- page

### @query (nativeQuery=false)

- select * from Entity
- select e from Entity e
- select e.* from Entity e
- from Entity
- select col1, col2, col3 from Entity // List<Object[]>
- select new ai.advance.your.package.Pojo(col1, col2, col3) from Entity
- select new map(... // 这种方式很傻，key为"0","1".etc（??col as keyName）

### criteria query

> @query (nativeQuery=false)是静态的，criteria可以用程序动态组装查询语句

- criteria: CriteriaBuilder + CriteriaQuery + Expression + Predicate => TypedQuery

    ```java
    final Root<?> root = query.from(Entity.class);
    query.where(this.buildPredicates(root, builder, params...));
    return lem.createQuery(query).getResultList();
    ```

- Specification 类似
- Example 很懒
- Join 比较复杂, 需要定义Entity时就指明关联关系

### result type

- select(root)
- Pojo(field1, field2) + Root<Entity>, builder.createQuery(Pojo.class) + multiselect(field1, field2)
  > Root仍然是Entity，用于组装Selection，Pojo为结果类型
- builder.construct(Pojo.class, field1, field2) 同上
- Tuple 作为结果类型
- 没有Pojo类，结果为Object[]

## native sql

### @query (nativeQuery=true)

- 普通的sql，大家好像偏爱这种方式
- 方便join语句
- 方便各种聚合查询

### createNativeQuery

- 程序组装sql，更灵活的方式写sql
- 注意模糊查询参数设置

    ```java
    sql += "col like :key";
    ...
    query.setParameter(key, "%" + value + "%");
    ```

### result transformer

> native query时可显示指定结果类型固定（即使select个别字段），然而有时可以借助ResultTransformer以返回更多样的结果类型

- Object[]
  > no resultType no transformer
- Map<String, Object>
  > map transformer
- Pojo
  > createNativeQuery(Pojo.class)
  > Transformers.aliasToBean(Pojo.class). sql中下划线col需要显示指定别名和Pojo中field名称相同
- List，Transformers.TO_LIST, bad idea

#### result transformer 例子

```java
String sql = "select col1, col2, col3 from t_entity";
Query query = em.createNativeQuery(sql); // no resultClass
// Query 接口是 spring-data-jpa 的接口，而 org.hibernate.SQLQuery 接口是 hibenate 的接口，这里的做法就是先转成 hibenate 的查询接口对象，然后设置结果转换器
query.unwrap(org.hibernate.SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
return query.getResultList(); // List<Map<String, Object>>, key为列名，含有下划线
```

> criteria 风格的 TypedQuery 生成的sql语句alias是col_m_n_之类的，不适用ALIAS_TO_ENTITY_MAP，aliasToBean。
> 
> query.unwrap 参数为org.hibernate.SQLQuery，而不是org.hibernate.Query。TypedQuery不能unwrap为org.hibernate.SQLQuery，即使unwrap成org.hibernate.Query，也得不到任何结果。
> 
> `ResultTransformer`接口

## 分页

- 老老实实limit offset, size
- JPA page小优化: 如果返回条数n小于size，就可以推测总条数为offset+n，否则，还需要再查询一次总条数
- 确定数据量小，比如<1000，可以选择在应用服务器里分页，甚至都丢给web端让web端分页
- 数据量特大，where id > last_page_max_id limit size，这样可以不管总条数是多少，如果where查询恰好为索引，总条数计算快，否则慢的不可想象

## 原则

- 具体类型对宽泛的类型更好
- 使用索引
- select 必要的字段
- 大量数据时分批
