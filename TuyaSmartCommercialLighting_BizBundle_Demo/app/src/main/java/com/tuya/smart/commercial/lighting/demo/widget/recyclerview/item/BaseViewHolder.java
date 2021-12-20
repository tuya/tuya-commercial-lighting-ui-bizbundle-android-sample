package com.tuya.smart.commercial.lighting.demo.widget.recyclerview.item;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.ArrayRes;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class BaseViewHolder extends RecyclerView.ViewHolder {

    public BaseViewHolder(View itemView) {
        super(itemView);
    }

    public void setVisibility(int visibility) {
        itemView.setVisibility(visibility);
    }

    public View getItemView() {
        return itemView;
    }

    public Context getContext() {
        return itemView.getContext();
    }

    public String getString(@StringRes int resId) {
        return getContext().getString(resId);
    }

    public String getString(@StringRes int resId, Object... formatArgs) {
        return getContext().getString(resId, formatArgs);
    }

    public String[] getStringArray(@ArrayRes int resId) {
        return getContext().getResources().getStringArray(resId);
    }

    public String getIcon(@StringRes int resId) {
        return getContext().getString(resId);
    }

    public int getColor(@ColorRes int resId) {
        return ContextCompat.getColor(getContext(), resId);
    }

    public Drawable getDrawable(@DrawableRes int resId) {
        return ContextCompat.getDrawable(getContext(), resId);
    }

    public Resources getResources() {
        return getContext().getResources();
    }

}
