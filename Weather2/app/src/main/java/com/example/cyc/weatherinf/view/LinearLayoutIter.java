package com.example.cyc.weatherinf.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * Created by cyc on 2016/9/29.
 */
public class LinearLayoutIter extends LinearLayout {

    public LinearLayoutIter(Context context) {
        super(context);
    }

    public LinearLayoutIter(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LinearLayoutIter(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int lastX = 0;
        int lastY = 0;
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        boolean intercepted = false;
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = x;
                lastY = y;
                intercepted = false;
                break;
            case MotionEvent.ACTION_MOVE:
                if (false) {
                    intercepted = true;
                } else {
                    intercepted = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                intercepted = false;
                break;
            default:
                break;
        }
        return intercepted;
    }
}
