package com.kuuga.updater

interface OnUpdaterListener{
    fun onLessGooglePlay()
    fun onEqualGooglePlay()
    fun onFailed(t:Throwable)
}