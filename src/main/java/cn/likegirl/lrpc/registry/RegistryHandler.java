package cn.likegirl.lrpc.registry;

import cn.likegirl.lrpc.protocol.InvokerProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.io.File;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Description :   .
 *
 * @author : LikeGirl
 * @date : Created in 2020/7/5 12:19
 */
public class RegistryHandler extends ChannelInboundHandlerAdapter {

    /**
     * 保存所有可用的服务
     */
    public final static ConcurrentMap<String, Object> registryMap = new ConcurrentHashMap<>();

    /**
     * 保存所有可用服务
     */
    private final List<String> classNames = new ArrayList<>();


    public RegistryHandler() {
        // 完成递归扫描
        scannerClass("cn.likegirl.lrpc.provider");
        // 完成服务注册
        doRegistry();

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        InvokerProtocol request = (InvokerProtocol) msg;
        if(registryMap.containsKey(request.getClassName())){
            Object clazz = registryMap.get(request.getClassName());
            Method method = clazz.getClass().getMethod(request.getMethodName(), request.getParames());
            Object result = method.invoke(clazz, request.getValues());
            ctx.write(result);
            ctx.flush();
            ctx.close();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    /**
     * 递归扫描
     */
    private void scannerClass(String packageName){
        try {
            URL url = this.getClass().getClassLoader().getResource(packageName.replaceAll("\\.", "/"));
            URI uri = url.toURI();
            File dir = new File(uri.getPath());
            for (File file : dir.listFiles()) {
                // 如果是一个文件夹， 继续递归
                if(file.isDirectory()){
                    scannerClass(packageName + "." + file.getName());
                }else{
                    classNames.add(packageName + "." + file.getName().replace(".class", "").trim());
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    private void doRegistry(){
        if(classNames.size() == 0){
            return;
        }
        for (String className : classNames) {
            try {
                Class<?> clazz = Class.forName(className);
                Class<?> i= clazz.getInterfaces()[0];
                registryMap.put(i.getName(), clazz.newInstance());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
