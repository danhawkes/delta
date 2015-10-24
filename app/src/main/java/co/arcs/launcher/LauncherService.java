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
import co.arcs.launcher.ui.launcher.LauncherViewController;
import co.arcs.launcher.ui.overlay.OverlayViewController;
import rx.subjects.PublishSubject;

public class LauncherService extends Service {

    private WindowManager windowManager;
    @Inject Store store;

    private final List<OverlayViewController> overlayControllers = new ArrayList<>();
    private LauncherViewController launcherController;

    private final PublishSubject<Void> destroyEvents = PublishSubject.create();

    @Override
    public void onCreate() {
        super.onCreate();
        LauncherApp.from(this).getAppComponent().inject(this);

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        launcherController = new LauncherViewController(this);
        launcherController.setCallback(() -> {
            removeFromWindow(launcherController);
        });

        store.triggerAreas().takeUntil(destroyEvents).subscribe(triggerAreas -> {

            for (OverlayViewController controller : overlayControllers) {
                removeFromWindow(controller);
            }
            overlayControllers.clear();

            for (TriggerArea area : triggerAreas) {
                OverlayViewController controller = new OverlayViewController(this, area);
                overlayControllers.add(controller);
                addToWindow(controller, null);

                controller.getWindowTouchEvents().takeUntil(destroyEvents).subscribe(e -> {
                    if (e.getActionMasked() == MotionEvent.ACTION_DOWN) {
                        addToWindow(launcherController, area);
                    }
                    launcherController.getView().dispatchTouchEvent(e);
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
        throw new RuntimeException("This service cannot be bound.");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        removeFromWindow(launcherController);
        launcherController.onDestroy();

        for (OverlayViewController controller : overlayControllers) {
            removeFromWindow(controller);
            controller.onDestroy();
        }
    }

    private void addToWindow(ServiceBoundViewController controller, @Nullable Object info) {
        View view = controller.getView();
        if (!view.isAttachedToWindow()) {
            windowManager.addView(view, controller.getLayoutParams());
            controller.onAddedToWindow(info);
        }
    }

    private void removeFromWindow(ServiceBoundViewController controller) {
        View view = controller.getView();
        if (view.isAttachedToWindow()) {
            windowManager.removeView(view);
            controller.onRemovedFromWindow();
        }
    }
}
