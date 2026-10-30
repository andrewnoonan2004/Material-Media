package org.videolan.vlc.gui.helpers

import android.app.Activity
import android.content.res.Configuration
import android.util.TypedValue
import androidx.annotation.StyleRes
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.WindowCompat
import com.google.android.material.color.DynamicColors
import com.google.android.material.color.DynamicColorsOptions
import org.videolan.tools.KEY_APP_THEME
import org.videolan.tools.KEY_DYNAMIC_COLOR
import org.videolan.tools.Settings
import org.videolan.vlc.R
import org.videolan.vlc.gui.BaseActivity

/**
 * Material 3 Expressive and Wallpaper Dynamic Color theme manager for VLC for Android.
 *
 * Centralizes pure JVM-testable theme resolution rules as well as runtime Activity/Window
 * Material You wallpaper accent adaptation and expressive spring physics parameters.
 */
object Material3ExpressiveThemeManager {

    const val BRAND_APP_NAME = "Material Media"
    const val BRAND_PACKAGE_ID = "app.material.media"

    data class ExpressiveSpringSpec(
        val stiffness: Float,
        val dampingRatio: Float
    )

    fun getBrandAppName(): String = BRAND_APP_NAME

    fun getBrandBasePackageId(): String = BRAND_PACKAGE_ID

    /**
     * Determines whether Material You dynamic colors from the system wallpaper should be applied.
     */
    fun shouldApplyDynamicColors(
        isDynamicColorPrefEnabled: Boolean,
        isDeviceDynamicColorSupported: Boolean = DynamicColors.isDynamicColorAvailable(),
        isTvUi: Boolean = false,
        @StyleRes forcedThemeRes: Int? = null
    ): Boolean {
        if (!isDynamicColorPrefEnabled || !isDeviceDynamicColorSupported || isTvUi) {
            return false
        }
        return forcedThemeRes != R.style.Theme_VLC_Onboarding
    }

    /**
     * Parses the night mode preference string into an [AppCompatDelegate] mode constant.
     */
    fun resolveNightMode(themePrefValue: String?): Int {
        return when (themePrefValue) {
            "-1" -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            "0" -> AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY
            "1" -> AppCompatDelegate.MODE_NIGHT_NO
            "2" -> AppCompatDelegate.MODE_NIGHT_YES
            else -> themePrefValue?.toIntOrNull() ?: AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        }
    }

    /**
     * Resolves the Material 3 Expressive DynamicColors theme overlay matching the active night mode.
     */
    @StyleRes
    fun resolveDynamicThemeOverlay(
        nightMode: Int,
        isSystemInNightMode: Boolean
    ): Int {
        val useDark = when (nightMode) {
            AppCompatDelegate.MODE_NIGHT_YES -> true
            AppCompatDelegate.MODE_NIGHT_NO -> false
            else -> isSystemInNightMode
        }
        return if (useDark) {
            com.google.android.material.R.style.ThemeOverlay_Material3Expressive_DynamicColors_Dark
        } else {
            com.google.android.material.R.style.ThemeOverlay_Material3Expressive_DynamicColors_Light
        }
    }

    /**
     * Returns Material 3 Expressive Default Spatial spring parameters.
     */
    fun getExpressiveDefaultSpatialSpring(): ExpressiveSpringSpec =
        ExpressiveSpringSpec(stiffness = 380f, dampingRatio = 0.8f)

    /**
     * Returns Material 3 Expressive Fast Spatial spring parameters.
     */
    fun getExpressiveFastSpatialSpring(): ExpressiveSpringSpec =
        ExpressiveSpringSpec(stiffness = 800f, dampingRatio = 0.6f)

    /**
     * Applies Material 3 Expressive theme and Wallpaper Dynamic Color overlay to a [BaseActivity].
     */
    fun applyToActivity(activity: BaseActivity) {
        val prefs = activity.settings
        val forced = activity.forcedTheme()
        val isSystemNight = (activity.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) ==
                Configuration.UI_MODE_NIGHT_YES
        val nightMode = resolveNightMode(prefs.getString(KEY_APP_THEME, "-1"))
        val dynamicPrefEnabled = prefs.getBoolean(KEY_DYNAMIC_COLOR, true)

        if (forced != null) {
            activity.setTheme(forced)
            if (forced == R.style.Theme_VLC_Onboarding) {
                val bgColor = activity.getColor(R.color.onboarding_grey)
                activity.window.setBackgroundDrawable(bgColor.toDrawable())
                WindowCompat.getInsetsController(activity.window, activity.window.decorView).apply {
                    isAppearanceLightStatusBars = false
                    isAppearanceLightNavigationBars = false
                }
                activity.window.navigationBarColor = bgColor
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                return
            }
            if (shouldApplyDynamicColors(dynamicPrefEnabled, DynamicColors.isDynamicColorAvailable(), Settings.showTvUi, forced)) {
                val overlay = resolveDynamicThemeOverlay(AppCompatDelegate.MODE_NIGHT_YES, true)
                DynamicColors.applyToActivityIfAvailable(
                    activity,
                    DynamicColorsOptions.Builder().setThemeOverlay(overlay).build()
                )
            }
            return
        }

        if (Settings.showTvUi) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            activity.setTheme(R.style.Theme_VLC_Black)
            return
        }

        AppCompatDelegate.setDefaultNightMode(nightMode)

        val useDark = when (nightMode) {
            AppCompatDelegate.MODE_NIGHT_YES -> true
            AppCompatDelegate.MODE_NIGHT_NO -> false
            else -> isSystemNight
        }

        if (useDark) {
            activity.setTheme(R.style.Theme_VLC_Black)
        } else {
            activity.setTheme(R.style.Theme_VLC)
        }

        if (shouldApplyDynamicColors(
                isDynamicColorPrefEnabled = dynamicPrefEnabled,
                isDeviceDynamicColorSupported = DynamicColors.isDynamicColorAvailable(),
                isTvUi = Settings.showTvUi,
                forcedThemeRes = null
            )
        ) {
            val overlay = resolveDynamicThemeOverlay(nightMode, isSystemNight)
            DynamicColors.applyToActivityIfAvailable(
                activity,
                DynamicColorsOptions.Builder().setThemeOverlay(overlay).build()
            )
            // Also apply VLC custom attribute bridge overlay so all custom VLC attributes resolve to dynamic M3 tokens
            activity.theme.applyStyle(
                if (useDark) R.style.ThemeOverlay_VLC_DynamicColors_Dark else R.style.ThemeOverlay_VLC_DynamicColors_Light,
                true
            )
        } else {
            // Apply static VLC orange overlay when user explicitly disables wallpaper dynamic colors
            activity.theme.applyStyle(
                if (useDark) R.style.ThemeOverlay_VLC_StaticOrange_Dark else R.style.ThemeOverlay_VLC_StaticOrange_Light,
                true
            )
        }

        // Sync window background and system bar appearance with resolved M3 surface color
        val surfaceTypedValue = TypedValue()
        if (activity.theme.resolveAttribute(R.attr.background_default, surfaceTypedValue, true)) {
            activity.window.setBackgroundDrawable(surfaceTypedValue.data.toDrawable())
        }
        WindowCompat.getInsetsController(activity.window, activity.window.decorView).apply {
            isAppearanceLightStatusBars = !useDark
            isAppearanceLightNavigationBars = !useDark
        }
    }
}
