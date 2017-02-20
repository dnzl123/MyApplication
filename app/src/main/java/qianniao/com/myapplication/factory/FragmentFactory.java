package qianniao.com.myapplication.factory;

import java.util.HashMap;
import java.util.Map;

import qianniao.com.myapplication.fragment.BaseFragment;
import qianniao.com.myapplication.fragment.CategoryFragment;
import qianniao.com.myapplication.fragment.MainFragment;
import qianniao.com.myapplication.fragment.TestFragment;

/**
 * Created by Administrator on 2017/1/18.
 */

public class FragmentFactory {
    public static final int FRAGMENT_MAIN = 0;//首页
    public static final int FRAGMENT_CATEGORY = 1;//分类
    public static final int FRAGMENT_TEST = 2;//测试
    public static Map<Integer, BaseFragment> mCacheFragments = new HashMap<>();

    public static BaseFragment createFragment(int position) {
        //定义Fragment对象
        BaseFragment fragment = null;

        //优先缓存集合中取出来
        if (mCacheFragments.containsKey(position)) {
            fragment = mCacheFragments.get(position);
            return fragment;
        }

        switch (position) {
            case FRAGMENT_MAIN://返回 首页 对应的fragment
                fragment = new MainFragment();
                break;
            case FRAGMENT_CATEGORY://返回 分类 对应的fragment
                fragment = new CategoryFragment();
                break;
            case FRAGMENT_TEST://返回 排行 对应的fragment
                fragment = new TestFragment();
                break;

            default:
                break;
        }
        //保存Fragment到集合中
        mCacheFragments.put(position, fragment);

        return fragment;
    }
}
