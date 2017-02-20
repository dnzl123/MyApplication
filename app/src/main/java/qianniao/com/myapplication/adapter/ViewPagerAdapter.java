package qianniao.com.myapplication.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;


/**
 * Created by Administrator on 2017/1/13.
 */

public class ViewPagerAdapter extends PagerAdapter {

    private final List mList;
    private final Context mContext;
    private TextView textView;

    public ViewPagerAdapter(List list ,Context context) {
        this.mList = list;
        this.mContext = context;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        position = position % mList.size();
        textView = new TextView(mContext);
        textView.setText("hehe"+position);
        textView.setTextSize(30);
        container.addView(textView);
        return textView;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        //return mList.size();
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view ==object;
    }
}
