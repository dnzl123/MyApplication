package qianniao.com.myapplication.presenter;

import qianniao.com.myapplication.base.BaseView;
import qianniao.com.myapplication.fragment.TestFragment;

/**
 * Created by Administrator on 2017/1/18.
 */

public class TestPresenter implements Presenter {
    private final TestFragment mTestFragment;

    public TestPresenter(BaseView baseView) {
        this.mTestFragment = (TestFragment) baseView;
    }

    @Override
    public void initeViewData() {
        mTestFragment.showSuccessView();
    }

    @Override
    public void changeViewData(int what) {

    }
}
