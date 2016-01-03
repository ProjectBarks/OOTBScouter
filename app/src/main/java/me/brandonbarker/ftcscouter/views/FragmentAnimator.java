package me.brandonbarker.ftcscouter.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class FragmentAnimator extends FrameLayout {

    public FragmentAnimator(Context context) {
        super(context);
    }

    public FragmentAnimator(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FragmentAnimator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public float getXFraction() {
        return getX() / getWidth();
    }

    public void setXFraction(float xFraction) {
        final int width = getWidth();
        setX((width > 0) ? (xFraction * width) : -9999);
    }
}
