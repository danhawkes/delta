package co.arcs.launcher.ui.launcher;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

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

public class LauncherController {

    private final Context context;
    private final PublishSubject<Void> destroyViewEvents = PublishSubject.create();
    @Inject Store store;

    @Bind(R.id.items_layout) ExpandingArcLayout arcLayout;

    private final int edgeOffset = 100;

    public LauncherController(Context context) {
        this.context = context;
        ((LauncherApp) context.getApplicationContext()).getAppComponent().inject(this);
    }

    public View onCreateView() {
        return LayoutInflater.from(context).inflate(R.layout.view_app_grid, null, false);
    }

    public void onViewCreated(View view) {
        ButterKnife.bind(this, view);

        AppTileAdapter adapter = new AppTileAdapter(context);
        UniversalConverterFactory.create(adapter, arcLayout);

        store.selectedAppLists().takeUntil(destroyViewEvents).subscribe(list -> {
            adapter.clear();
            adapter.addAll(list);
        });

        adapter.drops().takeUntil(destroyViewEvents).subscribe(app -> {
            IntentDispatcher.startApp(context, app.getComponentName());
        });

        //        View target1 = view.findViewById(R.id.target1);

        //        Observable<MotionEvent> share = RxView.touches(target1)
        //                .takeUntil(destroyViewEvents)
        //                .share();

        //        share.subscribe(ev -> Log.d("test", "t1 " + MotionEventDebug.toString(ev)));

        //        share.filter(event -> event.getActionMasked() == MotionEvent.ACTION_UP)
        //                .filter(event -> OutsideTouchEventDispatcher.eventInViewBounds(event, target1))
        //                .subscribe(event -> {
        //                    IntentDispatcher.startFirefox(view.getContext().getApplicationContext());
        //                    Log.d("test", "wooo!");
        //                });
    }

    public void onDestroyView() {
        destroyViewEvents.onNext(null);
    }

    public void onTriggerActivated(TriggerArea triggerArea, float x, float y) {

        arcLayout.onActivated(triggerArea);
//        float angularOffset;
//        float edgeXOffset;
//        float edgeYOffset;
//        switch (triggerArea.edge) {
//            case LEFT:
//                angularOffset = 0;
//                edgeXOffset = edgeOffset;
//                edgeYOffset = arcLayout.getHeight() / 2;
//                break;
//            case TOP:
//                angularOffset = (float) (Math.PI / 2.0);
//                edgeXOffset = arcLayout.getWidth() / 2;
//                edgeYOffset = edgeOffset;
//                break;
//            case RIGHT:
//                angularOffset = (float) Math.PI;
//                edgeXOffset = arcLayout.getWidth() - edgeOffset;
//                edgeYOffset = arcLayout.getHeight() / 2;
//                break;
//            case BOTTOM:
//            default:
//                angularOffset = (float) -(Math.PI / 2.0);
//                edgeXOffset = arcLayout.getWidth() / 2;
//                edgeYOffset = arcLayout.getHeight() - edgeOffset;
//                break;
//        }
//        arcLayout.setAngularOffset(angularOffset);
//        arcLayout.setOrigin(edgeXOffset, edgeYOffset);
//
//        float radiusInitial = 350;
//        float radiusDelta = 50;
//        float touchTravelRequired = radiusInitial + radiusDelta;
//        arcLayout.setRadius(radiusInitial);

//        arcLayout.setOnTouchListener((v, event) -> {
//            int ty = (int) event.getY();
//            float factor = Math.min(1.0f, (y - ty) / touchTravelRequired);
//            float decay = (float) Math.pow(factor, 0.5);
//            float radius = radiusInitial + (decay * radiusDelta);
//            arcLayout.setRadius(radius);
//
//            return true;
//        });

//        ObjectAnimator.ofFloat(arcLayout, "radius", 200.0f, 400.0f).start();
    }
}
