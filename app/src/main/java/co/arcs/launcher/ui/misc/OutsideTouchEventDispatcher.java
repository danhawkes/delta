package co.arcs.launcher.ui.misc;

import android.view.MotionEvent;
import android.view.View;

public class OutsideTouchEventDispatcher {

    public static boolean eventInViewBounds(MotionEvent e, View view) {
        return (e.getX() >= 0 && e.getY() >= 0 && e.getX() <= view.getWidth() &&
                e.getY() <= view.getHeight());
    }

    /**
     * Poor man's version of {@linkplain android.view.ViewGroup#dispatchTouchEvent(MotionEvent)
     * dispatchTouchEvent}. Notably does not check whether the event is within the view's bounds,
     * and dispatches it anyway.
     */
    public static boolean dispatchTouchEvent(MotionEvent e, View v) {
        if (v.getVisibility() == View.VISIBLE) {
            float xOffset = -v.getLeft();
            float yOffset = -v.getTop();
            e.offsetLocation(xOffset, yOffset);
            v.dispatchTouchEvent(e);
            e.offsetLocation(-xOffset, -yOffset);
        }
        return false;
    }
}
