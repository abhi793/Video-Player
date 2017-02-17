package com.example.abhi.videoplayer.uicomponents;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by abhic on 17-02-2017.
 */

public class LoaderView extends View implements ValueAnimator.AnimatorUpdateListener, Animator.AnimatorListener {
    private Paint loaderPaint;
    private boolean firstTimeDrawn;

    private float loaderRadius;

    private int[] colors;
    private float[] stops;

    private float centerX;
    private float centerY;

    private ValueAnimator rotationAnimator;
    private int loaderAngle;

    public LoaderView(Context context) {
        super(context);
        init();
    }

    public LoaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LoaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        firstTimeDrawn = false;
        loaderRadius = 20;
        loaderAngle = 0;

        loaderPaint = new Paint();
        loaderPaint.setStyle(Paint.Style.STROKE);
        loaderPaint.setStrokeWidth(5);
        loaderPaint.setAntiAlias(true);

        colors = new int[2];
        colors[0] = Color.parseColor("#00FFFFFF");
        colors[1] = Color.parseColor("#FFFFFFFF");

        stops = new float[2];
        stops[0] = 30 / 360f;
        stops[1] = 1f;

        rotationAnimator = new ValueAnimator();
        rotationAnimator.setDuration(500);
        rotationAnimator.setIntValues(0, 360);
        rotationAnimator.setRepeatMode(ValueAnimator.RESTART);
        rotationAnimator.setRepeatCount(ValueAnimator.INFINITE);
        rotationAnimator.setInterpolator(new LinearInterpolator());
        rotationAnimator.addUpdateListener(this);
        rotationAnimator.addListener(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!firstTimeDrawn) {
            setGradient();
            firstTimeDrawn = true;
        }
        canvas.rotate(loaderAngle, centerX, centerY);
        canvas.drawCircle(centerX, centerY, loaderRadius, loaderPaint);
        canvas.rotate(-loaderAngle, centerX, centerY);
    }

    private void setGradient() {
        centerX = getMeasuredWidth() / 2f;
        centerY = getMeasuredHeight() / 2f;

        SweepGradient sweepGradient = new SweepGradient(centerX, centerY, colors, stops);
        loaderPaint.setShader(sweepGradient);
    }

    public void startLoader() {
        if (!rotationAnimator.isRunning()) {
            rotationAnimator.start();
        }
    }

    public void endLoader() {
        if (rotationAnimator.isRunning()) {
            rotationAnimator.end();
        }
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        switch (visibility) {
            case VISIBLE:
                startLoader();
                break;
            case INVISIBLE:
            case GONE:
                endLoader();
                break;
        }
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        loaderAngle = (int) animation.getAnimatedValue();
        invalidate();
    }

    @Override
    public void onAnimationStart(Animator animation) {
        loaderAngle = 0;
    }

    @Override
    public void onAnimationEnd(Animator animation) {

    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }
}
