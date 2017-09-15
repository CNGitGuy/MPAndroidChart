
package com.apex.iot.thlinechart;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendForm;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.components.YAxis.AxisDependency;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class LineChartActivity2 extends DemoBase {

    private LineChart mChart;
    private SeekBar mSeekBarX, mSeekBarY;
    private TextView tvX, tvY;
    OnSeekBarChangeListener seekBarChangeListener = new OnSeekBarChangeListenerImp();
    OnChartValueSelectedListener chartValueSelectedListener = new OnChartValueSelectedListenerImp();

    class OnSeekBarChangeListenerImp implements OnSeekBarChangeListener {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            tvX.setText("" + (mSeekBarX.getProgress() + 1));
            tvY.setText("" + (mSeekBarY.getProgress()));
            setData(mSeekBarX.getProgress() + 1, mSeekBarY.getProgress());

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

    class OnChartValueSelectedListenerImp implements OnChartValueSelectedListener {

        @Override
        public void onValueSelected(Entry e, Highlight h) {
            Log.i("Entry selected", e.toString());

            mChart.centerViewToAnimated(e.getX(), e.getY(), mChart.getData().getDataSetByIndex(h.getDataSetIndex())
                    .getAxisDependency(), 500);
            //mChart.zoomAndCenterAnimated(2.5f, 2.5f, e.getX(), e.getY(), mChart.getData().getDataSetByIndex(dataSetIndex)
            // .getAxisDependency(), 1000);
            //mChart.zoomAndCenterAnimated(1.8f, 1.8f, e.getX(), e.getY(), mChart.getData().getDataSetByIndex(dataSetIndex)
            // .getAxisDependency(), 1000);
        }

        @Override
        public void onNothingSelected() {
            Log.i("Nothing selected", "Nothing selected.");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_linechart1);

        tvX = (TextView) findViewById(R.id.tvXMax);
        tvY = (TextView) findViewById(R.id.tvYMax);
        mSeekBarX = (SeekBar) findViewById(R.id.seekBar1);
        mSeekBarY = (SeekBar) findViewById(R.id.seekBar2);

        mSeekBarY.setOnSeekBarChangeListener(seekBarChangeListener);
        mSeekBarX.setOnSeekBarChangeListener(seekBarChangeListener);

        mChart = (LineChart) findViewById(R.id.chart1);
        mChart.setOnChartValueSelectedListener(chartValueSelectedListener);
        // no description text
        mChart.getDescription().setEnabled(false);
        // enable touch gestures
        mChart.setTouchEnabled(true);
        mChart.setDragDecelerationFrictionCoef(0.9f);
        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setDrawGridBackground(false);
        mChart.setHighlightPerDragEnabled(true);
        // if disabled, scaling can be done on x- and y-axis separately
        mChart.setPinchZoom(true);

        // set an alternative background color
//        mChart.setBackgroundColor(Color.LTGRAY);

        // add data
        setData(20, 60);//20个点，60的波动范围
        tvX.setText("" + 20);
        tvY.setText("" + 60);
        mSeekBarX.setProgress(20-1);
        mSeekBarY.setProgress(60);

        mChart.animateX(2500);
        int chartColor = getResources().getColor(R.color.tempLineColor);
        // get the legend (only possible after setting data)
        Legend legend = mChart.getLegend();

        // modify the legend ...
        legend.setForm(LegendForm.LINE);
        legend.setTypeface(mTfLight);
        legend.setTextSize(11f);
        legend.setTextColor(Color.WHITE);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(false);
//        l.setYOffset(11f);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setTypeface(mTfLight);
        xAxis.setTextSize(11f);
        xAxis.setTextColor(chartColor);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.enableGridDashedLine(10f, 10f, 0f);
        xAxis.setDrawGridLines(true);
        xAxis.setDrawAxisLine(false);

        LimitLine upperLL = new LimitLine(60, "60℃");
        upperLL.setLineWidth(1f);
        upperLL.enableDashedLine(10f, 5f, 0f);
        upperLL.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        upperLL.setTextSize(10f);
//        upperLL.setTypeface(typeface);

        LimitLine lowerLL = new LimitLine(-20f, "-20℃");
        lowerLL.setLineWidth(1f);
        lowerLL.enableDashedLine(10f, 5f, 0f);
        lowerLL.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        lowerLL.setTextSize(10f);
//        lowerLL.setTypeface(typeface);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setTypeface(mTfLight);
        leftAxis.setTextColor(ColorTemplate.getHoloBlue());
        leftAxis.setLabelCount(11);
        leftAxis.setAxisMaximum(80f);
        leftAxis.setAxisMinimum(-30f);
        leftAxis.setDrawGridLines(true);
        leftAxis.setGranularityEnabled(true);
        leftAxis.addLimitLine(upperLL);
        leftAxis.addLimitLine(lowerLL);

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setTypeface(mTfLight);
        rightAxis.setLabelCount(11);
        rightAxis.setTextColor(Color.RED);
        rightAxis.setAxisMaximum(80f);
        rightAxis.setAxisMinimum(-30f);
        rightAxis.setDrawGridLines(false);
        rightAxis.setDrawZeroLine(false);
        rightAxis.setGranularityEnabled(false);
    }

    private void setData(int count, float range) {
        ArrayList<Entry> values = new ArrayList<Entry>();
        for (int i = 0; i < count; i++) {
            float mult = range / 2f;
            float val = (float) (Math.random() * mult);
            values.add(new Entry(i, val));
        }
        LineDataSet set;
        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set = (LineDataSet) mChart.getData().getDataSetByIndex(0);
            set.setValues(values);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            // create a dataSet and give it a type
            set = new LineDataSet(values, "DataSet 1");
            set.setDrawValues(false);
            set.setAxisDependency(AxisDependency.LEFT);
            set.setColor(getResources().getColor(R.color.tempLineColor));
            set.setCircleColor(getResources().getColor(R.color.tempLineColor));
            set.setLineWidth(2f);
            set.setCircleRadius(3f);
            set.setFillAlpha(65);
            set.setFillColor(ColorTemplate.getHoloBlue());
            set.setHighLightColor(Color.rgb(244, 117, 117));
            set.setDrawCircleHole(false);
            // create a data object with the datasets
            LineData data = new LineData(set);
            data.setValueTextColor(Color.WHITE);
            data.setValueTextSize(9f);

            //enable chart log
            mChart.setLogEnabled(true);
            // set data
            mChart.setData(data);
        }
    }

}
