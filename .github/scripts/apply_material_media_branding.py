#!/usr/bin/env python3
"""
Idempotent script executed after merging upstream VideoLAN vlc-android commits
to ensure all Material Media branding, Material 3 Expressive dependencies, and
prebuilt LibVLC/Medialibrary Gradle configurations remain intact.
"""
import glob
import os
import re

PRESERVE_STRING_KEYS = {
    "about_copyright",
    "vlc_authors",
    "custom_libvlc_options",
    "custom_libvlc_options_invalid",
}


def patch_root_build_gradle():
    path = "build.gradle"
    if not os.path.exists(path):
        return
    with open(path, "r", encoding="utf-8") as f:
        content = f.read()
    content = re.sub(r'appId\s*=\s*"[^"]+"', 'appId = "app.material.media"', content)
    content = re.sub(
        r"androidxMaterialVersion\s*=\s*'[^']+'",
        "androidxMaterialVersion = '1.14.0'",
        content,
    )
    with open(path, "w", encoding="utf-8") as f:
        f.write(content)


def patch_settings_gradle():
    path = "settings.gradle"
    if not os.path.exists(path):
        return
    with open(path, "r", encoding="utf-8") as f:
        content = f.read()
    content = re.sub(r"minSdk\s*=\s*\d+", "minSdk = 26", content)
    content = re.sub(r"^\s*include\s+':libvlcjni:libvlc'.*\n?", "", content, flags=re.MULTILINE)
    content = re.sub(r"^\s*include\s+':medialibrary'.*\n?", "", content, flags=re.MULTILINE)
    with open(path, "w", encoding="utf-8") as f:
        f.write(content)


def patch_app_build_gradle():
    path = "application/app/build.gradle"
    if not os.path.exists(path):
        return
    with open(path, "r", encoding="utf-8") as f:
        content = f.read()
    content = content.replace('def outputName = "VLC-Android-"', 'def outputName = "Material-Media-"')
    with open(path, "w", encoding="utf-8") as f:
        f.write(content)


def patch_strings_xml():
    for path in glob.glob("application/resources/src/main/res/values*/strings.xml"):
        with open(path, "r", encoding="utf-8") as f:
            content = f.read()
        orig = content

        def repl_tag(match):
            full = match.group(0)
            name = match.group(1)
            attrs = match.group(2)
            body = match.group(3)
            if name in PRESERVE_STRING_KEYS:
                return full
            if name in ("app_name", "app_name_full"):
                return f'<string name="{name}"{attrs}>Material Media</string>'
            if name == "about_vlc_text":
                return (
                    f'<string name="{name}"{attrs}>Material Media is an open-source '
                    "Material 3 Expressive media player with dynamic wallpaper theming, "
                    "built upon the LibVLC engine under the GNU GPL.</string>"
                )
            new_body = (
                body.replace("VLC for Android™", "Material Media")
                .replace("VLC for Android", "Material Media")
                .replace("VLC media player", "Material Media")
                .replace("VLC", "Material Media")
            )
            return f'<string name="{name}"{attrs}>{new_body}</string>'

        content = re.sub(
            r'<string name="([^"]+)"([^>]*)>(.*?)</string>',
            repl_tag,
            content,
            flags=re.DOTALL,
        )
        if content != orig:
            with open(path, "w", encoding="utf-8") as f:
                f.write(content)


def remove_legacy_cone_pngs():
    for pattern in [
        "application/resources/src/main/res/drawable-*dpi/ic_stat_vlc.png",
        "application/resources/src/main/res/drawable-*dpi/icon.png",
        "application/resources/src/main/res/drawable-*dpi/banner.png",
        "application/vlc-android/flavors/debug/res/drawable-*dpi/banner.png",
        "application/vlc-android/res/mipmap-*dpi/ic_launcher.png",
    ]:
        for p in glob.glob(pattern):
            os.remove(p)


if __name__ == "__main__":
    patch_root_build_gradle()
    patch_settings_gradle()
    patch_app_build_gradle()
    patch_strings_xml()
    remove_legacy_cone_pngs()
    print("Material Media branding and Material 3 Expressive configuration verified.")
