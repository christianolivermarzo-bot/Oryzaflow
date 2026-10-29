# Oryza Flow 2.0 Android App

A fullscreen, landscape Android WebView wrapper for the ESP32 dashboard at `http://192.168.4.1`.

## App behavior
- Always launches in landscape.
- Uses Android immersive sticky fullscreen.
- Hides status and navigation bars.
- Reloads the ESP32 dashboard when returning to the app.
- Allows cleartext HTTP only because the ESP32 dashboard is local.
- Shows connection instructions if Oryza-AP is unavailable.
- Supports the CSV download endpoint.

## Build in Android Studio
1. Open this folder in Android Studio Ladybug or newer.
2. Allow Gradle sync to finish.
3. Build > Build APK(s).
4. Install `app/build/outputs/apk/debug/app-debug.apk` on the phone.

## Build automatically with GitHub
1. Create a GitHub repository and upload this project.
2. Open Actions > Build Android APK > Run workflow.
3. Download the `OryzaFlow-debug-apk` artifact.

Before launching the app, connect the Android phone to:
- SSID: Oryza-AP
- Password: oryzaflow20
