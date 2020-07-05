package cn.likegirl.lrpc.consumer.proxy;

import cn.likegirl.lrpc.protocol.InvokerProtocol;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Description :   .
 *
 * @author : LikeGirl
 * @date : Created in 2020/7/5 13:08
 */
public class RpcProxy {

    @SuppressWarnings("unchecked")
    public static <T> T create(Class<T> clazz){
        MethodProxy proxy = new MethodProxy(clazz);
        Class[] interfaces = clazz.isInterface() ? new Class[]{clazz} : clazz.getInterfaces();
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), interfaces, proxy);

    }

    private static class MethodProxy implements InvocationHandler{

        private Class<?> clazz;

        public MethodProxy(Class<?> clazz) {
            this.clazz = clazz;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if(Object.class.equals(method.getDeclaringClass())){
                return method.invoke(clazz, args);
            }else{
                // 如果传进来的是一个接口(核心)
                return rpcInvoker(proxy, method, args);
            }
        }


        public Object rpcInvoker(Object proxy, Method method, Object[] args){
            InvokerProtocol msg = new InvokerProtocol();
            msg.setClassName(this.clazz.getName());
            msg.setMethodName(method.getName());
            msg.setParames(method.getParameterTypes());
            msg.setValues(args);

            final RpcProxyHandler rpcProxyHandler = new RpcProxyHandler();
            EventLoopGroup group = new NioEventLoopGroup();
            try {
                Bootstrap b = new Bootstrap();
                b.group(group)
                        .channel(NioSocketChannel.class)
                        .option(ChannelOption.TCP_NODELAY, true)
                        .handler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel ch) throws Exception {
                                ChannelPipeline pipeline = ch.pipeline();
                                // 自定义协议解码器
                                /*
                                 * maxFrameLength: 框架的最大长度。 如果帧的长度大于此值， 则将抛出TooLongFrameException
                                 * lengthFieldOffset： 长度属性的偏移量。 即对应的长度属性在整个消息数据中的位置
                                 * lengthFieldLength：长度字段的长度。 如果长度属性是int型， 那么这个值就是4（long型就是8）
                                 * lengthAdjustment： 要添加到长度属性值的补偿值
                                 * initialBytesToStrip： 从解码帧中去除的第一个字节数
                                 */
                                pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0 ,4 , 0 ,4));
                                // 自定义协议编码器
                                pipeline.addLast(new LengthFieldPrepender(4));
                                // 对象参数类型编码器
                                pipeline.addLast("encoder", new ObjectEncoder());
                                // 对象参数类型解码器
                                pipeline.addLast("decoder", new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)));

                                pipeline.addLast("handler", rpcProxyHandler);
                            }
                        });
                ChannelFuture channelFuture = b.connect("localhost", 8080).sync();
                channelFuture.channel().writeAndFlush(msg).sync();
                channelFuture.channel().closeFuture().sync();
            } catch (Exception e) {
                group.shutdownGracefully();
            }

            return rpcProxyHandler.getResponse();
        }
    }

}
