package cn.likegirl.lrpc.consumer.proxy;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.Getter;

/**
 * Description :   .
 *
 * @author : LikeGirl
 * @date : Created in 2020/7/5 13:22
 */
public class RpcProxyHandler extends ChannelInboundHandlerAdapter {

    @Getter
    private Object response;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        response = msg;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("client exception is general");
    }
}
