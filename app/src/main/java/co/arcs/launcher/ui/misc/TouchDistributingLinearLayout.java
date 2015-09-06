package co.arcs.launcher.ui.misc;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

public class TouchDistributingLinearLayout extends LinearLayout {

    public TouchDistributingLinearLayout(Context context) {
        super(context);
    }

    public TouchDistributingLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
    }

    public TouchDistributingLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        for (int i = 0; i < getChildCount(); i++) {
            OutsideTouchEventDispatcher.dispatchTouchEvent(ev, getChildAt(i));
        }
        invalidate();
        x = (int) ev.getX();
        y = (int) ev.getY();
        return super.dispatchTouchEvent(ev);
    }

    int x, y;
    Paint p = new Paint();

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawLine(x, 0, x, getHeight(), p);
        canvas.drawLine(0, y, getWidth(), y, p);
    }
}
