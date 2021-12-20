package com.tuya.smart.commercial.lighting.demo.widget.recyclerview.exception;

import android.text.TextUtils;

public class RvAdapterException extends RuntimeException {

    public RvAdapterException(String errMsg) {
        super(!TextUtils.isEmpty(errMsg) ? errMsg : "RvAdapterException");
    }

}
