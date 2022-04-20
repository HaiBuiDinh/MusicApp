package com.aemyfiles.musicapp.External.adapter;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CenterZoomLayoutManager extends LinearLayoutManager {
    private final float mShrinkAmount = 0.25f;
    private final float mShrinkDistance = 2.0f;

    public CenterZoomLayoutManager(Context context) {
        super(context);
    }

    public CenterZoomLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public CenterZoomLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void onLayoutCompleted(RecyclerView.State state) {
        super.onLayoutCompleted(state);
        scaleChild();
    }

    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        int orientation = getOrientation();
        if (orientation == HORIZONTAL) {
            scaleChild();
            return super.scrollHorizontallyBy(dx, recycler, state);
        } else {
            return 0;
        }
    }

    private void scaleChild() {
        float midPoint = getWidth() / 2.f;
        float d1 = mShrinkDistance * midPoint;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            float childMidPoint = (getDecoratedRight(child) + getDecoratedLeft(child)) / 2f;
            float d = Math.min(d1, Math.abs(midPoint - childMidPoint));
            float scale = 1.05f - mShrinkAmount * d / d1;
            child.setScaleY(scale);
            child.setScaleX(scale);
        }
    }
}
