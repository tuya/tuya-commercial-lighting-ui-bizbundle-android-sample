package com.tuya.smart.commercial.lighting.demo.widget.recyclerview.anntations;

import androidx.annotation.IntDef;

@IntDef({LoadMoreStatus.LOADING, LoadMoreStatus.END, LoadMoreStatus.IDLE})
public @interface LoadMoreStatus {
    int LOADING = 1;
    int END = 2;
    int IDLE = 0;
}
