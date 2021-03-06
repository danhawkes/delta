package co.arcs.launcher.ui.misc;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

public class TouchDistributingRelativeLayout extends RelativeLayout {

    public TouchDistributingRelativeLayout(Context context) {
        super(context);
    }

    public TouchDistributingRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TouchDistributingRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        for (int i = 0; i < getChildCount(); i++) {
            OutsideTouchEventDispatcher.dispatchTouchEvent(ev, getChildAt(i));
        }
        return super.dispatchTouchEvent(ev);
    }
}
