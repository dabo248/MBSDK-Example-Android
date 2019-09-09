package com.daimler.reference.app

import androidx.multidex.MultiDexApplication
import com.daimler.reference.BuildConfig
import com.daimler.reference.login.LoginActivity
import com.daimler.mbloggerkit.MBLoggerKit
import com.daimler.mbloggerkit.PrinterConfig
import com.daimler.mbloggerkit.adapter.AndroidLogAdapter
import com.daimler.mbloggerkit.adapter.PersistingLogAdapter
import com.daimler.mbloggerkit.format.SimpleFileLogFormat
import com.daimler.mbloggerkit.obfuscation.ObfuscatingLogFormatter
import com.daimler.mbloggerkit.obfuscation.Obfuscations
import com.daimler.mbloggerkit.shake.LogOverlay
import com.daimler.mbmobilesdk.app.*

class ReferenceApplication : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

        val loggingEnabled = BuildConfig.DEBUG
        MBLoggerKit.usePrinterConfig(
                PrinterConfig.Builder().apply {
                    addAdapter(AndroidLogAdapter.Builder()
                            .setLoggingEnabled(loggingEnabled)
                            .build())
                    addAdapter(PersistingLogAdapter.Builder(this@ReferenceApplication)
                            .setLoggingEnabled(loggingEnabled)
                            .logFormatter(
                                    ObfuscatingLogFormatter(
                                            SimpleFileLogFormat(),
                                            Obfuscations.patternObfuscation(REGEX_JWT_HEADER, "- SECRET -") )
                            )
                            .build())
                    @Suppress("ConstantConditionIf")
                    if (loggingEnabled) {
                        showLogMenuWithShake(this@ReferenceApplication,
                                LogOverlay.Order.CHRONOLOGICAL_DESCENDING)
                    }
                }.build()
        )

        LogoutSessionExpiredHandler.init(this, LoginActivity::class.java)
        MBMobileSDK.init(
            MBMobileSDKConfig.Builder(this, true, APP_ID, CLIENT_ID, BuildConfig.DEBUG)
                .usePeriodicReconnect(SOCKET_RECONNECT_DELAY_SECONDS, SOCKET_RECONNECT_RETRIES)
                .useSessionExpiredHandler(LogoutSessionExpiredHandler)
                .enableFeatureToggling(true)
                .useInitialActivity(LoginActivity::class.java)
                .defaultRegionAndStage(Region.ECE, Stage.MOCK)
                .build()
        )
    }

    private companion object {
        private const val APP_ID = "reference_simple"
        private const val CLIENT_ID = "app"
        private const val SOCKET_RECONNECT_RETRIES = 5
        private const val SOCKET_RECONNECT_DELAY_SECONDS = 5

        private const val REGEX_JWT_HEADER = "Authorization: [a-zA-Z0-9\\-_]+?\\.[a-zA-Z0-9\\-_]+?\\.([a-zA-Z0-9\\-_]+)?\$"
    }
}
