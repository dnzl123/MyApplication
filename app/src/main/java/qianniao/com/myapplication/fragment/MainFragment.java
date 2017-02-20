package qianniao.com.myapplication.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import qianniao.com.myapplication.base.BaseView;
import qianniao.com.myapplication.presenter.MainFragmentPresenter;
import qianniao.com.myapplication.R;
import qianniao.com.myapplication.adapter.ListViewAdapter;
import qianniao.com.myapplication.adapter.ViewPagerAdapter;

/**
 * Created by Administrator on 2017/1/12.
 */

public class MainFragment extends BaseFragment implements BaseView {

    // private static final String TAG ="MainFragment" ;
    @BindView(R.id.pull_to_refresh_listview)
    PullToRefreshListView pullToRefreshListview;
    @BindView(R.id.search_editText)
    EditText searchEditText;
    @BindView(R.id.search_imageView)
    ImageView searchImageView;


    public List viewPagerImages = new ArrayList();
    public ArrayList<String> items = new ArrayList();
    private ViewPager hotShopingViewPagerCategoryFragment;


    //构造方法
    public MainFragment() {
        // Log.i(TAG, "MainFragment: 我是构造方法");
        presenter = new MainFragmentPresenter(this);
    }

    public ListViewAdapter listViewAdapter;

    @Override
    protected View initView() {
        View view = View.inflate(getActivity(), R.layout.fragment_main, null);
        ButterKnife.bind(this, view);
        //初始化监听
        initListener();
        return view;
    }


    /***
     * 设置pullToRefreshListView的配置
     */
    private void setPullToRefreshListViewMode() {
        //設置模式
        pullToRefreshListview.setMode(PullToRefreshBase.Mode.BOTH);

        //给pullToRefreshListview设置头布局
        ListView refreshableView = pullToRefreshListview.getRefreshableView();
        View header = View.inflate(getActivity(), R.layout.headview_mainfragment, null);
        hotShopingViewPagerCategoryFragment = (ViewPager) header.findViewById(R.id.hotShoping_viewPager_categoryFragment);
        refreshableView.addHeaderView(header);

        //图片和动画

        //设置语言
        ILoadingLayout startLabels = pullToRefreshListview.getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
        startLabels.setRefreshingLabel("正在载入...");// 刷新时
        startLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示

        ILoadingLayout endLabels = pullToRefreshListview.getLoadingLayoutProxy( false, true);
        endLabels.setPullLabel("上拉刷新...");// 刚下拉时，显示的提示
        endLabels.setRefreshingLabel("正在载入...");// 刷新时
        endLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示

    }


    @Override
    public void showSuccessView() {

        //上拉加载和下拉刷新listview的部分
        listViewAdapter = new ListViewAdapter(items, getActivity());
        pullToRefreshListview.setAdapter(listViewAdapter);


        //viewpager的逻辑部分
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(viewPagerImages,getActivity());
        hotShopingViewPagerCategoryFragment.setAdapter(viewPagerAdapter);


    }


    public void initListener() {
        setPullToRefreshListViewMode();
        //上拉加载和下拉刷新坚挺
        pullToRefreshListview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        presenter.changeViewData(0);
                    }
                }, 2000);

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        presenter.changeViewData(1);
                    }
                }, 2000);

            }
        });
    }

    @Override
    public void updataView() {
        pullToRefreshListview.onRefreshComplete();
        listViewAdapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }
}
