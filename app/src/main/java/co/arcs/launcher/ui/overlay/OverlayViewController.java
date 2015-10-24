package co.arcs.launcher.ui.overlay;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import co.arcs.launcher.model.TriggerArea;
import co.arcs.launcher.ServiceBoundViewController;
import rx.Observable;
import rx.Subscription;
import rx.subjects.PublishSubject;

public class OverlayViewController implements ServiceBoundViewController {

    private final WindowManager.LayoutParams layoutParams;
    private final OverlayView view;
    private final int statusBarHeight;
    private PublishSubject<MotionEvent> windowTouchEvents = PublishSubject.create();
    private Subscription touchEventsSubscription;

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

        this.statusBarHeight = getStatusBarHeight(context.getResources());
    }

    @Override
    public void onAddedToWindow(@Nullable Object info) {
        touchEventsSubscription = view.touchEvents().subscribe(e -> {
            float xOffset = e.getRawX() - e.getX();
            float yOffset = e.getRawY() - e.getY() - statusBarHeight;
            e.offsetLocation(xOffset, yOffset);
            windowTouchEvents.onNext(e);
            e.offsetLocation(-xOffset, -yOffset);
        });
    }

    @Override
    public void onRemovedFromWindow() {
        touchEventsSubscription.unsubscribe();
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

    public Observable<MotionEvent> getWindowTouchEvents() {
        return windowTouchEvents;
    }

    private int getStatusBarHeight(Resources res) {
        int result = 0;
        int resourceId = res.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId != 0) {
            result = res.getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
