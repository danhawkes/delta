package co.arcs.launcher.ui.launcher;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.raizlabs.universaladapter.converter.UniversalConverterFactory;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.arcs.launcher.LauncherApp;
import co.arcs.launcher.R;
import co.arcs.launcher.model.TriggerArea;
import co.arcs.launcher.model.redux.reducers.Store;
import co.arcs.launcher.ui.list_shit.AppTileAdapter;
import co.arcs.launcher.utils.IntentDispatcher;
import rx.subjects.PublishSubject;

public class LauncherController implements ServiceBoundView {

    private final Context context;
    private final PublishSubject<Void> destroyEvents = PublishSubject.create();
    @Inject Store store;
    private Callback callback;

    private View view;
    private WindowManager.LayoutParams layoutParams;

    @Bind((R.id.launcher)) LauncherView launcherView;
    @Bind(R.id.arc) ExpandingArcLayout arcLayout;
    @Bind(R.id.background) View background;

    public LauncherController(Context context) {
        this.context = context;
        ((LauncherApp) context.getApplicationContext()).getAppComponent().inject(this);
    }

    @Override
    public void onCreate() {

        // Create view, layout params
        view = LayoutInflater.from(context).inflate(R.layout.view_launcher, null, false);
        ButterKnife.bind(this, view);

        layoutParams = new WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        // ???
        launcherView.setCallback(() -> {
            if (callback != null) {
                callback.removeView();
            }
        });

        AppTileAdapter adapter = new AppTileAdapter(context);
        UniversalConverterFactory.create(adapter, arcLayout);

        store.selectedAppLists().takeUntil(destroyEvents).subscribe(list -> {
            adapter.clear();
            adapter.addAll(list);
        });

        adapter.drops().takeUntil(destroyEvents).subscribe(app -> {
            IntentDispatcher.startApp(context, app.getComponentName());
        });
    }

    @Override
    public void onAttachedToWindow() {

    }

    @Override
    public void onDetachedFromWindow() {

    }

    @Override
    public void onDestroy() {
        destroyEvents.onNext(null);
    }

    @Override
    public View getView() {
        return view;
    }

    @Override
    public WindowManager.LayoutParams getLayoutParams() {
        return layoutParams;
    }

    public void onTriggerActivated(TriggerArea triggerArea) {
        if (launcherView.isLayoutRequested()) {
            launcherView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    startActivationAnimations(triggerArea);
                    launcherView.removeOnLayoutChangeListener(this);
                }
            });
        } else {
            startActivationAnimations(triggerArea);
        }
    }

    private void startActivationAnimations(TriggerArea triggerArea) {
        arcLayout.onActivated(triggerArea);

        background.setAlpha(0.0f);
        background.animate().alpha(1.0f).start();
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public interface Callback {
        void removeView();
    }
}
