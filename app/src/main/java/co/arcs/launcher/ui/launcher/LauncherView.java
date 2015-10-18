package co.arcs.launcher.ui.launcher;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import co.arcs.launcher.ui.misc.TouchDistributingRelativeLayout;

public class LauncherView extends TouchDistributingRelativeLayout {

    private Callback callback;

    public LauncherView(Context context) {
        super(context);
    }

    public LauncherView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LauncherView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();
        if (action == MotionEvent.ACTION_UP) {
            if (callback != null) {
                callback.onFinish();
            }
        }
        return super.onTouchEvent(event);
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public interface Callback {
        void onFinish();
    }
}
