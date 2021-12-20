/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2021 Tuya Inc.
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */

package com.tuya.smart.commercial.lighting.demo.widget.circleprogress;

import android.graphics.Color;

import androidx.annotation.ColorInt;

/**
 * Created by Jakob on 05.09.2015.
 */
public class ColorUtils {

    public static int getRGBGradient(@ColorInt int startColor, @ColorInt int endColor, float proportion) {

        int[] rgb = new int[3];
        rgb[0] = interpolate(Color.red(startColor), Color.red(endColor), proportion);
        rgb[1] = interpolate(Color.green(startColor), Color.green(endColor), proportion);
        rgb[2] = interpolate(Color.blue(startColor), Color.blue(endColor), proportion);
        return Color.argb(255, rgb[0], rgb[1], rgb[2]);
    }


    private static int interpolate(float a, float b, float proportion) {
        return Math.round((a * (proportion)) + (b * (1 - proportion)));
    }


// not finished
//    public static @ColorInt int getHSVGradient(@ColorInt int startColor,@ColorInt int endColor, float proportion, HSVColorDirection _direction) {
//        float[] startHSV = new float[3];
//        float[] endHSV = new float[3];
//        Color.colorToHSV(startColor, startHSV);
//        Color.colorToHSV(endColor, endHSV);
//
//        float brightness = (startHSV[2] + endHSV[2]) / 2;
//        float saturation = (startHSV[1] + endHSV[1]) / 2;
//
//        // determine clockwise and counter-clockwise distance between hues
//        float distCCW = (startHSV[0] >= endHSV[0]) ?  360 - startHSV[0] -   endHSV[0] : startHSV[0] -   endHSV[0];
//        float distCW =  (startHSV[0] >= endHSV[0]) ?  endHSV[0] - startHSV[0] :   360 - endHSV[0] - startHSV[0];
//        float hue = 0;
//        switch (_direction) {
//
//            case ClockWise:
//                hue = startHSV[0] + (distCW * proportion) % 360;
//                break;
//            case CounterClockWise:
//                hue = startHSV[0] + (distCCW * proportion) % 360;
//                break;
//            case Shortest:
//                break;
//            case Longest:
//                break;
//        }
//
//        // interpolate h
//        float hue = (float) ((distCW <= distCCW) ? startHSV[0] + (distCW * proportion) : startHSV[0] - (distCCW * proportion));
//        //reuse array
//        endHSV[0] = hue;
//        endHSV[1] = saturation;
//        endHSV[2] = brightness;
//        return Color.HSVToColor(endHSV);
//
//    }
//
//    enums HSVColorDirection{
//        ClockWise,
//        CounterClockWise,
//        Shortest,
//        Longest
//    }

}
