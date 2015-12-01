package co.arcs.launcher.ui.overlay;

import android.content.Context;
import android.graphics.Color;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import rx.subjects.PublishSubject;

public class OverlayView extends FrameLayout {

    private PublishSubject<MotionEvent> motionEventsSubject = PublishSubject.create();

    static int[] colors = {Color.GREEN};
    static int i = 0;

    public OverlayView(Context context) {
        super(context);
        if (i == colors.length) {
            i = 0;
        }
        setBackgroundColor(colors[i++] & 0x40ffffff);
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

    public PublishSubject<MotionEvent> motionEvents() {
        return motionEventsSubject;
    }
}
