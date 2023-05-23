package com.app.deliforcemanager.CustomViews;

import android.content.Context;
import android.widget.LinearLayout;

/**
 * Created by nextbrain on 18/6/18.
 */

public class SquareLayout extends LinearLayout {
    public SquareLayout(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);

        setMeasuredDimension(widthMeasureSpec, widthMeasureSpec);
    }
}
