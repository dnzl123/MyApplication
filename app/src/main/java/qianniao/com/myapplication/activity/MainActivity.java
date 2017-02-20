package qianniao.com.myapplication.activity;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;

import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RadioButton;


import butterknife.BindView;
import butterknife.ButterKnife;
import qianniao.com.myapplication.factory.FragmentFactory;
import qianniao.com.myapplication.fragment.BaseFragment;
import qianniao.com.myapplication.R;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.viewpager_main)
    ViewPager viewpagerMain;
    @BindView(R.id.radioButton_main)
    RadioButton radioButtonMain;
    @BindView(R.id.radioButton_category)
    RadioButton radioButtonCategory;
    @BindView(R.id.activity_main)
    LinearLayout activityMain;
    @BindView(R.id.radioButton_test)
    RadioButton radioButtonTest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initeView();
        initeListener();

    }


    private void initeListener() {

        //点击事件
        radioButtonMain.setOnClickListener(this);
        radioButtonCategory.setOnClickListener(this);
        radioButtonTest.setOnClickListener(this);

        //fragment改变
        viewpagerMain.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                BaseFragment baseFragment = FragmentFactory.mCacheFragments.get(position);
                //触发加载数据
                baseFragment.presenter.initeViewData();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    private void initeView() {
        ButterKnife.bind(this);
        viewpagerMain = (ViewPager) findViewById(R.id.viewpager_main);
        FragmentManager fragmentManager = getSupportFragmentManager();
        viewpagerMain.setAdapter(new MainUiFragmentPagerAdapter(fragmentManager));

        //手动初始化第一个页面的数据
        viewpagerMain.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //ViewPager已经展示给用户看-->说明HomeFragment和AppFragment已经创建好了
                //手动选中第一页
                FragmentFactory.mCacheFragments.get(0).presenter.initeViewData();

                viewpagerMain.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.radioButton_main:
                viewpagerMain.setCurrentItem(0);
                break;
            case R.id.radioButton_category:
                viewpagerMain.setCurrentItem(1);
                break;
            case R.id.radioButton_test:
                viewpagerMain.setCurrentItem(2);
                break;
        }
    }


    //重写回退的点击事件,退出程序.
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("您真的要退出应用？");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            }).setNegativeButton("取消", null).create().show();
        }
        return super.onKeyDown(keyCode, event);
    }

    class MainUiFragmentPagerAdapter extends FragmentStatePagerAdapter {

        public MainUiFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            Fragment fragment = FragmentFactory.createFragment(position);
            return fragment;
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}
