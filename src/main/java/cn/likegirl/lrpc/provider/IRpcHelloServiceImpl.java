package cn.likegirl.lrpc.provider;

import cn.likegirl.lrpc.api.IRpcHelloService;

/**
 * Description :   .
 *
 * @author : LikeGirl
 * @date : Created in 2020/7/5 11:56
 */
public class IRpcHelloServiceImpl implements IRpcHelloService {

    @Override
    public String hello(String name) {
        return "Hello " + name + "!";
    }
}
