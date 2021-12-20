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

import com.alibaba.fastjson.JSONObject;
import com.tuya.smart.android.device.bean.BitmapSchemaBean;
import com.tuya.smart.android.device.bean.BoolSchemaBean;
import com.tuya.smart.android.device.bean.EnumSchemaBean;
import com.tuya.smart.android.device.bean.StringSchemaBean;
import com.tuya.smart.android.device.bean.ValueSchemaBean;

public class SchemaMapper {

    public static BoolSchemaBean toBoolSchema(String data) {
        return JSONObject.parseObject(data, BoolSchemaBean.class);
    }


    public static EnumSchemaBean toEnumSchema(String data) {
        return JSONObject.parseObject(data, EnumSchemaBean.class);
    }


    public static StringSchemaBean toStringSchema(String data) {
        return JSONObject.parseObject(data, StringSchemaBean.class);
    }


    public static ValueSchemaBean toValueSchema(String data) {
        return JSONObject.parseObject(data, ValueSchemaBean.class);
    }

    public static BitmapSchemaBean toBitmapSchema(String data) {
        return JSONObject.parseObject(data, BitmapSchemaBean.class);
    }
}
