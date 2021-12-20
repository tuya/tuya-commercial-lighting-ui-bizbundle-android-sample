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

package com.tuya.smart.commercial.lighting.demo.utils;

import android.text.TextUtils;

import com.tuya.smart.android.common.utils.HexUtil;
import com.tuya.smart.android.device.bean.BitmapSchemaBean;
import com.tuya.smart.android.device.bean.EnumSchemaBean;
import com.tuya.smart.android.device.bean.SchemaBean;
import com.tuya.smart.android.device.bean.StringSchemaBean;
import com.tuya.smart.android.device.bean.ValueSchemaBean;

public class SchemaUtil {

    public static boolean checkRawValue(String rawValue) {
        return HexUtil.checkHexString(rawValue) && rawValue.length() % 2 == 0;
    }

    public static boolean checkEnumValue(SchemaBean schemaBean, String enumValue) {
        EnumSchemaBean enumSchemaBean = SchemaMapper.toEnumSchema(schemaBean.getProperty());
        return false;
    }

    public static boolean checkStrValue(SchemaBean schemaBean, String strValue) {
        StringSchemaBean stringSchemaBean = SchemaMapper.toStringSchema(schemaBean.getProperty());
        return !TextUtils.isEmpty(strValue) && strValue.length() <= stringSchemaBean.getMaxlen();
    }

    public static boolean checkValue(SchemaBean schemaBean, String value) {
        int v;
        try {
            v = Integer.parseInt(value);
        } catch (Exception e) {
            return false;
        }
        ValueSchemaBean valueSchemaBean = SchemaMapper.toValueSchema(schemaBean.getProperty());
        int max = valueSchemaBean.getMax();
        int min = valueSchemaBean.getMin();
        return v <= max && v >= min;
    }

    public static boolean checkBitmapValue(SchemaBean schemaBean, String value) {
        BitmapSchemaBean schema = SchemaMapper.toBitmapSchema(schemaBean.getProperty());
        int bitmap;
        try {
            bitmap = Integer.parseInt(value);
        } catch (Exception e) {
            return false;
        }
        return bitmap >= 0 && bitmap < (1 << schema.getMaxlen());
    }
}
