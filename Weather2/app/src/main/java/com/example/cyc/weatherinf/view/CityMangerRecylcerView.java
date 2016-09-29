package com.example.cyc.weatherinf.view;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.widget.TextView;

import com.example.cyc.weatherinf.R;
import com.example.cyc.weatherinf.adapter.CityMangerAdapter;
import com.example.cyc.weatherinf.adapter.CityMangerViewHolder;

/**
 * Created by cyc on 2016/9/23.
 */
public class CityMangerRecylcerView extends RecyclerView {

    private int maxLength;
    private int mStartX = 0;
    private LinearLayout itemLayout;
    private int pos;
    private Rect mTouchFrame;
    private int xDown,xMove,yDown,yMove, mTouchSlop;
    private Scroller mScroller;
    private TextView textView;
    private ImageView imageView;
    private boolean isFirst = true;


    public CityMangerRecylcerView(Context context) {
        this(context, null);
    }

    public CityMangerRecylcerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CityMangerRecylcerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        //滑动最小距离
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        //滑动最大距离
        maxLength = ((int) (180 * context.getResources().getDisplayMetrics().density + 0.5f));
        mScroller = new Scroller(context, new LinearInterpolator(context, null));
    }

    private int dipToPx(Context context, int dip) {
        return (int) (dip * context.getResources().getDisplayMetrics().density + 0.5f);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        int x = (int) e.getX();
        int y = (int) e.getY();

        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDown = x;
                yDown = y;
                //计算当前position
                int mFirstPosition = ((LinearLayoutManager) getLayoutManager()).findFirstVisibleItemPosition();
                Rect frame = mTouchFrame;
                if (frame == null) {
                    mTouchFrame = new Rect();
                    frame = mTouchFrame;
                }

                int count = getChildCount();
                for (int i = count - 1; i >= 0; i--) {
                    final View child = getChildAt(i);
                    if (child.getVisibility() == View.VISIBLE) {
                        child.getHitRect(frame);
                        if (frame.contains(x, y)) {
                            pos = mFirstPosition + i;
                        }
                    }
                }
                View view = getChildAt(pos - mFirstPosition);
                CityMangerViewHolder viewHolder = (CityMangerViewHolder) getChildViewHolder(view);
                itemLayout = viewHolder.linearLayout;
                textView = (TextView) itemLayout.findViewById(R.id.item_manger_delete);
                imageView = (ImageView) itemLayout.findViewById(R.id.item_manger_delete_img);
                break;
            case MotionEvent.ACTION_MOVE:
                xMove = x;
                yMove = y;
                int dx = xMove - xDown;
                int dy = yMove - yDown;
                if (Math.abs(dy) < mTouchSlop * 2 && Math.abs(dx) > mTouchSlop) {
                    int scrollX = itemLayout.getScrollX();
                    int newScrollX = mStartX - x;
                    if (newScrollX < 0 && scrollX <= 0) {
                        newScrollX = 0;
                    } else if (newScrollX > 0 && scrollX >= maxLength) {
                        newScrollX = 0;
                    }
                    if (scrollX > maxLength / 2) {
                        textView.setVisibility(GONE);
                        imageView.setVisibility(VISIBLE);

                        if (isFirst) {
                            ObjectAnimator animatorX = ObjectAnimator.ofFloat(imageView, "scaleX", 1f, 1.2f, 1f);
                            ObjectAnimator animatorY = ObjectAnimator.ofFloat(imageView, "scaleY", 1f, 1.2f, 1f);
                            AnimatorSet animatorSet = new AnimatorSet();
                            animatorSet.play(animatorX).with(animatorY);
                            animatorSet.setDuration(800);
                            animatorSet.start();
                            isFirst = false;
                        }
                    } else {
                        textView.setVisibility(VISIBLE);
                        imageView.setVisibility(GONE);
                    }
                    itemLayout.scrollBy(newScrollX, 0);
                }
                break;
            case MotionEvent.ACTION_UP:
                int scrollX = itemLayout.getScrollX();
                if (scrollX > maxLength / 2) {
                    ((CityMangerAdapter) getAdapter()).removeItem(pos);
                } else {
                    mScroller.startScroll(scrollX, 0, -scrollX, 0);
                    invalidate();
                }
                isFirst = true;
                break;
        }

        mStartX = x;
        return super.onTouchEvent(e);
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            itemLayout.scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();
        }
    }
}