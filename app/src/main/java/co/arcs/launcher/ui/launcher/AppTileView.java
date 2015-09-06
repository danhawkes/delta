package co.arcs.launcher.ui.launcher;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import co.arcs.launcher.ui.misc.OutsideTouchEventDispatcher;
import rx.Observable;
import rx.subjects.PublishSubject;

public class AppTileView extends FrameLayout {

    private final PublishSubject<Object> dropsSubject = PublishSubject.create();

    public AppTileView(Context context) {
        super(context);
    }

    public AppTileView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AppTileView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if ((event.getActionMasked() == MotionEvent.ACTION_UP) &&
                OutsideTouchEventDispatcher.eventInViewBounds(event, this)) {
            dropsSubject.onNext(this);
        }
        return true;
    }

    public Observable<Object> drops() {
        return dropsSubject;
    }
}
