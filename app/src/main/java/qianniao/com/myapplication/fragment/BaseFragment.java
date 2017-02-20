package qianniao.com.myapplication.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import qianniao.com.myapplication.base.BaseView;
import qianniao.com.myapplication.presenter.Presenter;

/**
 * Created by Administrator on 2017/1/12.
 */

public abstract class BaseFragment extends Fragment implements BaseView {


    public Presenter presenter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return initView();
    }


    @Override
    public void showEmptyView() {

        TextView textView = new TextView(getActivity());
        textView.setText("加载数据为空");
    }

    @Override
    public void showErroView() {
        TextView textView = new TextView(getActivity());
        textView.setText("网络错误");
    }

    @Override
    public void showLoadingView() {
//        TextView textView = new TextView(getActivity());
//        textView.setText("正在加载中....");
    }

    protected abstract View initView();
}
