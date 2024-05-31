package com.peterfarlow.sentryandroidcodeownersdemo

import android.app.Application
import io.sentry.android.core.SentryAndroid

class SentryAndroidCodeownersDemoApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        SentryAndroid.init(this) { options ->
            options.apply {
                dsn = BuildConfig.sentryDsn
                environment = "production"
            }
        }
    }
}
