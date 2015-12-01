package co.arcs.launcher;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import co.arcs.launcher.model.TriggerArea;
import co.arcs.launcher.model.redux.reducers.Store;
import co.arcs.launcher.ui.launcher.LauncherController;
import co.arcs.launcher.ui.launcher.ServiceBoundView;
import co.arcs.launcher.ui.overlay.OverlayView;
import co.arcs.launcher.ui.overlay.OverlayViewController;
import rx.subjects.PublishSubject;

public class LauncherService extends Service {

    private WindowManager windowManager;
    private int statusBarHeight;

    @Inject Store store;

    private final List<OverlayViewController> overlayControllers = new ArrayList<>();
    private LauncherController launcherController;

    private final PublishSubject<Void> destroyEvents = PublishSubject.create();

    @Override
    public void onCreate() {
        super.onCreate();
        LauncherApp.from(this).getAppComponent().inject(this);

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        statusBarHeight = getStatusBarHeight();

        launcherController = new LauncherController(this);

        launcherController.setCallback(() -> {
            removeFromWindow(launcherController);
        });

        store.triggerAreas().takeUntil(destroyEvents).subscribe(triggerAreas -> {

            for (OverlayViewController controller : overlayControllers) {
                removeFromWindow(controller);
            }
            overlayControllers.clear();

            for (TriggerArea area : triggerAreas) {
                overlayControllers.add(new OverlayViewController(this, area));
            }

            for (OverlayViewController controller : overlayControllers) {
                addToWindow(controller);

                OverlayView view = (OverlayView) controller.getView();

                view.motionEvents().subscribe(e -> {
                    float x, y;
                    float xOffset = e.getRawX() - e.getX();
                    float yOffset = e.getRawY() - e.getY() - statusBarHeight;
                    e.offsetLocation(xOffset, yOffset);
                    x = e.getX();
                    y = e.getY();
                    launcherController.getView().dispatchTouchEvent(e);
                    e.offsetLocation(-xOffset, -yOffset);

                    int action = e.getActionMasked();
                    if (action == MotionEvent.ACTION_DOWN) {
                        addToWindow(launcherController);
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

        removeFromWindow(launcherController);
        for (OverlayViewController controller : overlayControllers) {
            removeFromWindow(controller);
        }
    }

    private void addToWindow(ServiceBoundView controller) {
        View view = controller.getView();
        if (!view.isAttachedToWindow()) {
            windowManager.addView(view, controller.getLayoutParams());
        }
    }

    private void removeFromWindow(ServiceBoundView controller) {
        View view = controller.getView();
        if (view.isAttachedToWindow()) {
            windowManager.removeView(view);
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
