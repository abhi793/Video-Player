package com.example.abhi.videoplayer.recyclercomponents;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

/**
 * Created by abhic on 19-02-2017.
 */

public class VideoListLayoutManager extends LinearLayoutManager implements ValueAnimator.AnimatorUpdateListener, Animator.AnimatorListener {
    private int centeringReference;
    private int width;

    private ValueAnimator scrollAnimator;
    private int previousDistance;

    private RecyclerView recyclerView;
    private int centeredIndex;

    private OnListItemCenteredListener onListItemCenteredListener;
    private boolean centered;

    public VideoListLayoutManager(Context context) {
        super(context);
        init();
    }

    public VideoListLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
        init();
    }

    public VideoListLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        scrollAnimator = new ValueAnimator();
        scrollAnimator.setDuration(200);
        scrollAnimator.setInterpolator(new DecelerateInterpolator());
        scrollAnimator.addUpdateListener(this);
        scrollAnimator.addListener(this);

        centered = false;
    }

    @Override
    public void onAttachedToWindow(RecyclerView view) {
        super.onAttachedToWindow(view);
        recyclerView = view;
        width = view.getWidth();
        centeringReference = width / 2;
        centeredIndex = 0;
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        if (state == RecyclerView.SCROLL_STATE_IDLE) {
            stabilizeList();
        }
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        super.onLayoutChildren(recycler, state);
        scaleChildren();
    }

    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        scaleChildren();
        return super.scrollHorizontallyBy(dx, recycler, state);
    }

    private void scaleChildren() {
        for (int i = 0; i < getChildCount(); i++) {
            int distanceFromCenter = Math.abs((getChildAt(i).getLeft() + (getChildAt(i).getWidth() / 2)) - centeringReference);
            float scale = 1 - (distanceFromCenter / 3000f);
            getChildAt(i).setScaleX(scale);
            getChildAt(i).setScaleY(scale);
        }
    }

    public void stabilizeList() {
        int min = 100000, signedDistance = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            int distanceFromCenter = (child.getLeft() + (child.getWidth() / 2)) - centeringReference;
            if (Math.abs(distanceFromCenter) < min) {
                min = Math.abs(distanceFromCenter);
                signedDistance = distanceFromCenter;
                centeredIndex = getPosition(child);
            }
        }
        smoothScrollAnimator(signedDistance);
    }

    private void smoothScrollAnimator(int distance) {
        previousDistance = 0;
        scrollAnimator.setIntValues(0, distance);
        scrollAnimator.start();
    }

    public void setOnListItemCenteredListener(OnListItemCenteredListener onListItemCenteredListener) {
        this.onListItemCenteredListener = onListItemCenteredListener;
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        int distance = (int) animation.getAnimatedValue();
        recyclerView.scrollBy(distance - previousDistance, 0);
        previousDistance = distance;
    }

    @Override
    public void onAnimationStart(Animator animation) {

    }

    @Override
    public void onAnimationEnd(Animator animation) {
        if (onListItemCenteredListener != null) {
            onListItemCenteredListener.OnListItemCentered(centeredIndex);
        }
    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }
}
