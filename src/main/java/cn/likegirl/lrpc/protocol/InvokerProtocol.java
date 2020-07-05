package cn.likegirl.lrpc.protocol;

import java.io.Serializable;
import lombok.Data;

/**
 * Description :   .
 *
 * @author : LikeGirl
 * @date : Created in 2020/7/5 11:53
 */
@Data
public class InvokerProtocol implements Serializable {

    /**
     * 类名
     */
    private String className;

    /**
     * 函数名称
     */
    private String methodName;

    /**
     * 参数类型
     */
    private Class<?>[] parames;

    /**
     * 参数列表
     */
    private Object[] values;

}
