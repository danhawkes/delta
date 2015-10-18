package co.arcs.launcher.ui.launcher;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import co.arcs.launcher.R;
import co.arcs.launcher.model.TriggerArea;

public class ExpandingArcLayout extends ArcLayout {

    private float edgeOffset;
    private float minRadius;
    private float maxRadius;

    public ExpandingArcLayout(Context context) {
        super(context);
        init(context);
    }

    public ExpandingArcLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ExpandingArcLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        Resources res = context.getResources();
        edgeOffset = res.getDimensionPixelOffset(R.dimen.arc_edge_offset);
        minRadius = res.getDimensionPixelOffset(R.dimen.arc_min_radius);
        maxRadius = res.getDimensionPixelOffset(R.dimen.arc_max_radius);
    }

    public void onActivated(TriggerArea triggerArea) {
        float angularOffset, x, y;
        switch (triggerArea.getEdge()) {
            case LEFT:
                angularOffset = 0;
                x = edgeOffset;
                y = getHeight() / 2;
                break;
            case TOP:
                angularOffset = (float) (Math.PI / 2.0);
                x = getWidth() / 2;
                y = edgeOffset;
                break;
            case RIGHT:
                angularOffset = (float) Math.PI;
                x = getWidth() - edgeOffset;
                y = getHeight() / 2;
                break;
            case BOTTOM:
            default:
                angularOffset = (float) -(Math.PI / 2.0);
                x = getWidth() / 2;
                y = getHeight() - edgeOffset;
                break;
        }
        setAngularOffset(angularOffset);
        setOrigin(x, y);
        setRadius(minRadius);

        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            child.setAlpha(0.0f);
            child.setTranslationX(-child.getLeft());
            child.setTranslationY(400);
            child.animate()
                    .alpha(1.0f)
                    .translationX(0)
                    .translationY(0)
                    .setInterpolator(new FastOutSlowInInterpolator())
                    .setDuration(1000)
                    .setStartDelay(i * 20)
                    .start();
        }

        float radiusInitial = minRadius;
        float radiusDelta = maxRadius - minRadius;
        float touchTravelRequired = radiusInitial + radiusDelta;

//        setOnTouchListener((v, event) -> {
//            int ty = (int) event.getY();
//            float factor = Math.min(1.0f, (y - ty) / touchTravelRequired);
//            float decay = (float) Math.pow(factor, 0.5);
//            float radius = radiusInitial + (decay * radiusDelta);
//            setRadius(radius);
//
//            return true;
//        });
    }

    float touchStartX;
    float touchStartY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
            touchStartX = event.getX();
            touchStartY = event.getY();
        }
        return super.onTouchEvent(event);
    }
}
