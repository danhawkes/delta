package co.arcs.launcher.ui.launcher;

import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;

import co.arcs.launcher.ui.misc.TouchDistributingFrameLayout;

public class ArcLayout extends TouchDistributingFrameLayout {

    private float radius = 400;
    private float angularSize = (float) Math.PI;
    private float angularOffset = 0;
    private PointF origin = new PointF();

    public ArcLayout(Context context) {
        super(context);
    }

    public ArcLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ArcLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected final void onLayout(boolean changed, int left, int top, int right, int bottom) {

        int children = getChildCount();
        final double thetaDelta = angularSize / (children - 1);
        double theta = angularOffset;

        for (int i = 0; i < children; i++) {
            View child = getChildAt(i);
            int x = (int) (origin.x + (Math.sin(theta) * radius));
            int y = (int) (origin.y - (Math.cos(theta) * radius));
            int cw = child.getMeasuredWidth();
            int ch = child.getMeasuredHeight();
            child.layout(x - (cw / 2), y - (cw / 2), x + cw, y + ch);
            theta += thetaDelta;
        }
    }

    public final void setRadius(float radius) {
        this.radius = radius;
        requestLayout();
    }

    public final void setAngularSize(float angularSize) {
        this.angularSize = angularSize;
        requestLayout();
    }

    public final void setAngularOffset(float angularOffset) {
        this.angularOffset = angularOffset;
        requestLayout();
    }

    public final void setOrigin(float x, float y) {
        origin.set(x, y);
        requestLayout();
    }
}
