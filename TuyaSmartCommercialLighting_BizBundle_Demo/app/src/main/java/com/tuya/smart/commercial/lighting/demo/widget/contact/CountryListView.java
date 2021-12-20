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

package com.tuya.smart.commercial.lighting.demo.widget.contact;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;

public class CountryListView extends ContactListView {

    public CountryListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void createScroller() {

        mScroller = new IndexScroller(getContext(), this);


        mScroller.setAutoHide(autoHide);

        // style 1
        //mScroller.setShowIndexContainer(false);
        //mScroller.setIndexPaintColor(Color.argb(255, 49, 64, 91));

        // style 2
        mScroller.setShowIndexContainer(true);
        mScroller.setIndexPaintColor(Color.WHITE);


        if (autoHide)
            mScroller.hide();
        else
            mScroller.show();


    }
}
