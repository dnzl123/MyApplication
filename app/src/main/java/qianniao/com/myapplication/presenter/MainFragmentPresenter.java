package qianniao.com.myapplication.presenter;


import qianniao.com.myapplication.base.BaseView;
import qianniao.com.myapplication.fragment.MainFragment;

/**
 * Created by Administrator on 2017/1/12.
 */

public class MainFragmentPresenter implements Presenter {


    private final MainFragment mMainFragment;

    public MainFragmentPresenter(BaseView baseView) {
        this.mMainFragment = (MainFragment) baseView;

    }

    @Override
    public void initeViewData() {
        //加载之前要显示正在加载视图
        mMainFragment.showLoadingView();
        //去网络加载数据根据返回状态确定显示的界面
        //MyApplication.mHttpLoader.display();
        if(mMainFragment.items.size()==0){
            for (int i =1 ;i<6;i++){
                mMainFragment.items.add("主界面条目");
            }
        }

        if (mMainFragment.viewPagerImages.size()==0){
            for (int i =1 ;i<6;i++){
                mMainFragment.viewPagerImages.add("轮播图");
            }
        }
        //如果成功显示成功视图,失败显示失败视图
        mMainFragment.showSuccessView();
    }

    @Override
    public void changeViewData(int what) {
        if (what==0){
            mMainFragment.items.add("我是下拉刷新的数据");
            mMainFragment.updataView();
        }
       if (what==1){
           mMainFragment.items.add("我是上拉加载的数据");
           mMainFragment.updataView();
       }
    }

}
