package co.arcs.launcher;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import co.arcs.launcher.model.TriggerArea;
import co.arcs.launcher.model.redux.reducers.Store;
import co.arcs.launcher.ui.launcher.LauncherController;
import co.arcs.launcher.ui.overlay.OverlayView;
import rx.subjects.PublishSubject;

public class LauncherService extends Service {

    private WindowManager windowManager;
    private int statusBarHeight;

    private final List<OverlayView> overlayViews = new ArrayList<>();

    @Inject Store store;
    private LauncherController launcherController;
    private final PublishSubject<Void> destroyEvents = PublishSubject.create();

    @Override
    public void onCreate() {
        super.onCreate();
        LauncherApp.from(this).getAppComponent().inject(this);

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        statusBarHeight = getStatusBarHeight();

        launcherController = new LauncherController(this);

        launcherController.setCallback(this::removeLauncherWindow);

        store.triggerAreas().takeUntil(destroyEvents).subscribe(triggerAreas -> {
            removeOverlayWindows();
            overlayViews.clear();

            for (TriggerArea area : triggerAreas) {
                OverlayView view = createOverlayView(area);
                windowManager.addView(view, view.getLayoutParams());
                overlayViews.add(view);

                view.motionEvents().subscribe(e -> {
                    float x, y;
                    float xOffset = e.getRawX() - e.getX();
                    float yOffset = e.getRawY() - e.getY() - statusBarHeight;
                    e.offsetLocation(xOffset, yOffset);
                    x = e.getX();
                    y = e.getY();
                    launcherView.dispatchTouchEvent(e);
                    e.offsetLocation(-xOffset, -yOffset);

                    int action = e.getActionMasked();
                    if (action == MotionEvent.ACTION_DOWN) {
                        addLauncherWindow(area, x, y);
                    }
                });
            }
        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null; // Cannot bind
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        removeLauncherWindow();
        removeOverlayWindows();
    }

    private OverlayView createOverlayView(TriggerArea area) {
        OverlayView overlayView = new OverlayView(getApplicationContext());

        boolean leftOrRight = area.getEdge() == TriggerArea.Edge.LEFT || area.getEdge() == TriggerArea.Edge.RIGHT;
        LayoutParams lp = new LayoutParams(leftOrRight ? area.getThickness() : area.getWidth(),
                leftOrRight ? area.getWidth() : area.getThickness(),
                leftOrRight ? 0 : area.getMidlineOffset(),
                leftOrRight ? area.getMidlineOffset() : 0, LayoutParams.TYPE_PHONE,
                LayoutParams.FLAG_NOT_TOUCH_MODAL |
                        LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH |
                        LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT);
        switch (area.getEdge()) {
            case LEFT:
                lp.gravity = Gravity.CENTER_VERTICAL | Gravity.LEFT;
                break;
            case TOP:
                lp.gravity = Gravity.CENTER_HORIZONTAL | Gravity.TOP;
                break;
            case RIGHT:
                lp.gravity = Gravity.CENTER_VERTICAL | Gravity.RIGHT;
                break;
            case BOTTOM:
                lp.gravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
                break;
        }
        overlayView.setLayoutParams(lp);
        return overlayView;
    }

    private void removeOverlayWindows() {
        for (View overlayView : overlayViews) {
            windowManager.removeView(overlayView);
        }
    }

    /**
     * @param trigger The trigger area that that activated the launcher.
     * @param x       The motion event that activated the launcher.
     */
    private void addLauncherWindow(TriggerArea trigger, float x, float y) {
        if (!launcherController.getView().isAttachedToWindow()) {
            LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT,
                    LayoutParams.TYPE_SYSTEM_OVERLAY, LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);

            windowManager.addView(launcherController.getView(), lp);
            launcherController.onAttachedToWindow();
            launcherController.onTriggerActivated(trigger);
        }
    }

    private void removeLauncherWindow() {
        if (launcherController.getView().isAttachedToWindow()) {
            windowManager.removeView(launcherController.getView());
            launcherController.onDestroy();
        }
    }

    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId != 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
