package qianniao.com.myapplication;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import org.senydevpkg.net.HttpLoader;

import qianniao.com.myapplication.helpdeskdemo.DemoHelper;
import qianniao.com.myapplication.helpdeskdemo.Preferences;


public class MyApplication extends Application{
    private static Context mContext;
    private static Handler mMainThreadHandler;
    public static HttpLoader mHttpLoader;


    /**
     * 得到上下文
     *
     * @return
     */
    public static Context getContext() {
        return mContext;
    }

    /**
     * 得到主线程里面的创建的一个hanlder
     *
     * @return
     */
    public static Handler getMainThreadHandler() {
        return mMainThreadHandler;
    }


    @Override
    public void onCreate() {//程序的入口方法
        super.onCreate();
        //上下文
        mContext = getApplicationContext();

        //主线程的Handler
        mMainThreadHandler = new Handler();
        //网络连接
        mHttpLoader = HttpLoader.getInstance(mContext);

        Preferences.init(this);
        DemoHelper.getInstance().init(this);

    }
}
