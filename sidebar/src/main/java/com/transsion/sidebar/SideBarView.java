package com.transsion.sidebar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 卢伟斌.
 * @date 2017/12/20.
 * ==================================
 * Copyright (c) 2017 TRANSSION.Co.Ltd.
 * All rights reserved.
 */
public class SideBarView extends View {

    private List<String> sideTextList;

    private Paint paint;

    private int textHeight;

    private int width, height;

    private float touchX, touchY;

    private int currentIndex;

    private int textSize;

    private int mTouchSlop;

    private float downX, downY;

    private boolean scrolling = false;

    private int defaultColor = 0xff956232, selectColor = 0xff225432;

    private int popTextColor = 0xffffffff, popBackgroundColor = 0xff956232;

    private OnSideItemSelectListener onSideItemSelectListener;

    public SideBarView(Context context) {
        super(context);
        init();
    }

    public SideBarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        if (width < textSize * 10) {
            width = textSize * 10;
        }
        setMeasuredDimension(width, height);
        if (sideTextList != null && sideTextList.size() > 0) {
            textHeight = height / sideTextList.size();
        }
        textSize = dp2px(getContext(), 10);
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        ViewConfiguration viewConfiguration = ViewConfiguration.get(getContext());
        mTouchSlop = viewConfiguration.getScaledPagingTouchSlop();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        touchX = event.getX();
        touchY = event.getY();
        int currentIndex = ((int) touchY) / textHeight;
        if (this.currentIndex != currentIndex) {
            this.currentIndex = currentIndex;
            if (onSideItemSelectListener != null) {
                int index = this.currentIndex - 1;
                if (index >= 0 && currentIndex >= 0 && currentIndex < sideTextList.size() - 1) {
                    onSideItemSelectListener.onSelectItem(index, sideTextList.get(currentIndex));
                }
            }
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = touchX;
                downY = touchY;
                scrolling = false;
                if (downX < getWidth() - textSize * 3) {
                    return super.onTouchEvent(event);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(touchY - downY) > mTouchSlop) {
                    scrolling = true;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                touchX = touchY = 0;
                scrolling = false;
                break;
            default:
                break;
        }
        invalidate();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawText(canvas);
        drawPop(canvas);
    }

    private void drawText(Canvas canvas) {
        if (sideTextList != null && sideTextList.size() > 0) {
            paint.setTextAlign(Paint.Align.CENTER);
            for (int i = 0; i < sideTextList.size(); i++) {
                paint.setTextSize(textSize);
                int drawX = width - textSize;
                int drawY = textHeight * (i + 1) - (textHeight / 2);
                if (scrolling && touchY > 0 && Math.abs(touchY - drawY) < textSize * 5) {
                    int offX = (textSize * 5 - (int) Math.abs(touchY - drawY));
                    drawX = drawX - offX;
                    float textAlpha = (float) offX / (float) (textSize * 5);
                    paint.setTextSize(textSize * (1 + textAlpha));
                }
                if (!scrolling && currentIndex == i) {
                    paint.setColor(selectColor);
                } else {
                    paint.setColor(defaultColor);
                }
                canvas.drawText(sideTextList.get(i), drawX, drawY, paint);
            }
        }
    }

    private void drawPop(Canvas canvas) {
        if (touchX > 0 && touchY > 0 && currentIndex < sideTextList.size()) {
            String text = sideTextList.get(currentIndex);
            if (TextUtils.isEmpty(text)) {
                return;
            }
            int px = width - textSize * 6;
            int py = (int) touchY;
            paint.setColor(popBackgroundColor);
            canvas.drawCircle(px, py, textSize * 2, paint);
            paint.setColor(popTextColor);
            paint.setTextSize(textSize * 2);
            paint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(text, px, py + textSize * 2 / 3, paint);
        }
    }

    private int dp2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 设置item字体默认颜色
     *
     * @param defaultColor
     */
    public void setDefaultColor(int defaultColor) {
        this.defaultColor = defaultColor;
        refreshView();
    }

    /**
     * 设置被选中的时候的颜色
     *
     * @param selectColor
     */
    public void setSelectColor(int selectColor) {
        this.selectColor = selectColor;
        refreshView();
    }

    /**
     * 设置pop字体颜色
     *
     * @param popTextColor
     */
    public void setPopTextColor(int popTextColor) {
        this.popTextColor = popTextColor;
        refreshView();
    }

    /**
     * 设置pop背景颜色
     *
     * @param popBackgroundColor
     */
    public void setPopBackgroundColor(int popBackgroundColor) {
        this.popBackgroundColor = popBackgroundColor;
        refreshView();
    }

    /**
     * 设置当前位置
     *
     * @param currentIndex
     */
    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex + 1;
        refreshView();
    }

    /**
     * 设置side item字符
     *
     * @param sideList
     */
    public void setSideText(List sideList) {
        sideTextList = new ArrayList<>();
        sideTextList.add("");
        sideTextList.addAll(sideList);
        sideTextList.add("");
        refreshView();
    }

    public void setOnSideItemSelectListener(OnSideItemSelectListener onSideItemSelectListener) {
        this.onSideItemSelectListener = onSideItemSelectListener;
    }

    private void refreshView() {
        if (isAttachedToWindow()) {
            requestLayout();
            invalidate();
        }
    }

    public interface OnSideItemSelectListener {
        /**
         * 选择side item的时候回调
         *
         * @param position
         * @param title
         */
        public void onSelectItem(int position, String title);
    }
}

