package cn.likegirl.lrpc.provider;

import cn.likegirl.lrpc.api.IRpcService;

/**
 * Description :   .
 *
 * @author : LikeGirl
 * @date : Created in 2020/7/5 11:57
 */
public class IRpcServiceImpl implements IRpcService {

    @Override
    public int add(int a, int b) {
        return a + b;
    }

    @Override
    public int sub(int a, int b) {
        return a - b;
    }

    @Override
    public int mult(int a, int b) {
        return a * b;
    }

    @Override
    public int div(int a, int b) {
        return a / b;
    }
}
