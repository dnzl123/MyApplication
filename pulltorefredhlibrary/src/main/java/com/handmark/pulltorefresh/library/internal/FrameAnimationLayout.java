package com.handmark.pulltorefresh.library.internal;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.R;

/**
 * Created by Administrator on 2017/2/9.
 */

public class FrameAnimationLayout extends LoadingLayout {
    private AnimationDrawable mAnimationDrawable;

    public FrameAnimationLayout(Context context, PullToRefreshBase.Mode mode, PullToRefreshBase.Orientation scrollDirection, TypedArray attrs) {
        super(context, mode, scrollDirection, attrs);
        mHeaderImage.setImageResource(R.drawable.loading_anim);
        mAnimationDrawable = (AnimationDrawable) mHeaderImage.getDrawable();
    }

    @Override
    protected int getDefaultDrawableResId() {
        return R.drawable.arrow_down;
    }

    @Override
    protected void onLoadingDrawableSet(Drawable imageDrawable) {
    }

    @Override
    protected void onPullImpl(float scaleOfLayout) {
    }

    @Override
    protected void pullToRefreshImpl() {
    }

    @Override
    protected void refreshingImpl() {
        mAnimationDrawable.start();
    }

    @Override
    protected void releaseToRefreshImpl() {
    }

    @Override
    protected void resetImpl() {
    }
}
