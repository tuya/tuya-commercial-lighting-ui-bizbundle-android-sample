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

package com.tuya.smart.commercial.lighting.demo.pages.login.adpater;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.tuya.smart.android.common.utils.L;
import com.tuya.smart.android.demo.R;
import com.tuya.smart.commercial.lighting.demo.bean.CountryViewBean;
import com.tuya.smart.commercial.lighting.demo.widget.contact.ContactItemInterface;
import com.tuya.smart.commercial.lighting.demo.widget.contact.ContactListAdapter;

import java.util.List;


public class CountryAdpater extends ContactListAdapter {
    private static final String TAG = "CountryAdpater";

    public CountryAdpater(Context _context, int _resource,
                          List<ContactItemInterface> _items) {
        super(_context, _resource, _items);
    }

    // override this for custom drawing
    public void populateDataForRow(View parentView, ContactItemInterface item, int position) {
        // default just draw the item only
        TextView fullNameView = (TextView) parentView.findViewById(R.id.nameView);

        if (item instanceof CountryViewBean) {
            CountryViewBean contactItem = (CountryViewBean) item;
            fullNameView.setText(contactItem.getCountryName());
            L.d(TAG, "countryItem" + fullNameView);
        }
    }
}
