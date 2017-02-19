package com.example.abhi.videoplayer.recyclercomponents;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by abhic on 19-02-2017.
 */

public class VideoListDecorator extends RecyclerView.ItemDecoration {
    private int endMargin;

    public VideoListDecorator() {
        super();
        endMargin = 200;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);

        if (position == 0) {
            outRect.left = endMargin;
        } else if (position == parent.getAdapter().getItemCount() - 1) {
            outRect.right = endMargin;
        }
    }
}
