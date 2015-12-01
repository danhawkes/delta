package co.arcs.launcher.ui.overlay;

import android.content.Context;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import rx.Observable;
import rx.subjects.PublishSubject;

public class OverlayView extends FrameLayout {

    private PublishSubject<MotionEvent> motionEventsSubject = PublishSubject.create();

    public OverlayView(Context context) {
        super(context);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent e) {
        // Only forward events that started inside the window
        if (e.getActionMasked() == MotionEvent.ACTION_OUTSIDE) {
            return false;
        } else {
            motionEventsSubject.onNext(e);
            return true;
        }
    }

    public Observable<MotionEvent> touchEvents() {
        return motionEventsSubject.cast(MotionEvent.class);
    }
}
