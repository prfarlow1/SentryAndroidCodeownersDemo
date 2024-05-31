package com.peterfarlow.sentryandroidcodeownersdemo

import android.app.Application
import com.peterfarlow.core.data.apiKey
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
        apiKey = BuildConfig.catApiKey
    }
}
