# lRpc
从0到1学习Dubbo原理


# 项目结构
 ```$xslt
+---src
|   +---main
|   |   +---java
|   |   |   \---cn
|   |   |       \---likegirl
|   |   |           \---lrpc
|   |   |               +---api                                   主要定义对外开放的功能与服务接口
|   |   |               +---consumer                              客户端调用
|   |   |               +---protocol                              主要定义自定义传输协议的内容  
|   |   |               +---provider                              实现对外提供的所有服务的具体功能
|   |   |               +---registry                              主要负责保存所有可用的服务名称和服务地址
|   |   |               \---serialization                         主要定义序列化方式
|   |   \---resources
|   |           application.properties
|   |           
|   \---test
|       \---java
|           \---cn
|               \---likegirl
|                   \---lrpc
|    
```                       


# 测试示例
```java
// 提供者(启动服务)
// 启动方法 RpcRegistry#main
public class RpcRegistry {
     public static void main(String[] args) {
         new RpcRegistry(8080).start(); 
     }
}

// 消费者
// 启动方法 RpcConsumer#main
public class RpcConsumer {

    public static void main(String[] args) {
        IRpcHelloService  iRpcHelloService = RpcProxy.create(IRpcHelloService.class);

        System.out.println(iRpcHelloService.hello("LikeGirl"));

        IRpcService iRpcService = RpcProxy.create(IRpcService.class);
        System.out.println("8 + 2 = " + iRpcService.add(8, 2));
        System.out.println("8 - 2 = " + iRpcService.sub(8 , 2));
        System.out.println("8 * 2 = " + iRpcService.mult(8, 2));
        System.out.println("8 / 2 = " + iRpcService.div(8, 2));
    }

}

```

# TODO LIST
- [ ] 注册中心， zookeeper，mysql...
- [ ] 负载策略, 轮询，hash, 随机...
- [ ] spring容器