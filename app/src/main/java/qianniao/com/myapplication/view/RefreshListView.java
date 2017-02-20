package qianniao.com.myapplication.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import qianniao.com.myapplication.R;
import qianniao.com.myapplication.utils.DensityUtil;


/**
 * 下拉刷新ListView
 */
public class RefreshListView extends ListView implements OnScrollListener {

	private View mHeaderView;
	private int mHeaderViewHeight;

	private View mFooterView;
	private int mFooterViewHeight;

	private static final int STATE_PULL_TO_REFRESH = 0;//下拉刷新状态
	private static final int STATE_RELEASE_TO_REFRESH = 1;//松开刷新
	private static final int STATE_REFRESHING = 2;//正在刷新

	private int mCurrentState = STATE_PULL_TO_REFRESH;//当前状态,默认下拉刷新

	private TextView tvState;
	private TextView tvTime;
	private ImageView ivArrow;
	private ProgressBar pbLoading;


	private boolean isLoadMore = false;//标记是否正在加载更多
	private ScaleAnimation animBig;
	private FrameLayout.LayoutParams params;

	public RefreshListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initHeaderView();
		initFooterView();
	}

	public RefreshListView(Context context, AttributeSet attrs) {
		this(context, attrs, -1);
	}

	public RefreshListView(Context context) {
		this(context, null);
	}

	//初始化头布局
	private void initHeaderView() {
		mHeaderView = View.inflate(getContext(),
				R.layout.pull_to_refresh_header, null);

		addHeaderView(mHeaderView);//给ListView添加头布局

		tvState = (TextView) mHeaderView.findViewById(R.id.tv_state);
		ivArrow = (ImageView) mHeaderView.findViewById(R.id.iv_arrow);
		pbLoading = (ProgressBar) mHeaderView.findViewById(R.id.pb_loading);

		//隐藏头布局
		//获取当前头布局高度, 然后设置负paddingTop,布局就会向上走
		//int height = mHeaderView.getHeight();//不能这样拿, 控件没有绘制完成,获取不到宽高
		mHeaderView.measure(0, 0);// 手动测量, 宽高传0表示不参与具体宽高的设定,全由系统决定
		mHeaderViewHeight = mHeaderView.getMeasuredHeight();//获取测量后的高度
		//System.out.println("height:" + mHeaderViewHeight);
		mHeaderView.setPadding(0, -mHeaderViewHeight-50, 0, 0);

		initArrowAnim();
		//setRefreshTime();
	}

	//初始化脚布局
	private void initFooterView() {
		mFooterView = View.inflate(getContext(),
					R.layout.pull_to_refresh_footer, null);
		addFooterView(mFooterView);

		mFooterView.measure(0, 0);
		mFooterViewHeight = mFooterView.getMeasuredHeight();
		//隐藏脚布局
		mFooterView.setPadding(0, -mFooterViewHeight, 0, 0);

		//设置滑动监听
		setOnScrollListener(this);
	}

	private int startY = -1;

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			startY = (int) ev.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			if (startY == -1) {//没有获取到按下的事件(按住头条新闻滑动时,按下事件被ViewPager消费了)
				startY = (int) ev.getY();//重新获取起点位置
			}
			int endY = (int) ev.getY();
			int dy = endY - startY;
			//获得图片的参数.
			params = (FrameLayout.LayoutParams) ivArrow.getLayoutParams();

			//如果正在刷新, 什么都不做
			if (mCurrentState == STATE_REFRESHING||isLoadMore) {
				break;
			}

			int firstVisiblePosition = this.getFirstVisiblePosition();//当前显示的第一个item的位置
			if (dy > 0 && firstVisiblePosition == 0) {

				//下拉动作&当前在ListView的顶部
				int padding = -mHeaderViewHeight + dy;

				if (padding >= 0 && mCurrentState != STATE_RELEASE_TO_REFRESH) {

					//设置图片宽高
					params.height=mHeaderViewHeight- DensityUtil.px2dip(getContext(),20);
					//params.width =mHeaderViewHeight;

					//切换到松开刷新状态
					mCurrentState = STATE_RELEASE_TO_REFRESH;
					refreshState();
				} else if (padding < 0) {
					//设置图片宽高
					params.height=dy;
					//params.width =dy;

					if ( mCurrentState != STATE_PULL_TO_REFRESH){
						//切换到下拉刷新状态
						mCurrentState = STATE_PULL_TO_REFRESH;
						refreshState();
					}

				}
				//将参数设置给图片
				ivArrow.setLayoutParams(params);
				//通过修改padding来设置当前刷新控件的最新位置
				mHeaderView.setPadding(0, padding, 0, 0);

				return true;//消费此事件,处理下拉刷新控件的滑动,不需要listview原生效果参与
			}

			break;
		case MotionEvent.ACTION_UP:
			startY = -1;//起始坐标归0

			if (mCurrentState == STATE_RELEASE_TO_REFRESH) {
				//切换成正在刷新
				mCurrentState = STATE_REFRESHING;

				mHeaderView.setPadding(0, 0, 0, 0);

				refreshState();
			} else if (mCurrentState == STATE_PULL_TO_REFRESH) {
				//隐藏刷新控件
				mHeaderView.setPadding(0, -mHeaderViewHeight-50, 0, 0);
			}

			break;

		default:
			break;
		}

		return super.onTouchEvent(ev);//要返回super, 方便listView原生滑动处理
	}

	//初始化箭头动画
	private void initArrowAnim() {

	}

	//根据当前状态刷新界面
	private void refreshState() {
		switch (mCurrentState) {
		case STATE_PULL_TO_REFRESH:
			tvState.setText("下拉刷新");
			pbLoading.setVisibility(View.GONE);
			ivArrow.setVisibility(View.VISIBLE);

		//	ivArrow.startAnimation(animBig);
			break;
		case STATE_RELEASE_TO_REFRESH:
			tvState.setText("松开刷新");
			pbLoading.setVisibility(View.GONE);
			ivArrow.setVisibility(View.VISIBLE);

		//	ivArrow.startAnimation(animBig);
			break;
		case STATE_REFRESHING:
			tvState.setText("正在刷新...");
			pbLoading.setVisibility(View.VISIBLE);

			ivArrow.clearAnimation();//清理动画才能隐藏
			ivArrow.setVisibility(View.GONE);

			//回调下拉刷新
			if (mListener != null) {
				mListener.onRefresh();
			}
			break;

		default:
			break;
		}
	}

	//刷新结束,隐藏控件
	public void onRefreshComplete() {
		if (!isLoadMore) {
			//隐藏控件
			mHeaderView.setPadding(0, -mHeaderViewHeight-50, 0, 0);

			//初始化状态
			tvState.setText("下拉刷新");
			pbLoading.setVisibility(View.GONE);
			ivArrow.setVisibility(View.VISIBLE);
			mCurrentState = STATE_PULL_TO_REFRESH;

			//更新刷新时间
			//setRefreshTime();
		} else {
			//隐藏加载更多的控件
			mFooterView.setPadding(0, -mFooterViewHeight, 0, 0);
			isLoadMore = false;
		}
	}

	private OnRefreshListener mListener;

	//设置刷新回调监听
	public void setOnRefreshListener(OnRefreshListener listener) {
		mListener = listener;
	}

	//回调接口,通知刷新状态
	public interface OnRefreshListener {
		//下拉刷新的回调
		public void onRefresh();

		//加载更多的回调
		public void onLoadMore();
	}

//	//设置刷新时间
//	private void setRefreshTime() {
//		//01,02 10;
//		//HH: 24小时制; hh:12小时制
//		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		String time = format.format(new Date());
//
//	}

	//滑动状态发生变化
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (scrollState == SCROLL_STATE_IDLE) {//空闲状态
			int lastVisiblePosition = getLastVisiblePosition();//当前显示的最后一个item的位置
			if (lastVisiblePosition == getCount() - 1 && !isLoadMore&&!(mCurrentState == STATE_REFRESHING)) {
				System.out.println("到底了...");

				isLoadMore = true;

				//显示加载中布局
				mFooterView.setPadding(0, 0, 0, 0);
				setSelection(getCount() - 1);//显示在最后一个item的位置(展示加载中布局)

				//加载更多数据
				if (mListener != null) {
					mListener.onLoadMore();
				}
			}
		}

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {

	}
}
