package com.example.abhi.videoplayer.uicomponents;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

/**
 * Created by abhic on 19-02-2017.
 */

public class RatingView extends View implements ValueAnimator.AnimatorUpdateListener, Animator.AnimatorListener {
    private int width;
    private int height;

    private Paint arcPaint;
    private Paint textPaint;

    private RectF arcBounds;
    private Rect textBounds;

    private ValueAnimator arcAnimator;
    private SweepGradient sweepGradient;

    private int[] colors;
    private float[] stops;
    private float sweepAngle;

    private float strokeWidth;
    private String ratingText;

    public RatingView(Context context) {
        super(context);
        init();
    }

    public RatingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RatingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        sweepAngle = 0;
        strokeWidth = 4;
        ratingText = "";

        arcBounds = new RectF();
        textBounds = new Rect();

        arcPaint = new Paint();
        arcPaint.setAntiAlias(true);
        arcPaint.setStyle(Paint.Style.STROKE);
        arcPaint.setStrokeWidth(strokeWidth);

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(35);
        textPaint.setColor(Color.parseColor("#F0F0F0"));

        colors = new int[2];
        colors[0] = Color.parseColor("#D6F41F");
        colors[1] = Color.parseColor("#299e68");

        stops = new float[2];
        stops[0] = 0f;
        stops[1] = 0f;

        arcAnimator = new ValueAnimator();
        arcAnimator.setDuration(1000);
        arcAnimator.setInterpolator(new DecelerateInterpolator());
        arcAnimator.addUpdateListener(this);
        arcAnimator.addListener(this);
    }

    private void setGradient() {
        stops[1] = sweepAngle / 360f;
        sweepGradient = new SweepGradient(width / 2f, height / 2f, colors, stops);
        arcPaint.setShader(sweepGradient);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getMeasuredWidth();
        height = getMeasuredHeight();
        arcBounds.set(strokeWidth, strokeWidth, Math.min(width, height) - strokeWidth, Math.min(width, height) - strokeWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.rotate(-90, width / 2f, height / 2f);
        canvas.drawArc(arcBounds, 0, sweepAngle, false, arcPaint);
        canvas.rotate(90, width / 2f, height / 2f);

        textPaint.getTextBounds(ratingText, 0, ratingText.length(), textBounds);
        canvas.drawText(ratingText, ((width / 2f) - (textBounds.width() / 2f)), ((height / 2f) + (textBounds.height() / 2f)), textPaint);
    }

    public void setRatingUsingFraction(float ratingFraction) {
        ratingText = String.valueOf(getRatingFromFraction(ratingFraction));
        sweepAngle = getAngleFromFraction(ratingFraction);
        setGradient();
        invalidate();
    }

    public void animateArcUsingFraction(float toRatingFraction) {
        ratingText = String.valueOf(getRatingFromFraction(toRatingFraction));
        arcAnimator.setFloatValues(0, getAngleFromFraction(toRatingFraction));
        arcAnimator.start();
    }

    private float getRatingFromFraction(float fraction) {
        fraction = Math.round(fraction * 100);
        fraction /= 10f;
        return fraction;
    }

    private float getAngleFromFraction(float fraction) {
        if (fraction < 0) {
            return 0;
        } else if (fraction > 360) {
            return 360f;
        } else {
            return fraction * 360f;
        }
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        sweepAngle = (float) animation.getAnimatedValue();
        setGradient();
        invalidate();
    }

    @Override
    public void onAnimationStart(Animator animation) {

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
