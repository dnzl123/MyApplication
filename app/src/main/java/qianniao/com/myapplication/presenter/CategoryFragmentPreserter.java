package qianniao.com.myapplication.presenter;

import qianniao.com.myapplication.base.BaseView;
import qianniao.com.myapplication.fragment.CategoryFragment;

/**
 * Created by Administrator on 2017/1/16.
 */

public class CategoryFragmentPreserter implements Presenter {

    private final CategoryFragment mCategoryFragment;

    public CategoryFragmentPreserter(BaseView baseView) {
        mCategoryFragment = (CategoryFragment) baseView;
    }

    @Override
    public void initeViewData() {
        if (mCategoryFragment.categoryItems.size()==0&&mCategoryFragment.viewPagerImages.size()==0){
            for (int i =1 ;i<6;i++){
                mCategoryFragment.categoryItems.add("千城通"+i);
            }
            for (int i =1 ;i<6;i++){
                mCategoryFragment.viewPagerImages.add("轮播图");
            }
            mCategoryFragment.showSuccessView();
        }

//        if (mCategoryFragment.viewPagerImages.size()==0){
//            for (int i =1 ;i<6;i++){
//                mCategoryFragment.viewPagerImages.add("轮播图");
//            }
//        }


    }

    @Override
    public void changeViewData(int what) {
        mCategoryFragment.categoryItems.add("我是刷新出来的数据");
        mCategoryFragment.updataView();
    }
}
