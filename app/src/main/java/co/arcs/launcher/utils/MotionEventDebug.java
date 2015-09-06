package co.arcs.launcher.utils;

import android.view.MotionEvent;

public class MotionEventDebug {

    public static String toString(MotionEvent e) {
        int action = e.getActionMasked();
        String actionStr;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                actionStr = "DOWN";
                break;
            case MotionEvent.ACTION_UP:
                actionStr = "UP";
                break;
            case MotionEvent.ACTION_MOVE:
                actionStr = "MOVE";
                break;
            case MotionEvent.ACTION_OUTSIDE:
                actionStr = "OUTSIDE";
                break;
            default:
                actionStr = Integer.toString(action);
                break;
        }
        return String.format("%s %.0f %.0f", actionStr, e.getX(), e.getY());
    }
}
