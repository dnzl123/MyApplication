package qianniao.com.myapplication.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;



/**
 * Created by Administrator on 2017/1/13.
 */

public class ListViewAdapter extends BaseAdapter {

    private final List<String> mList;
    private final Context mContext;

    public ListViewAdapter(List list, Context context){
        mList = list;
        mContext = context;
    }
    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView = new TextView(mContext);
        textView.setText(mList.get(position));
        return textView;
    }
}
