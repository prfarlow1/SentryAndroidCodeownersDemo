package com.peterfarlow.sentryandroidcodeownersdemo

import android.app.Application
import com.peterfarlow.core.data.apiKey
import io.sentry.ILogger
import io.sentry.SentryLevel
import io.sentry.android.core.SentryAndroid
import timber.log.Timber

class SentryAndroidCodeownersDemoApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        SentryAndroid.init(this) { options ->
            options.apply {
                dsn = BuildConfig.sentryDsn
                environment = "production"
                setLogger(object : ILogger {
                    override fun log(level: SentryLevel, message: String, vararg args: Any?) {
                        Timber.tag("ISentryLogger").log(level.ordinal + 2, message, args)
                    }

                    override fun log(level: SentryLevel, message: String, throwable: Throwable?) {
                        Timber.tag("ISentryLogger").log(level.ordinal + 2, throwable, message)
                    }

                    override fun log(
                        level: SentryLevel,
                        throwable: Throwable?,
                        message: String,
                        vararg args: Any?
                    ) {
                        Timber.tag("ISentryLogger").log(level.ordinal + 2, throwable, message, args)
                    }

                    override fun isEnabled(level: SentryLevel?) = true
                })
            }
        }
        apiKey = BuildConfig.catApiKey
    }
}
