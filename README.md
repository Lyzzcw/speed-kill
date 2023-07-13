##### starter 启动层

项目启动入口

##### interfaces  展示层

也叫做用户UI层，对外提供API接口，接收客户端请求，解析数据，返回结果数据，并对异常进行处理。

##### application 应用层

主要处理容易变化的业务场景，可对相关的事件、调度和其他聚合操作进行相关的处理。

##### infrastructure 基础设施层

对其他各层提供通用的基础能力，包括了缓存、工具类、消息、持久化等

##### domain 领域层

将业务系统中相对不变的部分抽象出来封装成领域模型


##### 工程依赖关系：

starter  -----> interfaces -----> application -----> infrastructure -----> domain



##### 集成各组件说明

redis-parents  缓存组件
依赖传递的问题: 
redis-parents 中的common-pool2 版本是2.11.1
依赖到speed-kill中版本莫名成成了了2.9.0，而且指向的依赖也是redis-parents(通过idea的依赖图)
猜测可能是spring-boot影响的,spring-data-redis模块中指定了common-pool2的版本,正好是出现问题的异常版本
2.5.5 指定了 2.9.0
2.7.5 指定了 2.11.1
因为redis-parents组件中用的版本较高，所以这里先指定common-pool2最低为2.11.1
后续在做组件开发时也要注意组件依赖的版本问题，尽量设置成<optional>true<optional>不传导依赖
由父类项目做决定

authentication-parents  权限组件
集成sa-token为主、

common-parents  工具组件 
像hutool,guava,apache-common这些依赖先用这里的，父工程不做引入
