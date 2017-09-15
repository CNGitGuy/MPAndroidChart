package com.github.mikephil.charting.charts;

import android.graphics.DashPathEffect;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;

/*******************************
 * Created by liuqiang          *
 *******************************
 * data: 2017/9/12               *
 *******************************/

public class SampleLineData {
    private final static String TAG = "SampleLineData";

    public static ILineDataSet sample(ILineDataSet dataSet,int previewCount) {


        ArrayList<Entry> values = new ArrayList<>();
        int t=(int)Math.ceil(dataSet.getEntryCount()/previewCount);
        for (int i = 0; i < dataSet.getEntryCount(); i++) {
            if (i*t<dataSet.getEntryCount())
            values.add(dataSet.getEntryForIndex(i*t));

        }

        LineDataSet set = new LineDataSet(values, "DataSet 1");

        set.setDrawIcons(false);

        // set the line to be drawn like this "- - - - - -"
        //set.enableDashedLine(10f, 5f, 0f);
        //set.enableDashedHighlightLine(10f, 5f, 0f);
        set.setColor(dataSet.getColor());
        set.setLineWidth(1f);
        set.setCircleRadius(3f);
        set.setDrawCircleHole(false);
        set.setDrawCircles(false);
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setValueTextSize(9f);
        set.setDrawFilled(false);
        set.setFillAlpha(100);
        set.setFormLineWidth(1f);
        set.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
        set.setFormSize(15.f);
        set.setDrawValues(false);

        set.setFillColor(dataSet.getFillColor());


        return set;
    }
}
