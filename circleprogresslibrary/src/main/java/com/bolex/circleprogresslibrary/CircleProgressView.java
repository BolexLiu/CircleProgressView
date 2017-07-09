package com.bolex.circleprogresslibrary;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

/**
 * Created by Bolex on 2017/7/8.
 */

public class CircleProgressView extends View {
    int angle = 0;//角度
    int arcRadius = 0; //进度条半径
    int mProgressHeadRadius = 10; //小圆点半径
    int mMaxProgressValue = 100; //最大值
    private float mProgressValue = 0;//当前进度

    private RectF mArcRectF;
    private Paint mArcPaint;
    private Paint mCirclePaint;
    private Paint mScalePaint;

    private Paint mCircleeBoderPaint;
    private Paint mArcBgPaint;
    private int mProgressBackgroundColor;
    private int mProgressColor;
    private int mProgressHeadColor;
    private int mTextColor;
    private int mTextSize;
    private boolean mStartupText;
    private int mProgressWidth;


    private int mProgressBackgroundWidth;
    private int measuredCircleSize;

    public CircleProgressView(Context context) {
        this(context, null);
    }

    public CircleProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttribute(attrs);
    }

    private void initAttribute(AttributeSet attrs) {
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.CircleProgressView);
        mProgressBackgroundColor = ta.getColor(R.styleable.CircleProgressView_progressBackgroundColor, Color.GRAY);
        mProgressColor = ta.getColor(R.styleable.CircleProgressView_progressColor, Color.BLUE);
        mProgressHeadColor = ta.getColor(R.styleable.CircleProgressView_progressHeadColor, Color.BLUE);
        mTextColor = ta.getColor(R.styleable.CircleProgressView_textColor, Color.WHITE);
        mStartupText = ta.getBoolean(R.styleable.CircleProgressView_startupText, true);
        mProgressWidth = (int) ta.getDimension(R.styleable.CircleProgressView_progressWidth, 5);
        mProgressBackgroundWidth = (int) ta.getDimension(R.styleable.CircleProgressView_progressBackgroundWidth, 4);
        mProgressHeadRadius = (int) ta.getDimension(R.styleable.CircleProgressView_progressHeadRadius, 10);
        mTextSize = (int) ta.getDimension(R.styleable.CircleProgressView_textSize, 8);
        mMaxProgressValue = ta.getInt(R.styleable.CircleProgressView_maxProgressValue, 100);
        mProgressValue = ta.getInt(R.styleable.CircleProgressView_progressValue, 0);
    }

    /**
     * 控件测量
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // TODO: 2017/7/9 得到控件宽高
        int width = measureWidth(widthMeasureSpec);
        int height = measureHeight(heightMeasureSpec);
        measuredCircleSize = (width < height) ? width : height;
        setMeasuredDimension(width, height);
        init();
    }

    private int measureHeight(int heightMeasureSpec) {
        int result = 50;
        int heightmode = MeasureSpec.getMode(heightMeasureSpec);    //取出高度的测量模式
        int heightsize = MeasureSpec.getSize(heightMeasureSpec);    //取出高度的确切数值
        switch (heightmode) {
            case MeasureSpec.AT_MOST:  //wrap_content
                result = Math.min(result, heightsize);
                break;
            /*EXACTLY通常是确切的值或 match_parent  ,
             有一种情况是父容器给的是wrap_content,
             子控件是match_parent的情况下
             子容器将 变成AT_MOST模式。注意这里子控件是match_parent但它不会是EXACTLY模式。
             这里不论是wrap_content和match_parent他的Size都会为确切的值，
             即只要子控件是match_parent都将得到具体的值。只是模式会收父容器影响。
            */
            case MeasureSpec.EXACTLY:
                result = heightsize;
                break;
            case MeasureSpec.UNSPECIFIED:
                break;
            default:
                break;
        }
        return result;
    }

    private int measureWidth(int widthMeasureSpec) {
        int result = 50;
        int widthmode = MeasureSpec.getMode(widthMeasureSpec);
        int widthsize = MeasureSpec.getSize(widthMeasureSpec);
        switch (widthmode) {
            case MeasureSpec.AT_MOST:
                result = Math.min(result, widthsize);
                break;
            case MeasureSpec.EXACTLY:
                result = widthsize;
                break;
            case MeasureSpec.UNSPECIFIED:
                break;
            default:
                break;
        }
        return result;
    }


    private void init() {
        this.arcRadius = (measuredCircleSize - (mProgressHeadRadius + mProgressWidth) * 3) / 2;
        arcPaintInit();
        arcBgPaintInit();
        circlePaintInit();
        circleBoderPaintInit();
        scalePaintInit();
    }

    private void arcBgPaintInit() {
        mArcBgPaint = new Paint();
        mArcBgPaint.setColor(mProgressBackgroundColor);
        mArcBgPaint.setStrokeWidth(mProgressBackgroundWidth);
        mArcBgPaint.setStyle(Paint.Style.STROKE);
        mArcBgPaint.setStrokeCap(Paint.Cap.ROUND); //圆头画笔
        mArcBgPaint.setAntiAlias(true);

    }

    private void circleBoderPaintInit() {
        mCircleeBoderPaint = new Paint();
        mCircleeBoderPaint.setColor(mProgressHeadColor - 0x77000000);
        mCircleeBoderPaint.setStyle(Paint.Style.STROKE);
        mCircleeBoderPaint.setStrokeWidth(2);
        mCircleeBoderPaint.setAntiAlias(true);

    }

    private void arcPaintInit() {
        mArcRectF = new RectF(0, 0, arcRadius * 2, arcRadius * 2);
        mArcPaint = new Paint();
        mArcPaint.setColor(mProgressColor);
        mArcPaint.setStrokeWidth(mProgressWidth);
        mArcPaint.setStyle(Paint.Style.STROKE);
        mArcPaint.setStrokeCap(Paint.Cap.ROUND); //圆头画笔
        mArcPaint.setAntiAlias(true);
    }

    private void circlePaintInit() {
        mCirclePaint = new Paint();
        mCirclePaint.setColor(mProgressHeadColor);
        mCirclePaint.setStyle(Paint.Style.FILL);
        mCirclePaint.setStrokeWidth(mProgressWidth);
        mCirclePaint.setAntiAlias(true);

    }

    private void scalePaintInit() {
        mScalePaint = new Paint();
        mScalePaint.setColor(mTextColor);
        mScalePaint.setStrokeWidth(1);
        mScalePaint.setTextSize(mTextSize);
        mScalePaint.setStyle(Paint.Style.STROKE);
        mScalePaint.setStrokeCap(Paint.Cap.ROUND); //圆头画笔
        mScalePaint.setAntiAlias(true);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        // TODO: 2017/7/8  padding
        canvas.translate(mProgressHeadRadius * 2, mProgressHeadRadius * 2);
        // TODO: 2017/7/8  画进度条
        draProgressArc(canvas);
        // TODO: 2017/7/8  移坐标到圆心
        canvas.translate(arcRadius, arcRadius);
        // TODO: 2017/7/8  计算圆心半径到圆周上的某个点坐标
        Coordinate coordinate = center2Point(angle - 90, arcRadius);
        float cx = coordinate.x; //绘制原点起始坐标x
        float cy = coordinate.y;
        // TODO: 2017/7/8 绘制 小圆点
        draHead(canvas, cx, cy);
        // TODO: 2017/7/8 绘制 小圆点上的文字
        draHeadText(canvas, cx, cy);
    }

    private void draProgressArc(Canvas canvas) {
        canvas.drawArc(mArcRectF, -90, 360, false, mArcBgPaint);
        canvas.drawArc(mArcRectF, -90, angle, false, mArcPaint);
    }

    private void draHead(Canvas canvas, float cx, float cy) {
        canvas.drawCircle(cx, cy, mProgressHeadRadius, mCirclePaint);
        canvas.drawCircle(cx, cy, mProgressHeadRadius + 1.5f, mCircleeBoderPaint);
    }

    private void draHeadText(Canvas canvas, float cx, float cy) {
        if (mStartupText) {
            //TODO: 刻度
            float x = cx - mScalePaint.measureText(String.valueOf(mProgressValue)) / 2; //减去文字的长度
            float y = cy + mTextSize / 2;
            canvas.drawText(String.valueOf(mProgressValue), x, y, mScalePaint);
        }
    }

    /**
     * 计算圆心半径到圆周上的某个点坐标
     *
     * @param angle  度数
     * @param radius 半径
     * @return
     */
    private Coordinate center2Point(int angle, int radius) {
        Coordinate coordinate = new Coordinate();
        coordinate.x = (float) (radius * Math.cos(angle * Math.PI / 180));
        coordinate.y = (float) (radius * Math.sin(angle * Math.PI / 180));
        return coordinate;
    }

    public void setProgress(float value) {
        if (value <= mMaxProgressValue) {
            toProgressValue(value, mProgressValue);
        }
    }

    public void setmProgressBackgroundWidth(int mProgressBackgroundWidth) {
        this.mProgressBackgroundWidth = mProgressBackgroundWidth;
        postInit();
    }

    public void setmProgressWidth(int mProgressWidth) {
        this.mProgressWidth = mProgressWidth;
        postInit();
    }

    public void setmStartupText(boolean mStartupText) {
        this.mStartupText = mStartupText;
        postInit();
    }

    public void setmTextSize(int mTextSize) {
        this.mTextSize = mTextSize;
        postInit();
    }

    public void setmTextColor(int mTextColor) {
        this.mTextColor = mTextColor;
        invalidate();
    }

    public void setmProgressHeadColor(int mProgressHeadColor) {
        this.mProgressHeadColor = mProgressHeadColor;
        postInit();
    }

    public void setmProgressColor(int mProgressColor) {
        this.mProgressColor = mProgressColor;
        postInit();
    }

    public void setmProgressBackgroundColor(int mProgressBackgroundColor) {
        this.mProgressBackgroundColor = mProgressBackgroundColor;
        postInit();
    }

    public void setmMaxProgressValue(int mMaxProgressValue) {
        this.mMaxProgressValue = mMaxProgressValue;
        postInit();
    }

    public void setmProgressHeadRadius(int mProgressHeadRadius) {
        this.mProgressHeadRadius = mProgressHeadRadius;
        postInit();
    }

    public void postInit() {
        init();
        invalidate();
    }

    public float getmProgressValue() {
        return mProgressValue;
    }

    private void toProgressValue(final float value, final float beforeValue) {

        ValueAnimator mValueAnimator = ValueAnimator.ofFloat(beforeValue, value);
        mValueAnimator.setDuration(500);
        mValueAnimator.setInterpolator(new DecelerateInterpolator());
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float tempValue = (float) animation.getAnimatedValue();
                angle = (int) (360 * (tempValue / mMaxProgressValue));
                CircleProgressView.this.mProgressValue = value;
                postInvalidate();
            }
        });
        mValueAnimator.start();
    }

    class Coordinate {
        float y = 0;
        float x = 0;
    }


}
