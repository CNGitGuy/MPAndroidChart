package com.apex.iot.thlinechart.simplify;

/**
 * Created by Jack on 2017/9/6.
 * 简化大量数据点显示，避免显示点数过多，使UI卡顿
 */
public class SimplifySets {
    final int DISPLAY_AMOUNT = 300;

    float[] points;

    float[] getDisplayPoints() {
        if (points.length <= DISPLAY_AMOUNT*2) {
            return points;//低于显示数量的两倍直接返回，无需缩放
        }
        int scale = points.length / DISPLAY_AMOUNT;
        float[] displayPoints = new float[DISPLAY_AMOUNT];

        return null;
    }
}
