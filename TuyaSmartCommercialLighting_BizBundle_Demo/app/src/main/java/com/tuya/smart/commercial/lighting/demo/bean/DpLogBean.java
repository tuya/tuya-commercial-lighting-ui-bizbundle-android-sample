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

package com.tuya.smart.commercial.lighting.demo.bean;

public class DpLogBean {
    private String errorMsg;
    private long timeStart;
    private long timeEnd;
    private String dpSend;
    private String dpReturn;

    public DpLogBean(long timeStart, long timeEnd, String dpSend, String dpReturn, String errorMsg) {
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.dpSend = dpSend;
        this.dpReturn = dpReturn;
        this.errorMsg = errorMsg;
    }

    public long getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(long timeStart) {
        this.timeStart = timeStart;
    }

    public long getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(long timeEnd) {
        this.timeEnd = timeEnd;
    }

    public String getDpSend() {
        return dpSend;
    }

    public void setDpSend(String dpSend) {
        this.dpSend = dpSend;
    }

    public String getDpReturn() {
        return dpReturn;
    }

    public void setDpReturn(String dpReturn) {
        this.dpReturn = dpReturn;
    }

    public String getErrorMsg() {
        return errorMsg;
    }
}
