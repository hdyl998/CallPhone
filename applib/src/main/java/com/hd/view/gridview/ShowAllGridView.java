package com.hd.view.gridview;

import android.content.Context;
import android.util.AttributeSet;

/**
 * @method
 * @pram
 * @return
 */
public class ShowAllGridView extends DividerGridView {
    public ShowAllGridView(Context context) {
        super(context);
        init();
    }

    public ShowAllGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ShowAllGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    

    private void init() {
        setFocusable(false);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
