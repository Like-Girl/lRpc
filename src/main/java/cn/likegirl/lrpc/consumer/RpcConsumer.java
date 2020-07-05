package cn.likegirl.lrpc.consumer;

import cn.likegirl.lrpc.api.IRpcHelloService;
import cn.likegirl.lrpc.api.IRpcService;
import cn.likegirl.lrpc.consumer.proxy.RpcProxy;

/**
 * Description :   .
 *
 * @author : LikeGirl
 * @date : Created in 2020/7/5 13:35
 */
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
