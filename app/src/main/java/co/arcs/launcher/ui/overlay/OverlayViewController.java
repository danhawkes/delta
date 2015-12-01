package co.arcs.launcher.ui.overlay;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import co.arcs.launcher.model.TriggerArea;
import co.arcs.launcher.ui.launcher.ServiceBoundView;

public class OverlayViewController implements ServiceBoundView {

    private final WindowManager.LayoutParams layoutParams;
    private final OverlayView view;

    public OverlayViewController(Context context, TriggerArea area) {

        this.view = new OverlayView(context);

        boolean leftOrRight = area.getEdge() == TriggerArea.Edge.LEFT || area.getEdge() == TriggerArea.Edge.RIGHT;
        this.layoutParams = new WindowManager.LayoutParams(
                leftOrRight ? area.getThickness() : area.getWidth(),
                leftOrRight ? area.getWidth() : area.getThickness(),
                leftOrRight ? 0 : area.getMidlineOffset(),
                leftOrRight ? area.getMidlineOffset() : 0, WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                        WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH |
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT);
        switch (area.getEdge()) {
            case LEFT:
                layoutParams.gravity = Gravity.CENTER_VERTICAL | Gravity.LEFT;
                break;
            case TOP:
                layoutParams.gravity = Gravity.CENTER_HORIZONTAL | Gravity.TOP;
                break;
            case RIGHT:
                layoutParams.gravity = Gravity.CENTER_VERTICAL | Gravity.RIGHT;
                break;
            case BOTTOM:
                layoutParams.gravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
                break;
        }
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onAttachedToWindow() {

    }

    @Override
    public void onDetachedFromWindow() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public View getView() {
        return view;
    }

    @Override
    public WindowManager.LayoutParams getLayoutParams() {
        return layoutParams;
    }
}
