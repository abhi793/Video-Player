package com.example.abhi.videoplayer.recyclercomponents;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by abhic on 17-02-2017.
 */

public class GridListDecorator extends RecyclerView.ItemDecoration {
    private int horizontalSpacing;

    public GridListDecorator() {
        super();
        horizontalSpacing = 30;
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
        if ((parent.getChildAdapterPosition(view) == parent.getAdapter().getItemCount() - 2)
                || (parent.getChildAdapterPosition(view) == parent.getAdapter().getItemCount() - 1)) {
            outRect.right = 0;
        } else {
            outRect.right = horizontalSpacing;
        }
    }
}
