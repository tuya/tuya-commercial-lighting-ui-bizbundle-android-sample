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

package com.tuya.smart.commercial.lighting.demo.app;

import android.app.Activity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class Constant {

    public static long HOME_ID = 1099001;

    private static ArrayList<WeakReference<Activity>> activityStack = new ArrayList<WeakReference<Activity>>();

    public static final String TAG = "tuya";

    public static void exitApplication() {
        finishActivity();
//        SyncServerTask.getInstance().onDestroy();
    }

    public static void detachActivity(Activity activity) {
        WeakReference<Activity> act = new WeakReference<Activity>(activity);
        activityStack.remove(act);
    }

    public static void finishActivity() {
        // System.out.println(activityStack.size());
        for (WeakReference<Activity> activity : activityStack) {
            if (activity != null && activity.get() != null) activity.get().finish();
        }
        activityStack.clear();
    }

    public static void finishLastActivity(int finishNum) {
        if (activityStack == null) return;
        int num = 1;
        ArrayList<WeakReference<Activity>> activityStacks = new ArrayList<WeakReference<Activity>>();
        int size = activityStack.size();
        for (int i = size - 1; i >= 0; i--) {
            WeakReference<Activity> activity = activityStack.get(i);
            if (activity != null && activity.get() != null) {
                activity.get().finish();
                activityStacks.add(activity);
                if (num++ == finishNum) break;
            }
        }

        for (WeakReference<Activity> activity : activityStacks) {
            activityStack.remove(activity);
        }
        activityStacks.clear();
    }

    public static void attachActivity(Activity activity) {
        WeakReference<Activity> act = new WeakReference<Activity>(activity);
        if (activityStack.indexOf(act) == -1) activityStack.add(act);
    }
}
