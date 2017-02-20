package qianniao.com.myapplication.factory;

import qianniao.com.myapplication.proxy.ThreadPoolProxy;

/**
 * Created by Administrator on 2017/1/18.
 */

public class ThreadPoolProxyFactory {

    static ThreadPoolProxy mNormalThreadPoolProxy;

    /**
     * 得到线程池代理
     */
    public static ThreadPoolProxy getNormalThreadPoolProxy() {
        if (mNormalThreadPoolProxy == null) {
            synchronized (ThreadPoolProxyFactory.class) {
                if (mNormalThreadPoolProxy == null) {
                    mNormalThreadPoolProxy = new ThreadPoolProxy(5, 5);
                }
            }
        }
        return mNormalThreadPoolProxy;
    }
}
