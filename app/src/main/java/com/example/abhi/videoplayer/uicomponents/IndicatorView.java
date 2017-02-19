package com.example.abhi.videoplayer.uicomponents;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by abhic on 16-02-2017.
 */

public class IndicatorView extends View {
    private int indicatorPosition;
    private int totalItems;

    private Paint nonSelectedPaint;
    private Paint selectedPaint;

    private float[] points;
    private int length;
    private int gap;
    private int indicatorX;
    private int lineThickness;

    public IndicatorView(Context context) {
        super(context);
        init();
    }

    public IndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public IndicatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        indicatorPosition = 0;
        totalItems = 3;
        length = 50;
        gap = 25;
        lineThickness = 5;

        nonSelectedPaint = new Paint();
        nonSelectedPaint.setStyle(Paint.Style.STROKE);
        nonSelectedPaint.setStrokeWidth(lineThickness);
        nonSelectedPaint.setColor(Color.parseColor("#70A5D9"));

        selectedPaint = new Paint();
        selectedPaint.setStyle(Paint.Style.STROKE);
        selectedPaint.setStrokeWidth(lineThickness);
        selectedPaint.setColor(Color.parseColor("#D3DDD7"));

        generatePoints();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension((length * totalItems) + (gap * (totalItems - 1)), lineThickness);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawLines(points, nonSelectedPaint);
        canvas.drawLine(indicatorX, lineThickness / 2f, indicatorX + length, lineThickness / 2f, selectedPaint);
    }

    private void generatePoints() {
        points = new float[(totalItems * 4) - 4];
        int x = 0 , pointCount = 0, e = 0, pointIndex = 0;
        for (int i = 0; i < (totalItems * 2); i++) {
            if (i == indicatorPosition * 2) {
                indicatorX = x;
                e = 4;
            } else {
                if (i != (indicatorPosition * 2) + 1) {
                    points[pointIndex - e] = x;
                    points[pointIndex - e + 1] = lineThickness / 2f;
                }
            }
            pointIndex += 2;
            if (pointCount % 2 == 0) {
                x += length;
            } else {
                x += gap;
            }
            pointCount++;
        }
    }

    public int getIndicatorPosition() {
        return indicatorPosition;
    }

    public void setIndicatorPosition(int indicatorPosition) {
        this.indicatorPosition = indicatorPosition;
        generatePoints();
        invalidate();
    }

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
        generatePoints();
        invalidate();
    }

    public int getGap() {
        return gap;
    }

    public void setGap(int gap) {
        this.gap = gap;
        invalidate();
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
        invalidate();
    }
}
