package cn.likegirl.lrpc.api;

/**
 * Description :   .
 *
 * @author : LikeGirl
 * @date : Created in 2020/7/5 11:50
 */
public interface IRpcService {

    /**
     * 加
     */
    int add(int a, int b);

    /**
     * 减
     */
    int sub(int a, int b);

    /**
     * 乘
     */
    int mult(int a, int b);

    /**
     * 除
     */
    int div(int a, int b);

}
