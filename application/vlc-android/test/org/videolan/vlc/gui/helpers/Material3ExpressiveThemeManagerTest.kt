package org.videolan.vlc.gui.helpers

import androidx.appcompat.app.AppCompatDelegate
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.videolan.vlc.R

@RunWith(JUnit4::class)
class Material3ExpressiveThemeManagerTest {

    @Test
    fun shouldApplyDynamicColors_returnsTrueWhenEnabledAndSupportedOnMobile() {
        val result = Material3ExpressiveThemeManager.shouldApplyDynamicColors(
            isDynamicColorPrefEnabled = true,
            isDeviceDynamicColorSupported = true,
            isTvUi = false,
            forcedThemeRes = null
        )
        assertTrue(result)
    }

    @Test
    fun shouldApplyDynamicColors_returnsFalseWhenDisabledByUserPreference() {
        val result = Material3ExpressiveThemeManager.shouldApplyDynamicColors(
            isDynamicColorPrefEnabled = false,
            isDeviceDynamicColorSupported = true,
            isTvUi = false,
            forcedThemeRes = null
        )
        assertFalse(result)
    }

    @Test
    fun shouldApplyDynamicColors_returnsFalseOnUnsupportedDeviceOrTvUi() {
        assertFalse(
            Material3ExpressiveThemeManager.shouldApplyDynamicColors(
                isDynamicColorPrefEnabled = true,
                isDeviceDynamicColorSupported = false,
                isTvUi = false,
                forcedThemeRes = null
            )
        )
        assertFalse(
            Material3ExpressiveThemeManager.shouldApplyDynamicColors(
                isDynamicColorPrefEnabled = true,
                isDeviceDynamicColorSupported = true,
                isTvUi = true,
                forcedThemeRes = null
            )
        )
    }

    @Test
    fun resolveNightMode_parsesValidAndFallbackValues() {
        assertEquals(
            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM,
            Material3ExpressiveThemeManager.resolveNightMode("-1")
        )
        assertEquals(
            AppCompatDelegate.MODE_NIGHT_NO,
            Material3ExpressiveThemeManager.resolveNightMode("1")
        )
        assertEquals(
            AppCompatDelegate.MODE_NIGHT_YES,
            Material3ExpressiveThemeManager.resolveNightMode("2")
        )
        assertEquals(
            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM,
            Material3ExpressiveThemeManager.resolveNightMode(null)
        )
    }

    @Test
    fun resolveDynamicThemeOverlay_returnsMatchingMaterial3ExpressiveOverlay() {
        assertEquals(
            com.google.android.material.R.style.ThemeOverlay_Material3Expressive_DynamicColors_Light,
            Material3ExpressiveThemeManager.resolveDynamicThemeOverlay(
                nightMode = AppCompatDelegate.MODE_NIGHT_NO,
                isSystemInNightMode = true
            )
        )
        assertEquals(
            com.google.android.material.R.style.ThemeOverlay_Material3Expressive_DynamicColors_Dark,
            Material3ExpressiveThemeManager.resolveDynamicThemeOverlay(
                nightMode = AppCompatDelegate.MODE_NIGHT_YES,
                isSystemInNightMode = false
            )
        )
        assertEquals(
            com.google.android.material.R.style.ThemeOverlay_Material3Expressive_DynamicColors_Dark,
            Material3ExpressiveThemeManager.resolveDynamicThemeOverlay(
                nightMode = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM,
                isSystemInNightMode = true
            )
        )
    }

    @Test
    fun expressiveSpringSpec_matchesMaterial3ExpressivePhysics() {
        val spatialSpring = Material3ExpressiveThemeManager.getExpressiveDefaultSpatialSpring()
        assertEquals(380f, spatialSpring.stiffness, 0.01f)
        assertEquals(0.8f, spatialSpring.dampingRatio, 0.01f)

        val fastSpring = Material3ExpressiveThemeManager.getExpressiveFastSpatialSpring()
        assertEquals(800f, fastSpring.stiffness, 0.01f)
        assertEquals(0.6f, fastSpring.dampingRatio, 0.01f)
    }

    @Test
    fun brandIdentity_usesMaterialMediaInsteadOfVlcBranding() {
        assertEquals("Material Media", Material3ExpressiveThemeManager.getBrandAppName())
        assertEquals("app.material.media", Material3ExpressiveThemeManager.getBrandBasePackageId())
        assertFalse(Material3ExpressiveThemeManager.getBrandAppName().contains("VLC", ignoreCase = true))
        assertTrue(org.videolan.vlc.BuildConfig.APP_ID.startsWith("app.material.media"))
    }
}
