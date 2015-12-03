package co.arcs.launcher.ui.launcher;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import co.arcs.launcher.R;
import co.arcs.launcher.model.TriggerArea;

public class ExpandingArcLayout extends ArcLayout {

    private float edgeOffset;
    private float arcRadius;
    private long animationDuration;

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
        arcRadius = res.getDimensionPixelOffset(R.dimen.arc_radius);
        animationDuration = res.getInteger(R.integer.reveal_animation_duration);
    }

    public void startRevealAnimation(TriggerArea triggerArea) {
        // Set position of arc.
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
        setRadius(arcRadius);

        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            child.setAlpha(0.4f);
            child.animate().withLayer()
                    .alpha(1.0f)
                    .setInterpolator(new DecelerateInterpolator())
                    .setDuration(animationDuration)
                    .start();
        }
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
