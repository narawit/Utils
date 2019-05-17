package com.kuuga.android.updater

interface OnUpdaterListener{
    fun onLessGooglePlay()
    fun onEqualGooglePlay()
    fun onFailed(t:Throwable)
}