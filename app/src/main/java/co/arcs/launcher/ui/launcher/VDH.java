package co.arcs.launcher.ui.launcher;

import android.content.Context;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import co.arcs.launcher.R;

public class VDH extends FrameLayout {

    ViewDragHelper vdh;

    public VDH(Context context) {
        super(context);
    }

    public VDH(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VDH(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        vdh = ViewDragHelper.create(this, new ViewDragHelper.Callback() {

            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return child.getId() == R.id.drag_handle;
            }
        });
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        vdh = null;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return vdh.shouldInterceptTouchEvent(ev) || super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        vdh.processTouchEvent(ev);
        return true;
    }
}
