# Configuration Guide - Tabdal Android

## Google Maps Setup

### Local Development

1. **Edit `local.properties`** in the project root:
```properties
MAPS_API_KEY=AIzaSyAPFJ4V08JvPVPHrGzo3-sDo14VcxP3bsM
BASE_URL=https://api.tabdal.ma/v1/
```

2. **Build & Run**:
```bash
./gradlew assembleDebug
```

### Before Production

⚠️ **IMPORTANT**: This API key is for beta/development only!

1. **Revoke the current key** in [Google Cloud Console](https://console.cloud.google.com):
   - APIs & Services → Credentials
   - Delete: `AIzaSyAPFJ4V08JvPVPHrGzo3-sDo14VcxP3bsM`

2. **Create a new restricted key**:
   - Application restrictions: Android apps
   - Package: `com.tabdal.android`
   - SHA-1 certificate fingerprint (from your release keystore)
   - API restrictions: Maps SDK for Android only

3. **Update configuration**:
   - Replace in `local.properties`
   - Update GitHub secret `GOOGLE_MAPS_API_KEY` for CI/CD

## GitHub Actions Secrets

Configure in **Settings → Secrets and variables → Actions**:

```
GOOGLE_MAPS_API_KEY = YOUR_NEW_KEY_HERE
```

Used in workflow (see `.github/workflows/`):
```yaml
env:
  MAPS_API_KEY: ${{ secrets.GOOGLE_MAPS_API_KEY }}
```

## File Protection

✅ These files are in `.gitignore`:
- `local.properties` (never committed)
- `*.keystore` / `*.jks` (signing keys)
- `.env.local` (local environment)

## AndroidManifest.xml

The Maps API key is injected via:
```xml
<meta-data
    android:name="com.google.android.geo.API_KEY"
    android:value="${MAPS_API_KEY}" />
```

This value comes from `build.gradle.kts`:
```kotlin
val mapsApiKey = localProp("MAPS_API_KEY", "YOUR_MAPS_API_KEY")
manifestPlaceholders["MAPS_API_KEY"] = mapsApiKey
```

## Troubleshooting

**Error**: `MapsInitializationException`
- Check API key is correct in `local.properties`
- Verify SHA-1 fingerprint matches in Google Cloud Console
- Ensure "Maps SDK for Android" is enabled in APIs

**Error**: `Failed to instantiate com.google.android.gms.maps.MapView`
- Confirm Play Services version in `libs.versions.toml`
- Check AndroidManifest permissions are declared
