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

import android.os.Message;

import com.tuya.smart.android.mvp.bean.Result;
import com.tuya.smart.android.network.http.BusinessResponse;

public class MessageUtil {
    public static Message getCallFailMessage(int msgWhat, BusinessResponse businessResponse) {
        return getCallFailMessage(msgWhat, businessResponse.getErrorCode(), businessResponse.getErrorMsg());
    }

    public static Message getCallFailMessage(int msgWhat, String errorCode, String errorMsg) {
        Message msg = new Message();
        msg.what = msgWhat;
        Result result = new Result();
        result.error = errorMsg;
        result.errorCode = errorCode;
        msg.obj = result;
        return msg;
    }

    public static Message getCallFailMessage(int msgWhat, String errorCode, String errorMsg, Object resultObj) {
        Message msg = new Message();
        msg.what = msgWhat;
        Result result = new Result();
        result.error = errorMsg;
        result.errorCode = errorCode;
        result.setObj(resultObj);
        msg.obj = result;

        return msg;
    }


    public static Message getCallFailMessage(int msgWhat, BusinessResponse businessResponse, Object resultObj) {
        return getCallFailMessage(msgWhat, businessResponse.getErrorCode(), businessResponse.getErrorMsg(), resultObj);
    }

    public static Message getMessage(int msgWhat, Object msgObj) {
        Message msg = new Message();
        msg.what = msgWhat;
        msg.obj = msgObj;
        return msg;
    }

    public static Message getResultMessage(int msgWhat, Object msgObj) {
        Message msg = new Message();
        msg.what = msgWhat;
        Result result = new Result();
        result.setObj(msgObj);
        msg.obj = result;
        return msg;
    }
}
