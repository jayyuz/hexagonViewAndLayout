package com.room.layoutmanagerdemo.preload;

import android.view.View;
import android.view.ViewGroup;

/**
 * 
 */
public class AsyncInflateItem {
    String inflateKey;
    int layoutResId;
    ViewGroup parent;
    OnInflateFinishedCallback callback;
    View inflatedView;

    private boolean cancelled;
    private boolean inflating;

    public AsyncInflateItem(String inflateKey, int layoutResId) {
        this(inflateKey, layoutResId, null, null);
    }

    public AsyncInflateItem(String inflateKey, int layoutResId, ViewGroup parent, OnInflateFinishedCallback callback) {
        this.layoutResId = layoutResId;
        this.parent = parent;
        this.callback = callback;
        this.inflateKey = inflateKey;
    }

    boolean isCancelled() {
        synchronized (this) {
            return cancelled;
        }
    }

    void setCancelled(boolean cancelled) {
        synchronized (this) {
            this.cancelled = cancelled;
        }
    }

    boolean isInflating() {
        synchronized (this) {
            return inflating;
        }
    }

    void setInflating(boolean inflating) {
        synchronized (this) {
            this.inflating = inflating;
        }
    }
}