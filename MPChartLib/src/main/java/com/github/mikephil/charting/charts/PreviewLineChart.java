package com.github.mikephil.charting.charts;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*******************************
 * Created by liuqiang          *
 *******************************
 * data: 2017/9/11               *
 *******************************/
public class PreviewLineChart extends LineChart implements OnChartGestureListener {

    private final static String TAG = "PreviewLineChart";
    private final static int VisibleXRangeMax = 200;

    /**
     * 选择区域的宽度
     */
    private int mWidth;

    private Paint mPreviewPaint1 = new Paint();
    private Paint mPreviewPaint2 = new Paint();

    /**
     * 选择区域的X坐标
     */
    private int mLeft;

    //onTouchEvent ACTION_DOWN事件中手指的起始位置
    private int mLastX;

    //选择区域的最大偏移量
    private int mMaxOffsetX;

    //PreviewLineChart控件的高度
    private int mChartHeight;
    private int mChartWidth;

    //onTouchEvent ACTION_DOWN事件中选择区域的X坐标
    private int mLastSelectPosition;

    //手指移动的偏移量
    private int mOffsetX;

    //PreviewLineChart中包含的X坐标的长度
    private int mPreviewCount;
    //原LineChart X坐标的长度
    private int mOriginPointAmount;

    private static final int DEFAULT_PREVIEW_TRANSPARENCY = 64;
    private static final int FULL_ALPHA = 255;
    //onTouchEvent ACTION_DOWN事件中手指的位置是否在选择区域中
    private boolean mInSelectedArea = true;
    private LineChart mLineChart;
    //是否移动选择区域
    private boolean mMoveSelectedArea = true;


    public PreviewLineChart(Context context) {
        super(context);
        initPreLineChart();
    }

    public PreviewLineChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPreLineChart();
    }

    public PreviewLineChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initPreLineChart();
    }

    private void initPreLineChart() {
        mPreviewPaint1.setAntiAlias(true);
        mPreviewPaint1.setStrokeJoin(Paint.Join.MITER);
        mPreviewPaint1.setColor(Color.GREEN);
        mPreviewPaint1.setStrokeWidth(3);
        mPreviewPaint1.setAlpha(DEFAULT_PREVIEW_TRANSPARENCY);
        mPreviewPaint1.setStyle(Paint.Style.FILL);

        mPreviewPaint2.setAntiAlias(true);
        mPreviewPaint2.setStrokeJoin(Paint.Join.MITER);
        mPreviewPaint2.setColor(Color.LTGRAY);
        mPreviewPaint2.setStrokeWidth(3);
        mPreviewPaint2.setStyle(Paint.Style.STROKE);
        mPreviewPaint2.setAlpha(FULL_ALPHA);

        setTouchEnabled(false);
        setScaleEnabled(false);
        setPinchZoom(false);
    }

    public void bindLineChart(LineChart lineChart, int previewCount) {
        List<ILineDataSet> dataSets = lineChart.getLineData().getDataSets();
        if (dataSets == null || dataSets.size() <= 0) {
            return;
        }
        this.mPreviewCount = previewCount;
        ArrayList<ILineDataSet> previewDataSets = new ArrayList<>();

        int[] originSetLengths = new int[dataSets.size()];
        for (int i = 0; i < dataSets.size(); i++) {
            originSetLengths[i] = dataSets.get(i).getEntryCount();
            previewDataSets.add(SampleLineData.sample(dataSets.get(i), previewCount));
        }

        Arrays.sort(originSetLengths);
        mOriginPointAmount = originSetLengths[originSetLengths.length - 1];

        LineData lineData = new LineData(previewDataSets);
        this.setData(lineData);

        this.mLineChart = lineChart;
        mLineChart.setVisibleXRangeMaximum(VisibleXRangeMax);
        mLineChart.setDragDecelerationFrictionCoef(0.5f);
        mLineChart.setOnChartGestureListener(this);
        setupPreviewLineChart();
    }


    private void setupPreviewLineChart() {
        if (mLineChart.getAxisLeft().getLimitLines().size() > 0) {
            for (LimitLine l : mLineChart.getAxisLeft().getLimitLines()) {
                mAxisLeft.addLimitLine(l);
            }
        }
        mAxisLeft.setAxisMaximum(mLineChart.getAxisLeft().getAxisMaximum());
        mAxisLeft.setAxisMinimum(mLineChart.getAxisLeft().getAxisMinimum());
        mAxisLeft.setDrawLimitLinesBehindData(true);
        mAxisRight.setEnabled(false);
        this.getAxisLeft().setDrawLabels(false);
        this.getAxisRight().setDrawLabels(false);
        this.getXAxis().setDrawLabels(true);
        this.getLegend().setEnabled(false);
        this.setDrawBorders(false);
        this.getDescription().setEnabled(false);

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mWidth = (mPreviewCount * getWidth()) / mOriginPointAmount;
        mMaxOffsetX = getWidth() - mWidth;
        mChartHeight = getHeight();
        mChartWidth = getWidth();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (mMoveSelectedArea) {
            mLeft = mLastSelectPosition + mOffsetX;
            Log.e("mOffsetX", "" + mOffsetX);
            if (mLeft < Utils.convertDpToPixel(mMinOffset)) {
                mLeft = (int) Utils.convertDpToPixel(mMinOffset);
            }
            if (mLeft > mMaxOffsetX) {
                mLeft = mMaxOffsetX;
                mLineChart.moveViewToX(mOriginPointAmount);
            } else {
                mLineChart.moveViewToX(mOriginPointAmount * (mLeft - Utils.convertDpToPixel(mMinOffset)) / mChartWidth);
            }
            //Log.e("mLeft", "" + mLeft);
        }
        //System.out.println("left= " + mLeft + " top= " + 0 + " right= " + mWidth + mLeft + " bottom= " + mChartHeight);
        canvas.drawRect(mLeft, 0, mWidth + mLeft, mChartHeight, mPreviewPaint1);
        canvas.drawRect(mLeft, 0, mWidth + mLeft, mChartHeight, mPreviewPaint2);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        int x = (int) event.getX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.e("ACTION_DOWN x", "" + x);
                if (x > (mLeft - 50) && x < (mLeft + mWidth + 50)) {

                    mInSelectedArea = true;
                    mMoveSelectedArea = true;
                    mLastX = x;
                    mLastSelectPosition = mLeft;
                } else {
                    mInSelectedArea = false;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                //Log.e("ACTION_MOVE X", "" + x);
                if (mInSelectedArea) {
                    mOffsetX = x - mLastX;
                    invalidate();
                }
                break;
        }
        return true;
    }


    @Override
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
    }


    @Override
    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
    }

    @Override
    public void onChartLongPressed(MotionEvent me) {
    }


    @Override
    public void onChartDoubleTapped(MotionEvent me) {
    }

    @Override
    public void onChartSingleTapped(MotionEvent me) {
    }

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {
    }

    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {

    }

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {
        mMoveSelectedArea = false;
        float lowestVisibleX = mLineChart.getLowestVisibleX();
        mLeft = (int) ((mMaxOffsetX * lowestVisibleX / mOriginPointAmount) + Utils.convertDpToPixel(mMinOffset));
        invalidate();
        Log.e("onChartTranslate", "" + lowestVisibleX);
    }

}
