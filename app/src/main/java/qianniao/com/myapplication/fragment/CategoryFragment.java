package qianniao.com.myapplication.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import qianniao.com.myapplication.base.BaseView;
import qianniao.com.myapplication.presenter.CategoryFragmentPreserter;
import qianniao.com.myapplication.utils.UIUtils;
import qianniao.com.myapplication.MyApplication;
import qianniao.com.myapplication.R;
import qianniao.com.myapplication.adapter.ListViewAdapter;
import qianniao.com.myapplication.adapter.ViewPagerAdapter;
import qianniao.com.myapplication.view.RefreshListView;


/**
 * Created by Administrator on 2017/1/12.
 */

public class CategoryFragment extends BaseFragment implements BaseView {


    public List categoryItems = new ArrayList();
    @BindView(R.id.refreshlistview_fragment_category)
    RefreshListView refreshlistviewFragmentCategory;
    @BindView(R.id.search_editText_categoryFragment)
    EditText searchEditTextCategoryFragment;
    @BindView(R.id.search_imageView_categoryFragment)
    ImageView searchImageViewCategoryFragment;

    private ListViewAdapter listViewAdapter;
    private ViewPager hotShopingViewPager;
    private LinearLayout pagerIndicator;
    private AutoScrollTask mAutoScrollTask;

    public CategoryFragment() {
        presenter = new CategoryFragmentPreserter(this);
    }


    @Override
    protected View initView() {

        View view = View.inflate(getActivity(), R.layout.fragment_category, null);
        ButterKnife.bind(this, view);
        initListener();
        return view;

    }


    @Override
    public void showSuccessView() {

        //viewpager的自动无限轮播.与指示器
        for (int i = 0; i < viewPagerImages.size(); i++) {
            ImageView ivIndicator = new ImageView(getContext());
            //设置默认时候的点的src
            ivIndicator.setImageResource(R.drawable.indicator_normal);

            //选择默认选中第一个点
            if (i == 0) {
                ivIndicator.setImageResource(R.drawable.indicator_selected);
            }
            int sixDp = (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 6,
                    UIUtils.getResources().getDisplayMetrics()) + .5f);

            int width = UIUtils.dip2Px(6);//6dp
            int height = UIUtils.dip2Px(6);//6dp

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);

            params.leftMargin = UIUtils.dip2Px(6);//6dp
            params.bottomMargin = UIUtils.dip2Px(6);//6dp
            pagerIndicator.addView(ivIndicator, params);
        }
        //监听ViewPager的页面切换操作
        hotShopingViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //处理position
                position = position % viewPagerImages.size();

                //控制Indicator选中效果
                for (int i = 0; i < viewPagerImages.size(); i++) {
                    ImageView ivIndicator = (ImageView) pagerIndicator.getChildAt(i);
                    //1.还原默认效果
                    ivIndicator.setImageResource(R.drawable.indicator_normal);
                    //2.选中应该选中的
                    if (position == i) {
                        ivIndicator.setImageResource(R.drawable.indicator_selected);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //设置viewPager页面的初始位置
        int curItem = Integer.MAX_VALUE / 2;

        //对curItem做偏差处理
        int diff = Integer.MAX_VALUE / 2 % viewPagerImages.size();

        curItem = curItem - diff;

        hotShopingViewPager.setCurrentItem(curItem);

        //实现自动轮播
        if (mAutoScrollTask == null) {
            mAutoScrollTask = new AutoScrollTask();
            mAutoScrollTask.start();
        }
        //按下去的时候停止轮播
        hotShopingViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mAutoScrollTask.stop();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        mAutoScrollTask.stop();
                        break;
                    case MotionEvent.ACTION_UP:
                         mAutoScrollTask.start();
                        break;

                    default:
                        break;
                }
                return false;
            }
        });
        //listView的界面

        listViewAdapter = new ListViewAdapter(categoryItems, getActivity());
        refreshlistviewFragmentCategory.setAdapter(listViewAdapter);
        //  listviewCategoryFragment.setAdapter(listViewAdapter);



    }


    public void initListener() {

        //添加头布局
        View header = View.inflate(getActivity(), R.layout.headview_mainfragment, null);
        hotShopingViewPager = (ViewPager) header.findViewById(R.id.hotShoping_viewPager_categoryFragment);
        pagerIndicator = (LinearLayout) header.findViewById(R.id.item_home_picture_container_indicator);
        refreshlistviewFragmentCategory.addHeaderView(header);
        //给viewPager设置选择器
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(viewPagerImages,getActivity());
        hotShopingViewPager.setAdapter(viewPagerAdapter);
        //设置监听回调
        refreshlistviewFragmentCategory.setOnRefreshListener(new RefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        presenter.changeViewData(0);
                    }
                }, 2000);

            }

            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        presenter.changeViewData(1);
                    }
                }, 2000);
            }
        });
    }

    public List viewPagerImages=new ArrayList();

    @Override
    public void updataView() {
       // siwprefreshlayoutFragmentCategory.setRefreshing(false);
        refreshlistviewFragmentCategory.onRefreshComplete();
        listViewAdapter.notifyDataSetChanged();


    }

    class AutoScrollTask implements Runnable {
        /**
         * 开始滚动
         */
        public void start() {
            stop();
            MyApplication.getMainThreadHandler().postDelayed(this, 3000);
        }

        /**
         * 结束滚动
         */
        public void stop() {
            MyApplication.getMainThreadHandler().removeCallbacks(this);
        }

        @Override
        public void run() {
            //切换ViewPager
            int currentItem = hotShopingViewPager.getCurrentItem();
            currentItem++;

            hotShopingViewPager.setCurrentItem(currentItem);

            start();
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }


}


