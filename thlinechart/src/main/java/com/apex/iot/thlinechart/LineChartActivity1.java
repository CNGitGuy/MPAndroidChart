package com.apex.iot.thlinechart;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PreviewLineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendForm;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.LimitLine.LimitLabelPosition;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class LineChartActivity1 extends DemoBase {

    private LineChart mChart;
    private PreviewLineChart mPreviewLineChart;
    OnChartGestureListener chartGestureListener = new OnChartGestureListenerImp();
    OnChartValueSelectedListener chartValueSelectedListener = new OnChartValueSelectedListenerImp();
    OnSeekBarChangeListener seekBarChangeListener = new OnSeekBarChangeListenerImp();

    class OnChartGestureListenerImp implements OnChartGestureListener {
        @Override
        public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
            Log.i("Gesture", "START, x: " + me.getX() + ", y: " + me.getY());
        }

        @Override
        public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
            Log.i("Gesture", "END, lastGesture: " + lastPerformedGesture);

            // un-highlight values after the gesture is finished and no single-tap
            if (lastPerformedGesture != ChartTouchListener.ChartGesture.SINGLE_TAP)
                mChart.highlightValues(null); // or highlightTouch(null) for callback to onNothingSelected(...)
        }

        @Override
        public void onChartLongPressed(MotionEvent me) {
            Log.i("LongPress", "Chart longpressed.");
        }

        @Override
        public void onChartDoubleTapped(MotionEvent me) {
            Log.i("DoubleTap", "Chart double-tapped.");
        }

        @Override
        public void onChartSingleTapped(MotionEvent me) {
            Log.i("SingleTap", "Chart single-tapped.");
        }

        @Override
        public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {
            Log.i("Fling", "Chart flinged. VeloX: " + velocityX + ", VeloY: " + velocityY);
        }

        @Override
        public void onChartScale(MotionEvent me, float scaleX, float scaleY) {
            Log.i("Scale / Zoom", "ScaleX: " + scaleX + ", ScaleY: " + scaleY);
        }

        @Override
        public void onChartTranslate(MotionEvent me, float dX, float dY) {
            Log.i("Translate / Move", "dX: " + dX + ", dY: " + dY);
        }
    }

    class OnChartValueSelectedListenerImp implements OnChartValueSelectedListener {

        @Override
        public void onValueSelected(Entry e, Highlight h) {
            Log.i("Entry selected", e.toString());
            Log.i("LOWHIGH", "low: " + mChart.getLowestVisibleX() + ", high: " + mChart.getHighestVisibleX());
            Log.i("MIN MAX", "xmin: " + mChart.getXChartMin() + ", xmax: " + mChart.getXChartMax() + ", ymin: " + mChart.getYChartMin() + ", ymax: " + mChart.getYChartMax());
        }

        @Override
        public void onNothingSelected() {
            Log.i("Nothing selected", "Nothing selected.");
        }
    }

    class OnSeekBarChangeListenerImp implements OnSeekBarChangeListener {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            // redraw
            mChart.invalidate();
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // TODO Auto-generated method stub

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_linechart1);
        initNormalChart();
        initPreviewChart();
    }

    void initNormalChart() {
        mChart = (LineChart) findViewById(R.id.chart1);
        mChart.setOnChartGestureListener(chartGestureListener);
        mChart.setOnChartValueSelectedListener(chartValueSelectedListener);
        mChart.setDrawGridBackground(false);

        // no description text
        mChart.getDescription().setEnabled(false);

        // enable touch gestures
        mChart.setTouchEnabled(true);

        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        // mChart.setScaleXEnabled(true);
        // mChart.setScaleYEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        mChart.setPinchZoom(true);

        // set an alternative background color
        // mChart.setBackgroundColor(Color.GRAY);

        // create a custom MarkerView (extend MarkerView) and specify the layout to use for it
        MyMarkerView mv = new MyMarkerView(this, R.layout.custom_marker_view);
        mv.setChartView(mChart); // For bounds control
        mChart.setMarker(mv); // Set the marker to the chart

        XAxis xAxis = mChart.getXAxis();
        xAxis.enableGridDashedLine(10f, 10f, 0f);
        xAxis.setPosition(XAxis.XAxisPosition.TOP);
        //xAxis.setValueFormatter(new MyCustomXAxisValueFormatter());
        //xAxis.addLimitLine(llXAxis); // add x-axis limit line


        Typeface typeface = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");

        LimitLine upperLL = new LimitLine(100f, "Upper Limit");
        upperLL.setLineWidth(1f);
        upperLL.enableDashedLine(10f, 5f, 0f);
        upperLL.setLabelPosition(LimitLabelPosition.RIGHT_TOP);
        upperLL.setTextSize(10f);
        upperLL.setTypeface(typeface);

        LimitLine lowerLL = new LimitLine(-20f, "Lower Limit");
        lowerLL.setLineWidth(1f);
        lowerLL.enableDashedLine(10f, 5f, 0f);
        lowerLL.setLabelPosition(LimitLabelPosition.RIGHT_BOTTOM);
        lowerLL.setTextSize(10f);
        lowerLL.setTypeface(typeface);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
        leftAxis.addLimitLine(upperLL);
        leftAxis.addLimitLine(lowerLL);
        leftAxis.setAxisMaximum(200f);
        leftAxis.setAxisMinimum(-50f);
        //leftAxis.setYOffset(20f);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setDrawZeroLine(false);

        // limit lines are drawn behind data (and not on top)
        leftAxis.setDrawLimitLinesBehindData(true);

        mChart.getAxisRight().setEnabled(false);

        //mChart.getViewPortHandler().setMaximumScaleY(2f);
        //mChart.getViewPortHandler().setMaximumScaleX(2f);

        // add data
        setData(4500, 100);
//        mChart.setVisibleXRange(20);
//        mChart.setVisibleYRange(20f, AxisDependency.LEFT);
//        mChart.centerViewTo(20, 50, AxisDependency.LEFT);

        mChart.animateX(2500);
        //mChart.invalidate();

        // get the legend (only possible after setting data)
        Legend l = mChart.getLegend();

        // modify the legend ...
        l.setForm(LegendForm.LINE);

        // // don't forget to refresh the drawing
        // mChart.invalidate();
    }

    void initPreviewChart() {
        mPreviewLineChart = (PreviewLineChart) findViewById(R.id.previewLineChart);
        mPreviewLineChart.bindLineChart(mChart, 200);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.line, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.actionToggleValues: {
                //意一个chart里面会有多个曲线，把所有曲线的属性都改变
                List<ILineDataSet> sets = mChart.getData().getDataSets();
                for (ILineDataSet iSet : sets) {
                    LineDataSet set = (LineDataSet) iSet;
                    set.setDrawValues(!set.isDrawValuesEnabled());
                }
                mChart.invalidate();
                break;
            }
            case R.id.actionToggleHighlight: {
                if (mChart.getData() != null) {
                    mChart.getData().setHighlightEnabled(!mChart.getData().isHighlightEnabled());
                    mChart.invalidate();
                }
                break;
            }
            case R.id.actionToggleFilled: {
                //意一个chart里面会有多个曲线，把所有曲线的属性都改变
                List<ILineDataSet> sets = mChart.getData().getDataSets();
                for (ILineDataSet iSet : sets) {
                    LineDataSet set = (LineDataSet) iSet;
                    if (set.isDrawFilledEnabled())
                        set.setDrawFilled(false);
                    else
                        set.setDrawFilled(true);
                }
                mChart.invalidate();
                break;
            }
            case R.id.actionToggleCircles: {
                //意一个chart里面会有多个曲线，把所有曲线的属性都改变
                List<ILineDataSet> sets = mChart.getData().getDataSets();
                for (ILineDataSet iSet : sets) {
                    LineDataSet set = (LineDataSet) iSet;
                    if (set.isDrawCirclesEnabled())
                        set.setDrawCircles(false);
                    else
                        set.setDrawCircles(true);
                }
                mChart.invalidate();
                break;
            }
            case R.id.actionToggleCubic: {
                //意一个chart里面会有多个曲线，把所有曲线的属性都改变
                List<ILineDataSet> sets = mChart.getData().getDataSets();
                for (ILineDataSet iSet : sets) {
                    LineDataSet set = (LineDataSet) iSet;
                    set.setMode(set.getMode() == LineDataSet.Mode.CUBIC_BEZIER
                            ? LineDataSet.Mode.LINEAR
                            : LineDataSet.Mode.CUBIC_BEZIER);
                }
                mChart.invalidate();
                break;
            }
            case R.id.actionToggleStepped: {
                //意一个chart里面会有多个曲线，把所有曲线的属性都改变
                List<ILineDataSet> sets = mChart.getData().getDataSets();
                for (ILineDataSet iSet : sets) {
                    LineDataSet set = (LineDataSet) iSet;
                    set.setMode(set.getMode() == LineDataSet.Mode.STEPPED
                            ? LineDataSet.Mode.LINEAR
                            : LineDataSet.Mode.STEPPED);
                }
                mChart.invalidate();
                break;
            }
            case R.id.actionToggleHorizontalCubic: {
                //意一个chart里面会有多个曲线，把所有曲线的属性都改变
                List<ILineDataSet> sets = mChart.getData().getDataSets();
                for (ILineDataSet iSet : sets) {
                    LineDataSet set = (LineDataSet) iSet;
                    set.setMode(set.getMode() == LineDataSet.Mode.HORIZONTAL_BEZIER
                            ? LineDataSet.Mode.LINEAR
                            : LineDataSet.Mode.HORIZONTAL_BEZIER);
                }
                mChart.invalidate();
                break;
            }
            case R.id.actionTogglePinch: {
                if (mChart.isPinchZoomEnabled())
                    mChart.setPinchZoom(false);
                else
                    mChart.setPinchZoom(true);

                mChart.invalidate();
                break;
            }
            case R.id.actionToggleAutoScaleMinMax: {
                mChart.setAutoScaleMinMaxEnabled(!mChart.isAutoScaleMinMaxEnabled());
                mChart.notifyDataSetChanged();
                break;
            }
            case R.id.animateX: {
                mChart.animateX(3000);
                break;
            }
            case R.id.animateY: {
                mChart.animateY(3000, Easing.EasingOption.EaseInCubic);
                break;
            }
            case R.id.animateXY: {
                mChart.animateXY(3000, 3000);
                break;
            }
            case R.id.actionSave: {
                if (mChart.saveToPath("title" + System.currentTimeMillis(), "")) {
                    Toast.makeText(getApplicationContext(), "Saving SUCCESSFUL!",
                            Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(getApplicationContext(), "Saving FAILED!", Toast.LENGTH_SHORT)
                            .show();

                // mChart.saveToGallery("title"+System.currentTimeMillis())
                break;
            }
        }
        return true;
    }


    private void setData(int count, float range) {
        ArrayList<Entry> values = new ArrayList<Entry>();
        for (int i = 0; i < count; i++) {
            float val = (float) (Math.random() * range) + 3;
            values.add(new Entry(i, val));
        }

        LineDataSet set;
        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set = (LineDataSet) mChart.getData().getDataSetByIndex(0);
            set.setValues(values);
            set.setDrawValues(false);
//            set.setDrawCircles(false);
            set.setDrawFilled(false);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set = new LineDataSet(values, "DataSet 1");

            set.setDrawValues(false);//不画数据值
//            set.setDrawCircles(false);//不画数据圆点
            // set the line to be drawn like this "- - - - - -"
            set.enableDashedLine(10f, 5f, 0f);
            set.enableDashedHighlightLine(10f, 5f, 0f);
            set.setColor(getResources().getColor(R.color.tempLineColor));
            set.setCircleColor(getResources().getColor(R.color.tempLineColor));
            set.setLineWidth(1f);
            set.setCircleRadius(3f);
            set.setDrawCircleHole(false);
            set.setValueTextSize(9f);
            set.setDrawFilled(true);
            set.setFormLineWidth(1f);
            set.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
            set.setFormSize(15.f);

            if (Utils.getSDKInt() >= 18) {
                // fill drawable only supported on api level 18 and above
                Drawable drawable = ContextCompat.getDrawable(this, R.drawable.fade_red);
                set.setFillDrawable(drawable);
            } else {
                set.setFillColor(Color.BLACK);
            }
            set.setDrawFilled(false);
            // create a data object with the datasets
            LineData data = new LineData(set);
            // set data
            mChart.setData(data);
            mChart.setVisibleXRangeMaximum(100);
            //enable chart log
            mChart.setLogEnabled(true);
        }
    }
}
